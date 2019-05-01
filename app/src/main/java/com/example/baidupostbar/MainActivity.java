package com.example.baidupostbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.baidupostbar.fragment.BarFragment;
import com.example.baidupostbar.fragment.MessageFragment;
import com.example.baidupostbar.fragment.RecommendFragment;
import com.example.baidupostbar.fragment.UserFragment;
import com.hjm.bottomtabbar.BottomTabBar;

public class MainActivity extends AppCompatActivity {

    private BottomTabBar mBottomTabBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mBottomTabBar = findViewById(R.id.bottom_tab_bar);

        mBottomTabBar.init(getSupportFragmentManager(), 720, 1280)
//                .setImgSize(70, 70)
//                .setFontSize(14)
//                .setTabPadding(5, 0, 5)
//                .setChangeColor(Color.parseColor("#FF00F0"),Color.parseColor("#CCCCCC"))
                .addTabItem("首页", R.drawable.discover_fill, R.drawable.discover, RecommendFragment.class)
                .addTabItem("进吧",R.drawable.bar_fill, R.drawable.bar, BarFragment.class)
                .addTabItem("消息",R.drawable.notice_fill, R.drawable.notice, MessageFragment.class)
                .addTabItem("我的",R.drawable.people_fill, R.drawable.people, UserFragment.class)
//                .isShowDivider(true)
//                .setDividerColor(Color.parseColor("#FF0000"))
//                .setTabBarBackgroundColor(Color.parseColor("#00FF0000"))
                .setOnTabChangeListener(new BottomTabBar.OnTabChangeListener() {
                    @Override
                    public void onTabChange(int position, String name, View view) {
                        if (position == 1)
                            mBottomTabBar.setSpot(1, false);
                    }
                })
                .setSpot(1, true)
                .setSpot(2, true);
        
    }
}
