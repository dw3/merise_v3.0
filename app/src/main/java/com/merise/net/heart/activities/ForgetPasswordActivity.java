package com.merise.net.heart.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.GouUtils;
import com.merise.net.heart.view.ClearEditView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/31 0031.
 */
public class ForgetPasswordActivity extends BaseActivity {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    //用户名
    @BindView(R.id.device_name)
    ClearEditView userName;
    @BindView(R.id.forgetpassword_phone_et)
    ClearEditView forgetpasswordPhoneEt;
    @BindView(R.id.forgetpassword_validate_et)
    ClearEditView forgetpasswordValidateEt;
    @BindView(R.id.forgetpassword_getvalidatecode)
    Button forgetpasswordGetvalidatecode;
    @BindView(R.id.forgetpassword_next)
    Button forgetpasswordNext;

    private String userNameVal;
    private String phoneNumVal;
    private String validateCodeVal;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.forget_password);
    }

    @Override
    public void initData() {

    }


    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COUNT:
                    int timeCount = msg.arg1;
                    if (timeCount > 0) {
                        forgetpasswordGetvalidatecode.setText("剩余" + timeCount + "秒");
                    }
                    if (timeCount == 0) {
                        forgetpasswordGetvalidatecode.setText("获取验证码");
                        forgetpasswordGetvalidatecode.setEnabled(true);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 一个获取验证码60秒定时器
     */

    private Timer timer = null;
    private int count = 60;
    private static final int COUNT = 0X0001;

    private void timerMMS() {
        forgetpasswordGetvalidatecode.setEnabled(false);
        if (timer == null)
            timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                count--;
                Message message = new Message();
                message.what = COUNT;
                message.arg1 = count;
                myHandler.sendMessage(message);
                if (count <= 0) {
                    count = 60;
                    timer.cancel();
                    timer = null;
                }
            }

        }, 1000, 1000);
    }


    @OnClick({R.id.back, R.id.forgetpassword_getvalidatecode, R.id.forgetpassword_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.forgetpassword_getvalidatecode:
                userNameVal = userName.getText().toString().trim();
                phoneNumVal = forgetpasswordPhoneEt.getText().toString().trim();
                getValidateCodeForgetPassword(userNameVal, phoneNumVal);
                break;
            case R.id.forgetpassword_next:
                userNameVal = userName.getText().toString().trim();
                validateCodeVal = forgetpasswordValidateEt.getText().toString().trim();
                findPasswordFirstStep(userNameVal, validateCodeVal);
                break;
        }
    }

    /**
     * 忘记密码获取验证码
     *
     * @param usernameVal
     * @param mobileVal
     */
    private void getValidateCodeForgetPassword(String usernameVal, String mobileVal) {
        AppOperate.getValidateCode(usernameVal, mobileVal, "2", this, new Report() {
            @Override
            public void onSucces(Object o) {
                TLog.log(TAG, "忘记密码获取验证码成功");
//                GouUtils.showTip(ForgetPasswordActivity.this, "获取成功");

            }

            @Override
            public void onError(Object o) {
                String error = o.toString();
                GouUtils.showTip(ForgetPasswordActivity.this, GouUtils.subString(error));
            }
        });
    }


    /**
     * 忘记密码下一步
     */
    private void findPasswordFirstStep(String name, String validateCodeVal) {
        AppOperate.findPasswordFirstStep(name, validateCodeVal,
                this, new Report() {
                    @Override
                    public void onSucces(Object o) {
                        TLog.log(TAG, "召回第一步成功");
                        startActivity(new Intent(ForgetPasswordActivity.this, RebuildPasswordActivity.class));
                    }

                    @Override
                    public void onError(Object o) {
//                        String error = o.toString();
                        TLog.log(TAG, "第一步失败");
//                        GouUtils.showTip(ForgetPasswordActivity.this, GouUtils.subString(error));
                    }
                });
    }

}
