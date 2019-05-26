package com.example.baidupostbar.Adapter;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.Aaa;
import com.example.baidupostbar.bean.BarList;

import java.util.List;

public class AaaAdapter extends BaseQuickAdapter<Aaa, BaseViewHolder> {
    public AaaAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Aaa item) {
//        helper.setText(R.id.text, item.getUserName());
//        helper.setImageResource(R.id.icon,item.getUserImg());

//        helper.setImageResource(R.id.iv_bar,item.getBarId());
        Glide.with(mContext).load(item.getBarImg()).into((ImageView) helper.getView(R.id.iv_bar));
        helper.setText(R.id.tv_bar,item.getBarName());

    }
}
