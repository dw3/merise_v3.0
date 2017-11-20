package com.merise.net.heart.api.util;

import com.android.framewok.util.DialogHelper;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by wangdawang on 2016/8/30 0030.
 */
public class RxHelper<T> {
    private String mMessage;

    public RxHelper(String mMessage) {
        this.mMessage = mMessage;
    }

    public Observable.Transformer<T, T> io_main(final RxAppCompatActivity context) {
        return io_main(context, true);
    }

    public Observable.Transformer<T, T> io_main(final RxAppCompatActivity context, final boolean show) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                Observable observable = tObservable
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                if (show) {
                                    DialogHelper.showProgressDlg(context, mMessage);
//                                    DialogHelper.showDialogForLoading(context,mMessage,false);
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxLifecycle.bindUntilEvent(context.lifecycle(), ActivityEvent.STOP));
                return observable;
            }
        };
    }
}
