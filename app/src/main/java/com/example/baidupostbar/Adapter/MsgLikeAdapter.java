package com.example.baidupostbar.Adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.MsgLike;

import java.util.List;

public class MsgLikeAdapter extends BaseItemDraggableAdapter<MsgLike, BaseViewHolder> {
    private Context context;
    public MsgLikeAdapter(List data,Context context) {
        super(R.layout.item_msg_like, data);
        this.context = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, MsgLike item) {
        helper
                .setText(R.id.tv_author,item.getPerson_name())
                .setText(R.id.tv_postContent,"我："+item.getPost_content());
        Glide.with(context).load("http://139.199.84.147"+item.getPerson_avatar()).into((ImageView) helper.getView(R.id.iv_author));
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
