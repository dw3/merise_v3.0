package com.merise.net.heart.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.framewok.util.NetUtil;
import com.merise.net.heart.R;
import com.merise.net.heart.api.ApiWrapper;
import com.merise.net.heart.api.util.RxHelper;
import com.merise.net.heart.api.util.RxSubscriber;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.Response;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.SnackbarUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import net.merise.nio.common.NioKeepClient;
import net.merise.nio.core.ProcessHandle;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by Administrator on 2016/9/9.
 */
public class VideoActivity extends BaseActivity {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.monitor_view)
    ImageView monitorView;
    @BindView(R.id.imagell)
    LinearLayout imagell;
    @BindView(R.id.open_button)
    Button openButton;
    @BindView(R.id.bottom_layout)
    LinearLayout bottomLayout;
    @BindView(R.id.root)
    CoordinatorLayout root;

    public static String videoSocketPort = "9523";
    public static String hostName;
    private NioKeepClient nkc;
    private static Bitmap bitmap = null;
    private final static int VEDIO = 0X0001;

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.video_title);
    }

    @Override
    public void initData() {
        final String sn = app.devices.get(app.currentIndex).getSn();
        hostName = app.devices.get(app.currentIndex).getServerAddress();
        if (NetUtil.isNetConnected(getBaseContext())) {
            SocketAddress sa = new InetSocketAddress(hostName,
                    Integer.valueOf(videoSocketPort));

            final Map<String, Object> heartbeat = new HashMap<String, Object>();

            nkc = new NioKeepClient(sa, new ProcessHandle() {
                @Override
                public void onConnected(SocketChannel channel) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("sn", sn);
                    nkc.writer(map);
                }

                @Override
                public void onRead(byte[] data, SocketChannel channel)
                        throws Exception {
                    bitmap = BitmapFactory
                            .decodeByteArray(data, 0, data.length);
                    Message m = Message.obtain();
                    m.what = VEDIO;
                    handler.sendMessage(m);
                    nkc.writer(heartbeat);
                }
            });
            nkc.start();
        } else {
            SnackbarUtil.LongSnackbar(root, getResources().getString(R.string.check_net), SnackbarUtil.Warning).show();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case VEDIO:
                    if (bitmap != null) {
                        monitorView.setImageBitmap(bitmap);
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nkc.dispose();
    }


    @OnClick({R.id.back, R.id.open_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                nkc.dispose();
                onBackPressed();
                break;
            case R.id.open_button:
                boolean doorIsOpen = app.devices.get(app.currentIndex).isDoorIsOpen();
                if (!doorIsOpen) {
                    AppOperate.openDoor(String.valueOf(app.devices.get(app.currentIndex).getId()), VideoActivity.this, new Report() {
                        @Override
                        public void onSucces(Object o) {
                            SnackbarUtil.LongSnackbar(root, getResources().getString(R.string.opendoor_success), SnackbarUtil.Confirm).show();
                            app.devices.get(app.currentIndex).setDoorIsOpen(true);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    app.devices.get(app.currentIndex).setDoorIsOpen(false);
                                }
                            }, Integer.valueOf(app.devices.get(app.currentIndex).getAutoCloseTime()) * 1000);
                        }

                        @Override
                        public void onError(Object o) {
                            SnackbarUtil.LongSnackbar(root, String.valueOf(o), SnackbarUtil.Alert).show();
                        }
                    });
                } else {
                    SnackbarUtil.LongSnackbar(root, getResources().getString(R.string.frequent_operation_try_agin_later), SnackbarUtil.Info).show();
                }
                break;
        }
    }
}
