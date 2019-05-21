package com.example.baidupostbar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.baidupostbar.R;
import com.example.baidupostbar.SearchActivity;
import com.example.baidupostbar.bean.FragmentLabel;

import java.util.ArrayList;
import java.util.List;

import km.lmy.searchview.SearchView;

public class BarFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private List<FragmentLabel> data = new ArrayList<>();
    Toolbar toolbar;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bar,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //searchView = view.findViewById(R.id.searchView);
        toolbar.setTitle("进吧");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
//        List<String> historyList = new ArrayList<>();
//        historyList.add("Joker");
//        historyList.add("Harry");
//        historyList.add("Kate");
//        historyList.add("Alice");
//        //设置全新的历史记录列表
//        searchView.setNewHistoryList(historyList);
//
//        //添加一条历史记录
//        searchView.addOneHistory("Jenson");
//
//        //设置搜索框默认值
//        searchView.setSearchEditText("test string");
//
//        //设置历史记录点击事件
//        searchView.setHistoryItemClickListener(new SearchView.OnHistoryItemClickListener() {
//            @Override
//            public void onClick(String historyStr, int position) {
//                //Toast.makeText(MainActivity.this, historyStr, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //设置软键盘搜索按钮点击事件
//        searchView.setOnSearchActionListener(new SearchView.OnSearchActionListener() {
//            @Override
//            public void onSearchAction(String searchText) {
//                //Toast.makeText(MainActivity.this, "搜索-->" + searchText, Toast.LENGTH_SHORT).show();
//                searchView.addOneHistory(searchText);
//            }
//        });
//
//
//        //设置输入文本监听事件
//        searchView.setOnInputTextChangeListener(new SearchView.OnInputTextChangeListener() {
//            @Override
//            public void onTextChanged(CharSequence charSequence) {
//                //TODO something
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence) {
//                //TODO something
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                //TODO something
//            }
//        });
        //使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
        StaggeredGridLayoutManager recyclerViewLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        //recyclerView.setLayoutManager(recyclerViewLayoutManager);
//        initData();
//        FragmentLabelAdapter adapter = new FragmentLabelAdapter(data, getContext());
//        //设置adapter
//        recyclerView.setAdapter(adapter);
    }
//    private void initData() {
//        for (int i = 0; i < 5; i++) {
//            FragmentLabel fragmentLabel0 = new FragmentLabel(R.drawable.label_pets1);
//            fragmentLabel0.setBarLabel("ACG");
//            data.add(fragmentLabel0);
//            FragmentLabel fragmentLabel1 = new FragmentLabel(R.drawable.label_books1);
//            fragmentLabel1.setBarLabel("文学");
//            data.add(fragmentLabel1);
//            FragmentLabel fragmentLabel2 = new FragmentLabel(R.drawable.label_sports1);
//            fragmentLabel2.setBarLabel("体育");
//            data.add(fragmentLabel2);
//            FragmentLabel fragmentLabel3 = new FragmentLabel(R.drawable.label_zayn);
//            fragmentLabel3.setBarLabel("娱乐");
//            data.add(fragmentLabel3);
//            FragmentLabel fragmentLabel4 = new FragmentLabel(R.drawable.label_zayn);
//            fragmentLabel4.setBarLabel("IT");
//            data.add(fragmentLabel4);
//        }
//    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //自动打开关闭SearchView
                //searchView.autoOpenOrClose();
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
