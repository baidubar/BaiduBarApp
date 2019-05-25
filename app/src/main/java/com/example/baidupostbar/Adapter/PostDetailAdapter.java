package com.example.baidupostbar.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.PostDetail;

import java.util.List;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

public class PostDetailAdapter extends BaseQuickAdapter<PostDetail, BaseViewHolder> {


    private List<PostDetail>mList;
    private BGANinePhotoLayout.Delegate delegate;
    private String userId;
    public PostDetailAdapter(int layoutResId, List data, BGANinePhotoLayout.Delegate delegate,Context context) {
        super(layoutResId, data);
        this.mList = data;
        this.delegate = delegate;
        SharedPreferences sharedPreferences = context.getSharedPreferences("theUser", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", "");
    }
    @Override
    protected void convert(BaseViewHolder helper, PostDetail item) {
        helper.addOnClickListener(R.id.btn_concerd);
        helper.addOnClickListener(R.id.btn_comment);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Log.e("PostDetail", String.valueOf(position));
        PostDetail postDetail = mList.get(position);
        if(position == 0){
            holder.setText(R.id.tv_time,postDetail.getTime())
                    .setText(R.id.tv_author,postDetail.getUserName())
                    .setText(R.id.tv_content,postDetail.getContent())
                    .setText(R.id.tv_likeNum,postDetail.getLikeNum())
                    .setText(R.id.tv_label,postDetail.getLabel())
                    .setText(R.id.tv_commentNum,postDetail.getCommentNum())
                    .setText(R.id.tv_bar,postDetail.getBarName());
            if(postDetail.getPersonId().equals(userId)){
                holder.getView(R.id.btn_concerd).setVisibility(View.GONE);
            }
            if(postDetail.getCollection_status()){
                holder.setText(R.id.btn_concerd,"已关注");
            }else {
                holder.setText(R.id.btn_concerd,"+关注");
            }
            Glide.with(mContext).load(postDetail.getUserImg()).into((ImageView) holder.getView(R.id.iv_author));
            BGANinePhotoLayout ninePhotoLayout = holder.getView(R.id.npl_item_moment_photos);
            ninePhotoLayout.setDelegate(delegate);
            if (postDetail.getPhoto()!=null) {
                ninePhotoLayout.setData(postDetail.getPhoto());
            }
        }else {
            holder.setText(R.id.tv_time,postDetail.getTime())
                    .setText(R.id.tv_author,postDetail.getUserName())
                    .setText(R.id.tv_content,postDetail.getContent())
                    .setText(R.id.tv_floor,"第"+ position + "楼");
            Glide.with(mContext).load(postDetail.getUserImg()).into((ImageView) holder.getView(R.id.iv_author));
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}