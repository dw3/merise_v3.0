package com.android.framewok.base;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.android.framewok.AppBaseContext;
import com.android.framewok.interf.BaseFragmentInterface;


public abstract class BaseFragment extends Fragment implements View.OnClickListener,BaseFragmentInterface {
    public final String TAG= this.getClass().getName();
    protected LayoutInflater mInflater;

    public AppBaseContext getApplication() {
        return (AppBaseContext) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mInflater = inflater;
        View view = inflater.inflate(getLayoutId(), container, false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initView(view);
        initData();
        return view;
    }

    protected int getLayoutId() {
        return 0;
    }


    @Override
    public void onClick(View view) {

    }

    /**
     * 打开一个Activity 默认 不关闭当前activity
     * @param clz
     */
    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz,boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    public  void gotoActivity(Class<?> clz,boolean isCloseCurrentActivity,Bundle ex) {
        Intent intent=new Intent(getActivity(), clz);
        if(ex!=null)
            intent.putExtras(ex);
        startActivity(intent);
        if (isCloseCurrentActivity) {
            getActivity().finish();
        }
    }
    public void gotoActivityForResult(Class<?> clz, int requestCode) {
        gotoActivityForResult(clz, requestCode, null);
    }

    public void gotoActivityForResult(Class<?> clz, int requestCode, Bundle ex) {
        Intent intent = new Intent(getActivity(), clz);
        if (ex != null) {
            intent.putExtras(ex);
        }
        startActivityForResult(intent, requestCode);
    }
}
