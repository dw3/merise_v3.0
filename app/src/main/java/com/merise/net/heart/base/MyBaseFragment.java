package com.merise.net.heart.base;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.framewok.base.BaseFragment;
import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.activities.HomeActivity;
import com.merise.net.heart.application.XYApplication;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.ITools;
import com.merise.net.heart.utils.SharedPreferencesTools;

/**
 * Created by Administrator on 2016/9/8.
 */
public abstract class MyBaseFragment extends BaseFragment {
    public static XYApplication app;
    public SharedPreferencesTools spt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        app = (XYApplication) XYApplication.getInstance();
        spt = new SharedPreferencesTools(getActivity());
        this.mInflater = inflater;
        View view = inflater.inflate(getLayoutId(), container, false);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.topRootRL);
        if (relativeLayout != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ITools.getActionBarHeight(getContext()));
                params.topMargin = ITools.getStatusHeight(getActivity());
                relativeLayout.setLayoutParams(params);
                //透明状态栏
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //透明导航栏
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
        initView(view);
        initData();
        return view;
    }

    public void loginSuccess() {
        app.isLogin = true;
        app.timeForGesture = 3;
        app.finishAllActivity();
        gotoActivity(HomeActivity.class);
        String headImgPath = app.userInfo.getFaceImg();// 服务器传过来的头像路径
        //当前服务器传回来的头像完整路径
        String newPath = Constant.TestUrl + headImgPath.replaceAll("\\\\", "/");
        TLog.log(TAG, "head image path:" + newPath);
        spt.saveSharedPreferences(Constant.NETHEADPATH, "http://img1.sc115.com/uploads/sc/jpg/183/img-0206-jls0rxjqkqa.jpg");
    }
}
