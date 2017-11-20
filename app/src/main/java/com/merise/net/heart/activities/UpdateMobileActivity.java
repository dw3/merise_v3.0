package com.merise.net.heart.activities;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.GouUtils;
import com.merise.net.heart.utils.SharedPreferencesTools;
import com.merise.net.heart.view.ClearEditView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/25 0025.
 */
public class UpdateMobileActivity extends BaseActivity {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.mobile)
    ClearEditView mobile;
    @BindView(R.id.getvalidatecode)
    Button getvalidatecode;
    @BindView(R.id.validatecode)
    ClearEditView validatecode;
    @BindView(R.id.change_mobile_finish)
    Button changeMobileFinish;
    private String name = "wsh";
    private String codetype = "1";
    private String mobileNum;
    private String validNum;
    public static SharedPreferencesTools spt;
    public String oldName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_mobile;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        spt = new SharedPreferencesTools(this);
    }

    @Override
    public void initData() {
//        oldName = spt.readSharedPreferencesString(Const.OLDNAME);
        oldName = app.userInfo.getName();
    }

    /**
     * 获取验证码 （修改手机号）
     */
    private void getValidateCode(String name, String mobile, String codetype) {
        AppOperate.getValidateCode(name, mobile, codetype, this, new Report() {
            @Override
            public void onSucces(Object o) {
                GouUtils.showTip(UpdateMobileActivity.this, "获取成功");
                timerMMS();
            }

            @Override
            public void onError(Object o) {
                String error = o.toString();
                GouUtils.showTip(UpdateMobileActivity.this, GouUtils.subString(error));
            }
        });
    }


    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COUNT:
                    int timeCount = msg.arg1;
                    if (timeCount > 0) {
                        getvalidatecode.setText("剩余" + timeCount + "秒");
                    }
                    if (timeCount == 0) {
                        getvalidatecode.setText("获取验证码");
                        getvalidatecode.setEnabled(true);
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
        getvalidatecode.setEnabled(false);
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


    @Override
    public void initListener() {

    }


    @OnClick({R.id.back, R.id.getvalidatecode, R.id.change_mobile_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.getvalidatecode:
                mobileNum = mobile.getText().toString().trim();
                if (GouUtils.isNullOrEmpty(mobileNum)) {
                    GouUtils.showTip(this, "请输入手机号！");
                    return;
                }
                if (!GouUtils.isMobile(mobileNum)) {
                    GouUtils.showTip(this, "请输入正确手机号码！");
                    return;
                }
                getValidateCode(name, mobileNum, codetype);
                break;
            case R.id.change_mobile_finish:
                mobileNum = mobile.getText().toString().trim();
                validNum = validatecode.getText().toString().trim();
                if (GouUtils.isNullOrEmpty(mobileNum)) {
                    GouUtils.showTip(this, "请输入手机号！");
                    return;
                }
                if (!GouUtils.isMobile(mobileNum)) {
                    GouUtils.showTip(this, "请输入正确手机号码！");
                    return;
                }
                if (GouUtils.isNullOrEmpty(validNum)) {
                    GouUtils.showTip(this, "请输入验证码!");
                    return;
                }
                // 保存修改
                changeAccout(oldName, validNum, validNum);
                break;
        }
    }

    /**
     * 保存修改手机号
     */
    private void changeAccout(String oldname, String mobile, String validcode) {
        AppOperate.changeAccount(oldname, mobile, validcode, this, new Report() {
            @Override
            public void onSucces(Object o) {
                TLog.log(TAG, "修改手机号成功");
                onLogin(1);
            }

            @Override
            public void onError(Object o) {
                String error = o.toString();
                GouUtils.showTip(UpdateMobileActivity.this, GouUtils.subString(error));
            }
        });
    }
}
