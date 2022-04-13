package com.tai06dothe.cookbook.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Adapter.ViewmoreAdapter;
import com.tai06dothe.cookbook.Model.Favorite;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewmoreActivity extends AppCompatActivity {

    Toolbar toolbar_viewmore;
    TextView title_viewmore;
    RecyclerView recycle_viewmore;
    ViewmoreAdapter viewmoreAdapter;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    String id_category, id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmore);
        init();
        getRecipeAdapter();
    }

    private void init() {
        toolbar_viewmore = findViewById(R.id.toolbar_viewmore);
        setSupportActionBar(toolbar_viewmore);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title_viewmore = findViewById(R.id.title_viewmore);
        recycle_viewmore = findViewById(R.id.recycle_viewmore);
        Intent intent = getIntent();
        id_category = intent.getStringExtra("categoryId");
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

    private void getRecipeAdapter() {
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
                title_viewmore.setText("Công thức các món " + id_category);
                setAdapterViewMore(recipes);
                viewmoreAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getInfoUser(TextView txt_username, CircleImageView img_user, String id_user) {
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

    //------- methods bellow called on ViewMoreAdapter -------

    public void checkFavorite(CheckBox checkFavorite, String id_recipe) {
        DatabaseReference rootFavorite = mDatabase.child("Favorite");
        Query firebaseQuery = rootFavorite.orderByChild("userId").equalTo(id_user);
        firebaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Favorite favorite = dataSnapshot.getValue(Favorite.class);
                    if (favorite != null && id_recipe.equals(favorite.getRecipeId())) {
                        checkFavorite.setChecked(Boolean.TRUE);
                        checkFavorite.setBackgroundResource(R.drawable.ic_favorite_true);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void addRecipeToFavorite(Recipe recipe, CheckBox checkFavorite) {
        String key = mDatabase.push().getKey();

        Map<String, String> addFavorite = new HashMap<>();
        addFavorite.put("favoriteId", key);
        addFavorite.put("userId", id_user);
        addFavorite.put("recipeId", recipe.getRecipeId());
        addFavorite.put("recipeName", recipe.getRecipeName());
        addFavorite.put("recipeImage", recipe.getRecipeImage());
        addFavorite.put("createById", recipe.getUserId());

        DatabaseReference rootAddFavorite = mDatabase.child("Favorite");
        if (key != null) {
            rootAddFavorite.child(key).setValue(addFavorite);
        }

        checkFavorite.setChecked(Boolean.TRUE);
        checkFavorite.setBackgroundResource(R.drawable.ic_favorite_true);

        Toast.makeText(ViewmoreActivity.this, "Them favorite thanh cong", Toast.LENGTH_SHORT).show();
    }

    public void removeRecipeFromFavorite(String recipeId, CheckBox checkFavorite) {
        DatabaseReference rootRemoveFavorite = mDatabase.child("Favorite");
        System.out.println("id_user = " + id_user);
        Query firebaseQuery = rootRemoveFavorite.orderByChild("userId").equalTo(id_user);
        firebaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Favorite favorite = dataSnapshot.getValue(Favorite.class);
                    if (favorite != null && favorite.getRecipeId().equalsIgnoreCase(recipeId)) {
                        rootRemoveFavorite.child(favorite.getFavoriteId()).removeValue();
                        checkFavorite.setChecked(Boolean.FALSE);
                        checkFavorite.setBackgroundResource(R.drawable.ic_favorite);
                        Toast.makeText(ViewmoreActivity.this, "Xoa khoi danh sach yeu thic cua ban", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showRecipe(Recipe recipe) {
        Intent intent = new Intent(ViewmoreActivity.this, DetailRecipeActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}