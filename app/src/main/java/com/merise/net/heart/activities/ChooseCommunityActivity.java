package com.merise.net.heart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.Community;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:xiangyang
 * 日期:2016/10/25
 */
public class ChooseCommunityActivity extends BaseActivity {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.bottom_layout)
    LinearLayout bottomLayout;
    @BindView(R.id.root)
    RelativeLayout root;

    private List<Community> mDatas;


    private static final int INIT = 0x01;

    private Community checkedCommunity;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_community;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.choose_community);
    }

    @Override
    public void initData() {
        getMyAreaList();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INIT:
                    for (int j = 0; j < mDatas.size(); j++) {
                        RadioButton button = (RadioButton) LayoutInflater.from(getBaseContext()).inflate(R.layout.item_choose_community, null);
                        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, 100);
                        button.setLayoutParams(params);
                        button.setText(mDatas.get(j).getName());
                        button.setId(mDatas.get(j).getId());
                        radioGroup.addView(button);
                    }
                    break;
            }
        }
    };

    /**
     * 获取区域列表
     */
    private void getMyAreaList() {
        AppOperate.getMyAreaList(this, new Report() {
            @Override
            public void onSucces(Object o) {
                mDatas = new ArrayList<>();
                mDatas = (List<Community>) o;
                handler.sendEmptyMessage(INIT);
            }

            @Override
            public void onError(Object o) {

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
                int checkedID = radioGroup.getCheckedRadioButtonId();
                for (int i = 0; i < mDatas.size(); i++) {
                    if (checkedID == mDatas.get(i).getId()) {
                        checkedCommunity = mDatas.get(i);
                        break;
                    }
                }
                Intent intent = new Intent();
                intent.putExtra(Constant.OBJ, checkedCommunity);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
