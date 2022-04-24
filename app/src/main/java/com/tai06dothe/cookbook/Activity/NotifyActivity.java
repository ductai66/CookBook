package com.tai06dothe.cookbook.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tai06dothe.cookbook.Adapter.ViewmoreAdapter;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.R;

import java.util.ArrayList;
import java.util.List;

public class NotifyActivity extends AppCompatActivity {

    Toolbar toolbar_notification;
    TextView title_notification;
    RecyclerView recycle_notification;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        init();
    }

    private void init() {
        toolbar_notification = findViewById(R.id.toolbar_viewmore);
        setSupportActionBar(toolbar_notification);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title_notification = findViewById(R.id.title_viewmore);
        recycle_notification = findViewById(R.id.recycle_viewmore);
        Intent intent = getIntent();
        id_user = intent.getStringExtra("userId");
    }

    private void setAdapterNotify(List<Recipe> mList) {
//        viewmoreAdapter = new ViewmoreAdapter(this, list);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
//        recycle_viewmore.setLayoutManager(gridLayoutManager);
//        recycle_viewmore.setAdapter(viewmoreAdapter);
//        recycle_viewmore.setHasFixedSize(true);
//        viewmoreAdapter.notifyDataSetChanged();
    }

    private void processListNotify() {
        DatabaseReference rootNotification = mDatabase.child("Recipe");
        rootNotification.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipe> mList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    if (recipe != null
                            && "NEW".equals(recipe.getStatus())
                            && !recipe.getUserId().equals(id_user)) {

                        mList.add(recipe);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}