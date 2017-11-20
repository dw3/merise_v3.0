package com.merise.net.heart.activities;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.application.XYApplication;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/14 0014.
 */
public class SafeSettingActivity extends BaseActivity {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.li_gesture_setting)
    LinearLayout liGestureSetting;
    @BindView(R.id.ll_finger_setting)
    LinearLayout liFingerSetting;
    @BindView(R.id.change_code)
    LinearLayout ChangeCode;
    @BindView(R.id.change_phone_num)
    LinearLayout ChangePhone;
    @BindView(R.id.li_perfect_message)
    LinearLayout liPerfectMessage;
    private FingerprintManager manager;

    @Override
    protected int getLayoutId() {
        return R.layout.safe_setting;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.code_management);

        //在23以上的基础上，检测硬件是否支持指纹识别模块
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            manager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            boolean b = manager.isHardwareDetected();
            TLog.log(TAG, "b----" + b);
            //如果系统存在API，而且硬件支持指纹识别模块，则显示
            if (b == true) {
                spt.saveSharedPreferences(Constant.HAS_FINGERPRINT_API, true);
            } else {
                spt.saveSharedPreferences(Constant.HAS_FINGERPRINT_API, false);
                liFingerSetting.setVisibility(View.GONE);
            }
        } else {
            spt.saveSharedPreferences(Constant.HAS_FINGERPRINT_API, false);
            liFingerSetting.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }


    @OnClick({R.id.back, R.id.li_gesture_setting, R.id.ll_finger_setting, R.id.change_code, R.id.change_phone_num, R.id.li_perfect_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.li_gesture_setting:
                startActivity(new Intent(this, GestureSettingActivity.class));
                break;
            case R.id.ll_finger_setting:
                startActivity(new Intent(this, FingerSettingActvity.class));
                break;
            case R.id.change_code:
                startActivity(new Intent(this, ChangeCodeActivity.class));
                break;
            case R.id.change_phone_num:
                startActivity(new Intent(this, UpdateMobileActivity.class));
                break;
            case R.id.li_perfect_message:
                startActivity(new Intent(this, PerfectMessageActivity.class));
                break;
        }
    }
}
