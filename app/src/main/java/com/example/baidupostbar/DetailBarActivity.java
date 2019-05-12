package com.example.baidupostbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.baidupostbar.Adapter.PostAdapter;
import com.example.baidupostbar.bean.Post;

import java.util.ArrayList;

public class DetailBarActivity extends AppCompatActivity {
    private ArrayList<Post> mDataList;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailBarActivity.this,CreatePostActivity.class);
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
        BaseQuickAdapter postAdapter = new PostAdapter(R.layout.item_post, mDataList);
        postAdapter.openLoadAnimation();
        View top = getLayoutInflater().inflate(R.layout.header_detail_bar, (ViewGroup) mRecyclerView.getParent(), false);
        postAdapter.addHeaderView(top);
        postAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(DetailBarActivity.this,DetailPost.class);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(postAdapter);
    }

    private void initData() {
        mDataList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Post item = new Post();
            mDataList.add(item);
        }
    }
}
