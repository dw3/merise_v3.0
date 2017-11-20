package com.merise.net.heart.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.SharedPreferencesTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/17 0017.
 */
public class FingerSettingActvity extends BaseActivity {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.cb_is_finger)
    CheckBox cbIsFinger;
    @BindView(R.id.change_finger_code)
    LinearLayout isFingerLL;
    private FingerprintManager manager;

    private boolean isOpenFinger;
    private boolean isSupport;
    private boolean resumeB;


    @Override
    protected int getLayoutId() {
        return R.layout.is_finger;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.finger_code);
        isFingerLL.setVisibility(View.GONE);
        manager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        boolean isFinger = spt.readSharedPreferencesBoolean(Constant.IS_OPEN_FINGER);
        if (isFinger) {
            isOpenFinger = true;
            cbIsFinger.setBackgroundResource(R.drawable.on_btn);
            isFingerLL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {

    }


    @OnClick({R.id.back, R.id.cb_is_finger, R.id.change_finger_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.cb_is_finger:
                TLog.log(TAG, "isOpenFinger-----" + isOpenFinger);
                if (isOpenFinger == false) {
                    //先显示增删指纹界面
                    isFingerLL.setVisibility(View.VISIBLE);
                    //手机是否录入了指纹，如果没有提示录入
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        isSupport = manager.hasEnrolledFingerprints();
                        //如果没有录入指纹，弹dialog提示
                        if (isSupport == false) {
                            //弹出dialog，三秒自动消失关闭activitiy
                            final MaterialDialog isFingerDialog = new MaterialDialog.Builder(FingerSettingActvity.this)
                                    .title(R.string.hot_hint)
                                    .content(R.string.isFingerSetting)
                                    .positiveText(R.string.confirm)
                                    .negativeText(R.string.cancel)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                            startActivity(intent);
                                        }
                                    })
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            TLog.log(TAG, "取消录入");
                                        }
                                    })
                                    .show();
                            //有指纹而且用户需要打开
                        } else {
                            isOpenFinger = true;
                            cbIsFinger.setBackgroundResource(R.drawable.on_btn);
                            spt.saveSharedPreferences(Constant.IS_OPEN_FINGER, true);
                            return;
                        }
                    }
                } else if (isOpenFinger == true) {
                    isOpenFinger = false;
                    cbIsFinger.setBackgroundResource(R.drawable.off_btn);
                    spt.saveSharedPreferences(Constant.IS_OPEN_FINGER, false);
                    isFingerLL.setVisibility(View.GONE);
                    return;
                }
            case R.id.change_finger_code:
                //手机是否录入了指纹
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    boolean isSupport = manager.hasEnrolledFingerprints();
                    //如果没有录入指纹，弹dialog提示
                    if (isSupport == false) {
                        //弹出dialog，三秒自动消失关闭activitiy
                        final MaterialDialog isFingerDialog = new MaterialDialog.Builder(FingerSettingActvity.this)
                                .title(R.string.hot_hint)
                                .content(R.string.isFingerSetting)
                                .positiveText(R.string.confirm)
                                .negativeText(R.string.cancel)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                        startActivity(intent);
                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        TLog.log(TAG, "取消录入");
                                    }
                                })
                                .show();
                        //用户已经录入指纹
                    } else {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    }
                }
                return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resumeB = manager.hasEnrolledFingerprints();
            TLog.log(TAG, "resumeB----" + resumeB);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
