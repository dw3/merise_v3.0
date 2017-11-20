package com.merise.net.heart.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.adapter.GridviewAdapter;
import com.merise.net.heart.api.util.MultipartBody;
import com.merise.net.heart.base.BaseGridviewActivity;
import com.merise.net.heart.bean.Community;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.view.CustomGridView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * 作者:xiangyang
 * 日期:2016/10/25
 */
public class RepairActivity extends BaseGridviewActivity implements TextWatcher {
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
    @BindView(R.id.titleTV)
    TextView titleTV;
    @BindView(R.id.canScanTv)
    TextView canScanTv;
    @BindView(R.id.canScanStaus)
    RelativeLayout canScanStaus;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.root)
    ScrollView root;

    private int itemType = -1;// 0x00 、公共报修，0x01、个人报修

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish;
    }


    private int checkID = -1;

    @Override
    public void initView() {
        ButterKnife.bind(this);
        itemType = getIntent().getIntExtra(Constant.TYPE, -1);
        if (itemType == RepairThePropertyActivity.COMMONREPAIR) {
            titleText.setText(R.string.common_repair);
        } else if (itemType == RepairThePropertyActivity.PERSONALREPAIR) {
            titleText.setText(R.string.personal_repair);
        }
        titleTV.setText(R.string.issue_commmunity);
        radioGroup.setVisibility(View.GONE);
        etContext.setHint(R.string.repair_detail_description);
        etContext.addTextChangedListener(this);
        finish.setEnabled(false);
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
            Community community = (Community) data.getSerializableExtra(Constant.OBJ);
            checkID = community.getId();
            canScanTv.setText(community.getName());
            if (canCommit()) {
                finish.setEnabled(true);
            } else {
                finish.setEnabled(false);
            }
        } else {
            if (!selectedPhotos.contains(GridviewAdapter.DEFAULTURL) && selectedPhotos.size() < maxPhotos) {
                selectedPhotos.add(GridviewAdapter.DEFAULTURL);
            }
            gridAdapter.notifyDataSetChanged();
        }

    }

    @OnClick({R.id.canScanStaus, R.id.finish, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.canScanStaus:
                gotoActivityForResult(ChooseCommunityActivity.class, REQUESTCODE);
                break;
            case R.id.finish:
                selectedPhotos.remove(GridviewAdapter.DEFAULTURL);
                ///storage/emulated/0/Pictures/JPEG_20161013_102200.jpg
                String content = etContext.getText().toString();
                MultipartBody multipartBody = filesToMultipartBody(uploadFiles);
                AppOperate.insertPropertyRepair(itemType, 1, content, 2, checkID, multipartBody, this, new Report() {
                    @Override
                    public void onSucces(Object o) {
                        String message = getResources().getString(R.string.success);
                        Snackbar snackbar = SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Confirm);
                        snackbar.setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                finish();
                            }
                        });
                        snackbar.show();
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
        if (checkID == -1) {
            return false;
        }
        return true;
    }
}
