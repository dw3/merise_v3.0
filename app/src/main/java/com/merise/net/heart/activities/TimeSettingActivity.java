package com.merise.net.heart.activities;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.view.PickerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/29.
 */
public class TimeSettingActivity extends BaseActivity {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.time_setting_pv)
    PickerView timeSettingPv;
    @BindView(R.id.finish)
    Button finish;

    @BindView(R.id.root)
    RelativeLayout root;
    private int autoCloseTime;

    private String deviceID;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_time_setting;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.time_setting);
        deviceID = String.valueOf(app.devices.get(app.currentIndex).getId());
        List<String> seconds = new ArrayList<String>();
        for (int i = 8; i <= 30; i++) {
            seconds.add(i < 10 ? "0" + i : "" + i);
        }
        autoCloseTime = Integer.valueOf(app.devices.get(
                app.currentIndex).getAutoCloseTime());
        timeSettingPv.setData(seconds);
        timeSettingPv
                .setSelected((autoCloseTime - 8) > 0 ? autoCloseTime - 8
                        : 0);
        timeSettingPv.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                autoCloseTime = Integer.valueOf(text);
            }
        });
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.back, R.id.finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.finish:
                autoCloseTimeSetting(deviceID,autoCloseTime);
                break;
        }
    }

    /**
     * 自动回锁时间的设置
     * @param deviceID
     * @param autoCloseTime
     */
    private void autoCloseTimeSetting(String deviceID, final int autoCloseTime) {
        AppOperate.autoCloseTimeSetting(deviceID, autoCloseTime, this, new Report() {
            @Override
            public void onSucces(Object o) {
                SnackbarUtil.LongSnackbar(root, getResources().getString(R.string.modify_success), SnackbarUtil.Confirm).show();
                app.devices.get(app.currentIndex).setAutoCloseTime(autoCloseTime);
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Confirm).show();
            }
        });
    }
}
