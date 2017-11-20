package com.merise.net.heart.api.util;


import android.content.Intent;
import android.os.Handler;

import com.android.framewok.util.TLog;
import com.merise.net.heart.MainActivity;
import com.merise.net.heart.activities.UnlockActivity;
import com.merise.net.heart.application.XYApplication;
import com.merise.net.heart.bean.Page;
import com.merise.net.heart.bean.Response;
import com.merise.net.heart.exception.ApiException;
import com.merise.net.heart.utils.Constant;

import rx.functions.Func1;

public class PageResponseFunc<T> implements Func1<Response<Page<T>>, T> {

    private static final String TAG = "PageResponseFunc";

    @Override
    public T call(Response<Page<T>> response) {
        if (response != null) {
            TLog.log(TAG, response.toString());
        }
        if (response.stateCode == 200) {
            return response.data.rows;
        } else if (response.stateCode == 205) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    AppManager.getAppManager().finishAllActivity();
                    Intent intent;
                    if (XYApplication.spt.readSharedPreferencesBoolean(Constant.IS_OPEN_FINGER) || XYApplication.spt.readSharedPreferencesBoolean(Constant.IS_OPEN_GESTURE)) {
                        intent = new Intent(XYApplication.getInstance(), UnlockActivity.class);//如果手势密码开启或者指纹开启则进入相应界面登录
                    } else {
                        intent = new Intent(XYApplication.getInstance(), MainActivity.class);//直接接入登录界面登录
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    XYApplication.reset();
                    XYApplication.getInstance().startActivity(intent);
                }
            }, 2000);
            throw new ApiException(response.message);
        } else {
            throw new ApiException(response.message);
        }
    }
}