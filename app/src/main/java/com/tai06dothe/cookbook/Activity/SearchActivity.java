package com.tai06dothe.cookbook.Activity;

import android.os.Bundle;
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
import com.tai06dothe.cookbook.Adapter.SearchAdapter;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recycle_search;
    TextInputEditText etxt_search;
    ImageButton clear_text;
    TextView empty_search;
//    EditText etxt_search;
//    Button btn_search;


    private List<Recipe> recipes;
    private SearchAdapter searchAdapter;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();
        processEvent();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        etxt_search = findViewById(R.id.etxt_search);
        clear_text = (ImageButton) findViewById(R.id.clear_text);
        empty_search =findViewById(R.id.empty_search);
        recycle_search = findViewById(R.id.recycle_search);
    }

    private void processEvent() {
//        btn_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                recipes = new ArrayList<>();
//                processSearch_Test();
//                setAdapter(recipes);
//            }
//        });
    }

    private void setAdapter(List<Recipe> mList) {
        searchAdapter = new SearchAdapter(SearchActivity.this, mList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recycle_search.setLayoutManager(gridLayoutManager);
        recycle_search.setAdapter(searchAdapter);
        recycle_search.setNestedScrollingEnabled(true);
        recycle_search.setHasFixedSize(true);
    }

    private void processSearch() {
        String searchText = etxt_search.getText().toString().trim();

        DatabaseReference rootSearch = root.child("Recipe");
        Query firebaseSearchQuery = rootSearch.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren())
                {
                    Recipe recipe = item.getValue(Recipe.class);
                    recipes.add(recipe);
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void processSearch_Test() {
        String searchText = etxt_search.getText().toString().trim().toLowerCase();

        DatabaseReference rootRecipeList = root.child("Recipe");
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
                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, "Get list recipe failed !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}