package com.example.baidupostbar.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.Recommend;

import java.util.List;
import java.util.Map;

public class recommendAdapter extends BaseQuickAdapter<Recommend, BaseViewHolder> {

    public recommendAdapter(int layoutResId, List data) {
        super(R.layout.item_post, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Recommend item) {
        helper.setText(R.id.post_tv_content,item.getContent());
    }

}