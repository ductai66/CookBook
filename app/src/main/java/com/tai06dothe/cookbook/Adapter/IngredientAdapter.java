package com.tai06dothe.cookbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tai06dothe.cookbook.R;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mListIngredient;

    public IngredientAdapter(Context mContext, List<String> mListIngredient) {
        this.mContext = mContext;
        this.mListIngredient = mListIngredient;
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_ingredient, parent, false);
        return new IngredientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {
        String item = mListIngredient.get(position);
        if (item == null) {
            return;
        }
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
        TextView item_name_ingredient;
        ImageButton item_delete_ingredient;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name_ingredient = itemView.findViewById(R.id.item_name_ingredient);
            item_delete_ingredient = itemView.findViewById(R.id.item_delete_ingredient);

            item_delete_ingredient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    mListIngredient.remove(index);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
