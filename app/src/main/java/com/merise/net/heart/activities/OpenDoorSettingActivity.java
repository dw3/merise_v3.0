package com.merise.net.heart.activities;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.SnackbarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/20.
 */
public class OpenDoorSettingActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.openTypeRadioGroup)
    RadioGroup openTypeRadioGroup;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.root)
    RelativeLayout root;
    private int openDoorStatue = 0;// 开门方式ID（1：指纹 2：卡 3：指纹AND卡 4：指纹OR卡 5：全部禁用）
    private final int FINGER = 1;
    private final int CARD = 2;
    private final int FINGERANDCARD = 3;
    private final int FINGERORCARD = 4;
    private final int ALL = 5;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_opendoor_setting;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(getResources().getString(R.string.opendoor_way));
        openDoorStatue = Integer.valueOf(app.devices.get(app.currentIndex).getOpenDoorWayID());
        setOpendoorWay(openDoorStatue);
    }

    @Override
    public void initData() {
        openTypeRadioGroup.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.back, R.id.finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.finish:
                String deviceID = String.valueOf(app.devices.get(app.currentIndex).getId());
                AppOperate.opendoorSetting(deviceID, openDoorStatue, OpenDoorSettingActivity.this, new Report() {
                    @Override
                    public void onSucces(Object o) {
                        setOpendoorWay(openDoorStatue);
                        app.devices.get(app.currentIndex).setOpenDoorWayID(openDoorStatue);
                        String messsage = getResources().getString(R.string.modify_opendoorway_success);
                        SnackbarUtil.LongSnackbar(root,messsage,SnackbarUtil.Info).show();
                    }

                    @Override
                    public void onError(Object o) {
//                        String failMessage = getResources().getString(R.string.modify_opendoorway_fail);
                        SnackbarUtil.LongSnackbar(root, (String) o,SnackbarUtil.Alert).show();
                    }
                });
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.open_card:// 刷卡开门 开门方式ID（1：指纹 2：卡 3：指纹AND卡 4：指纹OR卡 5：全部禁用）
                openDoorStatue = CARD;
                break;
            case R.id.open_fingerprint:// 指纹开门 开门方式ID（1：指纹 2：卡 3：指纹AND卡 4：指纹OR卡
                // 5：全部禁用）
                openDoorStatue = FINGER;
                break;
            case R.id.open_card_fingerprint:// 组合开门 开门方式ID（1：指纹 2：卡 3：指纹AND卡 4：指纹OR卡
                // 5：全部禁用）
                openDoorStatue = FINGERANDCARD;
                break;
            case R.id.open_card_or_fingerprint:// 指纹或刷卡 开门方式ID（1：指纹 2：卡 3：指纹AND卡
                // 4：指纹OR卡 5：全部禁用）
                openDoorStatue = FINGERORCARD;
                break;
            case R.id.all_no:// 全部禁用 开门方式ID（1：指纹 2：卡 3：指纹AND卡 4：指纹OR卡 5：全部禁用）
                openDoorStatue = ALL;
                break;
            default:
                break;
        }
    }
    /**
     * 设置开门方式
     *
     * @param key
     */
    private void setOpendoorWay(int key) {
        switch (key) {
            case FINGER:
                RadioButton openFingerprint = (RadioButton) findViewById(R.id.open_fingerprint);
                openFingerprint.setChecked(true);
                break;
            case CARD:
                RadioButton openCard = (RadioButton) findViewById(R.id.open_card);
                openCard.setChecked(true);
                break;
            case FINGERANDCARD:
                RadioButton openCardFingerprint = (RadioButton) findViewById(R.id.open_card_fingerprint);
                openCardFingerprint.setChecked(true);
                break;
            case FINGERORCARD:
                RadioButton openCardOrFingerprint = (RadioButton) findViewById(R.id.open_card_or_fingerprint);
                openCardOrFingerprint.setChecked(true);
                break;
            case ALL:
                RadioButton allNo = (RadioButton) findViewById(R.id.all_no);
                allNo.setChecked(true);
                break;
            default:
                break;
        }
    }
}
