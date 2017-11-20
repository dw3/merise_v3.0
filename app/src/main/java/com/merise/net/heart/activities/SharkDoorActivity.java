package com.merise.net.heart.activities;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;

import java.lang.ref.WeakReference;
import java.util.Calendar;

/**
 * Created by Administrator on 2016/12/13 0013.
 */

public class SharkDoorActivity extends BaseActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private static final int START_SHAKE = 0x1;
    private static final int AGAIN_SHAKE = 0x2;
    private static final int END_SHAKE = 0x3;
    private static final int OPEN_ERROR = 0x4;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Vibrator mVibrator;//手机震动
    private SoundPool mSoundPool;//摇一摇音效

    //记录摇动状态
    private boolean isShake = false;


    private LinearLayout mTopLayout;
    private LinearLayout mBottomLayout;
    private ImageView mTopLine;
    private ImageView mBottomLine;
    private ImageView isOpenView;

    private MyHandler mHandler;
    private int mWeiChatAudio;
    private TextView titleText;
    private Button back;
    private int xList = 0;
    private int yList = 0;
    private int zList = 0;
    private Calendar calendar;
    private int firstSecond;
    private int twoSecond;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_shark;
    }

    @Override
    public void initView() {

        //设置只竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //初始化View
        mHandler = new MyHandler(this);
        //初始化SoundPool
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        mWeiChatAudio = mSoundPool.load(this, R.raw.weichat_audio, 1);
        //获取Vibrator震动服务
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        back = (Button) findViewById(R.id.back);
        titleText = (TextView) findViewById(R.id.title_text);
        mTopLayout = (LinearLayout) findViewById(R.id.main_linear_top);
        mBottomLayout = ((LinearLayout) findViewById(R.id.main_linear_bottom));
        mTopLine = (ImageView) findViewById(R.id.main_shake_top_line);
        mBottomLine = (ImageView) findViewById(R.id.main_shake_bottom_line);
        isOpenView = (ImageView) findViewById(R.id.is_openview);

        //默认
        mTopLine.setVisibility(View.GONE);
        mBottomLine.setVisibility(View.GONE);
    }


    @Override
    public void initData() {
        calendar = Calendar.getInstance();
        titleText.setText(R.string.shark_door);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取 SensorManager 负责管理传感器
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        if (mSensorManager != null) {
            //获取加速度传感器
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mAccelerometerSensor != null) {
                mSensorManager.registerListener((SensorEventListener) this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    @Override
    protected void onPause() {
        // 务必要在pause中注销 mSensorManager
        // 否则会造成界面退出后摇一摇依旧生效的bug
        if (mSensorManager != null) {
            mSensorManager.unregisterListener((SensorEventListener) this);
        }
        super.onPause();
    }

    ///////////////////////////////////////////////////////////////////////////
    // SensorEventListener回调方法
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            //获取三个方向值
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            if (Math.abs(x) > 18) {
                ++xList;
                TLog.log(TAG, "xlist--" + xList);
            } else if (Math.abs(y) > 18) {
                ++yList;
                TLog.log(TAG, "yList--" + yList);
            } else if (Math.abs(z) > 18) {
                ++zList;
                TLog.log(TAG, "zList--" + zList);
            }
            //满足摇动，非连续摇动，而且，当前门是开的才执行openDoor操作
            if ((xList > 5 || yList > 5 || zList > 5) && !isShake && !(app.devices.get(app.currentIndex).isDoorIsOpen())) {
                isShake = true;
                xList = 0;
                yList = 0;
                zList = 0;
                // TODO: 2016/10/19 实现摇动逻辑, 摇动后进行震动
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            openDoor(String.valueOf(app.devices.get(app.currentIndex).getId()));
                            mHandler.obtainMessage(AGAIN_SHAKE).sendToTarget();
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    /**
     * Handler操作主界面
     */
    private static class MyHandler extends Handler {

        private WeakReference<SharkDoorActivity> mReference;
        private SharkDoorActivity mActivity;

        public MyHandler(SharkDoorActivity activity) {
            mReference = new WeakReference<SharkDoorActivity>(activity);
            if (mReference != null) {
                mActivity = mReference.get();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_SHAKE:
                    //This method requires the caller to hold the permission VIBRATE.
                    mActivity.mVibrator.vibrate(300);
                    //发出提示音
                    mActivity.mTopLine.setVisibility(View.VISIBLE);
                    mActivity.mBottomLine.setVisibility(View.VISIBLE);
                    mActivity.startAnimation(false);//参数含义: (不是回来) 也就是说两张图片分散开的动画
                    break;
                case AGAIN_SHAKE:
                    mActivity.mVibrator.vibrate(300);
                    mActivity.mSoundPool.play(mActivity.mWeiChatAudio, 1, 1, 0, 0, 1);
                    break;
                case END_SHAKE:
                    //整体效果结束, 将震动设置为false
                    mActivity.isShake = false;
                    //自动回锁之后展示上下两种图片回来的效果
                    mActivity.startAnimation(true);
                    break;
                case OPEN_ERROR:
                    mActivity.mVibrator.vibrate(300);
                    //发出提示音
                    mActivity.mSoundPool.play(mActivity.mWeiChatAudio, 1, 1, 0, 0, 1);
                    mActivity.mTopLine.setVisibility(View.VISIBLE);
                    mActivity.mBottomLine.setVisibility(View.VISIBLE);
                    mActivity.startAnimation(false);//参数含义: (不是回来) 也就是说两张图片分散开的动画

            }
        }

    }

    /**
     * 开门
     */
    private void openDoor(String deviceID) {
        AppOperate.openDoor(deviceID, this, new Report() {
            @Override
            public void onSucces(Object o) {
                TLog.log(TAG, "开门成功");
                Toast.makeText(SharkDoorActivity.this, "开门成功", Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isOpenView.setImageDrawable(getResources().getDrawable(R.drawable.gou));
                    }
                });
                //显示开门动画
                mHandler.obtainMessage(START_SHAKE).sendToTarget();
                //设置门锁状态
                app.devices.get(app.currentIndex).setDoorIsOpen(true);
                //弹出dialog，三秒自动消失关闭activitiy
                final MaterialDialog sharkDialog = new MaterialDialog.Builder(SharkDoorActivity.this)
                        .title(R.string.hot_hint)
                        .content(R.string.count_quit)
                        .positiveText(R.string.immediately_quit)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                finish();
                            }
                        })
                        .onAny(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                finish();
                            }
                        })
                        .show();

                //三秒自动关闭和finish
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sharkDialog.dismiss();
                        finish();
                    }
                }, 3 * 1000);


                //自动回锁时间到了设置门锁状态
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        app.devices.get(app.currentIndex).setDoorIsOpen(false);
                        TLog.log(TAG, "成功后设置门锁状态成功");
                    }
                }, Integer.valueOf(app.devices.get(app.currentIndex).getAutoCloseTime()) * 1000);
            }

            @Override
            public void onError(Object o) {
                TLog.log(TAG, "开门失败");
                Toast.makeText(SharkDoorActivity.this, (String) o, Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isOpenView.setImageDrawable(getResources().getDrawable(R.drawable.cha));
                    }
                });
                mHandler.obtainMessage(OPEN_ERROR).sendToTarget();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        app.devices.get(app.currentIndex).setDoorIsOpen(false);
                        mHandler.obtainMessage(END_SHAKE).sendToTarget();
                    }
                }, Integer.valueOf(app.devices.get(app.currentIndex).getAutoCloseTime()) * 1000);
            }
        });
    }


    /**
     * 开启 摇一摇动画
     *
     * @param isBack 是否是返回初识状态
     */
    private void startAnimation(boolean isBack) {
        //动画坐标移动的位置的类型是相对自己的
        int type = Animation.RELATIVE_TO_SELF;

        float topFromY;
        float topToY;
        float bottomFromY;
        float bottomToY;
        if (isBack) {
            topFromY = -0.5f;
            topToY = 0;
            bottomFromY = 0.5f;
            bottomToY = 0;
        } else {
            topFromY = 0;
            topToY = -0.5f;
            bottomFromY = 0;
            bottomToY = 0.5f;
        }

        //上面图片的动画效果
        TranslateAnimation topAnim = new TranslateAnimation(
                type, 0, type, 0, type, topFromY, type, topToY
        );
        topAnim.setDuration(200);
        //动画终止时停留在最后一帧~不然会回到没有执行之前的状态
        topAnim.setFillAfter(true);

        //底部的动画效果
        TranslateAnimation bottomAnim = new TranslateAnimation(
                type, 0, type, 0, type, bottomFromY, type, bottomToY
        );
        bottomAnim.setDuration(200);
        bottomAnim.setFillAfter(true);

        //大家一定不要忘记, 当要回来时, 我们中间的两根线需要GONE掉
        if (isBack) {
            bottomAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //当动画结束后 , 将中间两条线GONE掉, 不让其占位
                    mTopLine.setVisibility(View.GONE);
                    mBottomLine.setVisibility(View.GONE);
                }
            });
        }
        //设置动画
        mTopLayout.startAnimation(topAnim);
        mBottomLayout.startAnimation(bottomAnim);
    }
}
