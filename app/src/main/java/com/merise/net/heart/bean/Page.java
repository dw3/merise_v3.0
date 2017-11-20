package com.merise.net.heart.bean;


import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/9/21.
 */
public class Page<T> {
    @Nullable
    public int total;
    @Nullable
    public T rows;

    @Override
    public String toString() {
        return "Page{" +
                "rows=" + rows +
                ", total=" + total +
                '}';
    }
}
