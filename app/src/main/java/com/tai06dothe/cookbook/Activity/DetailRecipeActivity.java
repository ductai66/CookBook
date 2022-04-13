package com.tai06dothe.cookbook.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Adapter.CommentAdapter;
import com.tai06dothe.cookbook.Adapter.DetailRecipe.IngredientDetailAdapter;
import com.tai06dothe.cookbook.Adapter.DetailRecipe.RecipeStepDetailAdapter;
import com.tai06dothe.cookbook.Adapter.IngredientAdapter;
import com.tai06dothe.cookbook.Adapter.RecipeStepAdapter;
import com.tai06dothe.cookbook.Model.Comment;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.Model.RecipeStep;
import com.tai06dothe.cookbook.Model.User;
import com.tai06dothe.cookbook.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailRecipeActivity extends AppCompatActivity {

    TextInputEditText etxt_comment;
    ImageView recipeImageDetail;
    ImageButton send_add_comment;
    TextView recipeNameDetail;
    RecyclerView recycle_ingredients, recycle_recipeSteps, recycle_comment;
    List<String> ingredients;
    List<RecipeStep> recipeSteps;
    List<Comment> comments;
    String id_user;
    Recipe recipe;
    IngredientDetailAdapter ingredientDetailAdapter;
    RecipeStepDetailAdapter recipeStepDetailAdapter;
    CommentAdapter commentAdapter;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);
        init();
        getRecipe();
        getComment();
        processEvent();
    }

    private void init() {
        send_add_comment = findViewById(R.id.send_add_comment);
        etxt_comment = findViewById(R.id.etxt_comment);
        recycle_comment = findViewById(R.id.recycle_comment);

        recipeImageDetail = findViewById(R.id.recipeImageDetail);
        recipeNameDetail = findViewById(R.id.recipeNameDetail);
        recycle_ingredients = findViewById(R.id.recycle_ingredients);
        recycle_recipeSteps = findViewById(R.id.recycle_recipeSteps);

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        ingredients = recipe.getIngredientList();
        recipeSteps = recipe.getRecipeStepList();
        comments = new ArrayList<>();

        Intent intent = getIntent();
        id_user = intent.getStringExtra("userId");
    }

    private void getRecipe() {
        Picasso.get().load(recipe.getRecipeImage()).into(recipeImageDetail);
        recipeNameDetail.setText(recipe.getRecipeName());
        setIngredientAdapter(ingredients);
        setRecipeStepAdapter(recipeSteps);
    }

    private void getComment() {
        DatabaseReference rootComment = mDatabase.child("Comment");
        Query firebaseQueryComments = rootComment.orderByChild("recipeId").equalTo(recipe.getRecipeId());

        firebaseQueryComments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    comments.add(comment);
                }
                setCommentAdapter(comments);
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setIngredientAdapter(List<String> mList) {
        ingredientDetailAdapter = new IngredientDetailAdapter(DetailRecipeActivity.this, mList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DetailRecipeActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recycle_ingredients.setLayoutManager(gridLayoutManager);
        recycle_ingredients.setAdapter(ingredientDetailAdapter);
        recycle_ingredients.setNestedScrollingEnabled(true);
        recycle_ingredients.setHasFixedSize(true);
    }

    private void setRecipeStepAdapter(List<RecipeStep> mList) {
        recipeStepDetailAdapter = new RecipeStepDetailAdapter(DetailRecipeActivity.this, mList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DetailRecipeActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recycle_recipeSteps.setLayoutManager(gridLayoutManager);
        recycle_recipeSteps.setAdapter(recipeStepDetailAdapter);
        recycle_recipeSteps.setNestedScrollingEnabled(true);
        recycle_recipeSteps.setHasFixedSize(true);
    }

    private void setCommentAdapter(List<Comment> mList) {
        commentAdapter = new CommentAdapter(DetailRecipeActivity.this, mList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DetailRecipeActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recycle_comment.setLayoutManager(gridLayoutManager);
        recycle_comment.setAdapter(commentAdapter);
        recycle_comment.setNestedScrollingEnabled(true);
        recycle_comment.setHasFixedSize(true);
    }

    // add feature comment
    private void processEvent() {
        etxt_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    send_add_comment.setVisibility(View.VISIBLE);
                } else {
                    send_add_comment.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        send_add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkComment();
            }
        });
    }

    private void checkComment() {
        String content = etxt_comment.getText().toString().trim();

        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Please enter comment !", Toast.LENGTH_SHORT).show();
            return;
        }
        // process add comment
        addComment(content);
    }

    private void addComment(String content) {
        String key = mDatabase.push().getKey();

        Comment comment = new Comment();
        comment.setCommentId(key);
        comment.setContent(content);
        comment.setImage(recipe.getRecipeImage());
        comment.setUserId(id_user);
        comment.setRecipeId(recipe.getRecipeId());

        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // set time for create at
        comment.setCreateAt(formatter.format(ts));

        DatabaseReference rootAddComment = mDatabase.child("Comment");
        rootAddComment.child(key).setValue(comment);

        Toast.makeText(this, "Add comment success", Toast.LENGTH_SHORT).show();
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