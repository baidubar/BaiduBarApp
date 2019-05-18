package com.example.baidupostbar;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.baidupostbar.Adapter.ViewPagerAdapter;
import com.example.baidupostbar.fragment.SearchBarFragment;
import com.example.baidupostbar.fragment.SearchPostFragment;
import com.example.baidupostbar.fragment.SearchUserFragment;

public class SearchResultActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        SearchUserFragment searchUserFragment = new SearchUserFragment();
        SearchPostFragment searchPostFragment = new SearchPostFragment();
        SearchBarFragment searchBarFragment = new SearchBarFragment();
        adapter.addFragment(searchPostFragment, "贴");
        adapter.addFragment(searchBarFragment, "吧");
        adapter.addFragment(searchUserFragment, "人");
        viewPager.setAdapter(adapter);
        //设置tab模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


        tabLayout.setupWithViewPager(viewPager);
    }
}
