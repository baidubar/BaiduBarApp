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
import com.example.baidupostbar.bean.MsgLike;

import java.util.List;

public class MsgLikeAdapter extends RecyclerView.Adapter<MsgLikeAdapter.ViewHolder>{

    private List<MsgLike> list;
    private Context context;
    private boolean hasMore = true;


    public MsgLikeAdapter(List<MsgLike> list,Context context){
        this.list = list;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_author;
        TextView tv_author;
        View msgLike;
        TextView tv_postContent;
        Button btn_delete;


        public ViewHolder(View view) {
            super(view);
            msgLike = view;
            iv_author = (ImageView) view.findViewById(R.id.iv_author);
            tv_author = (TextView) view.findViewById(R.id.tv_author);
            tv_postContent = view.findViewById(R.id.tv_postContent);
            btn_delete = view.findViewById(R.id.btnDelete);
        }
    }

    public MsgLikeAdapter(List<MsgLike> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MsgLikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context == null){
            context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_msg_like, viewGroup, false);
        final MsgLikeAdapter.ViewHolder holder = new MsgLikeAdapter.ViewHolder(view);
        holder.msgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                MsgLike msgLike = list.get(position);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount() - position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MsgLikeAdapter.ViewHolder viewHolder, int i) {
        MsgLike msgLike = list.get(i);
        viewHolder.tv_author.setText(msgLike.getPerson_name());
        Glide.with(context).load("http://139.199.84.147"+msgLike.getPerson_avatar()).into(viewHolder.iv_author);
        viewHolder.tv_postContent.setText("我:" + msgLike.getPost_content());
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
    private MsgLikeAdapter.onSwipeListener mOnSwipeListener;

    public MsgLikeAdapter.onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(MsgLikeAdapter.onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }
    // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
    public void updateList(List<MsgLike> newDatas, boolean hasMore) {
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            list.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }
}