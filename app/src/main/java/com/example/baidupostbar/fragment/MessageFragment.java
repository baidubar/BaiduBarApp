package com.example.baidupostbar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baidupostbar.R;

import java.util.ArrayList;

public class MessageFragment extends Fragment {
    View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ArrayList<String> TitleList = new ArrayList<>();  //页卡标题集合
    private ArrayList<Fragment> ViewList = new ArrayList<>();   //页卡视图集合
    private Fragment newsFragment,noticeFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message,container,false);
        TitleList.clear();//加载布局前首先清空list，避免出现左右滑动时布局重新加载出现内容重复
        ViewList.clear();
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        newsFragment = new NewsFragment();
        noticeFragment = new NoticeFragment();

        //添加页卡视图
        ViewList.add(newsFragment);
        ViewList.add(noticeFragment);
        //添加页卡标题
        TitleList.add("消息");
        TitleList.add("通知");
        //设置tab模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        //添加tab选项卡
        tabLayout.addTab(tabLayout.newTab().setText(TitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(TitleList.get(1)));

        //设置adapter
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()){
            //fragment嵌套fragment时需要用到getchildfragmentmanager方法

            //获取每个页卡
            @Override
            public android.support.v4.app.Fragment getItem(int position){
                return ViewList.get(position);
            }

            //获取页卡数
            @Override
            public int getCount(){
                return  TitleList.size();
            }

            //获取页卡标题
            @Override
            public CharSequence getPageTitle(int position){
                return TitleList.get(position);
            }
        });

        //tab与viewpager绑定
        tabLayout.setupWithViewPager(viewPager);

    }

}
