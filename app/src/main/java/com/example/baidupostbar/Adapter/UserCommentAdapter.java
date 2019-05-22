package com.example.baidupostbar.Adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.UserComment;

import java.util.List;

public class UserCommentAdapter extends BaseItemDraggableAdapter<UserComment, BaseViewHolder> {
    private Context context;
    public UserCommentAdapter(List data, Context context) {
        super(R.layout.item_msg_setfloor, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserComment item) {
        helper
                .setText(R.id.tv_author,item.getWriter_name())
                .setText(R.id.tv_time,item.getTime())
                .setText(R.id.tv_floorContent,item.getFloor_content())
                .setText(R.id.tv_postContent,"我："+ item.getPost_content());
        Glide.with(context).load("http://139.199.84.147"+item.getWriter_avatar()).into((ImageView) helper.getView(R.id.iv_author));
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
