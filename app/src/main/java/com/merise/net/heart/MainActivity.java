package com.merise.net.heart;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.framewok.util.TLog;
import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.merise.net.heart.activities.ForgetPasswordActivity;
import com.merise.net.heart.activities.HomeActivity;
import com.merise.net.heart.activities.RegistActivity;
import com.merise.net.heart.activities.SharkDoorActivity;
import com.merise.net.heart.application.XYApplication;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.UserInfo;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.GlideCacheUtil;
import com.merise.net.heart.utils.GouUtils;
import com.merise.net.heart.utils.Utils;
import com.merise.net.heart.view.ClearEditView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * 登录界面
 * 包括登录，注册，忘记密码功能模块
 */

public class MainActivity extends BaseActivity {
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


    private String username;
    private String password;
    private String imei;
    private String model;
    private String oldPath;
    private Context activityContext;
    private int OVERLAY_PERMISSION_REQ_CODE = 01;


    @Override
    protected int getLayoutId() {
        TLog.log(TAG, "缓存大小:" + GlideCacheUtil.getInstance().getCacheSize(XYApplication.getContext()));
        return R.layout.activity_main;
    }

//    public void clear(View view) {
//        GlideCacheUtil.getInstance().clearImageAllCache(XYApplication.getContext());
//    }

    @OnClick({R.id.loginBtn, R.id.back, R.id.right, R.id.tv_login_forgetPassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                TLog.log(TAG, "触发点击登录");
                username = usernameEt.getText().toString().trim();
                password = passwordEt.getText().toString().trim();
//                username = "13272552315";
//                password = "123456";
                imei = GouUtils.getID(MainActivity.this);
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
     * 接受是否开启悬浮窗权限的判断
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "权限授予成功！", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * 登录方法
     */
    private void login() {
        AppOperate.getLogin(username, password, imei, model, 1, Utils.getVersionName(this), this, new Report() {
            @Override
            public void onSucces(Object o) {
                TLog.log(TAG + "loginSucces", o.toString());
                app.isLogin = true;
                UserInfo userInfo = (UserInfo) o;
                app.userInfo = userInfo;
                loginSuccess();
                //oldName 修改密码需要
                String oldName = userInfo.getName();
                String faceImg = userInfo.getFaceImg();
                String realFaceImg = faceImg.replaceAll("\\\\", "/");
                spt.saveSharedPreferences(Const.OLDNAME, oldName);
//                spt.saveSharedPreferences(Const.FACEIMG,);
                //保存账号信息(方便下次登录)
                spt.saveSharedPreferences(Const.NICKNAME, username);
                //头像更新的方法
                spt.saveSharedPreferences(Const.NETHEADPATH, Constant.TestUrl + realFaceImg);
                TLog.log(TAG + "网络获取的faceImg-------", spt.readSharedPreferencesString(Const.NETHEADPATH));

                //用登陆账号和密码注册登陸环信账号
                HXR2L hxr2L = new HXR2L(username, password);
                Thread thread = new Thread(hxr2L);
                thread.start();


//                Intent main2Home = new Intent();
//                main2Home.setClass(MainActivity.this, HomeActivity.class);
//                startActivity(main2Home);
            }

            @Override
            public void onError(Object o) {
                String e = (String) o;
                GouUtils.showTip(MainActivity.this, GouUtils.subString(e));
            }
        });
    }

    /**
     * HX注册登陆等等
     */
    public class HXR2L implements Runnable {
        private String username;
        private String password;

        public HXR2L(String userName, String passWord) {
            this.username = userName;
            this.password = passWord;
        }

        @Override
        public void run() {
            //注册账号
            try {
                EMClient.getInstance().createAccount(username, password);
                TLog.log(TAG, "环信注册成功！！");
                TLog.log(TAG, "进入登陆");
                EMClient.getInstance().login(username, password, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        TLog.log(TAG, "环信登陆成功");
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        //登陆成功后添加接受推送信息监听
//                            registerMessageListener();
                    }

                    @Override
                    public void onError(int i, String s) {
                        TLog.log(TAG, "环信登陆失败" + s.toString());
                    }

                    @Override
                    public void onProgress(int i, String s) {
                        TLog.log(TAG, "环信正在登陆" + s.toString());
                    }
                });
            } catch (HyphenateException e) {
                TLog.log(TAG, "环信注册失败！！" + e.toString() + e.getErrorCode());
                int errorCode = e.getErrorCode();
//                如果用户已经注册，直接登陆
                if (errorCode == EMError.USER_ALREADY_EXIST) {
                    TLog.log(TAG, "进入登陆");
                    EMClient.getInstance().login(username, password, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            TLog.log(TAG, "环信登陆成功");
                            EMClient.getInstance().groupManager().loadAllGroups();
                            EMClient.getInstance().chatManager().loadAllConversations();
                            //登陆成功后添加接受推送信息监听
//                            registerMessageListener();
                        }

                        @Override
                        public void onError(int i, String s) {
                            TLog.log(TAG, "环信登陆失败" + s.toString());
                        }

                        @Override
                        public void onProgress(int i, String s) {
                            TLog.log(TAG, "环信正在登陆" + s.toString());
                        }
                    });
                } else {
                    TLog.log(TAG, "下面环信注册失败");
                }
            }
        }

    }


    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.login);
        rightRegister.setText(R.string.regist);
        activityContext = this;
        //保存在本地的头像路径
        oldPath = spt.readSharedPreferencesString(Const.NETHEADPATH);
        TLog.log(TAG, "oldpath----" + oldPath);
        Glide.with(MainActivity.this).load(oldPath).placeholder(R.drawable.ic720).error(R.drawable.ic720)
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
        //手机是否开启悬浮窗功能
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无弹窗权限，请授权！", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void onResume() {
        Glide.with(this)
                .load(spt.readSharedPreferencesString(Const.NETHEADPATH))
                .placeholder(R.drawable.ic720)
                .error(R.drawable.ic720)
                .centerCrop()
                .crossFade()
                .bitmapTransform(new CropCircleTransformation(this))
                .into(headImage);
        super.onResume();
    }

}
