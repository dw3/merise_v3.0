package com.merise.net.heart.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.widget.Button;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/17 0017.
 */
public class FingerDetailActivity extends BaseActivity {


    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;

    @Override
    protected int getLayoutId() {
        return R.layout.finger_code_detail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.finger_code);
        titleText.setTextSize(30);
    }

    @Override
    public void initData() {
        FingerprintManager manager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        //权限检测
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            //手机是否支持指纹识别检测
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean isSupport = manager.hasEnrolledFingerprints();
            }
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.authenticate(null, null, 0, new MyCallBack(), null);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public class MyCallBack extends FingerprintManager.AuthenticationCallback {

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            TLog.log(TAG, "onAuthenticationError: " + errString);
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            TLog.log(TAG, "onAuthenticationHelp: " + helpString);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            TLog.log(TAG, "onAuthenticationSucceeded: " + "验证成功");
        }

        @Override
        public void onAuthenticationFailed() {
            TLog.log(TAG, "onAuthenticationFailed: " + "验证失败");
        }
    }


    @Override
    public void initListener() {
    }


    @OnClick(R.id.back)
    public void onClick() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
