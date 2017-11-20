package com.android.framewok;

import android.app.Application;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.android.framewok.util.TLog;

public class AppBaseContext extends Application {

    private static final java.lang.String TAG = "AppBaseContext";
    private static AppBaseContext app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        TLog.log(TAG, "ONCREATE...");
    }


    public static AppBaseContext getInstance() {
        return app;
    }

    public void closeSofeKeyboard() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
//        if (isOpen) {
//            TLog.log(TAG, "调整软键盘");
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        }
    }
}
