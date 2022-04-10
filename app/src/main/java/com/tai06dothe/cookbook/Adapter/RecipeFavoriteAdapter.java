package com.tai06dothe.cookbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Activity.RecipeFavoriteActivity;
import com.tai06dothe.cookbook.Activity.ViewmoreActivity;
import com.tai06dothe.cookbook.Model.Favorite;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeFavoriteAdapter extends RecyclerView.Adapter<RecipeFavoriteAdapter.ViewHolder> {
    private Context mContext;
    private List<Favorite> mListFavorite;

    public RecipeFavoriteAdapter(Context mContext, List<Favorite> mListFavorite) {
        this.mContext = mContext;
        this.mListFavorite = mListFavorite;
    }

    @NonNull
    @Override
    public RecipeFavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeFavoriteAdapter.ViewHolder holder, int position) {
        Favorite favorite = mListFavorite.get(position);
        holder.recipeName.setText(favorite.getRecipeName());
        Picasso.get().load(favorite.getRecipeImage()).into(holder.recipeImage);
        ((RecipeFavoriteActivity)mContext).getUserName(holder.userName, favorite.getCreateById());
        ((RecipeFavoriteActivity)mContext).getImageUser(holder.img_user, favorite.getCreateById());
        holder.checkFavorite.setBackgroundResource(R.drawable.ic_favorite_true);
        holder.checkFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    holder.checkFavorite.setBackgroundResource(R.drawable.ic_favorite);
                    ((RecipeFavoriteActivity)mContext).removeFavorite(favorite);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListFavorite.size();
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
        }
    }
}
