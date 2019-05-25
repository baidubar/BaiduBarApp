package com.example.baidupostbar.Adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.FloorDetail;

import java.util.List;

public class FloorDetailAdapter extends BaseQuickAdapter<FloorDetail, BaseViewHolder> {


    private List<FloorDetail>mList;
    public FloorDetailAdapter(int layoutResId, List data, Context context) {
        super(layoutResId, data);
        this.mList = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, FloorDetail item) {
//        helper.setText(R.id.text, item.getUserName());
//        helper.setImageResource(R.id.icon,item.getUserImg());

//        helper.setImageResource(R.id.iv_bar,item.getBarId());



    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        FloorDetail floorDetail = mList.get(position);
        if(position == 0){
            holder.setText(R.id.tv_author,floorDetail.getAuthorName())
                    .setText(R.id.tv_floor_num,"第"+floorDetail.getFloorNum()+"楼")
                    .setText(R.id.tv_time,floorDetail.getTime())
                    .setText(R.id.tv_content,floorDetail.getContent());
            Glide.with(mContext).load(floorDetail.getHeadImag()).into((ImageView) holder.getView(R.id.iv_author));
        }else {
            holder.setText(R.id.tv_author,floorDetail.getAuthorName())
                    .setText(R.id.tv_time,floorDetail.getTime())
                    .setText(R.id.tv_content,floorDetail.getContent());
            Glide.with(mContext).load(floorDetail.getHeadImag()).into((ImageView) holder.getView(R.id.iv_author));
        }
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }
}