package com.example.weichuanqi.im.controller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ContListVPAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList;
    private String [] title = {"好友","群组"};

    public ContListVPAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        this.mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
