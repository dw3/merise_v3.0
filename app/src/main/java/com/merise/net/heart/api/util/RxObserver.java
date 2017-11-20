package com.merise.net.heart.api.util;

import com.android.framewok.util.DialogHelper;
import com.android.framewok.util.NetUtil;
import com.android.framewok.util.TLog;
import com.merise.net.heart.application.XYApplication;

import rx.Observer;

/**
 * http://blog.csdn.net/jdsjlzx/article/details/51534504 //解决重复请求取消订阅的问题
 * Created by Administrator on 2016/9/27.
 */
public abstract class RxObserver<T> implements Observer<T> {
    private static final java.lang.String TAG = "Observer";

    @Override
    public void onCompleted() {
        TLog.log("Rx", "Rx onCompleted...");
        DialogHelper.stopProgressDlg();
    }

    @Override
    public void onError(Throwable e) {
        DialogHelper.stopProgressDlg();
        TLog.log(TAG, e.getMessage());
        if (!NetUtil.isNetConnected(XYApplication.getInstance())) {
            _onError("请检查网络!");
            return;
        }
        _onError(e.getMessage());
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    public abstract void _onNext(T t);

    public abstract void _onError(String e);
}
