package com.merise.net.heart.hxpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.framewok.util.TLog;
import com.merise.net.heart.utils.Const;

import static com.merise.net.heart.hxpush.receive.HXReceive.doorBellCount;
import static com.merise.net.heart.hxpush.receive.HXReceive.openDoorCount;
import static com.merise.net.heart.hxpush.receive.HXReceive.otherCount;

public class NotifyReceiver extends BroadcastReceiver {

    private static final String TAG = "NotifyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String aciton = intent.getAction();
        TLog.log(TAG, "ok");
        if (Const.STOP_DOORBELL_ACTION.equals(aciton)) {
            TLog.log(TAG, "门铃通知被删除");
            doorBellCount = 1;
        }
        if (Const.STOP_OTHER_ACTION.equals(aciton)) {
            Log.i(TAG, "其他通知被删除");
            otherCount = 1;
        }
        if (Const.STOP_OPENDOOR_ACTION.equals(aciton)) {
            Log.i(TAG, "开门通知被删除");
            openDoorCount = 1;
        }
    }
}