package com.merise.net.heart.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.DoorBell;
import com.merise.net.heart.bean.RecordBean;
import com.merise.net.heart.fragments.RecordDetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordViewActivity extends BaseActivity {
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.back)
    Button back;
    private ViewPager viewpager;
    private List<RecordDetailFragment> frags = new ArrayList<>();
    private int currentIndex;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_record_view;
    }

    /**
     * 页面滚动监听器
     */
    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(getString(R.string.record_check_detail));
        titleText.setTextSize(30);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        Bundle bundle = getIntent().getExtras();
        int logid = bundle.getInt(Const.LOGID);
        String logtype = bundle.getString(Const.LOGTYPE);
        String deviceId = bundle.getString(Const.DEVICEID);
        if (logtype.equals(Const.OPENDOOR)) {
            List<RecordBean> mListItems = (List<RecordBean>) bundle.getSerializable("List");
            TLog.log(TAG, "DATA---=-=-" + logid + "   " + logtype + "   " + deviceId + "   " + mListItems.size());
            for (int i = 0; i < mListItems.size(); i++) {
                RecordBean record = mListItems.get(i);
                if (record.getId() == logid) {
                    currentIndex = i;
                }
                frags.add(new RecordDetailFragment(record.getId(), deviceId, logtype));
            }
        } else {
            List<DoorBell> mListItems = (List<DoorBell>) bundle.getSerializable("List");
            TLog.log(TAG, "DATA---=-=-" + logid + "   " + logtype + "   " + deviceId + "   " + mListItems.size());
            for (int i = 0; i < mListItems.size(); i++) {
                DoorBell record = mListItems.get(i);
                if (record.getId() == logid) {
                    currentIndex = i;
                }
                frags.add(new RecordDetailFragment(record.getId(), deviceId, logtype));
            }
        }
        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(new myadapter(getSupportFragmentManager()));
        TLog.log(TAG, frags.size() + "====================");
        viewpager.setCurrentItem(currentIndex);
        viewpager.setOnPageChangeListener(pageChangeListener);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }


    @OnClick(R.id.back)
    public void onClick() {
        onBackPressed();
    }


    class myadapter extends FragmentPagerAdapter {

        public myadapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return frags.get(position);
        }

        @Override
        public int getCount() {
            return frags.size();
        }

    }

}
