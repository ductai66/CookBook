package com.tai06dothe.cookbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tai06dothe.cookbook.Activity.MainActivity;
import com.tai06dothe.cookbook.Model.Category;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.OOP.CategoryRecipes;
import com.tai06dothe.cookbook.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context mContext;
    private List<CategoryRecipes> mList;
    private RecipeAdapter recipeAdapter;

    public CategoryAdapter(Context mContext, List<CategoryRecipes> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_category_recipes, parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryRecipes item = mList.get(position);
        if (item.getRecipes().size() > 0) {
            holder.categoryName.setText(item.getCategory().getCategoryName());
            setAdapter(item.getRecipes(), holder.recycle_recipes);
        }
    }

    private void setAdapter(List<Recipe> mList, RecyclerView recyclerView) {
        recipeAdapter = new RecipeAdapter(mContext, mList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        recipeAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName, viewmore;
        RecyclerView recycle_recipes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            recycle_recipes = itemView.findViewById(R.id.recycle_recipes);
            viewmore = itemView.findViewById(R.id.view_more);
            viewmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CategoryRecipes item = mList.get(getAdapterPosition());
                    String id_category = item.getCategory().getCategoryName();
                    ((MainActivity)mContext).showViewmore(id_category);

                }
            });

        }
    }
}
