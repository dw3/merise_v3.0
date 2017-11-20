package com.merise.net.heart.activities;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.NotifyState;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/10 0010.
 */
public class PromptManage extends BaseActivity {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.open_door_notify)
    CheckBox openDoorNotify;
    //开门
    @BindView(R.id.open_tip_view)
    LinearLayout openTipView;
    @BindView(R.id.doorbell_notify)
    CheckBox doorbellNotify;
    //门铃
    @BindView(R.id.doorbering_tip_view)
    LinearLayout doorberingTipView;
    @BindView(R.id.battery_warn_notify)
    CheckBox batteryWarnNotify;
    //电量
    @BindView(R.id.battery_tip_view)
    LinearLayout batteryTipView;
    @BindView(R.id.magnetometer_warn_notify)
    CheckBox magnetometerWarnNotify;
    //门磁
    @BindView(R.id.magnetometer_tip_view)
    LinearLayout magnetometerTipView;
    @BindView(R.id.dismantle_warn_notify)
    CheckBox dismantleWarnNotify;
    //拆卸
    @BindView(R.id.dismantle_tip_view)
    LinearLayout dismantleTipView;
    @BindView(R.id.back)
    Button back;
//    @BindView(R.id.prompt_root)
//    CoordinatorLayout promptRoot;

    private String devideid;
    private String sIsOpen;
    private String permissionId;

    boolean openDoorHint = false;// 开门提醒(true:开启 false:关闭)
    boolean doorBellHint = false;// 门铃提醒(true:开启 false:关闭)
    boolean doorSensorHint = false;// 门磁提醒(true:开启 false:关闭)
    boolean powerHint = false;// 电量提醒(true:开启 false:关闭)
    boolean dismantleHint = false;// 拆卸提醒(true:开启 false:关闭)
    private String permissionIdStr = null;
    private String[] permissionIds = null;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_prompt_management;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.prompt_manage);
        TLog.log(TAG,"devices===="+app.currentIndex);
        TLog.log(TAG, "permission------" + app.devices.get(app.currentIndex)
                .getPermissionID());
        if (!(app.devices.get(app.currentIndex)
                .getPermissionID()== null)) {
            permissionIdStr = app.devices.get(app.currentIndex)
                    .getPermissionID();
            permissionIds = permissionIdStr.split(",");
        }
        double version = app.devices.get(app.currentIndex).getVersion();

        if (permissionIds != null && permissionIds.length > 0) {
            for (int i = 0; i < permissionIds.length; i++) {
                String id = permissionIds[i];
                // 判断当前用户是否是当前设备的管理员
                String adminId = app.devices.get(app.currentIndex)
                        .getAdminID();
                TLog.log(TAG, "adminId--" + adminId + "--------");
                if (Integer.valueOf(adminId) == app.userInfo.getId()) {
                    openTipView.setVisibility(View.VISIBLE);
                    magnetometerTipView.setVisibility(View.VISIBLE);
                    dismantleTipView.setVisibility(View.VISIBLE);
                    doorberingTipView.setVisibility(View.VISIBLE);
                    batteryTipView.setVisibility(View.VISIBLE);
                    if (version < 2.0) {
                        magnetometerTipView.setVisibility(View.GONE);
                        dismantleTipView.setVisibility(View.GONE);
                        batteryTipView.setVisibility(View.GONE);
                    }
                } else if (id.equals("4")) {
                    openTipView.setVisibility(View.VISIBLE);
                } else if (id.equals("6") && version >= 2) {
                    batteryTipView.setVisibility(View.VISIBLE);
                } else if (id.equals("7") && version >= 2) {
                    magnetometerTipView.setVisibility(View.VISIBLE);
                } else if (id.equals("8") && version >= 2) {
                    dismantleTipView.setVisibility(View.VISIBLE);
                } else if (id.equals("2")) {
                    doorberingTipView.setVisibility(View.VISIBLE);
                }
            }
        }

        if (app.devices.get(app.currentIndex).getTypeID()==3) {
            openTipView.setVisibility(View.VISIBLE);
            batteryTipView.setVisibility(View.GONE);
            dismantleTipView.setVisibility(View.GONE);
            magnetometerTipView.setVisibility(View.GONE);
            doorberingTipView.setVisibility(View.VISIBLE);
        } else if ((app.devices.get(app.currentIndex).getTypeID()
               ==2)
                || app.devices.get(app.currentIndex).getTypeID()
                ==1) {
            batteryTipView.setVisibility(View.VISIBLE);
            dismantleTipView.setVisibility(View.VISIBLE);
            magnetometerTipView.setVisibility(View.VISIBLE);
            doorberingTipView.setVisibility(View.VISIBLE);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (openDoorHint) {
                    openDoorNotify.setBackgroundResource(R.drawable.on_btn);
                } else {
                    openDoorNotify.setBackgroundResource(R.drawable.off_btn);
                }
                if (doorBellHint) {
                    doorbellNotify.setBackgroundResource(R.drawable.on_btn);
                } else {
                    doorbellNotify.setBackgroundResource(R.drawable.off_btn);
                }
                if (powerHint) {
                    batteryWarnNotify.setBackgroundResource(R.drawable.on_btn);
                } else {
                    batteryWarnNotify.setBackgroundResource(R.drawable.off_btn);
                }
                if (doorSensorHint) {
                    magnetometerWarnNotify.setBackgroundResource(R.drawable.on_btn);
                } else {
                    magnetometerWarnNotify.setBackgroundResource(R.drawable.off_btn);
                }
                if (dismantleHint) {
                    dismantleWarnNotify.setBackgroundResource(R.drawable.on_btn);
                } else {
                    dismantleWarnNotify.setBackgroundResource(R.drawable.off_btn);
                }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void initData() {
        if (app.devices != null) {
            if (app.devices.size() > app.currentIndex) {
                devideid = String.valueOf(app.devices.get(app.currentIndex).getId());
            }
            if (devideid != null) {
                getNotifyState(devideid);
            }
        } else {
//            SnackbarUtil.LongSnackbar(promptRoot, "没有找到设备", SnackbarUtil.green).show();
        }

    }


    private void getNotifyState(String devideid) {
        AppOperate.getNotifyState(devideid, (RxAppCompatActivity) this, new Report() {
            @Override
            public void onSucces(Object o) {
                NotifyState state = (NotifyState) o;
                for (int i = 0; i < state.getList().size(); i++) {
                    TLog.log(TAG, "state------" + state.getList().get(i).getPermissionID());
                    int permissionID = state.getList().get(i).getPermissionID();
                    if (permissionID == 2) {
                        doorBellHint = state.getList().get(i).isActived();
                        TLog.log(TAG,"doorBellHint"+doorBellHint);
                    }
                    if (permissionID == 4) {
                        openDoorHint = state.getList().get(i).isActived();
                        TLog.log(TAG,"openDoorHint"+openDoorHint);
                    }
                    if (permissionID == 6) {
                        powerHint = state.getList().get(i).isActived();
                    }
                    if (permissionID == 7) {
                        doorSensorHint = state.getList().get(i).isActived();
                    }
                    if (permissionID == 8) {
                        dismantleHint = state.getList().get(i).isActived();
                    }
                }
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onError(Object o) {

            }
        });
    }

    @Override
    public void initListener() {

    }


    @OnClick({R.id.back, R.id.open_door_notify, R.id.doorbell_notify, R.id.battery_warn_notify, R.id.magnetometer_warn_notify, R.id.dismantle_warn_notify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.open_door_notify:
                // 开门
                if (openDoorHint) {
                    notifySetting(devideid, true, openDoorNotify);
                } else {
                    notifySetting(devideid, false, openDoorNotify);
                }
                break;

            case R.id.doorbell_notify:
                // 门铃
                if (doorBellHint) {
                    notifySetting(devideid, true, doorbellNotify);
                } else {
                    notifySetting(devideid, false, doorbellNotify);
                }
                break;
            case R.id.battery_warn_notify:
                // 电量
                if (powerHint) {
                    notifySetting(devideid, true, batteryWarnNotify);
                } else {
                    notifySetting(devideid, false, batteryWarnNotify);
                }
                break;
            case R.id.magnetometer_warn_notify:
                // 门磁
                if (doorSensorHint) {
                    notifySetting(devideid, true, magnetometerWarnNotify);
                } else {
                    notifySetting(devideid, false, magnetometerWarnNotify);
                }
                break;
            case R.id.dismantle_warn_notify:
                // 拆卸
                if (dismantleHint) {
                    notifySetting(devideid, true, dismantleWarnNotify);
                } else {
                    notifySetting(devideid, false, dismantleWarnNotify);
                }
                break;
        }
    }

    void notifySetting(String devideid, final boolean isOpen, final Button button) {
        switch (button.getId()) {
            case R.id.open_door_notify:
                sIsOpen = String.valueOf(isOpen ? 0 : 1);// 开门
                permissionId = String.valueOf(4);
                break;
            case R.id.doorbell_notify:
                sIsOpen = String.valueOf(isOpen ? 0 : 1);// 门铃
                permissionId = String.valueOf(2);
                break;
            case R.id.battery_warn_notify:
                sIsOpen = String.valueOf(isOpen ? 0 : 1);// 电量
                permissionId = String.valueOf(6);
                break;
            case R.id.magnetometer_warn_notify:
                sIsOpen = String.valueOf(isOpen ? 0 : 1);// 门磁
                permissionId = String.valueOf(7);
                break;
            case R.id.dismantle_warn_notify:
                sIsOpen = String.valueOf(isOpen ? 0 : 1);// 拆卸
                permissionId = String.valueOf(8);
                break;
        }

        AppOperate.notifySetting(devideid, sIsOpen, permissionId, (RxAppCompatActivity) this, new Report() {
            @Override
            public void onSucces(Object o) {
                setCheckFlag(button, isOpen);
                initData();
            }

            @Override
            public void onError(Object o) {

            }
        });
    }

    // 设置状态
    void setCheckFlag(Button button, boolean isOpen) {
        switch (button.getId()) {
            case R.id.open_door_notify:
                // 开门
                openDoorHint = isOpen;
                break;
            case R.id.doorbell_notify:
                // 门铃
                doorBellHint = isOpen;
                break;
            case R.id.battery_warn_notify:
                // 电量
                powerHint = isOpen;
                break;
            case R.id.magnetometer_warn_notify:
                // 门磁
                doorSensorHint = isOpen;
                break;
            case R.id.dismantle_warn_notify:
                // 拆卸
                dismantleHint = isOpen;
                break;
        }
        if (isOpen) {
            button.setBackgroundResource(R.drawable.on_btn);
        } else {
            button.setBackgroundResource(R.drawable.off_btn);
        }
    }
}
