package com.merise.net.heart.activities;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.fragments.CommunityFragment;
import com.merise.net.heart.fragments.DefenceFragment;
import com.merise.net.heart.fragments.MallFragment;
import com.merise.net.heart.fragments.MyFragment;
import com.merise.net.heart.view.CustomViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangdawang on 2016/9/6 0006.
 */
public class HomeActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    private DefenceFragment defenceFragment;
    private MallFragment mallFragment;
    private CommunityFragment communityFragment;
    private MyFragment myFragment;
    private TabLayout.Tab defenceTab;
    private TabLayout.Tab mallTab;
    private TabLayout.Tab communityTab;
    private TabLayout.Tab myTab;
    private String[] homeTabList;

    public static int currentFragment;
    public static CustomViewPager viewpager;

    //    @BindView(R.id.viewpager)
//    CustomViewPager viewpager;
    @BindView(R.id.tablayout)
    TabLayout tablayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            defenceFragment = new DefenceFragment();
            mallFragment = new MallFragment();
            communityFragment = new CommunityFragment();
            myFragment = new MyFragment();
        }
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        viewpager = (CustomViewPager) findViewById(R.id.viewpager);
        homeTabList = getResources().getStringArray(R.array.homeTabList);
    }


    @Override
    public void initData() {
        viewpager.setAdapter(pagerAdapter);
        tablayout.setupWithViewPager(viewpager);
        defenceTab = tablayout.getTabAt(0);
        mallTab = tablayout.getTabAt(1);
        communityTab = tablayout.getTabAt(2);
        myTab = tablayout.getTabAt(3);
        defenceTab.setIcon(R.drawable.security_tab_click);
        mallTab.setIcon(R.drawable.store_tab);
        communityTab.setIcon(R.drawable.com_tab);
        myTab.setIcon(R.drawable.me_tab);
        viewpager.setPagingEnabled(false);
        viewpager.setOffscreenPageLimit(4);
        tablayout.setOnTabSelectedListener(this);
    }

    FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return defenceFragment;
            } else if (position == 1) {
                return mallFragment;
            } else if (position == 2) {
                return communityFragment;
            }
            return myFragment;
        }

        @Override
        public int getCount() {
            return homeTabList.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return homeTabList[position];
        }
    };

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewpager.setCurrentItem(tab.getPosition(), false);
        if (tab == tablayout.getTabAt(0)) {
            currentFragment = 0;
            defenceTab.setIcon(R.drawable.security_tab_click);
        } else if (tab == tablayout.getTabAt(1)) {
            currentFragment = 1;
            mallTab.setIcon(R.drawable.store_tab_click);
        } else if (tab == tablayout.getTabAt(2)) {
            currentFragment = 2;
            communityTab.setIcon(R.drawable.com_tab_click);
        } else if (tab == tablayout.getTabAt(3)) {
            currentFragment = 3;
            myTab.setIcon(R.drawable.me_tab_click);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if (tab == tablayout.getTabAt(0)) {
            defenceTab.setIcon(R.drawable.security_tab);
        } else if (tab == tablayout.getTabAt(1)) {
            mallTab.setIcon(R.drawable.store_tab);
        } else if (tab == tablayout.getTabAt(2)) {
            communityTab.setIcon(R.drawable.com_tab);
        } else if (tab == tablayout.getTabAt(3)) {
            myTab.setIcon(R.drawable.me_tab);
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        app.exit();
    }
}
