package com.example.baidupostbar.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.Remark;

import java.util.List;

public class RemarkAdapter extends BaseQuickAdapter<Remark, BaseViewHolder> {
    public RemarkAdapter(int layoutResId, List data) {
        super(R.layout.item_remark, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, Remark item) {
        helper.setText(R.id.remark_tv_content,item.getRemarkContent());
    }
}
