package com.tai06dothe.cookbook.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tai06dothe.cookbook.Adapter.MyRecipeAdapter;
import com.tai06dothe.cookbook.Adapter.RecipeFavoriteAdapter;
import com.tai06dothe.cookbook.Model.Favorite;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyRecipeActivity extends AppCompatActivity {

    RecyclerView recycle_myRecipe;
    MyRecipeAdapter myRecipeAdapter;
    Button btn_add_recipe;

    private List<Recipe> recipeList;
    private String id_user;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);

        init();
        processEvent();
    }

    private void init(){
        btn_add_recipe = findViewById(R.id.btn_add_recipe);
        recycle_myRecipe = findViewById(R.id.recycle_myRecipe);
    }

    private void processEvent(){
        btn_add_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyRecipeActivity.this,AddRecipeActivity.class);
                intent.putExtra("userId", id_user);
                startActivity(intent);
            }
        });
    }

    private void setMyRecipeAdapter(List<Recipe> list) {
        myRecipeAdapter = new MyRecipeAdapter(this, list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recycle_myRecipe.setLayoutManager(gridLayoutManager);
        recycle_myRecipe.setAdapter(myRecipeAdapter);
        recycle_myRecipe.setHasFixedSize(true);
    }

    private void getMyRecipe() {
        DatabaseReference mRef = mDatabase.child("Recipe");
        Query firebaseQueryRecipes = mRef.orderByChild("userId").equalTo(id_user);

        firebaseQueryRecipes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    recipeList.add(recipe);
                    setMyRecipeAdapter(recipeList);
                    myRecipeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void edit_recipe(Recipe recipe){
        Intent intent = new Intent(MyRecipeActivity.this, EditRecipeActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }

    // possible bug
    public void delete_recipe(Recipe recipe, int position) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Do you want to delete item ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase.child("Recipe").child(recipe.getRecipeId()).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                recipeList.remove(position);
                                setMyRecipeAdapter(recipeList);
                                myRecipeAdapter.notifyDataSetChanged();
                                Toast.makeText(MyRecipeActivity.this, "Delete item successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        id_user = intent.getStringExtra("userId");
        getMyRecipe();
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}