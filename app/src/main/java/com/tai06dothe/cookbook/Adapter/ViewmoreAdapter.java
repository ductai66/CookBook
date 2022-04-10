package com.tai06dothe.cookbook.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Activity.MainActivity;
import com.tai06dothe.cookbook.Activity.ViewmoreActivity;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewmoreAdapter extends RecyclerView.Adapter<ViewmoreAdapter.ViewHolder> {
    private Context mContext;
    private List<Recipe> mListRecipe;

    public ViewmoreAdapter(Context mContext, List<Recipe> mListRecipe) {
        this.mContext = mContext;
        this.mListRecipe = mListRecipe;
    }

    @NonNull
    @Override
    public ViewmoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewmoreAdapter.ViewHolder holder, int position) {
        Recipe recipe = mListRecipe.get(position);
        holder.recipeName.setText(recipe.getRecipeName());
        Picasso.get().load(recipe.getRecipeImage()).into(holder.recipeImage);
        ((ViewmoreActivity)mContext).getinfoUser(holder.userName, holder.img_user, recipe.getUserId());
        ((ViewmoreActivity)mContext).checkFavorite(holder.checkFavorite, recipe.getRecipeId());
    }

    @Override
    public int getItemCount() {
        return mListRecipe.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, recipeName;
        ImageView recipeImage;
        CircleImageView img_user;
        CheckBox checkFavorite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            userName = itemView.findViewById(R.id.userName);
            recipeName = itemView.findViewById(R.id.recipeName);
            img_user = itemView.findViewById(R.id.img_user);
            checkFavorite = itemView.findViewById(R.id.checkFavorite);
//            checkFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    if (compoundButton.isChecked()){
//                        checkFavorite.setBackgroundResource(R.drawable.ic_favorite_true);
//                        Recipe recipe = mListRecipe.get(getAdapterPosition());
//                        ((ViewmoreActivity)mContext).addRecipeFavorite(recipe.getRecipeId(), recipe.getRecipeName(), recipe.getRecipeImage(), recipe.getIngredientList(), recipe.getRecipeStepList(), recipe.getUserId());
//                    }
//                    else {
//                        checkFavorite.setBackgroundResource(R.drawable.ic_favorite);
//                        Recipe recipe = mListRecipe.get(getAdapterPosition());
//                        ((ViewmoreActivity)mContext).removeRecipeFavorite(recipe.getRecipeId());
//                    }
//                }
//            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Recipe recipe = mListRecipe.get(getAdapterPosition());
                    ((ViewmoreActivity) mContext).showRecipe(recipe);

                }
            });
        }
    }
}
