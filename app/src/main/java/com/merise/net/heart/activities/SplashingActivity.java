package com.merise.net.heart.activities;

import android.os.Handler;

import com.merise.net.heart.MainActivity;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;

/**
 * 作者:xiangyang
 * 日期:2016/11/10
 */
public class SplashingActivity extends BaseActivity {


    @Override
    public void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoActivity(HomeActivity.class);
            }
        }, 2000);
    }

    @Override
    public void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }
}
