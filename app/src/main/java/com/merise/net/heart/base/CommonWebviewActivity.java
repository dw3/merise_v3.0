package com.merise.net.heart.base;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.framewok.util.DialogHelper;
import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.utils.Constant;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/9.
 */
public class CommonWebviewActivity extends BaseActivity {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.webView)
    WebView webView;
    private ProgressDialog dialog;
    private static Dialog dialog1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_common_webview;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.detail);
    }

    @Override
    public void initData() {
        //设置WebView属性，能够执行JavaScript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        String html  = getIntent().getStringExtra(Constant.KEY);
        webView.loadUrl(Constant.TestUrl+html);
        //覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使得网页可以在WebView中打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候是控制网页在WebView中去打开，如果为false调用系统浏览器或第三方浏览器打开
                view.loadUrl(url);
                return true;
            }
            //WebViewClient帮助WebView去处理一些页面控制和请求通知
        });
        //启用支持Javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //WebView加载页面优先使用缓存加载
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebChromeClient(new WebChromeClient() {
                                       @Override
                                       public void onProgressChanged(WebView view, int newProgress) {
                                           //newProgress   1-100之间的整数
                                           if (newProgress == 100) {
                                               //页面加载完成，关闭ProgressDialog
                                               DialogHelper.stopDialog();
                                           } else {
                                               //网页正在加载，打开ProgressDialog
                                               DialogHelper.showDialogForLoading(CommonWebviewActivity.this,"",false);
                                           }
                                       }
                                   }
        );
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();   //返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }
    }
}
