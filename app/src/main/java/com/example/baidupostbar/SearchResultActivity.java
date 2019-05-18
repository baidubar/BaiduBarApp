package com.example.baidupostbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.baidupostbar.Adapter.ViewPagerAdapter;
import com.example.baidupostbar.bean.Search;
import com.example.baidupostbar.fragment.SearchBarFragment;
import com.example.baidupostbar.fragment.SearchPostFragment;
import com.example.baidupostbar.fragment.SearchUserFragment;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        searchText = intent.getStringExtra("searchText");

        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager = findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        SearchUserFragment searchUserFragment = new SearchUserFragment();
        SearchPostFragment searchPostFragment = new SearchPostFragment();
        SearchBarFragment searchBarFragment = new SearchBarFragment();


        final ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(searchPostFragment);
        fragments.add(searchBarFragment);
        fragments.add(searchUserFragment);
        //设置tab模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


        adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);


        tabLayout.setupWithViewPager(viewPager);
    }
    public String getSearchText(){
        return searchText;
    }

}
