package com.merise.net.heart.activities;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.bumptech.glide.Glide;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.Repair;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.SnackbarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:xiangyang
 * 日期:2016/10/26
 */
public class EvaluateActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.evaluateET)
    EditText evaluateET;
    @BindView(R.id.titleTV)
    TextView titleTV;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.evaluateStar)
    RelativeLayout evaluateStar;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.root)
    RelativeLayout root;

    private int repairID;
    private int satisfaction; //满意度
    private int repairStatus;//报修状态

    @Override
    protected int getLayoutId() {
        return R.layout.activity_evaluate;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.evaluate);
        finish.setEnabled(false);
        evaluateET.addTextChangedListener(this);
        repairID = getIntent().getIntExtra(Constant.KEYID, -1);
        repairStatus = getIntent().getIntExtra(Constant.STATUS, -1);
    }

    @Override
    public void initData() {

    }

    /**
     * 评价
     *
     * @param id
     * @param satisfaction
     * @param remark
     * @param status
     */
    private void insertEvaluate(int id, final int satisfaction, final String remark, int status) {
        AppOperate.insertEvaluate(id, satisfaction, remark, status, this, new Report() {
            @Override
            public void onSucces(Object o) {
                String message = getResources().getString(R.string.evaluate_success);
                SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Confirm).show();
                Intent intent = new Intent();
                intent.putExtra(Constant.REMARK,remark);
                intent.putExtra(Constant.SATISFACTION,satisfaction);
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Confirm).show();
            }
        });
    }

    @OnClick({R.id.back, R.id.finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.finish:
                String remark = evaluateET.getText().toString().trim();
                satisfaction = (int) ratingBar.getRating();
                insertEvaluate(repairID, satisfaction, remark, repairStatus);
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
        if (evaluateET.getText().toString().length() > 0) {
            finish.setEnabled(true);
        } else {
            finish.setEnabled(false);
        }
    }
}
