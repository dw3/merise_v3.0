package com.merise.net.heart.base;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.view.ClearEditView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/26.
 */
public abstract class BaseViewActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    public
    TextView titleText;
    @BindView(R.id.remarkEd)
    public
    ClearEditView remarkEd;
    @BindView(R.id.finish)
    public
    Button finish;
    @BindView(R.id.root)
    public
    RelativeLayout root;
    public String deviceID;

    public String newKeyNick;
    public String oldKeyNick;
    public int keyID;
    public static int typeValue = -1;//用来区分是修改还是添加
    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_view;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        finish.setEnabled(false);
        remarkEd.addTextChangedListener(this);
    }

    @Override
    public void initData() {
        deviceID = String.valueOf(app.devices.get(app.currentIndex).getId());
    }


    public abstract void finishBtnOnclickListner();


    @OnClick({R.id.back, R.id.finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.finish:
                finishBtnOnclickListner();
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
        if (remarkEd.getText().toString().trim().length() <= 0 || remarkEd.getText().toString().trim().length() > 10 || remarkEd.getText().toString().trim().equals(oldKeyNick)) {
            finish.setEnabled(false);
        } else {
            finish.setEnabled(true);
        }
    }
}
