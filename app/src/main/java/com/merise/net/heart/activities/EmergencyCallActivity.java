package com.merise.net.heart.activities;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.GouUtils;
import com.merise.net.heart.utils.SharedPreferencesTools;
import com.merise.net.heart.view.ClearEditView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/9/12 0012.
 */
public class EmergencyCallActivity extends BaseActivity {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.emergencyCallNum)
    ClearEditView emergencyCallNum;
    @BindView(R.id.emergencyCall_remark_et)
    ClearEditView emergencyCallRemarkEt;
    @BindView(R.id.finish)
    Button finish;
    private String callNum;
    private String remarkEdit;
    private SharedPreferencesTools spt;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_add_or_update_emergency_call;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.emergency_title);
    }

    @Override
    public void initData() {
        spt = new SharedPreferencesTools(this);
        String s = spt.readSharedPreferencesString(Const.EMERGENCYREMARK);
        String c = spt.readSharedPreferencesString(Const.EMERGENCYREMARK_NUM);
        if (!(s.length() == 0
                || (s == null))) {
            emergencyCallRemarkEt.setText(s);
        }
        if (!(c.length() == 0
                || (c == null))) {
            emergencyCallNum.setText(c);
        }
    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.back, R.id.finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.finish:
                callNum = emergencyCallNum.getText().toString().trim();
                remarkEdit = emergencyCallRemarkEt.getText().toString().trim();
                String deviceId = String.valueOf(app.devices.get(app.currentIndex).getId());
                if (GouUtils.isNullOrEmpty(callNum)) {
                    GouUtils.showTip(this, "请输入电话");
                    return;
                }
                if ((!GouUtils.isTelePhone(callNum)) && (!GouUtils.isMobile(callNum))) {
                    GouUtils.showTip(this, "请输入正确的电话!\n如：18999999999或024-12345678");
                    return;
                }
                setEmergencyCall(deviceId, callNum);
                break;
        }
    }


    /**
     * 紧急号码的设置
     *
     * @param deviceId
     * @param callNum
     */
    private void setEmergencyCall(String deviceId, final String callNum) {
        AppOperate.setEmergency(deviceId, callNum, (RxAppCompatActivity) this, new Report() {
            @Override
            public void onSucces(Object o) {
                app.devices.get(app.currentIndex).setEmergencyNumber(callNum);
                TLog.log(TAG, "callNum----" + callNum);
                spt.saveSharedPreferences(Const.EMERGENCYREMARK_NUM, callNum);
                spt.saveSharedPreferences(Const.EMERGENCYREMARK, remarkEdit);
                finish();
            }

            @Override
            public void onError(Object o) {
                TLog.log(TAG, "onError---" + o.toString());
            }
        });
    }
}
