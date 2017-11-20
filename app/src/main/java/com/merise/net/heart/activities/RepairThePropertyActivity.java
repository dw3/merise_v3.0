package com.merise.net.heart.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:xiangyang
 * 日期:2016/10/25
 */
public class RepairThePropertyActivity extends BaseActivity {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.commonRepairTv)
    TextView commonRepairTv;
    @BindView(R.id.commonRepairRl)
    RelativeLayout commonRepairRl;
    @BindView(R.id.commonRepairhint)
    TextView commonRepairhint;
    @BindView(R.id.personalRepairTv)
    TextView personalRepairTv;
    @BindView(R.id.personalRepairRl)
    RelativeLayout personalRepairRl;
    @BindView(R.id.personalRepairhint)
    TextView personalRepairhint;

    public static final int COMMONREPAIR = 2;
    public static final int PERSONALREPAIR = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_repair_the_property;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.property_repair);
        int left = (int) getResources().getDimension(R.dimen.title_text_padding_left);
        right.setPadding(left, 0, 0, 0);
        right.setVisibility(View.VISIBLE);
        right.setText(R.string.record_);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.back, R.id.commonRepairRl, R.id.personalRepairRl, R.id.right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.commonRepairRl:
                Bundle commonBundle = new Bundle();
                commonBundle.putInt(Constant.TYPE, COMMONREPAIR);
                gotoActivity(RepairActivity.class, false, commonBundle);
                break;
            case R.id.personalRepairRl:
                Bundle personalBundle = new Bundle();
                personalBundle.putInt(Constant.TYPE, PERSONALREPAIR);
                gotoActivity(RepairActivity.class, false, personalBundle);
                break;
            case R.id.right:
                gotoActivity(RepairRecordActivity.class);
                break;
        }
    }
}
