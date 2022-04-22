package com.tai06dothe.cookbook.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Adapter.IngredientAdapter;
import com.tai06dothe.cookbook.Adapter.RecipeStepAdapter;
import com.tai06dothe.cookbook.Model.Ingredient;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.Model.RecipeStep;
import com.tai06dothe.cookbook.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditRecipeActivity extends AppCompatActivity {
    private static final int UPLOAD_RECIPE = 1;
    private static final int UPLOAD_RECIPE_STEP = 2;
    private static final int EDIT_RECIPE_STEP = 3;

    Toolbar toolbar_editrecipe;
    TextInputEditText name_recipe, name_ingredient, name_step, stepName_dialog;
    ImageView img_recipe, img_show, stepImage_dialog;
    ImageButton btn_add_ingredient, btn_add_image_step, btn_add_step;
    Button btn_add_photo, btn_camera, btn_update_recipe, btn_add_photo_dialog, btn_camera_dialog, btn_update_dialog, btn_cancel_dialog;
    RecyclerView recycle_recipeSteps, recycle_ingredients;
    Spinner recipe_category;
    Dialog dialog;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri, imageStepUri;

    private List<String> listCategory;
    private Boolean check = Boolean.FALSE;
    private String category;
    private Recipe recipe;
    private List<String> ingredients;
    private List<RecipeStep> recipeSteps;
    private int stepId;
    private String text_button;


    private IngredientAdapter ingredientAdapter;
    private RecipeStepAdapter recipeStepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        init();
        // get
        getRecipe();
        // process
        processCategory();
        processIngredient();

        processEvent();
    }

    private void init() {
        toolbar_editrecipe = findViewById(R.id.toolbar_editrecipe);
        setSupportActionBar(toolbar_editrecipe);
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
        btn_update_recipe = findViewById(R.id.btn_update_recipe);

        //recyclerview
        recycle_recipeSteps = findViewById(R.id.recycle_recipeSteps);
        recycle_ingredients = findViewById(R.id.recycle_ingredients);

        //spinner
        recipe_category = findViewById(R.id.recipe_category);

        //arraylist category
        listCategory = new ArrayList<>();

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        ingredients = recipe.getIngredientList();
        recipeSteps = recipe.getRecipeStepList();
    }

    private void initDialog() {
        dialog = new Dialog(EditRecipeActivity.this);
        dialog.setTitle("Chỉnh sửa");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_edit_item_step);
        stepImage_dialog = (ImageView) dialog.findViewById(R.id.stepImage_dialog);
        btn_add_photo_dialog = (Button) dialog.findViewById(R.id.btn_add_photo_dialog);
        btn_camera_dialog = (Button) dialog.findViewById(R.id.btn_camera_dialog);
        stepName_dialog = (TextInputEditText) dialog.findViewById(R.id.stepName_dialog);
        btn_update_dialog = (Button) dialog.findViewById(R.id.btn_update_dialog);
        btn_cancel_dialog = (Button) dialog.findViewById(R.id.btn_cancel_dialog);
        dialog.show();
    }

    private void processEvent() {
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = Boolean.TRUE;
                text_button = btn_camera.getText().toString().trim();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = Boolean.TRUE;
                text_button = btn_add_photo.getText().toString().trim();
                selectImage(2);
            }
        });

        btn_add_image_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(3);
            }
        });

        btn_add_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage_photo(imageStepUri, UPLOAD_RECIPE_STEP);
            }
        });

        btn_update_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == true) {
                    if (text_button.contentEquals("Thư viện")) {
                        uploadImage_photo(imageUri, UPLOAD_RECIPE);
                    }
                    if (text_button.contentEquals("Chụp ảnh")) {
                        uploadImage_camera(img_recipe, UPLOAD_RECIPE);
                    }
                } else {
                    uploadRecipe(recipe.getRecipeImage());
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
                            case EDIT_RECIPE_STEP:
                                editRecipeStep(imageUrl, stepId);
                                break;
                        }
                    }
                });
            }
        });
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
                                case EDIT_RECIPE_STEP:
                                    editRecipeStep(imageUrl, stepId);
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

    private void processCategory() {
        DatabaseReference rootCategory = mDatabase.child("Category");
        rootCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String item = dataSnapshot.getValue(String.class);
                    listCategory.add(item);
                }
                setSpinnerCategory();
                for (int i = 0; i <= listCategory.size(); i++) {
                    if (listCategory.get(i).equals(recipe.getCategoryId())) {
                        recipe_category.setSelection(i);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditRecipeActivity.this, "Get list category failed !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinnerCategory() {
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(EditRecipeActivity.this, android.R.layout.simple_spinner_item, listCategory);
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

    private void getRecipe() {
        Picasso.get().load(recipe.getRecipeImage()).into(img_recipe);
        name_recipe.setText(recipe.getRecipeName());
        setIngredientAdapter(recipe.getIngredientList());
        setRecipeStepAdapter(recipe.getRecipeStepList());
    }

    private void uploadRecipe(String imageUrl) {
        String nameRecipe = String.valueOf(name_recipe.getText());
        DatabaseReference rootRecipe = mDatabase.child("Recipe");

        Recipe recipeUpload = new Recipe();
        recipeUpload.setRecipeId(recipe.getRecipeId());
        recipeUpload.setRecipeName(nameRecipe);
        recipeUpload.setRecipeImage(imageUrl);
        recipeUpload.setIngredientList(ingredients);
        recipeUpload.setRecipeStepList(recipeSteps);
        recipeUpload.setCategoryId(category);
        recipeUpload.setUserId(recipe.getUserId());

        if (recipe.getRecipeId() != null)
            rootRecipe.child(recipe.getRecipeId()).setValue(recipeUpload).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(EditRecipeActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditRecipeActivity.this, MyRecipeActivity.class);
                    intent.putExtra("userId", recipe.getUserId());
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditRecipeActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditRecipeActivity.this, value + " đã được thêm vào danh sách", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditRecipeActivity.this, "Vui lòng nhập thành phần", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // process show items to recycleview for Ingredient
    private void setIngredientAdapter(List<String> mList) {
        ingredientAdapter = new IngredientAdapter(EditRecipeActivity.this, mList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(EditRecipeActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recycle_ingredients.setLayoutManager(gridLayoutManager);
        recycle_ingredients.setAdapter(ingredientAdapter);
        recycle_ingredients.setNestedScrollingEnabled(true);
        recycle_ingredients.setHasFixedSize(true);
    }

    private void uploadRecipeStep(String imageUrl) {
        String description = String.valueOf(name_step.getText());
        RecipeStep step = new RecipeStep(imageUrl, description);
        recipeSteps.add(step);
        recipeStepAdapter.notifyDataSetChanged();
    }

    // process show items to recyclerview for RecipeStep
    private void setRecipeStepAdapter(List<RecipeStep> mList) {
        recipeStepAdapter = new RecipeStepAdapter(EditRecipeActivity.this, mList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(EditRecipeActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recycle_recipeSteps.setLayoutManager(gridLayoutManager);
        recycle_recipeSteps.setAdapter(recipeStepAdapter);
        recycle_recipeSteps.setNestedScrollingEnabled(true);
        recycle_recipeSteps.setHasFixedSize(true);
    }

    // get type file -> example: file.png, file.jpg,....
    private String getFileExtendsion(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) { // chup anh Image chinh
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img_recipe.setImageBitmap(bitmap);
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) { //chon anh Image chinh
            imageUri = data.getData();
            img_recipe.setImageURI(imageUri);
        } else if (requestCode == 3 && resultCode == RESULT_OK && data != null) { //chon anh Image cac buoc
            imageStepUri = data.getData();
            img_show.setImageURI(imageStepUri);
        } else
//            chon anh Image dialog
        if (requestCode == 4 && resultCode == RESULT_OK && data != null) {
            imageStepUri = data.getData();
            stepImage_dialog.setImageURI(imageStepUri);
        }
        else
            //chup anh Image dialog
            if (requestCode == 5 && resultCode == RESULT_OK && data != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                stepImage_dialog.setImageBitmap(bitmap);
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void editDialog(String Image, String Name, int id) {
        initDialog();
        stepId = id;

        Picasso.get().load(Image).into(stepImage_dialog);
        stepName_dialog.setText(Name);

        btn_add_photo_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = Boolean.TRUE;
                text_button = btn_add_photo_dialog.getText().toString().trim();
                selectImage(4);
            }
        });

        btn_camera_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = Boolean.TRUE;
                text_button = btn_camera_dialog.getText().toString().trim();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 5);
            }
        });

        btn_update_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == true) {
                    if (text_button.contentEquals("Thư viện")) {
                        uploadImage_photo(imageStepUri, EDIT_RECIPE_STEP);
                        dialog.cancel();
                    }
                    if (text_button.contentEquals("Chụp ảnh")) {
                        uploadImage_camera(stepImage_dialog, EDIT_RECIPE_STEP);
                        dialog.cancel();
                    }
                } else {
                    editRecipeStep(Image, id);
                    dialog.cancel();
                }
            }
        });

        btn_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    private void editRecipeStep(String imageUrl, int stepId) {
        String description = String.valueOf(stepName_dialog.getText());
        recipeSteps.get(stepId).setDescription(description);
        recipeSteps.get(stepId).setImage(imageUrl);
        recipeStepAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}