package com.merise.net.heart.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.android.framewok.util.Util;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.utils.Utils;
import com.merise.net.heart.view.ClearEditView;

import org.loader.autohideime.HideIMEUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class SetEmailActivity extends BaseActivity {


    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.ed_new_email)
    ClearEditView edNewEmail;
    @BindView(R.id.change_email_finish)
    Button changeEmailFinish;
    @BindView(R.id.email_root)
    LinearLayout emailRoot;
    private String email;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_email;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.perfect_set_email);
        HideIMEUtil.wrap(this);
    }

    @Override
    public void initData() {
        changeEmailFinish.setEnabled(false);
        edNewEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                email = edNewEmail.getText().toString().trim();
                if (Utils.isEmail(email)) {
                    changeEmailFinish.setEnabled(true);
                }
            }
        });

    }


    @OnClick({R.id.back, R.id.change_email_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.change_email_finish:
                updateUserEmail(email);
                break;
        }
    }

    /**
     * 设置邮箱
     */
    private void updateUserEmail(String email) {
        AppOperate.updateUserEmail(email, this, new Report() {
            @Override
            public void onSucces(Object o) {
                TLog.log(TAG, "修改邮箱");
                SnackbarUtil.LongSnackbar(emailRoot,
                        "修改成功", SnackbarUtil.Alert).show();
            }

            @Override
            public void onError(Object o) {
                TLog.log(TAG, o.toString() + "");
                SnackbarUtil.LongSnackbar(emailRoot,
                        (String) o, SnackbarUtil.Alert).show();
            }
        });
    }
}
