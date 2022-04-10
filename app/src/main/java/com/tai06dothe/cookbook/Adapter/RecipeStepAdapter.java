package com.tai06dothe.cookbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Model.RecipeStep;
import com.tai06dothe.cookbook.R;

import java.util.List;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {

    private Context mContext;
    private List<RecipeStep> mListRecipeStep;

    public RecipeStepAdapter(Context mContext, List<RecipeStep> mListRecipeStep) {
        this.mContext = mContext;
        this.mListRecipeStep = mListRecipeStep;
    }

    @NonNull
    @Override
    public RecipeStepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_recipestep, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepAdapter.ViewHolder holder, int position) {
        RecipeStep step = mListRecipeStep.get(position);
        Picasso.get().load(step.getImage()).into(holder.item_image_recipestep);
        holder.item_description_recipestep.setText(step.getDescription());
    }

    @Override
    public int getItemCount() {
        return mListRecipeStep.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_description_recipestep;
        ImageView item_image_recipestep;
        Button btn_edit_recipestep, btn_delete_recipestep;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_description_recipestep = itemView.findViewById(R.id.item_description_recipestep);
            item_image_recipestep = itemView.findViewById(R.id.item_image_recipestep);
            btn_edit_recipestep = itemView.findViewById(R.id.btn_edit_recipestep);
            btn_delete_recipestep = itemView.findViewById(R.id.btn_delete_recipestep);

            btn_delete_recipestep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    RecipeStep item = mListRecipeStep.get(index);
                    FirebaseStorage reference = FirebaseStorage.getInstance();
                    StorageReference imageRef = reference.getReferenceFromUrl(item.getImage());
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mListRecipeStep.remove(index);
                            notifyDataSetChanged();
                            Toast.makeText(mContext, "Item deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
