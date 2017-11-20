package com.merise.net.heart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/13.
 */
public class WhoCanScanActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.root)
    RelativeLayout root;

    private int scope = -1;
    @BindView(R.id.all)
    RadioButton all;
    @BindView(R.id.follow)
    RadioButton follow;
    @BindView(R.id.community)
    RadioButton community;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_who_can_scan;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.who_can_scan);
        radioGroup.setOnCheckedChangeListener(this);
        scope = getIntent().getIntExtra(Constant.TYPE, -1);
        if (scope == 2) {
            follow.setChecked(true);
        } else if (scope == 3) {
            community.setChecked(true);
        } else {
            all.setChecked(true);
        }
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.back, R.id.finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.finish:
                Intent intent = new Intent();
                intent.putExtra(Constant.TYPE, scope);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.all:
                scope = 1;
                break;
            case R.id.follow:
                scope = 2;
                break;
            case R.id.community:
                scope = 3;
                break;

        }
    }
}
