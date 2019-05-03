package com.example.baidupostbar.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baidupostbar.bean.PostDetail;

import java.util.List;

public class PostDetailAdapter extends BaseQuickAdapter<PostDetail, BaseViewHolder> {
    public PostDetailAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostDetail item) {
//        helper.setText(R.id.text, item.getUserName());
//        helper.setImageResource(R.id.icon,item.getUserImg());
    }
}