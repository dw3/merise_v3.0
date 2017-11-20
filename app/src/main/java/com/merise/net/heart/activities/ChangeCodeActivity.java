package com.merise.net.heart.activities;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.GouUtils;
import com.merise.net.heart.view.ClearEditView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/24 0024.
 */
public class ChangeCodeActivity extends BaseActivity {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.newPassword)
    ClearEditView newPassword;
    @BindView(R.id.change_code_finish)
    Button changeCodeFinish;
    @BindView(R.id.import_hint)
    TextView importHint;
    private String text;
    //输入提示
    final int MAX_LENGTH = 16;
    int Rest_Length = MAX_LENGTH;
    final int LEAST_LENTH = 6;
    int Rest_Least_Length = LEAST_LENTH;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_changecode;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.change_code);
        newPassword.setFilters(new InputFilter[]{GouUtils.filter});
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        changeCodeFinish.setPressed(false);
        /*
        监听文本变化，设置提交按钮状态
         */
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                TLog.log("TAG", "beforeTextChanged--------------->");
//                importHint.setText("请输入6-16位密码，不能全为数字");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TLog.log("TAG", "onTextChanged--------------->");
                text = newPassword.getText().toString().trim();
                //文本开始输入
                if (text.length() > 0 && text.length() < 6) {
                    Rest_Least_Length = LEAST_LENTH - text.length();
                } else if (text.length() >= 6 && text.length() < 16) {
                    Rest_Least_Length = 0;
                    Rest_Length = MAX_LENGTH - text.length();
                } else if (text.length() == 0) {
                    importHint.setText("请输入6-16位密码，不能全为数字");
                    Rest_Least_Length = LEAST_LENTH;
                    Rest_Length = MAX_LENGTH;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                TLog.log("TAG", "afterTextChanged--------------->");
                if (text.length() > 0 && text.length() < 6) {
                    importHint.setText("请至少再输入" + Rest_Least_Length + "个字符");
                    changeCodeFinish.setEnabled(false);
                    //如果文本在6到16之间还是全部为数字的话，提示两点！
                } else if (text.length() >= 6 && text.length() < 16) {
                    importHint.setText("您还可以再输入" + Rest_Length + "个字符");
                    //如果字符数字长度符合而且不全部为数字
                    if (text.matches("[0-9]+")) {
                        importHint.setText("您还可以再输入" + Rest_Length + "个字符" + ",注意不能全部为数字哦！");
                    } else if (!(text.matches("[0-9]+"))) {
                        importHint.setText("您还可以再输入" + Rest_Length + "个字符");
                        changeCodeFinish.setEnabled(true);
                    }
                }else if (text.length() > 16) {
                    newPassword.setEnabled(false);
                    importHint.setText("您还可以再输入" + Rest_Length + "个字符,已到字符上限");
                }
            }
        });
    }


    @OnClick({R.id.back, R.id.change_code_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.change_code_finish:
                updatePassword(text);
                break;
        }
    }

    /**
     * 修改密码
     *
     * @param text
     */
    private void updatePassword(String text) {
        AppOperate.updatePassword(text, this, new Report() {
            @Override
            public void onSucces(Object o) {
                GouUtils.showTip(ChangeCodeActivity.this, "修改成功");
                ChangeCodeActivity.this.finish();
            }

            @Override
            public void onError(Object o) {
                GouUtils.showTip(ChangeCodeActivity.this,o.toString());
            }
        });
    }
}
