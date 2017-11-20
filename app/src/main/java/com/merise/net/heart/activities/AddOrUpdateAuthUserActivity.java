package com.merise.net.heart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.AuthPermission;
import com.merise.net.heart.bean.AuthUser;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.view.ClearEditView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/28.
 */
public class AddOrUpdateAuthUserActivity extends BaseActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener {
    public static final int ADDAUTHUSER = 0x00;
    public static final int MODIFYAYTHUSER = 0x01;
    public static final String DOSTH = "doSth";
    public static final int INIT = 0x02;
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.usernameEd)
    ClearEditView usernameEd;
    @BindView(R.id.remarkEd)
    ClearEditView remarkEd;
    @BindView(R.id.opendoor_chk)
    CheckBox opendoorChk;
    @BindView(R.id.history_chk)
    CheckBox historyChk;
    @BindView(R.id.doorbell_chk)
    CheckBox doorbellChk;
    @BindView(R.id.opendoor_tip_chk)
    CheckBox opendoorTipChk;
    @BindView(R.id.magnetometer_chk)
    CheckBox magnetometerChk;
    @BindView(R.id.dismantle_chk)
    CheckBox dismantleChk;
    @BindView(R.id.battery_chk)
    CheckBox batteryChk;
    @BindView(R.id.finish)
    Button finish;

    @BindView(R.id.dianliang)
    RelativeLayout dianliang;
    @BindView(R.id.menci)
    RelativeLayout menci;
    @BindView(R.id.chaixie)
    RelativeLayout chaixie;

    @BindView(R.id.root)
    ScrollView root;

    private int typeValue = -1;
    private String deviceID;
    private String userID;

    private List<AuthPermission> permissions;

    private AuthUser authUser;

    private boolean nameLegal;//名字是否合法
    private boolean nickLegal;//备注是否合法

    private List<Integer> oldPermissions;
    private List<Integer> newPermissions;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_user_management;
    }

    /**
     * 初始化权限视图
     */
    private void initPermissionView() {
        if (permissions != null) {
            for (int i = 0; i < permissions.size(); i++) {
                int permissionID = permissions.get(i).getPermissionID();
                oldPermissions.add(permissionID);
                if (permissionID == 1) {
                    opendoorChk.setChecked(true);
                } else if (permissionID == 2) {
                    doorbellChk.setChecked(true);
                } else if (permissionID == 3) {
                    historyChk.setChecked(true);
                } else if (permissionID == 4) {
                    opendoorTipChk.setChecked(true);
                } else if (permissionID == 6) {
                    batteryChk.setChecked(true);
                } else if (permissionID == 7) {
                    magnetometerChk.setChecked(true);
                } else if (permissionID == 8) {
                    dismantleChk.setChecked(true);
                }
            }
        }
    }

    // 权限值
    private String getPermission() {
        newPermissions.clear();
        String permissions = "";
        if (opendoorChk.isChecked()) {
            permissions += "1,";
            newPermissions.add(1);
        }
        if (historyChk.isChecked()) {
            permissions += "3,";
            newPermissions.add(3);
        }
        if (doorbellChk.isChecked()) {
            permissions += "2,";
            newPermissions.add(2);
        }
        if (opendoorTipChk.isChecked()) {
            permissions += "4,";
            newPermissions.add(4);
        }
        if (magnetometerChk.isChecked()) {
            permissions += "7,";
            newPermissions.add(7);
        }
        if (batteryChk.isChecked()) {
            permissions += "6,";
            newPermissions.add(6);
        }
        if (dismantleChk.isChecked()) {
            permissions += "8,";
            newPermissions.add(8);
        }
        if (permissions.length() > 0) {
            permissions = permissions.substring(0, permissions.length() - 1);
        }
        return permissions;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INIT:
                    initPermissionView();
                    break;
            }
        }
    };

    @Override
    public void initView() {
        ButterKnife.bind(this);
        finish.setEnabled(false);
        remarkEd.setMaxLength(15);
        int typeID = app.devices.get(app.currentIndex)
                .getTypeID();
        if (typeID == 1) {
            dianliang.setVisibility(View.GONE);
            menci.setVisibility(View.GONE);
            chaixie.setVisibility(View.GONE);
        }
        deviceID = String.valueOf(app.devices.get(app.currentIndex).getId());
        Bundle bundle = getIntent().getExtras();
        typeValue = bundle.getInt(DOSTH);
        if (typeValue == ADDAUTHUSER) {
            titleText.setText(R.string.add_auth_user);
        } else if (typeValue == MODIFYAYTHUSER) {
            authUser = (AuthUser) bundle.getSerializable(Constant.AUTHUSER);
            titleText.setText(R.string.modify_auth_user);
            userID = String.valueOf(authUser.getId());
        }
        usernameEd.addTextChangedListener(this);
        remarkEd.addTextChangedListener(this);
        opendoorChk.setOnCheckedChangeListener(this);
        historyChk.setOnCheckedChangeListener(this);
        doorbellChk.setOnCheckedChangeListener(this);
        opendoorTipChk.setOnCheckedChangeListener(this);
        magnetometerChk.setOnCheckedChangeListener(this);
        dismantleChk.setOnCheckedChangeListener(this);
        batteryChk.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData() {
        oldPermissions = new ArrayList<>();
        newPermissions = new ArrayList<>();
        if (typeValue == MODIFYAYTHUSER) {
            usernameEd.setFocusable(false);
            usernameEd.setText(authUser.getName());
            remarkEd.setText(authUser.getNick());
            getAuthUserPermissions(deviceID, userID);
        }
    }


    @OnClick({R.id.back, R.id.finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.finish:
                String userName = usernameEd.getText().toString();
                String userNick = remarkEd.getText().toString();
                String permissionIDs = getPermission();
                modifyOrAddAuthUser(deviceID, userName, userNick, permissionIDs);
                break;
        }
    }

    /**
     * 获取用户权限
     *
     * @param deviceID
     * @param userID
     */
    private void getAuthUserPermissions(String deviceID, String userID) {
        AppOperate.getAuthPermissions(deviceID, userID, this, new Report() {
            @Override
            public void onSucces(Object o) {
                permissions = (List<AuthPermission>) o;
                handler.sendEmptyMessage(INIT);
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        checkLegal();
    }

    private void checkLegal() {
        TLog.log(TAG, "checkLegal");
        getPermission();
        if (usernameEd.getText().toString().trim().length() > 0 && usernameEd.getText().toString().trim().length() <= 16) {
            nameLegal = true;
        } else {
            nameLegal = false;
        }
        if (remarkEd.getText().toString().trim().length() > 0 && remarkEd.getText().toString().trim().length() <= 16) {
            nickLegal = true;
        } else {
            nickLegal = false;
        }
        TLog.log(TAG, "" + (oldPermissions.containsAll(newPermissions) && newPermissions.containsAll(oldPermissions)));
        if (nameLegal && nickLegal && getPermission().length() > 0) {
            finish.setEnabled(true);
            if (typeValue == MODIFYAYTHUSER) {
                if (remarkEd.getText().toString().trim().equals(authUser.getNick()) && (oldPermissions.containsAll(newPermissions) && newPermissions.containsAll(oldPermissions))) {
                    finish.setEnabled(false);
                }
            }
        } else {
            finish.setEnabled(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkLegal();
    }

    /**
     * 修改或添加授权用户
     *
     * @param deviceID
     * @param userName
     * @param userNick
     * @param permissionIDs
     */
    private void modifyOrAddAuthUser(String deviceID, String userName, final String userNick, String permissionIDs) {
        AppOperate.modifyOrAddAuthUser(deviceID, userName, userNick, permissionIDs, this, new Report() {
            @Override
            public void onSucces(Object o) {
                if (typeValue == MODIFYAYTHUSER) {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.NEWKEYNICK, userNick);
                    setResult(RESULT_OK, intent);
                } else {
                    setResult(RESULT_OK);
                }
                finish();
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }
}
