package com.merise.net.heart.activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.framewok.util.TLog;
import com.bumptech.glide.Glide;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.UserInfo;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.GouUtils;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.utils.Utils;
import com.merise.net.heart.view.ClearEditView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 作者:xiangyang
 * 日期:2016/11/17
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.iv_head_image)
    ImageView headImage;
    @BindView(R.id.cb_passwordVisible)
    CheckBox cbPasswordVisible;
    @BindView(R.id.tv_login_forgetPassword)
    TextView tvLoginForgetPassword;
    @BindView(R.id.usernameEt)
    ClearEditView usernameEt;
    @BindView(R.id.passwordEt)
    EditText passwordEt;
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button rightRegister;

    @BindView(R.id.root)
    LinearLayout root;

    private String username;
    private String password;
    private String imei;
    private String model;
    String oldPath;

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.login);
        rightRegister.setText(R.string.regist);
        oldPath = spt.readSharedPreferencesString(Constant.NETHEADPATH);
        Glide.with(LoginActivity.this).load(oldPath).override(100, 100).placeholder(R.drawable.ic720).error(R.drawable.ic720)
                .bitmapTransform(new CropCircleTransformation(this)).into(headImage);
        //        为密码checkbox设置监听
        cbPasswordVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //选中之后设置文本内容可见
                if (isChecked) {
                    TLog.log(TAG, "isChecked" + isChecked);
                    passwordEt.setInputType(0x90);
                } else {
                    passwordEt.setInputType(0x81);
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.loginBtn, R.id.back, R.id.right, R.id.tv_login_forgetPassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                TLog.log(TAG, "触发点击登录");
                username = usernameEt.getText().toString().trim();
                password = passwordEt.getText().toString().trim();
                imei = GouUtils.getID(LoginActivity.this);
                model = GouUtils.getModel() + "  " + GouUtils.getManufacturer();
                TLog.log(TAG, "username：" + username + "password：" + password);
                if ("".equals(username))
                    Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_LONG).show();
                else if (username.length() > 16)
                    Toast.makeText(this, "用户名错误！", Toast.LENGTH_LONG).show();
                else if ("".equals(password))
                    Toast.makeText(this, "密码不能为空！", Toast.LENGTH_LONG).show();
                else if (password.length() < 6 || password.length() > 16)
                    Toast.makeText(this, "密码错误！", Toast.LENGTH_LONG).show();
                else {
                    login();
                }
                break;
            case R.id.back:
                onBackPressed();
                break;
            //注册小模块
            case R.id.right:
                startActivity(new Intent(this, RegistActivity.class));
                break;
            case R.id.tv_login_forgetPassword:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
        }
    }

    /**
     * 登录方法
     */
    private void login() {
        AppOperate.getLogin(username, password, imei, model, 1, Utils.getVersionName(this), this, new Report() {
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
