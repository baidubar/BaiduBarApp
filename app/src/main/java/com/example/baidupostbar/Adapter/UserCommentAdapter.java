package com.example.baidupostbar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.UserComment;

import java.util.List;

public class UserCommentAdapter extends RecyclerView.Adapter<UserCommentAdapter.ViewHolder>{

    private List<UserComment> list;
    private Context context;
    private boolean hasMore = true;


    public UserCommentAdapter(List<UserComment> list,Context context){
        this.list = list;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_author;
        TextView tv_author;
        View userCommentView;
        TextView tv_time;
        TextView tv_floorContent;
        TextView tv_postContent;
        Button btn_delete;

        public ViewHolder(View view) {
            super(view);
            userCommentView = view;
            iv_author = (ImageView) view.findViewById(R.id.iv_author);
            tv_author = (TextView) view.findViewById(R.id.tv_author);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_floorContent = view.findViewById(R.id.tv_floorContent);
            tv_postContent = view.findViewById(R.id.tv_postContent);
            btn_delete = view.findViewById(R.id.btnDelete);
        }
    }

    public UserCommentAdapter(List<UserComment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context == null){
            context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_msg_setfloor, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.userCommentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                UserComment userComment = list.get(position);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (null != mOnSwipeListener) {
//                    //Toast.makeText(context, "删除", Toast.LENGTH_SHORT).show();
//                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
//                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
//                    //((CstSwipeDelMenu) holder.itemView).quickClose();
//                    mOnSwipeListener.onDel(holder.getAdapterPosition());
                list.remove(i);
                notifyItemRemoved(i);
                notifyDataSetChanged();
//                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserCommentAdapter.ViewHolder viewHolder, int i) {
        UserComment userComment = list.get(i);
        viewHolder.tv_author.setText(userComment.getWriter_name());
        viewHolder.tv_time.setText(userComment.getTime());
        Glide.with(context).load("http://139.199.84.147"+userComment.getWriter_avatar()).into(viewHolder.iv_author);
        viewHolder.tv_postContent.setText("我:" + userComment.getPost_content());
        viewHolder.tv_floorContent.setText(userComment.getFloor_content());
    }

    @Override
    public int getItemCount() {
        if(null==list) return 0;
        else return list.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public interface onSwipeListener {
        void onDel(int pos);

    }
    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }
    // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
    public void updateList(List<UserComment> newDatas, boolean hasMore) {
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            list.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }
}