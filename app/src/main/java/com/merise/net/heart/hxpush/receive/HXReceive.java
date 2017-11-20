package com.merise.net.heart.hxpush.receive;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.MainActivity;
import com.merise.net.heart.R;
import com.merise.net.heart.activities.HomeActivity;
import com.merise.net.heart.activities.ItemActivity;
import com.merise.net.heart.activities.RecordActivity;
import com.merise.net.heart.application.XYApplication;
import com.merise.net.heart.fragments.DefenceFragment;
import com.merise.net.heart.hxpush.NotifyReceiver;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.utils.GouUtils;


/**
 * Created by Administrator on 2016/11/28 0028.
 */

public class HXReceive extends BroadcastReceiver {
    private static final String TAG = "HXReceive";
    private Vibrator vibrator;
    private MediaPlayer player;
    private String message;
    private NotificationCompat.Builder mBuilder;
    private PendingIntent deleteIntent;
    private PendingIntent contentIntent;
    private String deviceId;
    public static int doorBellCount = 1;// 统计门铃
    public static int otherCount = 1;// 统计其他提示
    public static int openDoorCount = 1;// 开门提示

    public static final int DOORBELLID = 0x01;
    public static final int OPENDOORID = 0x02;
    public static final int OTHER = 0x03;
    public static final int HXALARM = 0x04;
    public static final int UNLOGIN = 0x05;

