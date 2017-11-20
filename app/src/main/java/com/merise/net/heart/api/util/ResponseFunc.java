package com.merise.net.heart.api.util;


import android.content.Intent;
import android.os.Handler;

import com.android.framewok.util.TLog;
import com.merise.net.heart.MainActivity;
import com.merise.net.heart.activities.UnlockActivity;
import com.merise.net.heart.application.XYApplication;
import com.merise.net.heart.bean.Response;
import com.merise.net.heart.exception.ApiException;
import com.merise.net.heart.utils.Constant;


import rx.functions.Func1;

/**
 * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 *
 * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
 */
public class ResponseFunc<T> implements Func1<Response<T>, T> {

    private static final java.lang.String TAG = "ResponseFunc";

    @Override
    public T call(Response<T> response) {
        if (response != null) {
            TLog.log(TAG, "00000000" + response.toString());
        }
        if (response.stateCode == 200) {
            if (response.data == null) {
                return (T) response.message;
            }
            return response.data;
        } else if (response.stateCode == 205) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
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