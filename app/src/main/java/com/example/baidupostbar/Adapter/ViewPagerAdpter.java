package com.example.baidupostbar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdpter extends FragmentPagerAdapter {
    private List<Fragment> list;

    public ViewPagerAdpter(FragmentManager fm) {
        super(fm);
    }
    public ViewPagerAdpter(FragmentManager fragmentManager, ArrayList<Fragment> fragments){
        super(fragmentManager);
        list = fragments;
    }

    public void setList(List<Fragment> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }
}
