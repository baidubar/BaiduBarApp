package com.example.baidupostbar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.UserBar;

import java.util.List;

public class UserBarAdapter extends RecyclerView.Adapter<UserBarAdapter.ViewHolder>{

    private List<UserBar> list;
    private Context context;
    private boolean hasMore = true;


    public UserBarAdapter(List<UserBar> list,Context context){
        this.list = list;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_bar;
        TextView tv_bar;
        View userBarView;
        TextView btn;

        public ViewHolder(View view) {
            super(view);
            userBarView = view;
            iv_bar = view.findViewById(R.id.iv_bar);
            tv_bar = view.findViewById(R.id.tv_bar);
            btn = view.findViewById(R.id.btn);
        }
    }

    public UserBarAdapter(List<UserBar> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserBarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context == null){
            context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_user_bar, viewGroup, false);
        final UserBarAdapter.ViewHolder holder = new UserBarAdapter.ViewHolder(view);
        holder.userBarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                UserBar userBar = list.get(position);
            }
        });
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                UserBar userBar = list.get(position);
                Snackbar.make(view,"确定要取消关注吗？",Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,getItemCount() - position);
                            }
                        })
                        .show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserBarAdapter.ViewHolder viewHolder, int i) {
        UserBar userBar = list.get(i);
        viewHolder.tv_bar.setText(userBar.getBar_name());
        Glide.with(context).load("http://139.199.84.147"+userBar.getBar_icon()).into(viewHolder.iv_bar);
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
    // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
    public void updateList(List<UserBar> newDatas, boolean hasMore) {
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            list.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }
}
