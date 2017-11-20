package com.merise.net.heart.api.util;


import com.android.framewok.util.DialogHelper;
import com.android.framewok.util.NetUtil;
import com.android.framewok.util.TLog;
import com.merise.net.heart.application.XYApplication;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by wangdawang on 2016/8/30 0030.
 */
public abstract class RxSubscriber<T> extends Subscriber<T> {
    private static final java.lang.String TAG = "RxSubscriber";

    @Override
    public void onCompleted() {
        TLog.log("Rx", "Rx onCompleted...");
        DialogHelper.stopProgressDlg();
        XYApplication.getInstance().closeSofeKeyboard();
    }

    @Override
    public void onError(Throwable e) {
        try {
            DialogHelper.stopProgressDlg();
            TLog.log(TAG + "onError0", e.toString() + "");
//            SocketTimeoutException socketTimeoutException;
//            ConnectException connectException;
            if (!NetUtil.isNetConnected(XYApplication.getInstance())) {
                _onError("请检查网络!");
                return;
            }
            _onError(e.getMessage());
        } catch (Exception e1) {
            TLog.log(TAG + "onError1", e1.toString() + "");
        }
    }

    @Override
    public void onNext(T t) {
        TLog.log("Rx", "Rx onNext...");
        _onNext(t);
    }

    public abstract void _onNext(T t);

    public abstract void _onError(String e);
}
