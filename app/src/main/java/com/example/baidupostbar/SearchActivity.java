package com.example.baidupostbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import com.example.baidupostbar.bean.Search;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import km.lmy.searchview.SearchView;

public class SearchActivity extends AppCompatActivity {

    View view;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Search search = new Search();
        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("username", "");

        LitePal.getDatabase();

        final SearchView searchView ;
        searchView = findViewById(R.id.searchView);
        //searchView.autoOpenOrClose();
//
//          List<String> historyList = new ArrayList<>();
//        List<Search> searches = LitePal.where("userName like ? ", userName).order("userName").find(Search.class);
//        for(int i = 0;i<searches.size();i++){
//            historyList.add(searches.get(i).getSearchData());
//        }
//        Log.e("SearchActivity", String.valueOf(searches.size()));
//        //设置全新的历史记录列表
//        searchView.setNewHistoryList(historyList);

        //添加一条历史记录
//        searchView.addOneHistory("Jenson");

        //设置搜索框默认值
        //searchView.setSearchEditText("test string");

//        //设置历史记录点击事件
//        searchView.setHistoryItemClickListener(new SearchView.OnHistoryItemClickListener() {
//            @Override
//            public void onClick(String historyStr, int position) {
//                //Toast.makeText(MainActivity.this, historyStr, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
//                intent.putExtra("searchText",historyStr);
//                startActivity(intent);
//            }
//        });
//        searchView.setOnCleanHistoryClickListener(new SearchView.OnCleanHistoryClickListener() {
//            @Override
//            public void onClick() {
//                LitePal.deleteAll(Search.class, "userName = ?" , userName);
//            }
//        });
        //设置软键盘搜索按钮点击事件
        searchView.setOnSearchActionListener(new SearchView.OnSearchActionListener() {
            @Override
            public void onSearchAction(String searchText) {


                search.setUserName(userName);
                search.setSearchData(searchText);

                Log.e("username + searchText",userName + searchText);
                search.save();
                Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                intent.putExtra("searchText",searchText);
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
