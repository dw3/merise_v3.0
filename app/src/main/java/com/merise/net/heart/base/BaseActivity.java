package com.merise.net.heart.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.framewok.base.BaseParentActivity;
import com.android.framewok.util.TLog;
import com.merise.net.heart.MainActivity;
import com.merise.net.heart.R;
import com.merise.net.heart.activities.HomeActivity;
import com.merise.net.heart.api.util.RetrofitUtil;
import com.merise.net.heart.application.XYApplication;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.ITools;
import com.merise.net.heart.utils.SharedPreferencesTools;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by wangdawang on 2016/8/30 0030.
 */
public abstract class BaseActivity extends BaseParentActivity implements View.OnClickListener {
    public XYApplication app;
    public SharedPreferencesTools spt;

    @Override
    public void initTitle() {
        noTitle = true;
    }

    @Override
    public void onClick(View v) {

    }

    public void initListener() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        spt = new SharedPreferencesTools(this);
        super.onCreate(savedInstanceState);
        app = (XYApplication) XYApplication.getInstance();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.topRootRL);
        if (relativeLayout != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ITools.getActionBarHeight(getBaseContext()));
                params.topMargin = ITools.getStatusHeight(getBaseContext());
                relativeLayout.setLayoutParams(params);
//                //透明状态栏
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                //透明导航栏
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
//        closeSofeKeyboard();
        initListener();
    }

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }
//    public void closeSofeKeyboard() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
//        if (isOpen) {
//            TLog.log(TAG, "调整软键盘");
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RetrofitUtil.cancelAllRequest();
    }

    /**
     * tag(1、代表普通登录,0、代表有验证码的登录)
     */
    public void onLogin(int tag) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Const.TAG, tag);
        this.startActivity(intent);
    }

    public void loginSuccess() {
        app.isLogin = true;
        app.timeForGesture = 3;
        app.finishAllActivity();
        gotoActivity(HomeActivity.class);
        String headImgPath = app.userInfo.getFaceImg();// 服务器传过来的头像路径
        //当前服务器传回来的头像完整路径
        String newPath = Constant.TestUrl + headImgPath.replaceAll("\\\\", "/");
        TLog.log(TAG, "head image path:" + newPath);
        spt.saveSharedPreferences(Constant.NETHEADPATH, newPath);
    }
}
