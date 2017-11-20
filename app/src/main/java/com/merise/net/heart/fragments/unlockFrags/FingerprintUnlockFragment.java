package com.merise.net.heart.fragments.unlockFrags;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.bumptech.glide.Glide;
import com.merise.net.heart.MainActivity;
import com.merise.net.heart.R;
import com.merise.net.heart.base.MyBaseFragment;
import com.merise.net.heart.bean.UserInfo;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.ITools;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.utils.Utils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.merise.net.heart.R.string.login;

/**
 * 作者:xiangyang
 * 日期:2016/11/11
 */
public class FingerprintUnlockFragment extends MyBaseFragment {

    @BindView(R.id.topTV)
    TextView topTV;
    @BindView(R.id.headImg)
    ImageView headImg;
    @BindView(R.id.tipTV)
    TextView tipTV;
    @BindView(R.id.changeUnlockWay)
    Button changeUnlockWay;
    @BindView(R.id.root)
    RelativeLayout root;

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
        TLog.log(TAG, "FingerprintUnlockFragment....initView");
        String headPath = spt.readSharedPreferencesString(Constant.NETHEADPATH);
        Glide.with(getActivity()).load(headPath).placeholder(R.drawable.ic720).error(R.drawable.ic720)
                .bitmapTransform(new CropCircleTransformation(getActivity())).into(headImg);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            startValidFingerprint();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 开始手势验证
     */
    private void startValidFingerprint() {
        TLog.log(TAG, "开始指纹验证");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            topTV.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ITools.getStatusHeight(getActivity())));
        }
        if (!spt.readSharedPreferencesBoolean(Constant.HAS_FINGERPRINT_API)) {
            return;
        }
        FingerprintManager manager = (FingerprintManager) getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.authenticate(null, null, 0, new MyCallBack(), null);
        }
    }

    @Override
    public void initData() {
    }

    @OnClick({R.id.changeUnlockWay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.changeUnlockWay:
                gotoActivity(MainActivity.class);
                break;
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
            login();
        }

        @Override
        public void onAuthenticationFailed() {
            TLog.log(TAG, "onAuthenticationFailed: " + "验证失败");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fingerprint;
    }

    /**
     * 登录方法
     */
    private void login() {
        AppOperate.getLogin("1327", "123456", "", "", 1, Utils.getVersionName(getContext()), (RxAppCompatActivity) getActivity(), new Report() {
            @Override
            public void onSucces(Object o) {
                app.userInfo = (UserInfo) o;
                loginSuccess();
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }
}
