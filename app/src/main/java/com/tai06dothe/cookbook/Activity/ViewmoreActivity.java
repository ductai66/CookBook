package com.tai06dothe.cookbook.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.FaceDetector;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Adapter.ViewmoreAdapter;
import com.tai06dothe.cookbook.Model.Favorite;
import com.tai06dothe.cookbook.Model.Ingredient;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.Model.RecipeStep;
import com.tai06dothe.cookbook.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewmoreActivity extends AppCompatActivity {
    RecyclerView recycle_viewmore;
    ViewmoreAdapter viewmoreAdapter;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    String id_category, id_user;
    private List<String> ingredients;
    private List<RecipeStep> recipeSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmore);
        init();
        getAdapter();
    }

    private void init() {
        recycle_viewmore = (RecyclerView) findViewById(R.id.recycle_viewmore);
        Intent intent = getIntent();
        id_category = intent.getStringExtra("CategoryId");
        id_user = intent.getStringExtra("userId");

    }

    private void setAdapterViewMore(List<Recipe> list) {
        viewmoreAdapter = new ViewmoreAdapter(this, list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recycle_viewmore.setLayoutManager(gridLayoutManager);
        recycle_viewmore.setAdapter(viewmoreAdapter);
        recycle_viewmore.setHasFixedSize(true);
        viewmoreAdapter.notifyDataSetChanged();
    }

    private void getAdapter() {
        DatabaseReference mRef = mDatabase.child("Recipe");
        Query firebaseQueryRecipes = mRef.orderByChild("categoryId").equalTo(id_category);

        firebaseQueryRecipes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    recipes.add(recipe);
                }
                setAdapterViewMore(recipes);
                viewmoreAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getinfoUser(TextView txt_username, CircleImageView img_user, String id_user) {
        mDatabase.child("User").child(id_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("name").getValue().toString();
                txt_username.setText(username);
                String img = snapshot.child("avatar").getValue().toString();
                Picasso.get().load(img).into(img_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkFavorite(CheckBox checkFavorite, String id_recipe) {
        DatabaseReference rootFavorite = mDatabase.child("Favorite");
        Query firebaseQuery = rootFavorite.orderByChild("recipeId").equalTo(id_recipe);
        firebaseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Favorite> favoriteList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Favorite favorite = dataSnapshot.getValue(Favorite.class);
                    favoriteList.add(favorite);
                    if (favorite != null && id_user.equals(favorite.getUserId())) {
                        checkFavorite.setBackgroundResource(R.drawable.ic_favorite_true);
                        checkFavorite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                checkFavorite.setBackgroundResource(R.drawable.ic_favorite);
                                for (int i = 0; i <= favoriteList.size(); i++){
                                    removeRecipeFavorite(favoriteList, id_recipe, i);
                                }
                            }
                        });

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void addRecipeFavorite(String recipeId, String nameRecipe, String imageRecipe, List<String> ingredients, List<RecipeStep> recipeSteps, String createById) {
        DatabaseReference rootFavorite = mDatabase.child("Favorite");
        String id = rootFavorite.push().getKey();
        Favorite favorite = new Favorite();
        favorite.setFavoriteId(id);
        favorite.setRecipeId(recipeId);
        favorite.setRecipeName(nameRecipe);
        favorite.setRecipeImage(imageRecipe);
        favorite.setIngredientList(ingredients);
        favorite.setRecipeStepList(recipeSteps);
        favorite.setCategoryId(id_category);
        favorite.setCreateById(createById);
        favorite.setUserId(id_user);

        if (id != null)
            rootFavorite.child(id).setValue(favorite).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(ViewmoreActivity.this, "Thêm thành công vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ViewmoreActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            });
    }

    public void removeRecipeFavorite(List<Favorite> favoriteList, String id_recipe, int position) {
        DatabaseReference rootRemoveFavorite = mDatabase.child("Favorite").child(id_recipe);
        rootRemoveFavorite.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                favoriteList.remove(position);
                Toast.makeText(ViewmoreActivity.this, "Đã loại khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showRecipe(Recipe recipe) {
        Intent intent = new Intent(ViewmoreActivity.this, DetailRecipeActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }
}