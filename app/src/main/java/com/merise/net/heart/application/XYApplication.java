package com.merise.net.heart.application;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.android.framewok.AppBaseContext;
import com.android.framewok.AppManager;
import com.android.framewok.util.TLog;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.merise.net.heart.bean.Device;
import com.merise.net.heart.bean.DeviceStatus;
import com.merise.net.heart.bean.FoucsInfo;
import com.merise.net.heart.bean.Function;
import com.merise.net.heart.bean.UserInfo;
import com.merise.net.heart.hxim.CallReceiver;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.SharedPreferencesTools;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;


/**
 * Created by Administrator on 2016/9/8.
 */
public class XYApplication extends AppBaseContext implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "XYApplication";
    public static List<Device> devices = new ArrayList<>();
    public static List<Function> functionList = new ArrayList<>();
    public static int currentIndex = -1; //当前操作设备在devices里面的下标
//    public int currentDeviceIndex = 0;// 记录当前房间的下标
    public static UserInfo userInfo;
    public static FoucsInfo foucsInfo;
    //判断是否存在指纹识别模块
//    public static final String HAS_FINGERPRINT_API = "hasFingerPrintApi";
    public static SharedPreferencesTools spt;
    public static boolean isLogin;// 标注用户是否登录

    public static int timeForGesture;//手势解锁的尝试次数
    public static int gestureMinLength = 5;//
    public static Context context;
    public CallReceiver callReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        spt = new SharedPreferencesTools(this);
        //HXPUSH
        context = getApplicationContext();
        EMOptions options = new EMOptions();
        EMClient.getInstance().init(getApplicationContext(), options);
        //注册监听消息
        registerMessageListener();
        //注册监听接受视屏通话信息
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }
        //register incoming call receiver
        context.registerReceiver(callReceiver, callFilter);
    }

    public static Context getContext() {
        return context;
    }

    public XYApplication() {

    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        System.exit(0);
    }

    public void exit() {
        isLogin = false;
        AppManager.getAppManager().AppExit(this);
    }

    public void finishAllActivity() {
        AppManager.getAppManager().finishAllActivity();
    }

    /**
     * 重置数据
     */
    public static void reset() {
        devices.clear();
        userInfo = null;
        currentIndex = -1;
        functionList.clear();
        isLogin = false;
    }



    /*
为推送消息设置监听
 */

    private void registerMessageListener() {

        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                DealMessageHelper.getInstance().parseMessages(messages);
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                TLog.log(TAG, "收到透传消息");
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> list) {
                TLog.log(TAG, "收到已读");
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> list) {
                TLog.log(TAG, "收到已送达回执");
            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {
                TLog.log(TAG, "消息状态变动");
            }
        });
    }

    /**
     * 搞个静态内部类
     */
    public static class DealMessageHelper {

        protected String TAG = "DealMessageHelper";


        private static DealMessageHelper instance = null;


        public synchronized static DealMessageHelper getInstance() {
            if (instance == null) {
                instance = new DealMessageHelper();
            }
            return instance;
        }

        /*
        解析服务器推送数据
         */
        public void parseMessages(List<EMMessage> messages) {

            EMMessage[] msgs = messages.toArray(new EMMessage[messages.size()]);
            for (EMMessage msg : msgs) {
                TLog.log(TAG, "type---" + msg.getChatType());
                if (msg.getChatType() == EMMessage.ChatType.Chat) {
                    EMTextMessageBody body = (EMTextMessageBody) msg.getBody();
                    //获取自定义消息
                    String messageID = msg.getStringAttribute(Const.messageID, null);
                    String url = msg.getStringAttribute(Const.URL, null);
                    String type = msg.getStringAttribute(Const.type, null);
                    String deviceID = msg.getStringAttribute(Const.DEVICEID, null);
//                    String doorMagnet = msg.getStringAttribute(Constant.DOORMAGNET, null);
//                    String online = msg.getStringAttribute(Constant.ONLINE, null);
                    String value = msg.getStringAttribute(Constant.VALUE, null);
                    //获取原定义消息
                    String text = body.getMessage();
                    String from = msg.getFrom();
                    String to = msg.getTo();
                    TLog.log(TAG, "messageID=====" + messageID + "-----url-----" + url + "----type----" + type);
                    //门铃推送处理
                    if (type.equalsIgnoreCase(Const.DOORBELL)) {
                        precessDoorBell(url, text);
                    }
                    //门磁入侵报警提示
                    else if (type.equalsIgnoreCase(Const.HXALARM)) {
                        precessAlarm(text);
                    }
                    //异地登录提示
                    else if (type.equalsIgnoreCase(Const.UNLOGIN)) {
                        precessUnlogin(text);
                    }
                    //开门提示
                    else if (type.equalsIgnoreCase(Const.OPENDOOR)) {
                        precessOpenDoor(text, deviceID);
                    }
                    //其他提示
                    else if (type.equalsIgnoreCase(Const.OTHER)) {
                        precessOhter(text);
                    }
                    //门磁状态
                    else if (type.equalsIgnoreCase(Constant.DOORMAGNET)) {
                        precessStatus(value, deviceID, type);
                    }
                    //在线状态
                    else if (type.equalsIgnoreCase(Constant.ONLINE)) {
                        precessStatus(value, deviceID, type);
                    }
                }
            }
        }

        /**
         * * 处理从服务器解析的数据(其他记录)
         *
         * @param text
         */
        private void precessOhter(String text) {
            TLog.log(TAG, "precessOhter");
            Intent intent = new Intent();
            intent.putExtra(Const.OPENDOOR, text);
            intent.setAction(Const.HXPUSHOTHER);
            getContext().sendBroadcast(intent);
        }

        /**
         * * 处理从服务器解析的数据(开门记录)
         *
         * @param text
         */
        private void precessOpenDoor(String text, String deviceID) {
            TLog.log(TAG, "precessOpenDoor");
            Intent intent = new Intent();
            intent.putExtra(Const.OPENDOOR, text);
            intent.putExtra(Const.DEVICEID, deviceID);
            intent.setAction(Const.HXPUSHOPENDOOR);
            getContext().sendBroadcast(intent);
        }

        /**
         * 处理从服务器解析的数据(异地登录)
         *
         * @param text
         */
        private void precessUnlogin(String text) {
            TLog.log(TAG, "precessUnlogin");
            Intent intent = new Intent();
            intent.putExtra(Const.UNLOGIN, text);
            intent.setAction(Const.HXPUSHUNLOGIN);
            getContext().sendBroadcast(intent);
        }


        /**
         * 处理从服务器解析的数据(门铃)
         */
        public void precessDoorBell(String url, String text) {
            TLog.log(TAG, "precessdoorbell");
            Intent intent = new Intent();
            intent.putExtra(Const.DOORBELL, text);
            intent.putExtra(Const.URL, url);
            intent.setAction(Const.HXPUSHDOORBEll);
            getContext().sendBroadcast(intent);
        }

        /**
         * 处理从服务器解析的数据(门磁报警)
         */
        public void precessAlarm(String text) {
            Intent intent = new Intent();
            intent.putExtra(Const.ALARMTEXT, text);
            intent.setAction(Const.HXPUSHALARM);
            getContext().sendBroadcast(intent);
        }

        /**
         * 处理从服务器解析的数据(门磁状态/上电状态)
         */
        private void precessStatus(String value, String deviceID, String type) {
            TLog.log(TAG, "更新：" + value);
            for (int i = 0; i < devices.size(); i++) {
                Device device = devices.get(i);
//                TLog.log(TAG, "deviceID:" + deviceID + "deviceIDLocal:" + device.getId());
                if (deviceID.equals(String.valueOf(device.getId()))) {
                    List<DeviceStatus> status = devices.get(i).getStatus();
                    for (int j = 0; j < status.size(); j++) {
                        if (status.get(j).getField().equals(type)) {
                            status.get(j).setType(Integer.valueOf(value));
                        }
                    }
//                    for (DeviceStatus status1 : status) {
//                        TLog.log(TAG, status1.toString());
//                    }
                    devices.get(i).setStatus(status);
                }
            }
            TLog.log(TAG, "precessStatus...");
            Intent intent = new Intent();
            intent.setAction(Constant.UPDATEDEVICESTATUS);
            getContext().sendBroadcast(intent);
        }
    }

}
