package com.merise.net.heart.activities;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.GouUtils;
import com.merise.net.heart.utils.UIHelper;
import com.merise.net.heart.view.ClearEditView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/31 0031.
 */
public class RebuildPasswordActivity extends BaseActivity {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.rebuild_password_et)
    ClearEditView rebuildPasswordEt;
    @BindView(R.id.forgetpassword_finish)
    Button forgetpasswordFinish;

    private String rebuidPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rebulid_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.forget_password);
        titleText.setTextSize(30);
    }

    @Override
    public void initData() {

    }



    @OnClick({R.id.back, R.id.forgetpassword_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.forgetpassword_finish:
                rebuidPassword = rebuildPasswordEt.getText().toString().trim();
                rebuildPassword(rebuidPassword);
                break;
        }
    }

    /**
     * 忘记密码第二步
     */
    private void rebuildPassword(String newPassword) {
        AppOperate.rebuildPassword(newPassword,
                this, new Report() {
                    @Override
                    public void onSucces(Object o) {
                        TLog.log(TAG, "召回第二步成功");
                        UIHelper.showLoading(RebuildPasswordActivity.this,
                                "修改成功,正跳向登录页...");
                        new Timer().schedule(new TimerTask() {

                            @Override
                            public void run() {
                                UIHelper.closeLoading();
                                onLogin(1);
                            }
                        }, 3 * 1000);
                    }

                    @Override
                    public void onError(Object o) {
                        String error = o.toString();
                        GouUtils.showTip(RebuildPasswordActivity.this, GouUtils.subString(error));
                    }
                });
    }
}
