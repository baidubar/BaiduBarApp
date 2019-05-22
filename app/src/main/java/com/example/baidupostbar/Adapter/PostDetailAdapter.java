package com.example.baidupostbar.Adapter;

import android.util.Log;
import android.widget.ImageView;

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
    public PostDetailAdapter(int layoutResId, List data, BGANinePhotoLayout.Delegate delegate) {
        super(layoutResId, data);
        this.mList = data;
        this.delegate = delegate;
    }
    @Override
    protected void convert(BaseViewHolder helper, PostDetail item) {
//            helper.setText(R.id.tv_time,item.getTime())
//                    .setText(R.id.tv_author,item.getUserName())
//                    .setText(R.id.tv_content,item.getContent())
//                    .setText(R.id.tv_likeNum,item.getLikeNum());
//            Glide.with(mContext).load(item.getUserImg()).into((ImageView) helper.getView(R.id.iv_author));

//            helper.setText(R.id.tv_time,item.getTime())
//                    .setText(R.id.tv_author,item.getUserName())
//                    .setText(R.id.tv_content,item.getContent())
//                    .setText(R.id.tv_likeNum,item.getLikeNum())
//                    .setText(R.id.tv_label,item.getLabel())
//                    .setText(R.id.tv_commentNum,item.getCommentNum())
//                    .setText(R.id.tv_bar,item.getBarName());
//            Glide.with(mContext).load(item.getUserImg()).into((ImageView) helper.getView(R.id.iv_author));

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
            Glide.with(mContext).load(postDetail.getUserImg()).into((ImageView) holder.getView(R.id.iv_author));
            BGANinePhotoLayout ninePhotoLayout = holder.getView(R.id.npl_item_moment_photos);
            ninePhotoLayout.setDelegate(delegate);
            ninePhotoLayout.setData(postDetail.getPhoto());
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