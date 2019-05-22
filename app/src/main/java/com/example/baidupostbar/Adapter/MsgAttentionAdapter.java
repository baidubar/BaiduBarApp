package com.example.baidupostbar.Adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.MsgAttention;

import java.util.List;

public class MsgAttentionAdapter extends BaseItemDraggableAdapter<MsgAttention, BaseViewHolder> {
    public MsgAttentionAdapter(List data) {
        super(R.layout.item_msg_attention, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MsgAttention item) {
        helper.setText(R.id.iv_user, item.getAvatar())
                .setText(R.id.tv_userName, item.getUsername());
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