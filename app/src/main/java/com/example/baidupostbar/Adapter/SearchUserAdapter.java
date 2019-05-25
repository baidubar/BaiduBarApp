package com.example.baidupostbar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.SearchUser;

import java.util.List;

public class SearchUserAdapter extends BaseQuickAdapter<SearchUser,BaseViewHolder> {


    private Context context;
    public SearchUserAdapter(int layoutResId, List data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }
    @Override
    protected void convert(BaseViewHolder helper, SearchUser item) {
        helper.setText(R.id.tv_userName,item.getUser_name())
                .setText(R.id.tv_concerned,"关注   " + item.getFollower_number());
        Glide.with(context).load(item.getUser_avatar()).into((ImageView) helper.getView(R.id.iv_user));
    }
}
