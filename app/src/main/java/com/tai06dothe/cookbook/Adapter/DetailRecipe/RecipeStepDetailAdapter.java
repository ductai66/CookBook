package com.tai06dothe.cookbook.Adapter.DetailRecipe;

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
import com.tai06dothe.cookbook.Adapter.RecipeStepAdapter;
import com.tai06dothe.cookbook.Model.RecipeStep;
import com.tai06dothe.cookbook.R;

import java.util.List;

public class RecipeStepDetailAdapter extends RecyclerView.Adapter<RecipeStepDetailAdapter.ViewHolder> {
    private Context mContext;
    private List<RecipeStep> mListRecipeStep;

    public RecipeStepDetailAdapter(Context mContext, List<RecipeStep> mListRecipeStep) {
        this.mContext = mContext;
        this.mListRecipeStep = mListRecipeStep;
    }

    @NonNull
    @Override
    public RecipeStepDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_recipestep_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepDetailAdapter.ViewHolder holder, int position) {
        RecipeStep step = mListRecipeStep.get(position);
        holder.txt_number_step.setText("Bước " + (position + 1));
        Picasso.get().load(step.getImage()).into(holder.item_image_recipestep);
        holder.item_description_recipestep.setText(step.getDescription());
    }

    @Override
    public int getItemCount() {
        return mListRecipeStep.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_number_step, item_description_recipestep;
        ImageView item_image_recipestep;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_number_step = itemView.findViewById(R.id.txt_number_step);
            item_description_recipestep = itemView.findViewById(R.id.item_description_recipestep);
            item_image_recipestep = itemView.findViewById(R.id.item_image_recipestep);
        }
    }
}
