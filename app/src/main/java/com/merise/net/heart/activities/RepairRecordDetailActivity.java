package com.merise.net.heart.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.Repair;
import com.merise.net.heart.fragments.RepairRecordDetailFragment;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:xiangyang
 * 日期:2016/10/25
 */
public class RepairRecordDetailActivity extends BaseActivity {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.viewpager)
    CustomViewPager customViewPager;
    @BindView(R.id.rootLL)
    LinearLayout rootLL;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.destinationDateTV)
    TextView destinationDateTV;
    @BindView(R.id.evaluateTitleTV)
    TextView evaluateTitleTV;
    @BindView(R.id.evaluateContentTV)
    EditText evaluateContentTV;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.evaluateDateTV)
    TextView evaluateDateTV;
    @BindView(R.id.feedbackContentTV)
    EditText feedbackContentTV;
    @BindView(R.id.feedbackContentDateTV)
    TextView feedbackContentDateTV;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.line)
    TextView line;
    @BindView(R.id.rlTile)
    RelativeLayout rlTile;
    @BindView(R.id.detailRl)
    RelativeLayout detailRl;
    @BindView(R.id.destinationDateRL)
    RelativeLayout destinationDateRL;
    @BindView(R.id.contentRL)
    RelativeLayout contentRL;
    @BindView(R.id.evaluateTitleRl)
    RelativeLayout evaluateTitleRl;
    @BindView(R.id.evaluateContentRL)
    RelativeLayout evaluateContentRL;
    @BindView(R.id.evaluateLevelRL)
    RelativeLayout evaluateLevelRL;
    @BindView(R.id.evaluateRL)
    RelativeLayout evaluateRL;
    @BindView(R.id.feedbackContentRL)
    RelativeLayout feedbackContentRL;
    @BindView(R.id.feedbackContentDateRL)
    RelativeLayout feedbackContentDateRL;
    @BindView(R.id.feedbackRL)
    RelativeLayout feedbackRL;
    @BindView(R.id.indexTV)
    TextView indexTV;
    private List<RepairRecordDetailFragment> fragments = new ArrayList<>();
    String[] imageUrl = new String[]{"http://img.taopic.com/uploads/allimg/110914/8879-11091422541844.jpg",
            "http://img5.imgtn.bdimg.com/it/u=4246510199,2069483326&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3123432318,2547934550&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=963551012,3660149984&fm=21&gp=0.jpg",
            "http://pic.pedaily.cn/newseed/project/20150707/20150707215142992.jpg"
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_repair_record_detail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        final ArrayList<String> pictureUrl = (ArrayList<String>) getIntent().getSerializableExtra(Constant.LIST);
        final int index = getIntent().getIntExtra(Constant.INDEX, 0);
        Repair repair = (Repair) getIntent().getSerializableExtra(Constant.OBJ);
        if (repair != null) {
            String contentStr = repair.getContent();
            if (contentStr != null) {
                contentRL.setVisibility(View.VISIBLE);
                if (repair.getRepairType() == 1) {
                    title.setText(R.string.personal_repair);
                } else if (repair.getRepairType() == 2) {
                    title.setText(R.string.common_repair);
                }
                content.setText(contentStr);
                destinationDateTV.setText(repair.getAreaName() + repair.getCreateTime());
            }
            String remarkStr = repair.getRemark();
            if (remarkStr != null) {
                evaluateRL.setVisibility(View.VISIBLE);
                evaluateTitleTV.setText(R.string.evaluate);
                evaluateContentTV.setText(repair.getRemark());
                ratingBar.setRating(repair.getSatisfaction());
                evaluateDateTV.setText(repair.getAreaName() + repair.getEndTime());
            }
            String repairUsername = repair.getRepairPersonnel();
            if (repairUsername != null) {
                line.setVisibility(View.VISIBLE);
                feedbackRL.setVisibility(View.VISIBLE);
                feedbackContentTV.setText("已经被" + repair.getRepairPersonnel() + "处理");
                feedbackContentDateTV.setText(repair.getAreaName() + repair.getEndTime());
            }
        }
        if (pictureUrl != null) {
            for (int i = 0; i < pictureUrl.size(); i++) {
                TLog.log(TAG, pictureUrl.get(i));
                RepairRecordDetailFragment fragment = new RepairRecordDetailFragment(pictureUrl.get(i));
                fragments.add(fragment);
            }
            customViewPager.setOffscreenPageLimit(3);
            customViewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
            customViewPager.setCurrentItem(index);
            indexTV.setVisibility(View.VISIBLE);
            indexTV.setText((index + 1) + "/" + pictureUrl.size());
            showAnnimation();
            customViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    TLog.log(TAG, "onPageScrolled");
                    indexTV.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageSelected(int position) {
                    TLog.log(TAG, "onPageSelected");
                    indexTV.setText((position + 1) + "/" + pictureUrl.size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    TLog.log(TAG, "onPageScrollStateChanged");
//                    indexTV.setVisibility(View.GONE);
                    showAnnimation();
                }
            });
        }

    }
    private void showAnnimation(){
        AlphaAnimation  alp = new AlphaAnimation(0.7f, 0.0f);
        alp.setDuration(2000);
        alp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                indexTV.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        indexTV.setAnimation(alp);
    }

    @Override
    public void initData() {
        titleText.setText(R.string.repair_detail);
    }


    @OnClick(R.id.back)
    public void onClick() {
        onBackPressed();
    }


    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }
}
