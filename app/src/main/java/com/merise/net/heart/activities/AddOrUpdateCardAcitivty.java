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
 * Created by Administrator on 2016/9/26.
 */
public class AddOrUpdateCardAcitivty extends BaseViewActivity {
    public static final int ADDCARD = 0x00;
    public static final int MODIFYCARD = 0x01;


    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void finishBtnOnclickListner() {
        if (typeValue == ADDCARD) {
            newKeyNick = remarkEd.getText().toString();
            AppOperate.addCard(deviceID, newKeyNick, AddOrUpdateCardAcitivty.this, new Report() {
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
        } else if (typeValue == MODIFYCARD) {
            newKeyNick = remarkEd.getText().toString();
            AppOperate.modifyCardNick(deviceID, keyID, newKeyNick, AddOrUpdateCardAcitivty.this, new Report() {
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

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        typeValue = bundle.getInt(BaseSwipmenuRecycleViewActivity.DOSTH);
        if (typeValue == ADDCARD) {
            titleText.setText(getResources().getString(R.string.add_card));
            finish.setText(getResources().getString(R.string.start_input_card));
        } else if (typeValue == MODIFYCARD) {
            oldKeyNick = bundle.getString(Constant.OLDKEYNICK);
            keyID = bundle.getInt(Constant.KEYID);
            titleText.setText(getResources().getString(R.string.modify_card_remark));
            remarkEd.setText(oldKeyNick);
            finish.setText(getResources().getString(R.string.finish));
        }
    }
}
