package com.example.baidupostbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.baidupostbar.Adapter.BarListAdapter;
import com.example.baidupostbar.bean.BarList;

import java.util.ArrayList;

public class ListBarActivity extends AppCompatActivity {

    private ArrayList<BarList> mDataList;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bar);
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
        BaseQuickAdapter barListAdapter = new BarListAdapter(R.layout.item_bar, mDataList);
        barListAdapter.openLoadAnimation();
        barListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ListBarActivity.this,DetailBarActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(barListAdapter);
    }

    private void initData() {
        mDataList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            BarList item = new BarList();
            mDataList.add(item);
        }
    }
}
