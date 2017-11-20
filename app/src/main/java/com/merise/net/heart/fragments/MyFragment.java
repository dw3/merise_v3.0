package com.merise.net.heart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.bumptech.glide.Glide;
import com.merise.net.heart.R;
import com.merise.net.heart.activities.AboutActivity;
import com.merise.net.heart.activities.CommonEquipment;
import com.merise.net.heart.activities.NewSuggestionActivity;
import com.merise.net.heart.activities.QRCodeActivity;
import com.merise.net.heart.activities.SafeSettingActivity;
import com.merise.net.heart.activities.SingleUserDetailActivity;
import com.merise.net.heart.activities.SuggestionActivity;
import com.merise.net.heart.base.MyBaseFragment;
import com.merise.net.heart.bean.Article;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.SnackbarUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2016/9/6.
 */
public class MyFragment extends MyBaseFragment {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.head_information)
    LinearLayout headInformation;
    @BindView(R.id.li_regular)
    LinearLayout liRegular;
    @BindView(R.id.li_safe_setting)
    LinearLayout liSafeSetting;
    @BindView(R.id.li_font_size)
    LinearLayout liFontSize;
    @BindView(R.id.li_suggestion)
    LinearLayout liSuggestion;
    @BindView(R.id.li_about_we)
    LinearLayout liAboutWe;
    @BindView(R.id.quit_bt)
    Button quitBt;
    @BindView(R.id.my_root)
    CoordinatorLayout myRoot;
    @BindView(R.id.head_information_image)
    ImageView headInformationImage;

    private static final int INIT = 0x00;
    @BindView(R.id.nickName_tv)
    TextView nickNameTv;
    @BindView(R.id.mood_tv)
    TextView moodTv;

    private String faceImg;
    private Article author;
    private List<Article> mDatas;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INIT:
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.AUTHUSER, author.getAuthor());
                    bundle.putString(Constant.HEADIMGURL, faceImg);
                    gotoActivity(SingleUserDetailActivity.class, false, bundle);
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
        titleText.setText(R.string.my);
        back.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
    }

    /*
//    获取社区交流相关信息
//     */
    private void getCommunityList(int page, int rows, final int what) {
        AppOperate.getCommunityList(page, rows, (RxAppCompatActivity) getContext(), new Report() {
            @Override
            public void onSucces(Object o) {
                TLog.log(TAG, "myFragment===社区交流" + o.toString());
                mDatas.clear();
                mDatas.addAll((List<Article>) o);
                //遍历获取的交流信息找到指定的信息
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mDatas.get(i).getAuthor() == app.userInfo.getId()) {
                        faceImg = mDatas.get(i).getFaceImg();
                        author = mDatas.get(i);
                    }
                }
                handler.sendEmptyMessage(what);
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(myRoot, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            setView();
        }
    }

    @OnClick({R.id.back, R.id.head_information, R.id.li_regular, R.id.li_safe_setting, R.id.li_font_size, R.id.li_suggestion, R.id.li_about_we, R.id.quit_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.head_information:
                getCommunityList(1, 100, INIT);
                break;
            case R.id.li_regular:
                Intent commonEquipment = new Intent(getContext(), CommonEquipment.class);
                startActivity(commonEquipment);
                break;
            case R.id.li_safe_setting:
                Intent SafeSetting = new Intent(getContext(), SafeSettingActivity.class);
                startActivity(SafeSetting);
                break;
            case R.id.li_font_size:
                startActivity(new Intent(getContext(), QRCodeActivity.class));
                break;
            case R.id.li_suggestion:
                Intent suggestion = new Intent(getContext(), NewSuggestionActivity.class);
                startActivity(suggestion);
                break;
            case R.id.li_about_we:
                Intent aboutUS = new Intent(getContext(), AboutActivity.class);
                startActivity(aboutUS);
                break;
            case R.id.quit_bt:
//                app.reset();
//                setView();
                logout();
//                startActivity(new Intent(getContext(), MainActivity.class));
                break;
        }

    }

    /**
     * 退出登录调用接口
     */
    private void logout() {
        AppOperate.logout((RxAppCompatActivity) getContext(), new Report() {
            @Override
            public void onSucces(Object o) {
                TLog.log(TAG, "注销登录成功" + o.toString());
                app.reset();
                setView();
            }

            @Override
            public void onError(Object o) {
                TLog.log(TAG, "注销登录失败" + o.toString());
            }
        });
    }

    private void setView() {
        if (app.isLogin) {
            headInformation.setVisibility(View.VISIBLE);
            liRegular.setVisibility(View.VISIBLE);
            liSafeSetting.setVisibility(View.VISIBLE);
            liFontSize.setVisibility(View.VISIBLE);
            quitBt.setVisibility(View.VISIBLE);
        } else {
            headInformation.setVisibility(View.GONE);
            liRegular.setVisibility(View.GONE);
            liSafeSetting.setVisibility(View.GONE);
            liFontSize.setVisibility(View.GONE);
            quitBt.setVisibility(View.GONE);
        }
    }

    //
    @Override
    public void onResume() {
        Glide.with(getContext())
                .load(spt.readSharedPreferencesString(Const.NETHEADPATH))
                .placeholder(R.drawable.ic720)
                .error(R.drawable.ic720)
                .centerCrop()
                .crossFade()
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(headInformationImage);

//        if (spt.readSharedPreferencesString(Const.NICKNAME) != null ||
//                spt.readSharedPreferencesString(Const.NICKNAME).length() != 0) {
//            nickNameTv.setText(spt.readSharedPreferencesString(Const.NICKNAME));
//        } else if (spt.readSharedPreferencesString(Const.SIGN) != null ||
//                spt.readSharedPreferencesString(Const.SIGN).length() != 0) {
//            moodTv.setText(spt.readSharedPreferencesString(Const.SIGN));
//        }
        super.onResume();
    }
}
