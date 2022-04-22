package com.tai06dothe.cookbook.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tai06dothe.cookbook.Adapter.IngredientAdapter;
import com.tai06dothe.cookbook.Adapter.RecipeStepAdapter;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.Model.RecipeStep;
import com.tai06dothe.cookbook.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class AddRecipeActivity extends AppCompatActivity {

    private static final int UPLOAD_RECIPE = 1;
    private static final int UPLOAD_RECIPE_STEP = 2;

    Toolbar toolbar_addrecipe;
    TextInputEditText name_recipe, name_ingredient, name_step;
    ImageView img_recipe, img_show;
    ImageButton btn_add_ingredient, btn_add_image_step, btn_add_step;
    Button btn_add_photo, btn_camera, btn_insert_recipe;
    RecyclerView recycle_recipeSteps, recycle_ingredients;
    Spinner recipe_category;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri, imageStepUri;

    private List<String> listCategory;
    private List<String> ingredients;
    private List<RecipeStep> recipeSteps;
    private Boolean check = Boolean.FALSE;
    private String category;
    private String id_user;
    private String text_button;

    private IngredientAdapter ingredientAdapter;
    private RecipeStepAdapter recipeStepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        init();
        processCategory();

        processEvent();
        processIngredient();

        //ingredients
        setIngredientAdapter(ingredients);
        //recipe steps
        setRecipeStepAdapter(recipeSteps);
    }

    private void init() {
        toolbar_addrecipe = findViewById(R.id.toolbar_addrecipe);
        setSupportActionBar(toolbar_addrecipe);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // text
        name_recipe = findViewById(R.id.name_recipe);
        name_ingredient = findViewById(R.id.name_ingredient);
        name_step = findViewById(R.id.name_step);

        //image
        img_recipe = findViewById(R.id.img_recipe);
        img_show = findViewById(R.id.img_show);

        //button
        btn_add_ingredient = findViewById(R.id.btn_add_ingredient);
        btn_add_image_step = findViewById(R.id.btn_add_image_step);
        btn_add_photo = findViewById(R.id.btn_add_photo);
        btn_camera = findViewById(R.id.btn_camera);
        btn_add_step = findViewById(R.id.btn_add_step);
        btn_insert_recipe = findViewById(R.id.btn_insert_recipe);

        //recyclerview
        recycle_recipeSteps = findViewById(R.id.recycle_recipeSteps);
        recycle_ingredients = findViewById(R.id.recycle_ingredients);

        //spinner
        recipe_category = findViewById(R.id.recipe_category);

        //arraylist
        listCategory = new ArrayList<>();
        ingredients = new ArrayList<>();
        recipeSteps = new ArrayList<>();

        Intent intent = getIntent();
        id_user = intent.getStringExtra("userId");
    }

    private void processEvent() {
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = Boolean.TRUE;
                text_button = btn_camera.getText().toString().trim();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = Boolean.TRUE;
                text_button = btn_add_photo.getText().toString().trim();
                selectImage(2);
            }
        });

        btn_add_image_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(3);
            }
        });

        btn_add_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage_photo(imageStepUri, UPLOAD_RECIPE_STEP);
            }
        });

        btn_insert_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check == true) {
                    if (text_button.contentEquals("Thư viện")) {
                        uploadImage_photo(imageUri, UPLOAD_RECIPE);
                    }
                    if (text_button.contentEquals("Chụp ảnh")) {
                        uploadImage_camera(img_recipe, UPLOAD_RECIPE);
                    }
                } else {
                    Toast.makeText(AddRecipeActivity.this, "Vui lòng thêm ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectImage(int request) {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, request);
    }

    private void uploadImage_camera(ImageView imageView, int option) {
        if (imageView != null) {
            Calendar calendar = Calendar.getInstance();
            StorageReference fileRef = reference.child("image" + calendar.getTimeInMillis() + ".png");

            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = fileRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String imageUrl = task.getResult().toString();
                            switch (option) {
                                case UPLOAD_RECIPE:
                                    uploadRecipe(imageUrl);
                                    break;
                                case UPLOAD_RECIPE_STEP:
                                    uploadRecipeStep(imageUrl);
                                    break;
                            }
                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, "Please select image for ?", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadImage_photo(Uri uri, int option) {
        if (uri != null) {
            StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtendsion(uri));
            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            switch (option) {
                                case UPLOAD_RECIPE:
                                    uploadRecipe(imageUrl);
                                    break;
                                case UPLOAD_RECIPE_STEP:
                                    uploadRecipeStep(imageUrl);
                                    break;
                            }
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            // test
            Toast.makeText(this, "Please select image for ?", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadRecipe(String imageUrl) {
        String nameRecipe = String.valueOf(name_recipe.getText());
        DatabaseReference rootRecipe = mDatabase.child("Recipe");
        String id = rootRecipe.push().getKey();

        Recipe recipe = new Recipe();
        recipe.setRecipeId(id);
        recipe.setRecipeName(nameRecipe);
        recipe.setRecipeImage(imageUrl);
        recipe.setIngredientList(ingredients);
        recipe.setRecipeStepList(recipeSteps);
        recipe.setCategoryId(category);
        recipe.setUserId(id_user);
        recipe.setFavoriteNumber(0);

        if (id != null && !name_recipe.getText().toString().isEmpty() && !ingredients.isEmpty() && !recipeSteps.isEmpty()){
            rootRecipe.child(id).setValue(recipe).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(AddRecipeActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddRecipeActivity.this, MyRecipeActivity.class);
                    intent.putExtra("userId", recipe.getUserId());
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddRecipeActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (name_recipe.getText().toString().isEmpty()){
            Toast.makeText(this, "Bạn chưa nhập tên công thức", Toast.LENGTH_SHORT).show();
        }
        else if (ingredients.isEmpty()){
            Toast.makeText(this, "Bạn chưa nhập nguyên liệu", Toast.LENGTH_SHORT).show();
        }
        else if (recipeSteps.isEmpty()){
            Toast.makeText(this, "Bạn chưa nhập cách làm", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadRecipeStep(String imageUrl) {
        String description = String.valueOf(name_step.getText());
        RecipeStep step = new RecipeStep(imageUrl, description);
        recipeSteps.add(step);
        recipeStepAdapter.notifyDataSetChanged();
    }

    // get type file -> example: file.png, file.jpg,....
    private String getFileExtendsion(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    // process get list category
    private void processCategory() {
        DatabaseReference rootCategory = mDatabase.child("Category");
        rootCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String item = dataSnapshot.getValue(String.class);
                    listCategory.add(item);
                }
                setSpinnerCategory(listCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddRecipeActivity.this, "Get list category failed !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinnerCategory(List<String> lstCategory) {
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(AddRecipeActivity.this, android.R.layout.simple_spinner_item, listCategory);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipe_category.setAdapter(adapterType);
        recipe_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = listCategory.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // process add nguyen lieu cho recipe
    private void processIngredient() {
        btn_add_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = String.valueOf(name_ingredient.getText());
                if (!"".equals(value)) {
                    ingredients.add(value);
                    ingredientAdapter.notifyDataSetChanged();
                    name_ingredient.getText().clear();
                    Toast.makeText(AddRecipeActivity.this, value + " đã được thêm vào danh sách", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddRecipeActivity.this, "Khong dc de trong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //process show items to recycleview for Ingredient
    private void setIngredientAdapter(List<String> mList) {
        ingredientAdapter = new IngredientAdapter(AddRecipeActivity.this, mList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AddRecipeActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recycle_ingredients.setLayoutManager(gridLayoutManager);
        recycle_ingredients.setAdapter(ingredientAdapter);
        recycle_ingredients.setNestedScrollingEnabled(true);
        recycle_ingredients.setHasFixedSize(true);
    }

    // process show items to recyclerview for RecipeStep
    private void setRecipeStepAdapter(List<RecipeStep> mList) {
        recipeStepAdapter = new RecipeStepAdapter(AddRecipeActivity.this, mList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AddRecipeActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recycle_recipeSteps.setLayoutManager(gridLayoutManager);
        recycle_recipeSteps.setAdapter(recipeStepAdapter);
        recycle_recipeSteps.setNestedScrollingEnabled(true);
        recycle_recipeSteps.setHasFixedSize(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img_recipe.setImageBitmap(bitmap);
        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            img_recipe.setImageURI(imageUri);
        }
        if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            imageStepUri = data.getData();
            img_show.setImageURI(imageStepUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}