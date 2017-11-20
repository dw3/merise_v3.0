package com.merise.net.heart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.adapter.MenuAdapter;
import com.merise.net.heart.base.BaseSwipmenuRecycleViewActivity;
import com.merise.net.heart.bean.AuthUser;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.SnackbarUtil;
import com.yanzhenjie.recyclerview.swipe.Closeable;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */
public class AuthUserManagementActivity extends BaseSwipmenuRecycleViewActivity<AuthUser> {
    @Override
    public void initView() {
        super.initView();
        titleText.setText(R.string.auth_user_list);
    }

    @Override
    public void initData() {
        super.initData();
        getAuthUserList(deviceID, INIT);
    }

    @Override
    public void initListAdapter() {
        adapter = new MenuAdapter<>(mDatas, AuthUser.class);
    }

    @Override
    public void refreshListAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setOnRightBtnClickListner() {
        Bundle bundle = new Bundle();
        bundle.putInt(DOSTH, AddOrUpdateAuthUserActivity.ADDAUTHUSER);
        gotoActivityForResult(AddOrUpdateAuthUserActivity.class, REQUESTCODE_ADD, bundle);
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
        holder.setTitle(mDatas.get(position).getNick());
        holder.setTextColor(mDatas.get(position).isActived());
    }

    @Override
    public void onItemClick(View v, int position) {
        currentPostion = position;
        switch (v.getId()) {
            case R.id.set:
                TLog.log(TAG, "position:" + position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.AUTHUSER, mDatas.get(position));
                bundle.putInt(DOSTH, AddOrUpdateAuthUserActivity.MODIFYAYTHUSER);
                gotoActivityForResult(AddOrUpdateAuthUserActivity.class, REQUESTCODE_MODIFY, bundle);
                break;
        }
    }

    /**
     * 修改授权用户列表
     *
     * @param userID
     * @param deviceID
     * @param actived
     */
    private void modifyAuthUserState(String userID, String deviceID, final int actived) {
        AppOperate.modifyAuthUserState(userID, deviceID, actived, this, new Report() {
            @Override
            public void onSucces(Object o) {
                mDatas.get(currentPostion).setActived(actived == 1);
                refreshItem(currentPostion);
                String message = getResources().getString(R.string.modify_success);
                SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Confirm).show();
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }

    /**
     * 删除授权用户
     *
     * @param userID
     * @param deviceID
     */
    private void deleteAuthUser(String userID, String deviceID) {
        AppOperate.deleteAuthUser(userID, deviceID, this, new Report() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE_MODIFY) {
            String newKeyNick = data.getStringExtra(Constant.NEWKEYNICK);
            mDatas.get(currentPostion).setNick(newKeyNick);
            refreshListAdapter();
            SnackbarUtil.LongSnackbar(root, getResources().getString(R.string.modify_success), SnackbarUtil.Confirm).show();
        } else if (resultCode == RESULT_OK && requestCode == REQUESTCODE_ADD) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getAuthUserList(deviceID, REFRESH);
                }
            });
        }
    }

    /**
     * 获取授权用户列表
     *
     * @param deviceID
     * @param what
     */
    private void getAuthUserList(String deviceID, final int what) {
        AppOperate.getAuthUserList(deviceID, this, new Report() {
            @Override
            public void onSucces(Object o) {
                if (mDatas != null) {
                    mDatas.clear();
                }
                mDatas = (List<AuthUser>) o;
                for (AuthUser authUser : mDatas) {
                    TLog.log(TAG, authUser.toString());
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

    @Override
    public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
        TLog.log(TAG, "adapterPosition:" + adapterPosition + "menuPostion:" + menuPosition + "derection:" + direction);
        currentPostion = adapterPosition;
        int userID = mDatas.get(adapterPosition).getId();
        boolean actived = mDatas.get(adapterPosition).isActived();
        if (menuPosition == 1) {
            modifyAuthUserState(String.valueOf(userID), deviceID, actived ? 0 : 1);
        } else if (menuPosition == 3) {
            deleteAuthUser(String.valueOf(userID), deviceID);
        }
    }
}
