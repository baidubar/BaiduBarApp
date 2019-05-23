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
import com.example.baidupostbar.bean.MsgAttention;

import java.util.List;

public class MsgAttentionAdapter extends RecyclerView.Adapter<MsgAttentionAdapter.ViewHolder>{

    private List<MsgAttention> list;
    private Context context;
    private boolean hasMore = true;


    public MsgAttentionAdapter(List<MsgAttention> list,Context context){
        this.list = list;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_user;
        TextView tv_userName;
        View msgAttentionView;
        Button btn_delete;

        public ViewHolder(View view) {
            super(view);
            msgAttentionView = view;
            iv_user = view.findViewById(R.id.iv_user);
            tv_userName = view.findViewById(R.id.tv_userName);
            btn_delete = view.findViewById(R.id.btnDelete);
        }
    }

    public MsgAttentionAdapter(List<MsgAttention> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MsgAttentionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context == null){
            context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_msg_attention, viewGroup, false);
        final MsgAttentionAdapter.ViewHolder holder = new MsgAttentionAdapter.ViewHolder(view);
        holder.msgAttentionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                MsgAttention msgAttention = list.get(position);
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
    public void onBindViewHolder(@NonNull MsgAttentionAdapter.ViewHolder viewHolder, int i) {
        MsgAttention msgAttention = list.get(i);
        viewHolder.tv_userName.setText(msgAttention.getUsername());
        Glide.with(context).load("http://139.199.84.147"+msgAttention.getAvatar()).into(viewHolder.iv_user);
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
    private UserCommentAdapter.onSwipeListener mOnSwipeListener;

    public UserCommentAdapter.onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(UserCommentAdapter.onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }
    // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
    public void updateList(List<MsgAttention> newDatas, boolean hasMore) {
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            list.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }
}