package com.merise.net.heart.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.MainActivity;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.QRCodeUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QRCodeActivity extends BaseActivity {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.topRootRL)
    RelativeLayout topRootRL;
    @BindView(R.id.qrcodeImg)
    ImageView qrcodeImg;
    @BindView(R.id.copyQRCode)
    Button copyQRCode;
    private String filePath;

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.electron_key);
    }

    @Override
    public void initData() {
        AppOperate.getVirtualKey(String.valueOf(app.devices.get(app.currentIndex).getId()),
                this, new Report() {
                    @Override
                    public void onSucces(Object o) {
                        TLog.log(TAG, "二维成功");
                        makeQRcode();
                    }

                    @Override
                    public void onError(Object o) {
                        TLog.log(TAG, "二维失败");
                    }
                });
    }

    private void makeQRcode() {
        filePath = getFileRoot(this) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";
        final String qrtoken = spt.readSharedPreferencesString(Const.QRTOKEN);
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = QRCodeUtil.createQRImage(Constant.TestUrl+"/merise/device/vk/"+qrtoken, 800, 800,
                        BitmapFactory.decodeResource(getResources(), R.drawable.ic720),
                        filePath);

                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            qrcodeImg.setImageBitmap(BitmapFactory.decodeFile(filePath));
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qrcode;
    }

    @OnClick({R.id.back, R.id.copyQRCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.copyQRCode:
                shareSingleImage();
                break;
        }
    }

    //分享单张图片
    public void shareSingleImage() {
//        String imagePath = Environment.getExternalStorageDirectory() + File.separator + "test.jpg";
        //由文件得到uri
        Uri imageUri = Uri.fromFile(new File(filePath));
        Log.d("share", "uri:" + imageUri);  //输出：file:///storage/emulated/0/test.jpg

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage("com.tencent.mm");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    //文件存储根目录
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }
}
