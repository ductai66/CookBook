package com.tai06dothe.cookbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Activity.MainActivity;
import com.tai06dothe.cookbook.Activity.MyRecipeActivity;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.R;

import java.util.List;

public class MyRecipeAdapter extends RecyclerView.Adapter<MyRecipeAdapter.ViewHolder> {
    private Context mContext;
    private List<Recipe> mList;

    public MyRecipeAdapter(Context mContext, List<Recipe> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_myrecipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipeAdapter.ViewHolder holder, int position) {
        Recipe recipe = mList.get(position);
        holder.recipeName.setText(recipe.getRecipeName());
        Picasso.get().load(recipe.getRecipeImage()).into(holder.recipeImage);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName;
        ImageView recipeImage;
        Button btn_edit_recipe, btn_delete_recipe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            btn_edit_recipe = itemView.findViewById(R.id.btn_edit_recipe);
            btn_delete_recipe = itemView.findViewById(R.id.btn_delete_recipe);
            btn_edit_recipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Recipe recipe = mList.get(getAdapterPosition());
                    ((MyRecipeActivity)mContext).edit_recipe(recipe);
                }
            });
            btn_delete_recipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Recipe recipe = mList.get(position);
                    ((MyRecipeActivity)mContext).delete_recipe(recipe, position);

                }
            });
        }
    }
}
