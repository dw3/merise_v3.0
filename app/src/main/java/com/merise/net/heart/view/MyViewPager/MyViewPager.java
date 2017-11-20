package com.merise.net.heart.view.MyViewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class MyViewPager extends ViewPager {
    private int startX;
    private int startY;

    public MyViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                int dX = (int) (ev.getX() - startX);
                int dY = (int) (ev.getY() - startX);
                if (Math.abs(dX) > Math.abs(dY)) {//左右滑动
                    return true;
                } else {//上下滑动
                    return false;
                }
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
