package com.tai06dothe.cookbook.Adapter.DetailRecipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tai06dothe.cookbook.Adapter.IngredientAdapter;
import com.tai06dothe.cookbook.R;

import java.util.List;

public class IngredientDetailAdapter extends RecyclerView.Adapter<IngredientDetailAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mListIngredient;

    public IngredientDetailAdapter(Context mContext, List<String> mListIngredient) {
        this.mContext = mContext;
        this.mListIngredient = mListIngredient;
    }
    @NonNull
    @Override
    public IngredientDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_ingredient_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientDetailAdapter.ViewHolder holder, int position) {
        String item = mListIngredient.get(position);
        holder.txt_number_ingredient.setText(String.valueOf(position + 1));
        holder.item_name_ingredient.setText(item);
    }

    @Override
    public int getItemCount() {
        if (mListIngredient != null) {
            return mListIngredient.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_number_ingredient, item_name_ingredient;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_number_ingredient = itemView.findViewById(R.id.txt_number_ingredient);
            item_name_ingredient = itemView.findViewById(R.id.item_name_ingredient);
        }
    }
}
