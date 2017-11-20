package com.merise.net.heart.hxim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // username
        String from = intent.getStringExtra("from");
        // call type
        String type = intent.getStringExtra("type");
        if ("video".equals(type)) { // video call
            context.startActivity(new Intent(context, MyVideoActivity.class)
                    .putExtra("username", from)
                    .putExtra("isComingCall", true)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
