package com.merise.net.heart.fragments;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.framewok.util.Util;
import com.bumptech.glide.Glide;
import com.merise.net.heart.R;
import com.merise.net.heart.base.MyBaseFragment;
import com.merise.net.heart.view.TouchImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:xiangyang
 * 日期:2016/10/26
 */
@SuppressLint("ValidFragment")
public class RepairRecordDetailFragment extends MyBaseFragment {
    @BindView(R.id.detail_iv)
    TouchImageView detailIv;
    private String url;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_repair_record_detail;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
//        int screenWidth = Util.getScreenWidth(getActivity());
//        int screenHeigth = Util.getScreenHeight(getActivity());
//        detailIv.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth,screenHeigth));
        Glide.with(getActivity()).load("http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=cb0609ba6709c93d07a706f3aa0dd4ea/4a36acaf2edda3cc7ec8fb9401e93901213f9251.jpg").placeholder(R.drawable.default_error).into(detailIv);
    }

    @Override
    public void initData() {

    }


    public RepairRecordDetailFragment(String url) {
        this.url = url;
    }
}
