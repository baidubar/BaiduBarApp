//package com.example.baidupostbar.Adapter;
//
//import android.util.Log;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.example.baidupostbar.R;
//import com.example.baidupostbar.bean.Recommend;
//
//import java.util.List;
//
//public class RecommendAdapter extends BaseQuickAdapter<Recommend, BaseViewHolder> {
//
//    public RecommendAdapter(int layoutResId, List data) {
//        super(R.layout.item_post, data);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, Recommend item) {
//        helper.setText(R.id.post_tv_content,item.getContent());
//        Log.e("TAGGG","RecommendAdapter");
//    }
//
//}