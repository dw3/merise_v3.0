package com.merise.net.heart.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.bumptech.glide.Glide;
import com.merise.net.heart.R;
import com.merise.net.heart.adapter.GridviewAdapter;
import com.merise.net.heart.api.util.MultipartBody;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.base.BaseGridviewActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.ITools;
import com.merise.net.heart.utils.ImageTools;
import com.merise.net.heart.utils.Utils;
import com.merise.net.heart.view.ClearEditView;
import com.merise.net.heart.view.CustomGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者:xiangyang
 * 日期:2016/11/1
 */
public class RentActivity extends BaseGridviewActivity implements TextWatcher {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.gridView)
    CustomGridView gridView;
    @BindView(R.id.imgBtn)
    ImageButton imgBtn;
    @BindView(R.id.addImaFL)
    FrameLayout addImaFL;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.pub_line)
    ImageView pubLine;
    @BindView(R.id.titleEd)
    ClearEditView titleEd;
    @BindView(R.id.roomTitleRL)
    RelativeLayout roomTitleRL;
    @BindView(R.id.area)
    TextView area;
    @BindView(R.id.pub_line1)
    ImageView pubLine1;
    @BindView(R.id.areaEd)
    ClearEditView areaEd;
    @BindView(R.id.areaTL)
    RelativeLayout areaTL;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.pub_line2)
    ImageView pubLine2;
    @BindView(R.id.locationEd)
    ClearEditView locationEd;
    @BindView(R.id.locationTL)
    RelativeLayout locationTL;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.pub_line3)
    ImageView pubLine3;
    @BindView(R.id.priceEd)
    ClearEditView priceEd;
    @BindView(R.id.unit)
    TextView unit;
    @BindView(R.id.priceTL)
    RelativeLayout priceTL;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.pub_line4)
    ImageView pubLine4;
    @BindView(R.id.mobileEd)
    ClearEditView mobileEd;
    @BindView(R.id.mobileTL)
    RelativeLayout mobileTL;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.pub_line5)
    ImageView pubLine5;
    @BindView(R.id.descriptionEd)
    ClearEditView descriptionEd;
    @BindView(R.id.descriptionRL)
    RelativeLayout descriptionRL;
    @BindView(R.id.detailSV)
    ScrollView detailSV;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.finishRL)
    RelativeLayout finishRL;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rent;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.i_wanna_rend);
        titleEd.addTextChangedListener(this);
        areaEd.addTextChangedListener(this);
        locationEd.addTextChangedListener(this);
        priceEd.addTextChangedListener(this);
        mobileEd.addTextChangedListener(this);
        descriptionEd.addTextChangedListener(this);
        finish.setEnabled(false);
    }

    private boolean canCommit() {
        int titleLength = titleEd.getText().toString().trim().length();
        int areaLength = areaEd.getText().toString().trim().length();
        int locationLength = locationEd.getText().toString().trim().length();
        int priceLength = priceEd.getText().toString().trim().length();
        String mobileValue = mobileEd.getText().toString().trim();
        int descriptionLength = descriptionEd.getText().toString().trim().length();
        if (titleLength == 0 || titleLength > 11) {
            TLog.log(TAG, "标题不符合");
            return false;
        }
        if (areaLength == 0 || areaLength > 255) {
            TLog.log(TAG, "小区不符合");
            return false;
        }
        if (locationLength == 0 || locationLength > 20) {
            TLog.log(TAG, "位置不符合");
            return false;
        }
        if (priceLength == 0 || priceLength > 11) {
            TLog.log(TAG, "价格不符合");
            return false;
        }
        if (!Utils.isMobile(mobileValue)) {
            TLog.log(TAG, "电话不合格");
            return false;
        }
        if (descriptionLength == 0 || descriptionLength > 255) {
            TLog.log(TAG, "描述不符合");
            return false;
        }
        return true;
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            if (data != null) {
                TLog.log(TAG, "onActivityResult。。。");
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (photos.size() > 0 && photos != null) {
                    compressAndSave(photos);
                    loadAdpater(photos);
                    imgBtn.setVisibility(View.GONE);
                } else {
                    selectedPhotos.clear();
                    imgBtn.setVisibility(View.VISIBLE);
                }
            }
        } else {
            TLog.log(TAG, "onActivityResult1...");
            if (!selectedPhotos.contains(GridviewAdapter.DEFAULTURL) && selectedPhotos.size() < maxPhotos) {
                selectedPhotos.add(GridviewAdapter.DEFAULTURL);
            }
            gridAdapter.notifyDataSetChanged();
        }

    }


    @OnClick({R.id.back, R.id.imgBtn, R.id.finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.imgBtn:
                PhotoPicker.builder()
                        .setPhotoCount(maxPhotos)
                        .setShowCamera(true)
                        .setPreviewEnabled(true)
                        .start(RentActivity.this);
                break;
            case R.id.finish:
//                String title, int price, String address, String areaName,
//                    String remark, int phoneNumber, MultipartBody multipartBody
                String titleVal = titleEd.getText().toString().trim();
                int priceVal = Integer.parseInt(priceEd.getText().toString().trim());
                String addressVal = locationEd.getText().toString().trim();
                String areaName = areaEd.getText().toString().trim();
                String remarkVal = descriptionEd.getText().toString().trim();
                String phoneNunberVal = mobileEd.getText().toString().trim();
                MultipartBody multipartBody = filesToMultipartBody(uploadFiles);
                insertHouseLease(titleVal, priceVal, addressVal, areaName, remarkVal, phoneNunberVal, multipartBody);
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

    /**
     * 新增房屋出租
     * @param title
     * @param price
     * @param address
     * @param areaName
     * @param remark
     * @param phoneNumber
     * @param multipartBody
     */
    private void insertHouseLease(String title, int price, String address, String areaName,
                                  String remark, String phoneNumber, MultipartBody multipartBody) {
        AppOperate.insertHouseLease(title, price, address, areaName, remark, phoneNumber, multipartBody, this, new Report() {
            @Override
            public void onSucces(Object o) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(Object o) {
                TLog.log(TAG + "onError", o.toString());
                selectedPhotos.clear();
                selectedPhotos.add(GridviewAdapter.DEFAULTURL);
                if (uploadFiles != null) {
                    uploadFiles.clear();
                }
                gridAdapter.notifyDataSetChanged();
            }
        });
    }
}
