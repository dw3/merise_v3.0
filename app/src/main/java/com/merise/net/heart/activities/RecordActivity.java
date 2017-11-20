package com.merise.net.heart.activities;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.adapter.RecordViewPagerAdapter;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.Device;
import com.merise.net.heart.fragments.RecordDoorBellFragment;
import com.merise.net.heart.fragments.RecordOpenDoorFragment;
import com.merise.net.heart.fragments.RecordPoliceFragment;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/9/23 0023.
 */
public class RecordActivity extends BaseActivity {

    @BindView(R.id.v_indicate_line)
    View vIndicateLine;
    @BindView(R.id.record_viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_tab_opendoor)
    TextView tvTabOpendoor;
    @BindView(R.id.tv_tab_doorbell)
    TextView tvTabDoorbell;
    @BindView(R.id.tv_tab_police)
    TextView tvTabPolice;
    @BindView(R.id.ll_tab_opendoor)
    LinearLayout llTabOpendoor;
    @BindView(R.id.ll_tab_doorbell)
    LinearLayout llTabDoorbell;
    @BindView(R.id.ll_tab_police)
    LinearLayout llTabPolice;
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;

    private List<Fragment> fragments;
    private RecordViewPagerAdapter adapter;
    public static String deviceID;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(getString(R.string.record_check));
    }

    @Override
    public void initData() {
        deviceID = String.valueOf(app.devices.get(app.currentIndex).getId());
        fragments = new ArrayList<Fragment>();
        //创建fragment对象才能存入集合
        TLog.log(TAG, "deviceID---" + deviceID);
        RecordOpenDoorFragment recordOpenDoorFragment = new RecordOpenDoorFragment(deviceID);
        RecordDoorBellFragment doorBellFragment = new RecordDoorBellFragment(deviceID);
        RecordPoliceFragment policeFragment = new RecordPoliceFragment(deviceID);

        fragments.add(recordOpenDoorFragment);
        fragments.add(doorBellFragment);
        fragments.add(policeFragment);
        adapter = new RecordViewPagerAdapter(getSupportFragmentManager(), fragments);

        viewpager.setAdapter(adapter);
        //高亮显示文本内容
        textLightAndScale();
        //设置红线的宽度
        computeIndicateLineWidth(3);
    }


    public void initListener() {

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //这里position表示当前选中的页面
            public void onPageSelected(int position) {
                textLightAndScale();
            }

            @Override
            //滑动过程中不断调用
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            //positionOffsetPixels表示滑动像素
            //如果滑动过程中出现两个界面，position是前一个的索引
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //计算红线位移的距离
                System.out.println(positionOffsetPixels);
                int distance = positionOffsetPixels / 3;
                //持续时间为0，立刻生效，因为红线的移动需要与用户滑动同步
                ViewPropertyAnimator.animate(vIndicateLine).translationX(distance + position * vIndicateLine.getWidth()).setDuration(0);
            }
        });
        llTabOpendoor.setOnClickListener(this);
        llTabDoorbell.setOnClickListener(this);
        llTabPolice.setOnClickListener(this);

    }

    /**
     * 改变文本动画效果
     */
    public void textLightAndScale() {
        int item = viewpager.getCurrentItem();
        tvTabOpendoor.setTextColor(item == 0 ? Color.BLACK : Color.GRAY);
        tvTabDoorbell.setTextColor(item == 1 ? Color.BLACK : Color.GRAY);
        tvTabPolice.setTextColor(item == 2 ? Color.BLACK : Color.GRAY);
        //操作对象          改变宽度至指定比例                     持续时间
        ViewPropertyAnimator.animate(tvTabOpendoor).scaleX(item == 0 ? 1.2f : 1).setDuration(200);
        ViewPropertyAnimator.animate(tvTabDoorbell).scaleX(item == 1 ? 1.2f : 1).setDuration(200);
        ViewPropertyAnimator.animate(tvTabPolice).scaleX(item == 2 ? 1.2f : 1).setDuration(200);

        ViewPropertyAnimator.animate(tvTabOpendoor).scaleY(item == 0 ? 1.2f : 1).setDuration(200);
        ViewPropertyAnimator.animate(tvTabDoorbell).scaleY(item == 1 ? 1.2f : 1).setDuration(200);
        ViewPropertyAnimator.animate(tvTabPolice).scaleY(item == 2 ? 1.2f : 1).setDuration(200);
    }

    /**
     * 计算红线宽度
     */
    private void computeIndicateLineWidth(int i) {
        int width = getWindowManager().getDefaultDisplay().getWidth();
        vIndicateLine.getLayoutParams().width = width / i;
    }

    @OnClick({R.id.ll_tab_opendoor, R.id.ll_tab_doorbell, R.id.ll_tab_police, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_tab_opendoor:
                viewpager.setCurrentItem(0);
                break;
            case R.id.ll_tab_doorbell:
                viewpager.setCurrentItem(1);
                break;
            case R.id.ll_tab_police:
                viewpager.setCurrentItem(2);
                break;
            case R.id.back:
                onBackPressed();
        }
    }
}
