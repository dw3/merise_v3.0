package com.merise.net.heart.fragments.unlockFrags;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.framewok.util.TLog;
import com.bumptech.glide.Glide;
import com.merise.net.heart.MainActivity;
import com.merise.net.heart.R;
import com.merise.net.heart.base.MyBaseFragment;
import com.merise.net.heart.bean.UserInfo;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.ITools;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.utils.Utils;
import com.merise.net.heart.view.gesture.GestureLockViewGroup;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 作者:xiangyang
 * 日期:2016/11/11
 */
public class Lock9Fragment extends MyBaseFragment {
    @BindView(R.id.topTV)
    TextView topTV;
    @BindView(R.id.headImg)
    ImageView headImg;
    @BindView(R.id.tipTV)
    TextView tipTV;
    @BindView(R.id.gestureLockViewGroup)
    GestureLockViewGroup gestureLockViewGroup;
    @BindView(R.id.changeUnlockWay)
    Button changeUnlockWay;
    @BindView(R.id.root)
    RelativeLayout root;

//    private static final int minLength = 5;//手势密码默认长度
//
//    private static final int tryTimes = 5;//尝试输入手势密码的次数

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
        TLog.log(TAG, "Lock9Fragment....initView");
        String headPath = spt.readSharedPreferencesString(Constant.NETHEADPATH);
        Glide.with(getActivity()).load(headPath).placeholder(R.drawable.ic720).error(R.drawable.ic720)
                .bitmapTransform(new CropCircleTransformation(getActivity())).into(headImg);
    }

    @Override
    public void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            topTV.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ITools.getStatusHeight(getActivity())));
        }
        char[] answerFromSpt = spt.readSharedPreferencesString(Constant.GESTUREANSWER).toCharArray();
        int[] answer = new int[answerFromSpt.length];
        for (int i = 0; i < answerFromSpt.length; i++) {
            answer[i] = answerFromSpt[i] - 48;
            TLog.log(TAG, "手势密码为:" + answer[i]);
        }
        gestureLockViewGroup.setMinLength(app.gestureMinLength);
        gestureLockViewGroup.setUnMatchExceedBoundary(app.timeForGesture);
        gestureLockViewGroup.setAnswer(answer);
        gestureLockViewGroup
                .setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {

                    @Override
                    public void onUnmatchedExceedBoundary() {
//                        Toast.makeText(getActivity(), "错误5次...",
//                                Toast.LENGTH_SHORT).show();
                        tipTV.setText("不能尝试了，该干嘛干嘛去！");
                        ITools.vibrator(getActivity());
                        spt.saveSharedPreferences(Constant.IS_OPEN_GESTURE, false); //手势密码错误3次，关闭手势密码功能
//                        gestureLockViewGroup.setUnMatchExceedBoundary(5);
                        getActivity().finish();
                    }

                    @Override
                    public void onGestureIllegal() {
                        TLog.log(TAG, gestureLockViewGroup.getMinLength() + "最短长度");
                        setTextMessage("密码长度至少" + app.gestureMinLength + "个点");
                        ITools.vibrator(getActivity());
                    }

                    @Override
                    public void onSettingGesture(boolean result) {

                    }

                    @Override
                    public void isComfirmGesture(int times) {

                    }

                    @Override
                    public void onGestureEvent(boolean matched) {
                        if (matched) {
                            TLog.log(TAG, "手势密码匹配成功");
                            tipTV.setText("手势验证成功");
                            gestureLockViewGroup.reset();
                            login();
                        }
                        if (!matched) {
                            ITools.vibrator(getActivity());
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
        return R.layout.activity_lock9;
    }

    @OnClick(R.id.changeUnlockWay)
    public void onClick() {
        gotoActivity(MainActivity.class);
    }

    /**
     * 登录方法
     */
    private void login() {
        AppOperate.getLogin("1327", "123456", "", "", 1, Utils.getVersionName(getContext()), (RxAppCompatActivity) getActivity(), new Report() {
            @Override
            public void onSucces(Object o) {
                app.userInfo = (UserInfo) o;
                loginSuccess();
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }

    private void setTextMessage(String message) {
        tipTV.setText(message);
        tipTV.startAnimation(shakeAnimation(6));
        tipTV.setTextColor(Color.RED);
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
}
