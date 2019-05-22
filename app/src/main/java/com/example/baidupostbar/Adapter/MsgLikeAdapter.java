package com.example.baidupostbar.Adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.MsgLike;

import java.util.List;

public class MsgLikeAdapter extends BaseItemDraggableAdapter<MsgLike, BaseViewHolder> {
    public MsgLikeAdapter(List data) {
        super(R.layout.item_msg_like, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MsgLike item) {
        helper.setText(R.id.iv_author,item.getPerson_avatar())
                .setText(R.id.tv_author,item.getPerson_name())
                .setText(R.id.tv_postContent,item.getPost_content());
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
