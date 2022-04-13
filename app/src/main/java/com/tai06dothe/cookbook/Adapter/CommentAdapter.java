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
import com.tai06dothe.cookbook.Activity.DetailRecipeActivity;
import com.tai06dothe.cookbook.Model.Comment;
import com.tai06dothe.cookbook.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mContext;
    private List<Comment> mListComment;

    public CommentAdapter(Context mContext, List<Comment> mListComment) {
        this.mContext = mContext;
        this.mListComment = mListComment;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment = mListComment.get(position);
        ((DetailRecipeActivity) mContext).getInfoUser(holder.userName_Comment, holder.userImage_Comment, comment.getUserId());
        holder.commentContent.setText(comment.getContent());
        Picasso.get().load(comment.getImage()).into(holder.commentImage);

    }

    @Override
    public int getItemCount() {
        if (mListComment != null) {
            return mListComment.size();
        }
        return 0;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName_Comment, commentContent;
        ImageView commentImage;
        CircleImageView userImage_Comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName_Comment = itemView.findViewById(R.id.userName_Comment);
            userImage_Comment = itemView.findViewById(R.id.userImage_Comment);
            commentContent = itemView.findViewById(R.id.commentContent);
            commentImage = itemView.findViewById(R.id.commentImage);
        }
    }
}
