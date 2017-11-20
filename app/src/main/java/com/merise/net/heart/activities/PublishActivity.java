package com.merise.net.heart.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.bigkoo.pickerview.TimePickerView;
import com.merise.net.heart.R;
import com.merise.net.heart.adapter.GridviewAdapter;
import com.merise.net.heart.api.util.MultipartBody;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.base.BaseGridviewActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.ITools;
import com.merise.net.heart.utils.ImageTools;
import com.merise.net.heart.view.ClearEditView;
import com.merise.net.heart.view.CustomGridView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
 * Created by Administrator on 2016/10/12.
 */
public class PublishActivity extends BaseGridviewActivity implements TimePickerView.OnTimeSelectListener, RadioGroup.OnCheckedChangeListener, TextWatcher {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.et_context)
    EditText etContext;
    @BindView(R.id.gridView)
    CustomGridView gridView;
    @BindView(R.id.trends)
    RadioButton trends;
    @BindView(R.id.activityBtn)
    RadioButton activityBtn;
    @BindView(R.id.chooseDate)
    RelativeLayout chooseDate;
    @BindView(R.id.addressEt)
    ClearEditView addressEt;
    @BindView(R.id.canScanTv)
    TextView canScanTv;
    @BindView(R.id.canScanStaus)
    RelativeLayout canScanStaus;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.date)
    TextView dateTv;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.activityItem)
    RelativeLayout activityItem;

    private TimePickerView timePickerView;
    private int scope = 3;//默认社区

    private int itemType = 1;//是发状态还是发活动 1 、发状态，2、发活动


    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish;
    }


    @Override
    public void initView() {
        ButterKnife.bind(this);
        timePickerView = new TimePickerView(this, TimePickerView.Type.ALL);
        timePickerView.setCancelable(true);
        timePickerView.setOnTimeSelectListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        titleText.setText(R.string.issue);
        etContext.addTextChangedListener(this);
        addressEt.addTextChangedListener(this);
        finish.setEnabled(false);
        if (scope == 2) {
            canScanTv.setText(R.string.for_follow);
        } else if (scope == 3) {
            canScanTv.setText(R.string.for_community);
        } else {
            canScanTv.setText(R.string.for_everbody);
        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TLog.log(TAG, "onActivityResult。。。");
        if (resultCode == RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                compressAndSave(photos);
                loadAdpater(photos);
            }
        } else if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {
            scope = data.getIntExtra(Constant.TYPE, -1);
            if (scope == 2) {
                canScanTv.setText(R.string.for_follow);
            } else if (scope == 3) {
                canScanTv.setText(R.string.for_community);
            } else {
                canScanTv.setText(R.string.for_everbody);
            }
        } else {
            if (!selectedPhotos.contains(GridviewAdapter.DEFAULTURL) && selectedPhotos.size() < maxPhotos) {
                selectedPhotos.add(GridviewAdapter.DEFAULTURL);
            }
            gridAdapter.notifyDataSetChanged();
        }

    }

    @OnClick({R.id.chooseDate, R.id.canScanStaus, R.id.finish, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chooseDate:
                timePickerView.show();
                break;
            case R.id.canScanStaus:
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.TYPE, scope);
                gotoActivityForResult(WhoCanScanActivity.class, REQUESTCODE, bundle);
                break;
            case R.id.finish:
                selectedPhotos.remove(GridviewAdapter.DEFAULTURL);
                ///storage/emulated/0/Pictures/JPEG_20161013_102200.jpg
                String content = etContext.getText().toString();
                String date = dateTv.getText().toString();
                String address = addressEt.getText().toString();
                if (itemType == 1) {
                    date = null;
                    address = null;
                }
                MultipartBody multipartBody = filesToMultipartBody(uploadFiles);
                AppOperate.insertArticle(content, itemType, date, address, scope, 1, 1, multipartBody, this, new Report() {
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
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onTimeSelect(Date date) {
        dateTv.setText(ITools.getCurrentSimpleDateFormat(date));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.trends:
                activityItem.setVisibility(View.GONE);
                itemType = 1;
                break;
            case R.id.activityBtn:
                activityItem.setVisibility(View.VISIBLE);
                itemType = 2;
                break;
        }
        if (canCommit()) {
            finish.setEnabled(true);
        } else {
            finish.setEnabled(false);
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
        if (etContext.getText().toString().trim().length() <= 0) {
            return false;
        }
        if (itemType == 2) {
            if (dateTv.getText().toString().trim().length() <= 0) {
                return false;
            }
            if (addressEt.getText().toString().trim().length() <= 0) {
                return false;
            }
        }
        return true;
    }
}
