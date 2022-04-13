package com.tai06dothe.cookbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Activity.InfoUserActivity;
import com.tai06dothe.cookbook.Activity.MainActivity;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoUserAdapter extends RecyclerView.Adapter<InfoUserAdapter.ViewHolder> {

    private Context mContext;
    private List<Recipe> mList;

    public InfoUserAdapter(Context mContext, List<Recipe> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public InfoUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_recipeuser, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoUserAdapter.ViewHolder holder, int position) {
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeImage = itemView.findViewById(R.id.recipeImage);
        }
    }
}
