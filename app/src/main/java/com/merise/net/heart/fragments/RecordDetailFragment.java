package com.merise.net.heart.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.android.framewok.base.BaseFragment;
import com.android.framewok.util.TLog;
import com.bumptech.glide.Glide;
import com.merise.net.heart.R;
import com.merise.net.heart.bean.RecordDetail;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.GouUtils;
import com.merise.net.heart.view.ClipImage.ZoomImageView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class RecordDetailFragment extends BaseFragment {
    @BindView(R.id.detail_iv)
    ZoomImageView zoomImageView;
    @BindView(R.id.detail_tv)
    TextView textView;
    private int logid;
    private String deviceId;
    private String logtype;

    private String logDate;
    private String imageurl;
    private String imageurl2;

    public RecordDetailFragment(int logid, String deviceId, String logtype) {
        super();
        this.logid = logid;
        this.deviceId = deviceId;
        this.logtype = logtype;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.recorddetail_frag;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
    }

    private Bitmap bitmap = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    TLog.log(TAG, "执行handler");
                    Glide.with(getContext()).load(imageurl).into(zoomImageView);
                    break;
            }
        }
    };

    @Override
    public void initData() {
        getRecordInfo(deviceId, logid, logtype);

    }


    // //将序列化成json格式后日期(毫秒数)转成日期格式
    public String changeDateFormat(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String time = null;
        if (GouUtils.isNum(str)) {
            time = dateFormat.format(Long.parseLong(str));
        } else {
            time = str;
        }
        TLog.log(TAG, "time----------" + time);
        return time;
    }


    private void getRecordInfo(String deviceId, int logid, String logtype) {
        AppOperate.getRecordInfo(deviceId, String.valueOf(logid), logtype, (RxAppCompatActivity) getContext(), new Report() {
            @Override
            public void onSucces(Object o) {
                RecordDetail object = (RecordDetail) o;
                logDate = object.getLogDate();
                imageurl = object.getImages();
                //将线程立刻发送到线程队列中
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (logDate != null) {
                            textView.setText(changeDateFormat(logDate));
                        }
                        if (imageurl != null) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                });
            }

            @Override
            public void onError(Object o) {
                TLog.log(TAG, "onError-----" + o.toString());
            }
        });
    }
}
