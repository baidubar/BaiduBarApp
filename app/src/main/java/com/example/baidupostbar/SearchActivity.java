package com.example.baidupostbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import km.lmy.searchview.SearchView;

public class SearchActivity extends AppCompatActivity {

    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final SearchView searchView ;
        searchView = findViewById(R.id.searchView);
        //searchView.autoOpenOrClose();
        List<String> historyList = new ArrayList<>();
        historyList.add("Joker");
        historyList.add("Harry");
        historyList.add("Kate");
        historyList.add("Alice");
        //设置全新的历史记录列表
        searchView.setNewHistoryList(historyList);

        //添加一条历史记录
        searchView.addOneHistory("Jenson");

        //设置搜索框默认值
        //searchView.setSearchEditText("test string");

        //设置历史记录点击事件
        searchView.setHistoryItemClickListener(new SearchView.OnHistoryItemClickListener() {
            @Override
            public void onClick(String historyStr, int position) {
                //Toast.makeText(MainActivity.this, historyStr, Toast.LENGTH_SHORT).show();
            }
        });

        //设置软键盘搜索按钮点击事件
        searchView.setOnSearchActionListener(new SearchView.OnSearchActionListener() {
            @Override
            public void onSearchAction(String searchText) {
                Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                startActivity(intent);
                searchView.addOneHistory(searchText);
            }
        });


        //设置输入文本监听事件
        searchView.setOnInputTextChangeListener(new SearchView.OnInputTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence) {
                //TODO something
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence) {
                //TODO something
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //TODO something
            }
        });
        //设置搜索框返回按钮的点击事件
        searchView.setOnSearchBackIconClickListener(new SearchView.OnSearchBackIconClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
