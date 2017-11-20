package com.merise.net.heart.activities;

import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.Agreement;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/21 0021.
 */
public class AgreementActivity extends BaseActivity {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.agreement)
    TextView agreementTextView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            agreementTextView.setText((CharSequence) msg.obj);
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.layout_agreement;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.agreement);
        //实现textview上下滑动功能
        agreementTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    public void initData() {
        getAgreement();
    }

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

            }
        });
    }

    @Override
    public void initListener() {

    }


    @OnClick(R.id.back)
    public void onClick() {
        onBackPressed();
    }
}
