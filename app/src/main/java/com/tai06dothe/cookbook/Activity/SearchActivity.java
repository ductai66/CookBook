package com.tai06dothe.cookbook.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Adapter.SearchAdapter;
import com.tai06dothe.cookbook.Model.Favorite;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recycle_search;
    TextInputEditText etxt_search;
    ImageButton clear_text;
    TextView empty_search;
    String id_user;
    private List<Recipe> recipes;
    private SearchAdapter searchAdapter;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();
        Click_event();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etxt_search = findViewById(R.id.etxt_search);
        clear_text = findViewById(R.id.clear_text);
        empty_search =findViewById(R.id.empty_search);
        recycle_search = findViewById(R.id.recycle_search);

        Intent intent = getIntent();
        id_user = intent.getStringExtra("id_user");

    }

    private void Click_event(){
        clear_text.setVisibility(View.GONE);
        etxt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    clear_text.setVisibility(View.VISIBLE);
                }else {
                    clear_text.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etxt_search.getText().clear();
            }
        });
    }

    //icon seacrch on toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_bar:
                String searchText = etxt_search.getText().toString().trim();
                if (searchText.equals("")){
                    Toast.makeText(this, "Bạn chưa nhập từ khóa", Toast.LENGTH_SHORT).show();
                }else{
                    recipes = new ArrayList<>();
                    setRecipeAdapter(recipes);
                    processSearch_Test(searchText);
                    closeKeyboard();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setRecipeAdapter(List<Recipe> mList) {
        searchAdapter = new SearchAdapter(SearchActivity.this, mList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recycle_search.setLayoutManager(gridLayoutManager);
        recycle_search.setAdapter(searchAdapter);
        recycle_search.setNestedScrollingEnabled(true);
        recycle_search.setHasFixedSize(true);
    }

    private void processSearch_Test(String searchText) {

        DatabaseReference rootRecipeList = mDatabase.child("Recipe");
        rootRecipeList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipe> allRecipe = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    allRecipe.add(recipe);
                }

                for (int i = 0; i < allRecipe.size(); i++) {
                   if (allRecipe.get(i).getRecipeName().toLowerCase().contains(searchText)) {
                       recipes.add(allRecipe.get(i));
                       searchAdapter.notifyDataSetChanged();
                       empty_search.setVisibility(View.GONE);
                   }
                   else {
                       empty_search.setVisibility(View.VISIBLE);
                   }
                }
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

    public void showRecipe(Recipe recipe) {
        Intent intent = new Intent(SearchActivity.this, DetailRecipeActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
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

        Toast.makeText(SearchActivity.this, "Them favorite thanh cong", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SearchActivity.this, "Xoa khoi danh sach yeu thic cua ban", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view!=null){
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}