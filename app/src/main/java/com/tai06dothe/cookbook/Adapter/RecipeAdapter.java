package com.tai06dothe.cookbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Activity.MainActivity;
import com.tai06dothe.cookbook.Activity.ViewmoreActivity;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private Context mContext;
    private List<Recipe> mList;

    public RecipeAdapter(Context mContext, List<Recipe> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_recipe, parent, false);
        return new RecipeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        Recipe item = mList.get(position);
        ((MainActivity) mContext).getInfoUser(holder.userName, holder.img_user, item.getUserId());
        holder.numberFavorite.setText(String.valueOf(item.getFavoriteNumber()));
        holder.recipeName.setText(item.getRecipeName());
        Picasso.get().load(item.getRecipeImage()).into(holder.recipeImage);

        // check favorite
        ((MainActivity) mContext).checkFavorite(holder.checkFavorite, item.getRecipeId());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName, userName, numberFavorite;
        ImageView recipeImage;
        CircleImageView img_user;
        CheckBox checkFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            img_user = itemView.findViewById(R.id.img_user);
            checkFavorite = itemView.findViewById(R.id.checkFavorite);
            numberFavorite = itemView.findViewById(R.id.numberFavorite);

            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Recipe recipe = mList.get(getAdapterPosition());
                    ((MainActivity) mContext).showInfoUser(recipe);
                }
            });

            recipeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Recipe recipe = mList.get(getAdapterPosition());
                    ((MainActivity) mContext).showRecipe(recipe);
                }
            });

            checkFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isCheck = checkFavorite.isChecked();
                    int position = getAdapterPosition();
                    Recipe recipe = mList.get(position);
                    if (!isCheck) {
                        recipe.setFavoriteNumber(recipe.getFavoriteNumber() - 1);
                        ((MainActivity) mContext).removeRecipeFromFavorite(recipe, checkFavorite);
                        numberFavorite.setText(String.valueOf(recipe.getFavoriteNumber()));
                    } else {
                        recipe.setFavoriteNumber(recipe.getFavoriteNumber() + 1);
                        ((MainActivity) mContext).addRecipeToFavorite(recipe, checkFavorite);
                        numberFavorite.setText(String.valueOf(recipe.getFavoriteNumber()));
                    }
                }
            });
        }
    }
}
