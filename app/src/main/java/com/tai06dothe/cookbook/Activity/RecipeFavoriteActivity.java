package com.tai06dothe.cookbook.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
        getAdapter();
    }

    private void init(){
        recycle_recipeFavorite = (RecyclerView) findViewById(R.id.recycle_recipeFavorite);
        favoriteList = new ArrayList<>();
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

    private void getAdapter() {
        DatabaseReference mRef = mDatabase.child("Favorite");
        Query firebaseQueryRecipes = mRef.orderByChild("userId").equalTo(id_user);

        firebaseQueryRecipes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Favorite> favoriteList = new ArrayList<>();
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
        DatabaseReference mRef = mDatabase.child("Favorite").child(favorite.getFavoriteId());
        mRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                favoriteList.remove(favorite);
                setAdapterFavorite(favoriteList);
                recipeFavoriteAdapter.notifyDataSetChanged();
                Toast.makeText(RecipeFavoriteActivity.this, "Xóa thành công khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getUserName(TextView txt_username, String id_user) {
        mDatabase.child("User").child(id_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("name").getValue().toString();
                txt_username.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getImageUser(CircleImageView img_user, String id_user) {
        mDatabase.child("User").child(id_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String img = snapshot.child("avatar").getValue().toString();
                Picasso.get().load(img).into(img_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}