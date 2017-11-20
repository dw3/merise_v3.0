package com.merise.net.heart.activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.utils.UpdateManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/20 0020.
 */
public class AboutActivity extends BaseActivity {


    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.version_update)
    LinearLayout versionUpdate;
    @BindView(R.id.brief_description)
    LinearLayout briefDescription;
    @BindView(R.id.server_protocol)
    LinearLayout serverProtocol;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.about_us);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }


    @OnClick({R.id.back, R.id.version_update, R.id.brief_description, R.id.server_protocol})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.version_update:
                new UpdateManager(mContext).checkUpdate();
                break;
            case R.id.brief_description:
                Intent Introduction = new Intent(this, IntroductionActivity.class);
                startActivity(Introduction);
                break;
            case R.id.server_protocol:
                Intent Agree = new Intent(this, AgreementActivity.class);
                startActivity(Agree);
                break;
        }
    }
}
