package com.example.baidupostbar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> mFragmentTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments){
        super(fragmentManager);
        this.fragments = fragments;
    }
    public ViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }
    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
