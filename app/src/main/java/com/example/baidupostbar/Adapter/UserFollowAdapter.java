package com.example.baidupostbar.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baidupostbar.bean.UserFollow;

import java.util.List;

public class UserFollowAdapter extends BaseQuickAdapter<UserFollow, BaseViewHolder> {
    public UserFollowAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserFollow item) {
//        helper.setText(R.id.text, item.getUserName());
//        helper.setImageResource(R.id.icon,item.getUserImg());

//        helper.setImageResource(R.id.iv_bar,item.getBarId());

    }
}
