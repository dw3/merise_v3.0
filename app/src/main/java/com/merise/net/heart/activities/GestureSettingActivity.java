package com.merise.net.heart.activities;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.framewok.util.TLog;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.ITools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/17 0017.
 */
public class GestureSettingActivity extends BaseActivity {


    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.cb_is_gesture)
    CheckBox cbIsGesture;
    @BindView(R.id.is_getstrue_LL)
    LinearLayout isGetstrueLL;
    @BindView(R.id.modifyGesturePwd)
    RelativeLayout modifyGesturePwd;

    private boolean isOpenGesture;

    @Override
    protected int getLayoutId() {
        return R.layout.is_gesture;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.gesture_code);
    }

    @Override
    public void initData() {
//        isOpenGesture = spt.readSharedPreferencesBoolean(Constant.IS_OPEN_GESTURE);
//        cbIsGesture.setChecked(isOpenGesture);
//        setModifyGestureRvVisible();
    }

    @Override
    public void initListener() {

    }


    @OnClick({R.id.back, R.id.cb_is_gesture, R.id.modifyGesturePwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.cb_is_gesture:
                String gestureAnswer = spt.readSharedPreferencesString(Constant.GESTUREANSWER);
                if (gestureAnswer == null || gestureAnswer.length() == 0) { //设置手势密码
                    cbIsGesture.setChecked(false);
                    AlertView alertView = new AlertView("提示", "没有手势密码或密码已过期", "知道了", new String[]{"前往设置"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            TLog.log(TAG, "position:" + position);
                            if (position == 0) {
                                gotoActivity(UpdateGestureUpdateActivity.class);
                            }
                        }
                    });
                    alertView.show();
                } else if(app.timeForGesture<=0){
                    cbIsGesture.setChecked(false);
                    Toast.makeText(this,"手势错误次数已达上限",Toast.LENGTH_LONG).show();
                }else {
                    setModifyGestureRvVisible();
                    spt.saveSharedPreferences(Constant.IS_OPEN_GESTURE, cbIsGesture.isChecked()); //打开或关闭手势密码
                }
                break;
            case R.id.modifyGesturePwd:
                gotoActivity(UpdateGestureUpdateActivity.class);
                break;

        }
    }

    /**
     * 设置修改手势选项的可视与否
     */
    private void setModifyGestureRvVisible() {
        if (cbIsGesture.isChecked()) {
            modifyGesturePwd.setVisibility(View.VISIBLE);
        } else {
            modifyGesturePwd.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOpenGesture = spt.readSharedPreferencesBoolean(Constant.IS_OPEN_GESTURE);
        cbIsGesture.setChecked(isOpenGesture);
        setModifyGestureRvVisible();
    }
}
