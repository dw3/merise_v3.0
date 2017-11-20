package com.merise.net.heart.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by wangdawang on 2016/7/12 0012.
 */
public class RecordViewPagerAdapter extends FragmentPagerAdapter {

    //存放fragment的集合
    List<Fragment> fragments;

    public RecordViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    //返回的fragment对象会作为viewpager的条目
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
