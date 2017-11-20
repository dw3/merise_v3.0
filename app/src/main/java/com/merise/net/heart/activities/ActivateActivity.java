package com.merise.net.heart.activities;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.view.ClearEditView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/7.
 */
public class ActivateActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.SNEd)
    ClearEditView SNEd;
    @BindView(R.id.remarkEd)
    ClearEditView remarkEd;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.root)
    RelativeLayout root;
    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.activate_door);
        SNEd.addTextChangedListener(this);
        remarkEd.addTextChangedListener(this);
        finish.setEnabled(false);
    }

    @Override
    public void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_activate;
    }


    @OnClick({R.id.back, R.id.finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.finish:
                activateDevice(SNEd.getText().toString(),remarkEd.getText().toString());
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (canCommit()) {
            finish.setEnabled(true);
        } else {
            finish.setEnabled(false);
        }
    }

    private boolean canCommit() {
        if (SNEd.getText().toString().trim().length() == 0) {
            return false;
        }
        if (remarkEd.getText().toString().trim().length() == 0) {
            return false;
        }
        return true;
    }
    private void activateDevice(String sn,String name){
        AppOperate.activateDevice(sn, name, this, new Report() {
            @Override
            public void onSucces(Object o) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o,SnackbarUtil.Alert).show();
            }
        });
    }
}
