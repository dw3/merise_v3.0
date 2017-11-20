package com.merise.net.heart.activities;

import android.widget.Button;
import android.widget.TextView;

import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/21 0021.
 */
public class IntroductionActivity extends BaseActivity {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_company_introduction;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.introduction);
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
}
