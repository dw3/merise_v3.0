package com.android.framewok.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.android.framewok.AppManager;
import com.android.framewok.interf.BaseViewInterface;
import com.android.framewok.util.TLog;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public abstract class BaseParentActivity extends RxAppCompatActivity implements BaseViewInterface {

    protected static String TAG;
    protected LayoutInflater mInflater;
    protected Context mContext;
    private AppManager appManager = AppManager.getAppManager();
    protected boolean noTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appManager.addActivity(this);
        int layoutID = getLayoutId();
        if (layoutID != 0) {
            setContentView(layoutID);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //透明状态栏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //透明导航栏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
        TAG = this.getClass().getSimpleName();
        mInflater = getLayoutInflater();
        mContext = this;
        initTitle();
        if (!noTitle) {
            initTitleBar();
        }
        initView();
        initData();
    }

    /**
     * 打开一个Activity 默认 不关闭当前activity
     *
     * @param clz
     */
    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex) {
        Intent intent = new Intent(this, clz);
        if (ex != null)
            intent.putExtras(ex);
        startActivity(intent);
        if (isCloseCurrentActivity) {
            finish();
        }
    }

    public void gotoActivityForResult(Class<?> clz, int requestCode) {
        gotoActivityForResult(clz, requestCode, null);
    }

    public void gotoActivityForResult(Class<?> clz, int requestCode, Bundle ex) {
        Intent intent = new Intent(this, clz);
        if (ex != null) {
            intent.putExtras(ex);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appManager.finishActivity(this);
    }

    protected int getLayoutId() {
        return 0;
    }

    protected void initTitleBar() {
    }
}