    private NotificationManager unNotification;
    private NotificationCompat.Builder unBuilder;
    private NotificationCompat.Builder otherBuilder;

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        //共用的通知管理器
        unNotification = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        //门铃推送
        if (intentAction.equals(Const.HXPUSHDOORBEll)) {
            TLog.log(TAG, "门铃推送");
            message = intent.getStringExtra(Const.DOORBELL);


            mBuilder = new NotificationCompat.Builder(context);

            mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon));

            mBuilder.setContentTitle("门铃通知");

            mBuilder.setContentText("2货，有人敲门");

            mBuilder.setTicker("门铃响了！！！");

            mBuilder.setWhen(System.currentTimeMillis());

            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

            mBuilder.setSmallIcon(R.drawable.app_icon);

            mBuilder.setAutoCancel(true);

            mBuilder.setSound(Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.goldenfst));
            // 设置通知栏清空意图
            TLog.log(TAG, "1");
            Intent notiintent = new Intent();
            intent.setAction(Const.STOP_DOORBELL_ACTION);
            deleteIntent = PendingIntent
                    .getBroadcast(context, 3, notiintent, 3);
            mBuilder.setDeleteIntent(deleteIntent);

            TLog.log(TAG, "3" + GouUtils.isRunning(context) + "   " + XYApplication.isLogin);
            // 如果当前程序运行且处于登录状态（有两种情况选择相应的contentIntent）
            if (GouUtils.isRunning(context) && XYApplication.isLogin) {
//            String currentDeviceId = BaseActivity.app.devices.get(
//                    BaseActivity.app.currentIndex).getId() + "";
                // 第一种
                if ("com.merise.net.heart.activities.HomeActivity".equals(GouUtils
                        .getTopActivity(context))
                        && HomeActivity.currentFragment == 0) {
                    TLog.log(TAG, "第一种");
                    // 主界面登录可选状态1,deviceId和当前currentDeviceId一致的话就启用conentIntent统计数据
                    Intent intent1 = new Intent();
                    intent.setAction(Const.STOP_DOORBELL_ACTION);
                    contentIntent = PendingIntent.getBroadcast(context, 4,
                            intent1, 4);
                }
                //新增
                else if ("com.merise.net.heart.activities.HomeActivity".equals(GouUtils
                        .getTopActivity(context))
                        && HomeActivity.currentFragment != 0) {
                    HomeActivity.viewpager.setCurrentItem(0);
                    Intent intent2 = new Intent();
                    intent.setAction(Const.STOP_DOORBELL_ACTION);
                    contentIntent = PendingIntent.getBroadcast(context, 11,
                            intent2, 11);
                }
                // 第二种
                else {
                    TLog.log(TAG, "第二种");
                    Intent I = new Intent();
                    I.setClass(context, HomeActivity.class);
                    contentIntent = PendingIntent.getActivity(context, 5, I, PendingIntent.FLAG_UPDATE_CURRENT);
                }

                //第三种
            } else {
                TLog.log(TAG, "第三种");
                Intent I = new Intent(context, MainActivity.class);
                contentIntent = PendingIntent.getActivity(context, 6, I,
                        PendingIntent.FLAG_UPDATE_CURRENT);
            }
            // 根据不同情况选择相应的contentIntent
            mBuilder.setContentIntent(contentIntent);
            // 显示
            unNotification.notify(DOORBELLID, mBuilder.build());
            doorBellCount++;
        }

        /*
        ---------------------------------------------------------------------------------------------------
         */

        //门磁拆卸报警
        else if (intentAction.equals(Const.HXPUSHALARM)) {
            TLog.log(TAG, "门磁拆卸报警");
            message = intent.getStringExtra(Const.ALARMTEXT);
            // 等待1秒，震动2秒，等待1秒，震动3秒
            long[] pattern = {1000, 2000, 1000, 3000};
            if (player == null && vibrator == null) {
                player = MediaPlayer.create(context, R.raw.myalarm);
                vibrator = (Vibrator) context
                        .getSystemService(Context.VIBRATOR_SERVICE);
                player.start();
                vibrator.vibrate(pattern, 0);
                // 循环播放
                player.setLooping(true);
                // 弹出对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setType(
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_main_info);
                TextView tv_title = (TextView) window
                        .findViewById(R.id.tv_dialog_title);
                tv_title.setText("门磁报警");
                TextView tv_message = (TextView) window
                        .findViewById(R.id.tv_dialog_message);
                tv_message.setText(message);
                Button close = (Button) window.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 关闭效果
                        if (player != null) {
                            player.release();
                            player = null;
                            vibrator.cancel();
                            vibrator = null;
                            dialog.dismiss();
                        }
                    }
                });
            }
        }


        /*
        -------------------------------------------------------------------------------------------------
         */
        //异地登录提示
        else if (intentAction.equals(Const.HXPUSHUNLOGIN)) {
            TLog.log(TAG, "异地登录");
            message = intent.getStringExtra(Const.UNLOGIN);
            XYApplication.isLogin = false;
            unBuilder = new NotificationCompat.Builder(context);

            unBuilder.setSmallIcon(R.drawable.app_icon);

            unBuilder.setAutoCancel(true);

            unBuilder.setTicker("异地登录");

            unBuilder.setContentTitle("= =！");

            unBuilder.setContentText(message);

            unBuilder.setWhen(System.currentTimeMillis());

            unBuilder.setDefaults(Notification.DEFAULT_ALL);

            Intent i = new Intent(context, MainActivity.class);
            i.putExtra(Const.TAG, 1);

            PendingIntent UnContentIntent = PendingIntent.getActivity(
                    context, 7, i, PendingIntent.FLAG_UPDATE_CURRENT);

            unBuilder.setContentIntent(UnContentIntent);

            unNotification.notify(UNLOGIN, unBuilder.build());
        }

        /*
        -------------------------------------------------------------------------------------------------
         */
        //开门提示
        else if (intentAction.equals(Const.HXPUSHOPENDOOR)) {
            TLog.log(TAG, "开门提示");
//            nm = (NotificationManager) context
//                    .getSystemService(Context.NOTIFICATION_SERVICE);
            //获取推送信息
            message = intent.getStringExtra(Const.OPENDOOR);
            deviceId = intent.getStringExtra(Const.DEVICEID);

            unBuilder = new NotificationCompat.Builder(context);

            unBuilder.setSmallIcon(R.drawable.app_icon);

            unBuilder.setAutoCancel(true);

            unBuilder.setTicker("开门提示");

            unBuilder.setContentTitle("哈哈哈哈哈哈");

            unBuilder.setContentText(message);

            unBuilder.setWhen(System.currentTimeMillis());

            unBuilder.setDefaults(Notification.DEFAULT_ALL);
            //通知删除事件
            Intent deleteIntent = new Intent();
            deleteIntent.setAction(Const.STOP_OPENDOOR_ACTION);
            PendingIntent deIntent = PendingIntent.getBroadcast(context,
                    8, deleteIntent, 8);
            unBuilder.setDeleteIntent(deIntent);
            //通知点击事件
            PendingIntent contentIntent = null;
            // 判断应用是否启动并且是否处于登录状态
            // 若启动且处于登录状态则当点击接收到开门推送时根据权限判断是否进入记录查看界面。
            if (GouUtils.isRunning(context) && XYApplication.isLogin) {
                Intent I = new Intent();
                I.setClass(context, RecordActivity.class);
                I.putExtra(Const.DEVICEID, deviceId);
                // 重置推送条数
                contentIntent = PendingIntent.getActivity(context,
                        7, I, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            unBuilder.setContentIntent(contentIntent);
            unNotification.notify(OPENDOORID, unBuilder.build());
            openDoorCount++;
        }
         /*
        -------------------------------------------------------------------------------------------------
         */
        //其他提示
        else if (intentAction.equals(Const.HXPUSHOTHER)) {
            TLog.log(TAG, "其他提示");
            message = intent.getStringExtra(Const.OTHER);

            otherBuilder = new NotificationCompat.Builder(context);
            // 换Xmessage
            otherBuilder.setContentTitle("其他提示");
            // 换otherCount + ""
            otherBuilder.setContentText("其他提示内容");

            otherBuilder.setTicker("有其他提示");

            otherBuilder.setDefaults(Notification.DEFAULT_ALL);

            otherBuilder.setWhen(System.currentTimeMillis());

            otherBuilder.setSmallIcon(R.drawable.app_icon);

            otherBuilder.setAutoCancel(true);

            Intent otherIntent = new Intent();
            otherIntent.setClass(context, NotifyReceiver.class);
            otherIntent.setAction(Const.STOP_OTHER_ACTION);
            // 发送广播统计其他提示
            PendingIntent otherDeleIntent = PendingIntent.getBroadcast(context,
                    6, otherIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            otherBuilder.setDeleteIntent(otherDeleIntent);
            otherBuilder.setContentIntent(otherDeleIntent);
            unNotification.notify(OTHER, otherBuilder.build());
            otherCount++;
        }
        /**
         * 更新设备状态
         */
        else if(intentAction.equals(Constant.UPDATEDEVICESTATUS)){
            TLog.log(TAG,"update device status...");
            DefenceFragment.adapter.notifyDataSetChanged();
            ItemActivity.mAdapter.notifyDataSetChanged();
        }
    }
}
