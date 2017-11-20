package com.merise.net.heart.activities;

import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseViewActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.SnackbarUtil;

/**
 * Created by Administrator on 2016/9/29.
 */
public class ModifyDoornickActivity extends BaseViewActivity {
    @Override
    public void initView() {
        super.initView();
        titleText.setText(R.string.modify_doornick);
        finish.setText(R.string.finish);
        oldKeyNick = app.devices.get(app.currentIndex).getName();
        remarkEd.setText(oldKeyNick);
    }

    @Override
    public void finishBtnOnclickListner() {
        modifyDoorNick(deviceID,remarkEd.getText().toString());
    }

    /**
     * 修改门禁备注
     */
    private void  modifyDoorNick(String deviceID, final String name){
        AppOperate.modifyDoorNick(deviceID, name, this, new Report() {
            @Override
            public void onSucces(Object o) {
                app.devices.get(app.currentIndex).setName(name);
                SnackbarUtil.LongSnackbar(root,getResources().getString(R.string.modify_success),SnackbarUtil.Confirm).show();
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o,SnackbarUtil.Alert).show();
            }
        });
    }
}
