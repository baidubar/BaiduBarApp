package com.example.baidupostbar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.baidupostbar.ListBarActivity;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.FragmentLabel;

import java.util.List;

public class FragmentLabelAdapter extends RecyclerView.Adapter<FragmentLabelAdapter.ViewHolder>{
    private Context mContext;
    /**
     * 数据集合
     */
    private List<FragmentLabel> data;

    public FragmentLabelAdapter(List<FragmentLabel> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public FragmentLabelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_label, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.labelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                FragmentLabel fragmentLabel = data.get(position);
                String BarLabel = fragmentLabel.getBarLabel();
                Intent intent = new Intent();
                intent.setClass(view.getContext(), ListBarActivity.class);
                intent.putExtra("BarLabel",BarLabel);
                view.getContext().startActivity(intent);
            }
        });
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(FragmentLabelAdapter.ViewHolder holder, int position) {
        //将数据设置到item上
        FragmentLabel fragmentLabel = data.get(position);
        holder.beautyImage.setImageResource(fragmentLabel.getImageId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView beautyImage;
        View labelView;

        public ViewHolder(View itemView) {
            super(itemView);
            labelView = itemView;
            beautyImage = itemView.findViewById(R.id.image_item);
        }
    }
}
