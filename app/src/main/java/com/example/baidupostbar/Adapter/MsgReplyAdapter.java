package com.example.baidupostbar.Adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.MsgReply;

import java.util.List;

public class MsgReplyAdapter extends BaseItemDraggableAdapter<MsgReply, BaseViewHolder> {
    public MsgReplyAdapter(List data) {
        super(R.layout.item_msg_reply, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MsgReply item) {
        helper.setText(R.id.iv_author,item.getComment_user_avatar())
                .setText(R.id.tv_author,item.getComment_user_name())
                .setText(R.id.tv_content,item.getContent())
                .setText(R.id.tv_floorContent,"我" + item.getReplied_content())
                .setText(R.id.tv_time,item.getTime());
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
