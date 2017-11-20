package com.merise.net.heart.activities;

import android.graphics.Color;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.framewok.util.TLog;
import com.bigkoo.alertview.AlertView;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.ITools;
import com.merise.net.heart.view.gesture.GestureLockViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:xiangyang
 * 日期:2016/11/14
 */
public class UpdateGestureUpdateActivity extends BaseActivity {
    @BindView(R.id.gestureLockViewGroup)
    GestureLockViewGroup gestureLockViewGroup;
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.headImg)
    ImageView headImg;
    @BindView(R.id.tipTV)
    TextView tipTV;
    @BindView(R.id.title_text)
    TextView titleText;

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.gesture_pwd);
    }

    @Override
    public void initData() {
        gestureLockViewGroup.setMinLength(app.gestureMinLength); //手势密码的长度
        final char[] answerFromSpt = spt.readSharedPreferencesString(Constant.GESTUREANSWER).toCharArray();
        //根据需求办事
//        if (answerFromSpt == null || answerFromSpt.length == 0) {
            gestureLockViewGroup.setSettingPwd(true);//第一次设置手势密码
            gestureLockViewGroup.setUnMatchExceedBoundary(2);
            tipTV.setText("请绘制手势密码");
//        } else {
//            gestureLockViewGroup.setSettingPwd(false);//修改手势密码
//            int[] answer = new int[answerFromSpt.length];
//            for (int i = 0; i < answerFromSpt.length; i++) {
//                answer[i] = answerFromSpt[i] - 48;
////                TLog.log(TAG, "手势密码为:" + answer[i]);
//            }
//            tipTV.setText("请输入旧的手势密码");
//            gestureLockViewGroup.setUnMatchExceedBoundary(app.timeForGesture);
//            gestureLockViewGroup.setAnswer(answer);
//        }
        gestureLockViewGroup
                .setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {

                    @Override
                    public void onUnmatchedExceedBoundary() {
                        if (answerFromSpt == null || answerFromSpt.length == 0) { //设置新的手势密码
                            gestureLockViewGroup.setUnMatchExceedBoundary(2);
                        } else {
                            gestureLockViewGroup.reset();
                            tipTV.setText("不能尝试了，该干嘛干嘛去！");
                            ITools.vibrator(UpdateGestureUpdateActivity.this);
                            spt.saveSharedPreferences(Constant.IS_OPEN_GESTURE, false);//手势密码错误3次，关闭手势密码功能
                            finish();
//                            gestureLockViewGroup.setUnMatchExceedBoundary(5);
                        }
                    }

                    @Override
                    public void onGestureIllegal() {
//                        setTextMessage(getResources().getString(R.string.gesture_pwd_at_least_length));
                        setTextMessage("密码长度至少" + app.gestureMinLength + "个点");
                        ITools.vibrator(UpdateGestureUpdateActivity.this);
                    }

                    @Override
                    public void onSettingGesture(boolean result) {
                        if (result) {
                            tipTV.setText("设置完成");
                            tipTV.setTextColor(Color.RED);
                            ITools.vibrator(UpdateGestureUpdateActivity.this);
                            gestureLockViewGroup.reset();
                            int[] answer = gestureLockViewGroup.getmAnswer();
                            String answerStr = arrayToString(answer);
                            spt.saveSharedPreferences(Constant.GESTUREANSWER, answerStr);
                            spt.saveSharedPreferences(Constant.IS_OPEN_GESTURE,true);
                            finish();
                        } else {
                            setTextMessage(getResources().getString(R.string.gesture_pwd_wrong));
                            ITools.vibrator(UpdateGestureUpdateActivity.this);
                        }
                    }

                    @Override
                    public void isComfirmGesture(int times) {
                        if (times == gestureLockViewGroup.COMFIRM) {
                            tipTV.setTextColor(Color.RED);
                            tipTV.setText(R.string.comfirm_gesture_pwd);
                        }
                    }

                    @Override
                    public void onGestureEvent(boolean matched) {
                        if (matched) {
                            gestureLockViewGroup.setSettingPwd(true);
                            tipTV.setText("请输入新的手势密码");
                        } else if (!matched) {
                            ITools.vibrator(UpdateGestureUpdateActivity.this);
                            tipTV.setTextColor(Color.RED);
                            tipTV.setText("密码错误，还可以尝试:" + gestureLockViewGroup.getmTryTimes());
                            app.timeForGesture = gestureLockViewGroup.getmTryTimes();
                        }
                    }

                    @Override
                    public void onBlockSelected(int cId) {
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_gesture;
    }

    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }


    @OnClick(R.id.back)
    public void onClick() {
        onBackPressed();
    }

    private void setTextMessage(String message) {
        tipTV.setText(message);
        tipTV.startAnimation(shakeAnimation(6));
        tipTV.setTextColor(Color.RED);
    }

    private String arrayToString(int[] a) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
        }
        return sb.toString();
    }
}
