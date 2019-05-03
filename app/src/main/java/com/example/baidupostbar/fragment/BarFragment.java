package com.example.baidupostbar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baidupostbar.R;
import com.example.baidupostbar.Adapter.FragmentLabelAdapter;
import com.example.baidupostbar.bean.FragmentLabel;

import java.util.ArrayList;
import java.util.List;

public class BarFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private List<FragmentLabel> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bar,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
        StaggeredGridLayoutManager recyclerViewLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //线性布局Manager
//        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
//        recyclerViewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //网络布局Manager
//        GridLayoutManager recyclerViewLayoutManager = new GridLayoutManager(this, 3);
        //给recyclerView设置LayoutManager
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        initData();
        FragmentLabelAdapter adapter = new FragmentLabelAdapter(data, getContext());
        //设置adapter
        recyclerView.setAdapter(adapter);
    }
    private void initData() {
        FragmentLabel fragmentLabel;
        for (int i = 0; i < 5; i++) {
            fragmentLabel = new FragmentLabel(R.drawable.label_pets);
            data.add(fragmentLabel);
            fragmentLabel = new FragmentLabel(R.drawable.label_book);
            data.add(fragmentLabel);
            fragmentLabel = new FragmentLabel(R.drawable.label_sports);
            data.add(fragmentLabel);
            fragmentLabel = new FragmentLabel(R.drawable.label_zayn);
            data.add(fragmentLabel);
            fragmentLabel = new FragmentLabel(R.drawable.label_zayn);
            data.add(fragmentLabel);
        }
    }
}
