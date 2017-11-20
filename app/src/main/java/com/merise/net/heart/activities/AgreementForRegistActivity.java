package com.merise.net.heart.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.merise.net.heart.R;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.Agreement;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.GouUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/27 0027.
 */
public class AgreementForRegistActivity extends BaseActivity {

    private final int AGREEMENT = 0X0001;
    @BindView(R.id.agreement_content)
    TextView agreementContent;
    @BindView(R.id.reject_btn)
    Button rejectBtn;
    @BindView(R.id.agree_btn)
    Button agreeBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_agreement_for_regist;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        agreementContent.setMovementMethod(ScrollingMovementMethod
                .getInstance());
        getAgreement();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }


    @OnClick({R.id.reject_btn, R.id.agree_btn})
    public void onClick(View view) {
        Intent data = new Intent();
        switch (view.getId()) {
            case R.id.reject_btn:
                data.putExtra(Const.AGREEMENT, false);
                // 请求代码可以自己设置，这里设置成20
                setResult(AGREEMENT, data);
                finish();
                break;
            case R.id.agree_btn:
                data.putExtra(Const.AGREEMENT, true);
                // 请求代码可以自己设置，这里设置成20
                setResult(AGREEMENT, data);
                finish();
                break;
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            agreementContent.setText((CharSequence) msg.obj);
        }
    };


    /**
     * 获取用户协议
     */
    private void getAgreement() {
        AppOperate.getAgreement(this, new Report() {
            @Override
            public void onSucces(Object o) {
                Agreement agrement = (Agreement) o;
                String content = agrement.getContent();
                Message msg = new Message();
                msg.obj = content;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Object o) {
                String error = o.toString();
                GouUtils.showTip(AgreementForRegistActivity.this, GouUtils.subString(error));
            }
        });
    }
}
