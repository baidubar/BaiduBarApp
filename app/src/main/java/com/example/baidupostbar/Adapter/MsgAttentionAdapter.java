package com.example.baidupostbar.Adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.MsgAttention;

import java.util.List;

public class MsgAttentionAdapter extends BaseItemDraggableAdapter<MsgAttention, BaseViewHolder> {
    private Context context;
    public MsgAttentionAdapter(List data,Context context) {
        super(R.layout.item_msg_attention, data);
        this.context = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, MsgAttention item) {
        helper
                .setText(R.id.tv_userName, item.getUsername());
        Glide.with(context).load("http://139.199.84.147"+item.getAvatar()).into((ImageView) helper.getView(R.id.iv_user));
//        switch (helper.getLayoutPosition() % 3) {
////            case 0:
////                helper.setImageResource(R.id.iv_head, R.mipmap.head_img0);
////                break;
////            case 1:
////                helper.setImageResource(R.id.iv_head, R.mipmap.head_img1);
////                break;
////            case 2:
////                helper.setImageResource(R.id.iv_head, R.mipmap.head_img2);
////                break;
////            default:
////                break;
////        }
////        helper.setText(R.id.tv, item);
    }
}