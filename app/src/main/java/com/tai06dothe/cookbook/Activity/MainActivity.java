package com.tai06dothe.cookbook.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Adapter.CategoryAdapter;
import com.tai06dothe.cookbook.Adapter.RecipeAdapter;
import com.tai06dothe.cookbook.LoginSignup.ChangePasswordActivity;
import com.tai06dothe.cookbook.LoginSignup.LoginActivity;
import com.tai06dothe.cookbook.Model.Category;
import com.tai06dothe.cookbook.Model.Favorite;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.OOP.CategoryRecipes;
import com.tai06dothe.cookbook.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle mDrawerToggle;
    NavigationView navigationView;
    TextView name_user, email_user;
    RecyclerView recycleView_main;
    CircleImageView img_user;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private List<CategoryRecipes> categoryRecipesList;
    private boolean back_home = Boolean.FALSE;
    private String id_user;

    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getUser();
    }

    private void init() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        img_user = view.findViewById(R.id.img_user);
        name_user = view.findViewById(R.id.name_user);
        email_user = view.findViewById(R.id.email_user);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerLayout = findViewById(R.id.draw_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        recycleView_main = findViewById(R.id.recycleView_main);

        Intent intent = getIntent();
        id_user = intent.getStringExtra("userId");
    }

    private void getUser() {
        DatabaseReference mRef1 = mDatabase.child("User").child(id_user);
        mRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name_user.setText(dataSnapshot.child("name").getValue().toString());
                email_user.setText(dataSnapshot.child("email").getValue().toString());
                String linkImage = dataSnapshot.child("avatar").getValue().toString();
                Picasso.get().load(linkImage).into(img_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setCategoryAdapter(List<CategoryRecipes> mList) {
        categoryAdapter = new CategoryAdapter(MainActivity.this, mList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recycleView_main.setLayoutManager(gridLayoutManager);
        recycleView_main.setAdapter(categoryAdapter);
        recycleView_main.setNestedScrollingEnabled(true);
        recycleView_main.setHasFixedSize(true);
    }

    private void processCategoryRecipes() {
        DatabaseReference rootCategoryRecipes = mDatabase.child("Category");
        rootCategoryRecipes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String categoryName = dataSnapshot.getValue(String.class);

                    CategoryRecipes categoryRecipes = new CategoryRecipes();
                    categoryRecipes.setCategory(new Category(categoryName));

                    processRecipesOfCategory(categoryName, categoryRecipes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void processRecipesOfCategory(String categoryName, CategoryRecipes categoryRecipes) {
        DatabaseReference rootRecipes = mDatabase.child("Recipe");

        Query firebaseQueryRecipes = rootRecipes.orderByChild("categoryId").equalTo(categoryName);
        firebaseQueryRecipes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Recipe recipe = item.getValue(Recipe.class);
                    recipes.add(recipe);
                }
                if (recipes.size() > 0) {
                    categoryRecipes.setRecipes(recipes);
                    categoryRecipesList.add(categoryRecipes);
                    categoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displayView(item.getItemId());
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayView(int id) {
        switch (id) {
            case R.id.menu_home:
                back_home = true;
                break;
            case R.id.my_recipe:
                Intent intent = new Intent(MainActivity.this, MyRecipeActivity.class);
                intent.putExtra("userId", id_user);
                startActivity(intent);
                break;
            case R.id.my_profile:
                Intent intent1 = new Intent(MainActivity.this, ProfileActivity.class);
                intent1.putExtra("userId", id_user);
                startActivity(intent1);
                break;
            case R.id.menu_favorite:
                Intent intent2 = new Intent(MainActivity.this, RecipeFavoriteActivity.class);
                intent2.putExtra("userId", id_user);
                startActivity(intent2);
                break;
            case R.id.change_password:
                Intent intent3 = new Intent(MainActivity.this, ChangePasswordActivity.class);
                intent3.putExtra("userId", id_user);
                startActivity(intent3);
                break;
            case R.id.logout:
                Intent intent4 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent4);
                break;
        }
    }

    // ph???n setup menu search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        return true;
    }

    // x??? l?? khi click v??o icon search
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_bar:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("userId", id_user);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!back_home) {
                displayView(R.id.menu_home);
            } else {
                moveTaskToBack(true);
            }
        }
    }

    @Override
    protected void onResume() {
        categoryRecipesList = new ArrayList<>();
        processCategoryRecipes();
        setCategoryAdapter(categoryRecipesList);
        super.onResume();
    }

    //------- methods bellow called on RecipeAdapter -------

    public void showViewmore(String id_category) {
        Intent intent = new Intent(MainActivity.this, ViewmoreActivity.class);
        intent.putExtra("categoryId", id_category);
        intent.putExtra("userId", id_user);
        startActivity(intent);
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

    public void showRecipe(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, DetailRecipeActivity.class);
        intent.putExtra("userId", id_user);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }

    public void showInfoUser(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, InfoUserActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
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

        Toast.makeText(MainActivity.this, "Them favorite thanh cong", Toast.LENGTH_SHORT).show();
    }

    public void removeRecipeFromFavorite(String recipeId, CheckBox checkFavorite) {
        DatabaseReference rootRemoveFavorite = mDatabase.child("Favorite");

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
                        Toast.makeText(MainActivity.this, "Xoa khoi danh sach yeu thic cua ban", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}