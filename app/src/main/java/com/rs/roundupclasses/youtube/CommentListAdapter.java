package com.rs.roundupclasses.youtube;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rs.roundupclasses.R;
import com.rs.roundupclasses.models.Comment;
import java.util.ArrayList;
import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.Category_RecyclerView> {

  public   List<Comment> list = new ArrayList<>();
    Context context;

    public CommentListAdapter(Context context)
    {
        this.context = context;
    }

    @NonNull
    @Override
    public Category_RecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_listview,parent,false);
        return new CommentListAdapter.Category_RecyclerView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Category_RecyclerView holder, int position) {
      holder.categorytext.setText(list.get(position).getComment());
      holder.notificationtime.setText(list.get(position).getCreated_datetime());
    }

    public List<Comment> getCurrentList()
    {
        return list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Category_RecyclerView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categorytext,notificationtime;

        public Category_RecyclerView(View itemView) {
            super(itemView);
            categorytext = (TextView) itemView.findViewById( R.id.msg);
            notificationtime = (TextView) itemView.findViewById( R.id.notificationtime);
        }

        @Override
        public void onClick(View v) {
          /*  switch (v.getId())
            {
                case R.id.categorytext:
                    ((AddProductActivity) context).setcategory(list.get(getAdapterPosition()).getName(),list.get(getAdapterPosition()).getId().toString());
                    break;
            }*/
        }
    }
}
