package com.example.baidupostbar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.baidupostbar.Adapter.BarListAdapter;
import com.example.baidupostbar.DetailBarActivity;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.BarList;

import java.util.ArrayList;

public class UserBarFragment extends Fragment {
    View view;
    private ArrayList<BarList> mDataList;
    private RecyclerView mRecyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_bar,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initAdapter();
    }
    private void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
        BaseQuickAdapter barListAdapter = new BarListAdapter(R.layout.item_bar, mDataList);
        barListAdapter.openLoadAnimation();
        barListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), DetailBarActivity.class);
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
