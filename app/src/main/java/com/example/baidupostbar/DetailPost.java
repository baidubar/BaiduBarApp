package com.example.baidupostbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.baidupostbar.Adapter.PostDetailAdapter;
import com.example.baidupostbar.bean.PostDetail;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DetailPost extends AppCompatActivity {

    private ArrayList<PostDetail> mDataList;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        //悬浮按钮的点击事件
        //收藏当前帖子
        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.btn_collect);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionA.setTitle("按钮一被单击");
            }
        });
        //在当前帖子下盖楼
        final FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.btn_comment);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPost.this,SetFloorActivity.class);
                startActivity(intent);
            }
        });
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
