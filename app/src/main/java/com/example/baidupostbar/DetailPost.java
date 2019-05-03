package com.example.baidupostbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.baidupostbar.Adapter.PostDetailAdapter;
import com.example.baidupostbar.bean.PostDetail;

import java.util.ArrayList;

public class DetailPost extends AppCompatActivity {

    private ArrayList<PostDetail> mDataList;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        initView();
        initData();
        initAdapter();
    }
    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
        BaseQuickAdapter postDetailAdapter = new PostDetailAdapter(R.layout.item_post_floor, mDataList);
        postDetailAdapter.openLoadAnimation();
        View top = getLayoutInflater().inflate(R.layout.header_detail_post, (ViewGroup) mRecyclerView.getParent(), false);
        postDetailAdapter.addHeaderView(top);
        postDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        mRecyclerView.setAdapter(postDetailAdapter);
    }

    private void initData() {
        mDataList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            PostDetail item = new PostDetail();
            mDataList.add(item);
        }
    }
}
