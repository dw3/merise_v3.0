package com.merise.net.heart.activities;


import android.content.Intent;
import android.os.Bundle;

import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseSwipmenuRecycleViewActivity;
import com.merise.net.heart.base.BaseViewActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.SnackbarUtil;


/**
 * Created by Administrator on 2016/9/27.
 */
public class AddOrUpdateFingerprintActivity extends BaseViewActivity {
    public static final int ADDFINGERPRINT = 0x00;
    public static final int MODIFYFINGERPRINT = 0x01;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_view;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        typeValue = bundle.getInt(BaseSwipmenuRecycleViewActivity.DOSTH);
        if (typeValue == ADDFINGERPRINT) {
            titleText.setText(getResources().getString(R.string.add_fingerprint));
            finish.setText(getResources().getString(R.string.start_input_fingerprint));
        } else if (typeValue == MODIFYFINGERPRINT) {
            oldKeyNick = bundle.getString(Constant.OLDKEYNICK);
            keyID = bundle.getInt(Constant.KEYID);
            titleText.setText(getResources().getString(R.string.modify_fingerprint_remark));
            remarkEd.setText(oldKeyNick);
            finish.setText(getResources().getString(R.string.finish));
        }
    }

    @Override
    public void finishBtnOnclickListner() {
        if (typeValue == ADDFINGERPRINT) {
            newKeyNick = remarkEd.getText().toString();
            AppOperate.addFingerprint(deviceID, newKeyNick, this, new Report() {
                @Override
                public void onSucces(Object o) {
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onError(Object o) {
                    SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
                }
            });
        } else if (typeValue == MODIFYFINGERPRINT) {
            newKeyNick = remarkEd.getText().toString();
            AppOperate.modifFingerprintNick(deviceID, keyID, newKeyNick, this, new Report() {
                @Override
                public void onSucces(Object o) {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.NEWKEYNICK, newKeyNick);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onError(Object o) {
                    SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
                }
            });
        }
    }
}
