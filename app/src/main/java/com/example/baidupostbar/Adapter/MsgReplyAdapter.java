package com.example.baidupostbar.Adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.MsgReply;

import java.util.List;

public class MsgReplyAdapter extends BaseItemDraggableAdapter<MsgReply, BaseViewHolder> {
    private Context context;
    public MsgReplyAdapter(List data,Context context) {
        super(R.layout.item_msg_reply, data);
        this.context = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, MsgReply item) {
        helper
                .setText(R.id.tv_author,item.getComment_user_name())
                .setText(R.id.tv_content,item.getContent())
                .setText(R.id.tv_floorContent,"我" + item.getReplied_content())
                .setText(R.id.tv_time,item.getTime());
        Glide.with(context).load("http://139.199.84.147"+item.getComment_user_avatar()).into((ImageView) helper.getView(R.id.iv_author));
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
