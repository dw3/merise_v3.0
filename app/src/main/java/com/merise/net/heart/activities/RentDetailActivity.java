package com.merise.net.heart.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.Rent;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.view.HomeSlideShowView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:xiangyang
 * 日期:2016/10/31
 */
public class RentDetailActivity extends BaseActivity {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.banner)
    HomeSlideShowView banner;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.detailRL)
    RelativeLayout detailRL;
    @BindView(R.id.cutLine)
    TextView cutLine;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.finish)
    Button finish;
    private Rent rentDetail;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rent_detail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.detail);
        rentDetail = (Rent) getIntent().getSerializableExtra(Constant.OBJ);
        String imgUrl = rentDetail.getUrl();
        List<String> imgList = new ArrayList<>();
        if(imgUrl!=null) {
            String[] imgArray = imgUrl.split(",");
            for (int i = 0; i < imgArray.length; i++) {
                imgList.add(Constant.TestUrl + "/" + imgArray[i]);
            }
        }
        banner.refreshView(imgList);
    }

    @Override
    public void initData() {
        address.setText(rentDetail.getAddress());
        date.setText(rentDetail.getCreateTime());
        String message = "<font color=\"#e43e3d\"><big><big><big>" + rentDetail.getPrice() + "</big></big></big></font>";
        price.setText(Html.fromHtml(message));
        location.setText(rentDetail.getAreaName());
        description.setText(rentDetail.getRemark());
    }

    @OnClick({R.id.back, R.id.finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.finish:
                //意图：想干什么事
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + rentDetail.getPhoneNumber()));
                //开启系统拨号器
                startActivity(intent);
                break;
        }
    }
}
