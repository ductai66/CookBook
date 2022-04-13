package com.tai06dothe.cookbook.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Adapter.InfoUserAdapter;
import com.tai06dothe.cookbook.Adapter.MyRecipeAdapter;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.R;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoUserActivity extends AppCompatActivity {
    Toolbar toolbar_infoUser;
    TextView title_infoUser, userName, userEmail;
    CircleImageView userImage;
    RecyclerView recycle_recipeofUser;
    InfoUserAdapter infoUserAdapter;
    Recipe recipe;
    List<Recipe> recipeList;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        init();
        getInfoUser();
        getRecipe();
    }

    private void init() {
        toolbar_infoUser = findViewById(R.id.toolbar_infoUser);
        setSupportActionBar(toolbar_infoUser);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title_infoUser = findViewById(R.id.title_infoUser);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userImage = findViewById(R.id.userImage);
        recycle_recipeofUser = findViewById(R.id.recycle_recipeofUser);

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
    }

    private void getRecipe() {
        DatabaseReference rootRecipe = mDatabase.child("Recipe");
        Query firebaseQueryRecipes = rootRecipe.orderByChild("userId").equalTo(recipe.getUserId());

        firebaseQueryRecipes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Recipe recipes = dataSnapshot.getValue(Recipe.class);
                    recipeList.add(recipes);

                }
                setRecipeAdapter(recipeList);
                infoUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setRecipeAdapter(List<Recipe> list) {
        infoUserAdapter = new InfoUserAdapter(this, list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recycle_recipeofUser.setLayoutManager(gridLayoutManager);
        recycle_recipeofUser.setAdapter(infoUserAdapter);
        recycle_recipeofUser.setHasFixedSize(true);
    }

    public void getInfoUser() {
        DatabaseReference rootUser = mDatabase.child("User").child(recipe.getUserId());
        rootUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                title_infoUser.setText(snapshot.child("name").getValue().toString());
                userName.setText(snapshot.child("name").getValue().toString());
                userEmail.setText(snapshot.child("email").getValue().toString());
                String img = snapshot.child("avatar").getValue().toString();
                Picasso.get().load(img).into(userImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}