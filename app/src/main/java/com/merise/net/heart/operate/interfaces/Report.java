package com.merise.net.heart.operate.interfaces;

/**
 * Created by Administrator on 2016/9/9.
 */
public interface Report<T> {
    void onSucces(T t);
    void onError(T t);
}
