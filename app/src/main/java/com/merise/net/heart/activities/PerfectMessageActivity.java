package com.merise.net.heart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.application.XYApplication;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.UserInfo;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.utils.SnackbarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class PerfectMessageActivity extends BaseActivity {


    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.change_username)
    RelativeLayout changeUsername;
    @BindView(R.id.e_mail_change)
    RelativeLayout eMailChange;
    @BindView(R.id.show_email)
    TextView showEmail;
    @BindView(R.id.show_username)
    TextView showUsername;
    @BindView(R.id.prefect_root)
    LinearLayout root;
    private UserInfo userInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_perfect;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.perfect_data);
        userInfo = XYApplication.userInfo;
        showUsername.setText(userInfo.getName());


    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.back, R.id.change_username, R.id.e_mail_change})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.change_username:
                //如果用户设置没有设置用户名默认为手机号则可更改
                TLog.log(TAG, userInfo.getName() + "----" + userInfo.getMobile());
                if (userInfo.getName().equalsIgnoreCase(userInfo.getMobile())) {
                    startActivity(new Intent(this, SetUserNameActivity.class));
                } else {
                    SnackbarUtil.LongSnackbar(root,
                            getResources().getString(R.string.can_not_change), SnackbarUtil.Alert).show();
                }
                break;
            case R.id.e_mail_change:
                startActivity(new Intent(this, SetEmailActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String s = spt.readSharedPreferencesString(Const.CHANGE_USERNAME);
        if (s != null && s.length() != 0 && s.equalsIgnoreCase(userInfo.getName())) {
            showUsername.setText(s);
        }
    }
}
