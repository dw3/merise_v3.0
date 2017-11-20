package com.merise.net.heart.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.utils.Const;
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
 * Created by wangdawang on 2016/10/27 0027.
 */
public class RegistActivity extends BaseActivity {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.usernameEt)
    ClearEditView usernameEt;
    @BindView(R.id.phone_num_et)
    ClearEditView phoneNumEt;
    @BindView(R.id.validate_et)
    ClearEditView validateEt;
    @BindView(R.id.getvalidatecode)
    Button getvalidatecode;
    @BindView(R.id.passwordEt)
    EditText passwordEt;
    @BindView(R.id.cb_passwordVisible_regist)
    CheckBox cbPasswordVisibleRegist;
    @BindView(R.id.accept_agreement)
    CheckBox acceptAgreement;
    @BindView(R.id.registbtn)
    Button registbtn;


    String usernameVal;
    String mobileVal;
    String passwordVal;
    String validateCodeVal;
    //一个flag
    private final int AGREEMENT = 0X0001;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.regist);
        //密码栏禁用空格
        passwordEt.setFilters(new InputFilter[]{GouUtils.filter});
    }


    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        //为密码checkbox设置监听 可见不可见
        cbPasswordVisibleRegist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //选中之后设置文本内容可见
                if (isChecked) {
                    passwordEt.setInputType(0x90);
                } else {
                    passwordEt.setInputType(0x81);
                }
            }
        });

        //为接受协议checkbox设置监听
        acceptAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //被选中
                if (isChecked) {
                    startActivityForResult(new Intent(RegistActivity.this,
                            AgreementForRegistActivity.class), AGREEMENT);
                }
            }
        });

    }


    @OnClick({R.id.back, R.id.getvalidatecode, R.id.registbtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.getvalidatecode:
                usernameVal = usernameEt.getText().toString().trim();
                mobileVal = phoneNumEt.getText().toString().trim();
                //先判断手机号是否存在且正确
                if (!GouUtils.isMobile(mobileVal)) {
                    GouUtils.showTip(this, "请输入正确手机号码！");
                    return;
                    //手机号没问题，如果用户没有输入用户名，默认用户名为手机号
                } else if (usernameVal.length() == 0 || usernameVal == null) {
                    usernameEt.setText(mobileVal);
                    usernameVal = mobileVal;
                }
                getValidateCodeRegist(usernameVal, mobileVal);
                break;
            case R.id.registbtn:
                usernameVal = usernameEt.getText().toString().trim();
                mobileVal = phoneNumEt.getText().toString().trim();
                validateCodeVal = validateEt.getText().toString().trim();
                passwordVal = passwordEt.getText().toString().trim();
                if (GouUtils.isNullOrEmpty(mobileVal)) {
                    GouUtils.showTip(this, "请输入用户名！");
                    break;
                }
                if (!GouUtils.isMobile(mobileVal)) {
                    GouUtils.showTip(this, "请输入正确手机号码！");
                    break;
                }
                if (GouUtils.isNullOrEmpty(passwordVal)) {
                    GouUtils.showTip(this, "请输入密码！");
                    break;
                }
                if (passwordVal.length() > 16 || passwordVal.length() < 6) {
                    GouUtils.showTip(this, "密码长度应为6-16位");
                    break;
                }
                if (passwordVal.matches("[0-9]+")) {
                    GouUtils.showTip(this, "密码不能全为数字");
                    break;
                }
                if (GouUtils.isNullOrEmpty(validateCodeVal)) {
                    GouUtils.showTip(this, "请输入验证码!");
                    break;
                }
                if (!acceptAgreement.isChecked()) {
                    GouUtils.showTip(this, "您不同意该协议，无法完成注册！");
                    break;
                }
                regist(usernameVal, mobileVal, passwordVal, "56234326", validateCodeVal);
        }
    }

    /**
     * 获取弹出窗体用户选择之后的结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (AGREEMENT == resultCode) {
            boolean isAgre = data.getExtras().getBoolean(Const.AGREEMENT);
            if (isAgre)
                acceptAgreement.setChecked(true);
            else
                acceptAgreement.setChecked(false);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 注册获取验证码
     *
     * @param usernameVal
     * @param mobileVal
     */
    private void getValidateCodeRegist(String usernameVal, String mobileVal) {
        AppOperate.getValidateCode(usernameVal, mobileVal, "1", this, new Report() {
            @Override
            public void onSucces(Object o) {
                TLog.log(TAG, "获取验证码成功");
                GouUtils.showTip(RegistActivity.this, "获取成功");
                timerMMS();
            }

            @Override
            public void onError(Object o) {
                String error = o.toString();
                GouUtils.showTip(RegistActivity.this, GouUtils.subString(error));
            }
        });
    }

    /*
    刷新UI
     */
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

    /*
    验证按钮定时器
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

    /**
     * 注册
     */
    private void regist(String name, String mobile, String password, String registrationID, String validateCodeVal) {
        AppOperate.regist(name, mobile, password, registrationID, validateCodeVal, this, new Report() {
            @Override
            public void onSucces(Object o) {
                TLog.log(TAG, "注册成功");
                GouUtils.showTip(RegistActivity.this, "注册成功");
                finish();
            }

            @Override
            public void onError(Object o) {
                String error = o.toString();
                GouUtils.showTip(RegistActivity.this, GouUtils.subString(error));
            }
        });
    }
}
