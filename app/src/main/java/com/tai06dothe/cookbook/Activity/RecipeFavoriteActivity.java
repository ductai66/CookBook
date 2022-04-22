package com.tai06dothe.cookbook.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Adapter.RecipeFavoriteAdapter;
import com.tai06dothe.cookbook.Adapter.ViewmoreAdapter;
import com.tai06dothe.cookbook.Model.Favorite;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeFavoriteActivity extends AppCompatActivity {
    Toolbar toolbar_favorite;
    private RecyclerView recycle_recipeFavorite;
    private RecipeFavoriteAdapter recipeFavoriteAdapter;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    String id_user;
    List<Favorite> favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_favorite);
        init();
        getAdapterFavorite();
    }

    private void init(){
        toolbar_favorite = findViewById(R.id.toolbar_favorite);
        setSupportActionBar(toolbar_favorite);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycle_recipeFavorite = (RecyclerView) findViewById(R.id.recycle_recipeFavorite);
        Intent intent = getIntent();
        id_user = intent.getStringExtra("userId");
    }

    private void setAdapterFavorite(List<Favorite> list) {
        recipeFavoriteAdapter = new RecipeFavoriteAdapter(this, list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recycle_recipeFavorite.setLayoutManager(gridLayoutManager);
        recycle_recipeFavorite.setAdapter(recipeFavoriteAdapter);
        recycle_recipeFavorite.setHasFixedSize(true);
        recipeFavoriteAdapter.notifyDataSetChanged();
    }

    private void getAdapterFavorite() {
        DatabaseReference mRef = mDatabase.child("Favorite");
        Query firebaseQueryRecipes = mRef.orderByChild("userId").equalTo(id_user);

        firebaseQueryRecipes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Favorite favorite = dataSnapshot.getValue(Favorite.class);
                    favoriteList.add(favorite);
                }
                setAdapterFavorite(favoriteList);
                recipeFavoriteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeFavorite(Favorite favorite) {
        DatabaseReference rootFavorite = mDatabase.child("Favorite").child(favorite.getFavoriteId());
//        DatabaseReference rootRecipe = mDatabase.child("Recipe").child(id_recipe);
        rootFavorite.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                favoriteList.remove(favorite);

                // process update number favorite for recipe
                DatabaseReference rootNumberFavoriteRecipe = mDatabase.child("Recipe").child(favorite.getRecipeId());
                rootNumberFavoriteRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Recipe recipe = snapshot.getValue(Recipe.class);
                        rootNumberFavoriteRecipe.child("favoriteNumber").setValue(recipe.getFavoriteNumber() - 1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                setAdapterFavorite(favoriteList);
                recipeFavoriteAdapter.notifyDataSetChanged();
                Toast.makeText(RecipeFavoriteActivity.this, "Xóa thành công khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getnumberFavorite(TextView txt_number, String id_recipe) {
        DatabaseReference rootRecipe = mDatabase.child("Recipe").child(id_recipe);
        rootRecipe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String number = snapshot.child("favoriteNumber").getValue().toString();
                txt_number.setText(number);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}