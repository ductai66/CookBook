package com.tai06dothe.cookbook.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Adapter.IngredientAdapter;
import com.tai06dothe.cookbook.Adapter.RecipeStepAdapter;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.Model.RecipeStep;
import com.tai06dothe.cookbook.Model.User;
import com.tai06dothe.cookbook.R;

import java.util.ArrayList;
import java.util.List;

public class DetailRecipeActivity extends AppCompatActivity {
    ImageView recipeImageDetail;
    TextView recipeNameDetail;
    RecyclerView recycle_ingredients, recycle_recipeSteps;
    Recipe recipe;
    IngredientAdapter ingredientAdapter;
    RecipeStepAdapter recipeStepAdapter;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);
        init();
        getRecipe();
    }

    private void init(){
        recipeImageDetail = (ImageView) findViewById(R.id.recipeImageDetail);
        recipeNameDetail = (TextView) findViewById(R.id.recipeNameDetail);
        recycle_ingredients = (RecyclerView) findViewById(R.id.recycle_ingredients);
        recycle_recipeSteps = (RecyclerView) findViewById(R.id.recycle_recipeSteps);

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
    }

    private void getRecipe(){
        recipe = new Recipe();
        DatabaseReference rootRecipe = mDatabase.child("Recipe").child(recipe.getRecipeId());
        rootRecipe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeNameDetail.setText(snapshot.child("recipeName").getValue().toString());
                String linkImage = snapshot.child("recipeImage").getValue().toString();
                Picasso.get().load(linkImage).into(recipeImageDetail);
                getIngredientList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getIngredientList(){
        DatabaseReference rootIngredient = mDatabase.child("Recipe").child(recipe.getRecipeId()).child("ingredientList");

        rootIngredient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> ingredientList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String item = dataSnapshot.getValue(String.class);
                    ingredientList.add(item);
                    setIngredient(ingredientList);
                    ingredientAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//    private void getRecipeStepList(String id_recipe){
//        mDatabase.child("Recipe").child(id_recipe).child("recipeStepList").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<String> recipeStepList = new ArrayList<>();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    String item = dataSnapshot.getValue(String.class);
//                    recipeStepList.add(item);
//                    setRecipeStepAdapter(recipeStepList);
//                    recipeStepAdapter.notifyDataSetChanged();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

    private void setIngredient(List<String> mList) {
        ingredientAdapter = new IngredientAdapter(DetailRecipeActivity.this, mList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DetailRecipeActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recycle_ingredients.setLayoutManager(gridLayoutManager);
        recycle_ingredients.setAdapter(ingredientAdapter);
        recycle_ingredients.setNestedScrollingEnabled(true);
        recycle_ingredients.setHasFixedSize(true);
    }
}