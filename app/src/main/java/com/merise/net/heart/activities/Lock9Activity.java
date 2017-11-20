package com.merise.net.heart.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.ITools;
import com.merise.net.heart.view.gesture.GestureLockViewGroup;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/11.
 */
public class Lock9Activity extends BaseActivity {

    @BindView(R.id.gestureLockViewGroup)
    GestureLockViewGroup gestureLockViewGroup;
    @BindView(R.id.changeUnlockWay)
    Button changeUnlockWay;
    @BindView(R.id.topTV)
    TextView topTV;
    @BindView(R.id.tipTV)
    TextView tipTV;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_lock9;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        TLog.log(TAG, "状态栏：" + ITools.getStatusHeight(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            topTV.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ITools.getStatusHeight(this)));
        }
        char[] answerFromSpt = spt.readSharedPreferencesString(Constant.GESTUREANSWER).toCharArray();
        int[] answer = new int[answerFromSpt.length];
        for (int i = 0; i < answerFromSpt.length; i++) {
            answer[i] = answerFromSpt[i] - 48;
            TLog.log(TAG, "手势密码为:" + answer[i]);
        }
        gestureLockViewGroup.setMinLength(5);
        gestureLockViewGroup.setUnMatchExceedBoundary(5);
        gestureLockViewGroup.setAnswer(answer);
        gestureLockViewGroup
                .setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {

                    @Override
                    public void onUnmatchedExceedBoundary() {
                        Toast.makeText(Lock9Activity.this, "错误5次...",
                                Toast.LENGTH_SHORT).show();
//                        gestureLockViewGroup.setUnMatchExceedBoundary(5);
                    }

                    @Override
                    public void onGestureIllegal() {
                        TLog.log(TAG, gestureLockViewGroup.getMinLength() + "最短长度");
                    }

                    @Override
                    public void onSettingGesture(boolean result) {

                    }

                    @Override
                    public void isComfirmGesture(int times) {

                    }

                    @Override
                    public void onGestureEvent(boolean matched) {
//                        Toast.makeText(Lock9Activity.this, matched + "",
//                                Toast.LENGTH_SHORT).show();
                        if (matched) {
                            TLog.log(TAG, "手势密码匹配成功");
                            gestureLockViewGroup.setUnMatchExceedBoundary(5);
                            gestureLockViewGroup.reset();
                        }
                        if (!matched) {
                            ITools.vibrator(Lock9Activity.this);
                            tipTV.setText("密码错误，还可以尝试:" + gestureLockViewGroup.getmTryTimes());
                        }
                    }

                    @Override
                    public void onBlockSelected(int cId) {
//						Toast.makeText(MainActivity.this, cId+"", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void initData() {
    }


    @OnClick(R.id.changeUnlockWay)
    public void onClick() {
        Toast.makeText(Lock9Activity.this, "切换方式" + ITools.getStatusHeight(this),
                Toast.LENGTH_SHORT).show();
    }

}
