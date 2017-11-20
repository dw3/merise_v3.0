package com.merise.net.heart.activities;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.fragments.unlockFrags.FingerprintUnlockFragment;
import com.merise.net.heart.fragments.unlockFrags.Lock9Fragment;
import com.merise.net.heart.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:xiangyang
 * 日期:2016/11/11
 */
public class UnlockActivity extends BaseActivity {
    private Lock9Fragment lock9Fragment;
    private FingerprintUnlockFragment fingerprintUnlockFragment;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<Fragment> frags = new ArrayList<>();

    @Override
    public void initView() {
        ButterKnife.bind(this);
        lock9Fragment = new Lock9Fragment();

        fingerprintUnlockFragment = new FingerprintUnlockFragment();
        //如果手势登录开启并且有设置手势密码
        String gestureAnswer = spt.readSharedPreferencesString(Constant.GESTUREANSWER);

        if (spt.readSharedPreferencesBoolean(Constant.IS_OPEN_FINGER))
            frags.add(fingerprintUnlockFragment);

        if (spt.readSharedPreferencesBoolean(Constant.IS_OPEN_GESTURE) && gestureAnswer != null && gestureAnswer.length() >= app.gestureMinLength)
            frags.add(lock9Fragment);

    }

    @Override
    public void initData() {
        viewpager.setAdapter(fragmentPagerAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_unlock;
    }

    FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return frags.get(position);
        }

        @Override
        public int getCount() {
            return frags.size();
        }
    };


}
