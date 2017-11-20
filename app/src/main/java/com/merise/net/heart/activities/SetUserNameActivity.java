package com.merise.net.heart.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.view.ClearEditView;

import org.loader.autohideime.HideIMEUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.merise.net.heart.application.XYApplication.userInfo;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class SetUserNameActivity extends BaseActivity {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.ed_new_username)
    ClearEditView edNewUsername;
    @BindView(R.id.change_username_finish)
    Button changeUsernameFinish;
    @BindView(R.id.set_username_root)
    LinearLayout setUserNameRoot;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_username;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.perfect_set_username);
        HideIMEUtil.wrap(this);
        edNewUsername.setText(userInfo.getName());
    }

    @Override
    public void initData() {
        changeUsernameFinish.setEnabled(false);
        edNewUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edNewUsername.getText() != null && edNewUsername.getText().length() == 0) {
                    changeUsernameFinish.setEnabled(true);
                }
            }
        });
    }


    @OnClick({R.id.ed_new_username, R.id.change_username_finish, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ed_new_username:
                break;
            case R.id.change_username_finish:
                String newUserName = edNewUsername.getText().toString().trim();
                if (newUserName.equalsIgnoreCase(userInfo.getMobile())) {
                    SnackbarUtil.LongSnackbar(setUserNameRoot,
                            "不能和手机号一样哦，亲", SnackbarUtil.Alert).show();
                } else {
                    updateUserName(newUserName);
                }
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    /**
     * 更改用户名
     */
    private void updateUserName(final String userName) {
        AppOperate.updateUserName(userName, this, new Report() {
            @Override
            public void onSucces(Object o) {
                TLog.log(TAG, "修改用户名成功");
                SnackbarUtil.LongSnackbar(setUserNameRoot,
                        "修改成功", SnackbarUtil.Alert).show();
                spt.saveSharedPreferences(Const.CHANGE_USERNAME, userName);
            }

            @Override
            public void onError(Object o) {
                TLog.log(TAG, o.toString() + "");
            }
        });
    }
}
