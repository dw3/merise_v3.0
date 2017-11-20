package com.merise.net.heart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.adapter.MenuAdapter;
import com.merise.net.heart.base.BaseSwipmenuRecycleViewActivity;
import com.merise.net.heart.bean.Fingerprint;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.SnackbarUtil;
import com.yanzhenjie.recyclerview.swipe.Closeable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class FingerprintManagementActivity extends BaseSwipmenuRecycleViewActivity<Fingerprint> {
    @Override
    public void initView() {
        super.initView();
        titleText.setText(getResources().getString(R.string.fingerprint_management));
    }

    @Override
    public void initData() {
        super.initData();
        getFingerprintList(deviceID, INIT);
    }

    @Override
    public void initListAdapter() {
        adapter = new MenuAdapter(mDatas, Fingerprint.class);
    }

    @Override
    public void refreshListAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setOnRightBtnClickListner() {
        Bundle bundle = new Bundle();
        bundle.putInt(DOSTH, AddOrUpdateFingerprintActivity.ADDFINGERPRINT);
        gotoActivityForResult(AddOrUpdateFingerprintActivity.class, REQUESTCODE_ADD, bundle);
    }

    @Override
    public int getItemType(int position) {
        if (mDatas.get(position).isActived()) {
            return MenuAdapter.ACTIVED;
        }
        return MenuAdapter.UNACTIVED;
    }

    @Override
    public void onBindViewHolder(MenuAdapter.DefaultViewHolder holder, int position) {
        holder.setTitle(mDatas.get(position).getFingerprintNick());
        holder.setTextColor(mDatas.get(position).isActived());
    }

    @Override
    public void onItemClick(View v, int position) {
        currentPostion = position;
        switch (v.getId()) {
            case R.id.set:
                TLog.log(TAG, "position:" + position);
                int fingerID = mDatas.get(position).getId();
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.KEYID, fingerID);
                bundle.putInt(DOSTH, AddOrUpdateFingerprintActivity.MODIFYFINGERPRINT);
                bundle.putString(Constant.OLDKEYNICK, mDatas.get(position).getFingerprintNick());
                gotoActivityForResult(AddOrUpdateFingerprintActivity.class, REQUESTCODE_MODIFY, bundle);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TLog.log(TAG, "onActivityResult");
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE_MODIFY) {
            String newKeyNick = data.getStringExtra(Constant.NEWKEYNICK);
            mDatas.get(currentPostion).setFingerprintNick(newKeyNick);
            refreshListAdapter();
            SnackbarUtil.LongSnackbar(root, getResources().getString(R.string.modify_success), SnackbarUtil.Confirm).show();
        } else if (resultCode == RESULT_OK && requestCode == REQUESTCODE_ADD) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getFingerprintList(deviceID, REFRESH);
                }
            });
        }
    }

    @Override
    public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
        super.onItemClick(closeable, adapterPosition, menuPosition, direction);
        TLog.log(TAG, "adapterPosition:" + adapterPosition + "menuPostion:" + menuPosition + "derection:" + direction);
        currentPostion = adapterPosition;
        int fingerID = mDatas.get(adapterPosition).getId();
        boolean actived = mDatas.get(adapterPosition).isActived();
        if (menuPosition == 1) {
            modifyFingerprint(deviceID, fingerID, actived ? 0 : 1);
        } else if (menuPosition == 3) {
            deleteFingerprint(deviceID, fingerID);
        }
    }


    /**
     * 获取指纹列表
     *
     * @param deviceID
     */
    private void getFingerprintList(String deviceID, final int what) {
        AppOperate.getFingerprintList(deviceID, 1, 100, this, new Report() {
            @Override
            public void onSucces(Object o) {
                if (mDatas != null) {
                    mDatas.clear();
                }
                mDatas = (List<Fingerprint>) o;
                for (Fingerprint fingerprint : mDatas) {
                    TLog.log(TAG, fingerprint.toString());
                }
                handler.sendEmptyMessage(what);
                if (what == REFRESH) {
                    SnackbarUtil.LongSnackbar(root, getResources().getString(R.string.add_success), SnackbarUtil.Confirm).show();
                }
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }

    /**
     * 修改指纹
     *
     * @param deviceID
     * @param fingerID
     * @param actived
     */
    private void modifyFingerprint(String deviceID, int fingerID, final int actived) {
        AppOperate.modifyFingerprint(deviceID, fingerID, actived, this, new Report() {
            @Override
            public void onSucces(Object o) {
                mDatas.get(currentPostion).setActived(actived == 1);
                refreshItem(currentPostion);
                String message = getResources().getString(R.string.modify_success);
                SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Confirm).show();
            }

            @Override
            public void onError(Object o) {
                String message = (String) o;
                SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Alert).show();
            }
        });
    }

    /**
     * 删除指纹
     *
     * @param deviceID
     * @param fingerID
     */
    private void deleteFingerprint(String deviceID, int fingerID) {
        AppOperate.deleteFingerprint(deviceID, fingerID, this, new Report() {
            @Override
            public void onSucces(Object o) {
                mDatas.remove(currentPostion);
                deleteItem(currentPostion);
                String message = getResources().getString(R.string.delete_success);
                SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Confirm).show();
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }
}
