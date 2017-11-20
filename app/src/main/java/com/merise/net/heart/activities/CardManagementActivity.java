package com.merise.net.heart.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.adapter.MenuAdapter;
import com.merise.net.heart.api.ApiWrapper;
import com.merise.net.heart.api.util.RxHelper;
import com.merise.net.heart.api.util.RxSubscriber;
import com.merise.net.heart.base.BaseSwipmenuRecycleViewActivity;
import com.merise.net.heart.bean.Card;
import com.merise.net.heart.bean.Response;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.SnackbarUtil;
import com.yanzhenjie.recyclerview.swipe.Closeable;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;


/**
 * Created by Administrator on 2016/9/20.
 */
public class CardManagementActivity extends BaseSwipmenuRecycleViewActivity<Card> {

    @Override
    public void initView() {
        super.initView();
        titleText.setText(R.string.card_management);
    }

    @Override
    public void initData() {
        super.initData();
        getCardList(deviceID, INIT);
    }

    @Override
    public void initListAdapter() {
        adapter = new MenuAdapter(mDatas, Card.class);
    }

    @Override
    public void refreshListAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setOnRightBtnClickListner() {
        Bundle bundle = new Bundle();
        bundle.putInt(DOSTH, AddOrUpdateCardAcitivty.ADDCARD);
        gotoActivityForResult(AddOrUpdateCardAcitivty.class, REQUESTCODE_ADD, bundle);
    }

    /**
     * 获取卡片列表
     *
     * @param deviceID
     */
    private void getCardList(String deviceID, final int what) {
        AppOperate.getCardList(deviceID, 1, 100, this, new Report() {
            @Override
            public void onSucces(Object o) {
                if (mDatas != null) {
                    mDatas.clear();
                }
                mDatas = (List<Card>) o;
                for (Card card : mDatas) {
                    TLog.log(TAG, card.toString());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TLog.log(TAG, "onActivityResult");
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE_MODIFY) {
            String newKeyNick = data.getStringExtra(Constant.NEWKEYNICK);
            mDatas.get(currentPostion).setKeyNick(newKeyNick);
            refreshListAdapter();
            SnackbarUtil.LongSnackbar(root, getResources().getString(R.string.modify_success), SnackbarUtil.Confirm).show();
        } else if (resultCode == RESULT_OK && requestCode == REQUESTCODE_ADD) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getCardList(deviceID, REFRESH);
                }
            });
        }
    }

    @Override
    public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
        super.onItemClick(closeable, adapterPosition, menuPosition, direction);
        TLog.log(TAG, "adapterPosition:" + adapterPosition + "menuPostion:" + menuPosition + "derection:" + direction);
        currentPostion = adapterPosition;
        int keyID = mDatas.get(adapterPosition).getId();
        boolean actived = mDatas.get(adapterPosition).isActived();
        if (menuPosition == 1) {
            modifyCardState(deviceID, keyID, actived ? 0 : 1);
        } else if (menuPosition == 3) {
            deleteCard(deviceID, keyID);
        }
    }

    @Override
    public int getItemType(int position) {
        if (mDatas.get(position).isActived()) {
            return MenuAdapter.ACTIVED;
        }
        return MenuAdapter.UNACTIVED;
    }

    @Override
    public void onItemClick(View v, int position) {
        currentPostion = position;
        switch (v.getId()) {
            case R.id.set:
                TLog.log(TAG, "position:" + position);
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.KEYID, mDatas.get(position).getId());
                bundle.putString(Constant.OLDKEYNICK, mDatas.get(position).getKeyNick());
                bundle.putInt(DOSTH, AddOrUpdateCardAcitivty.MODIFYCARD);
                gotoActivityForResult(AddOrUpdateCardAcitivty.class, REQUESTCODE_MODIFY, bundle);
                break;
        }
    }

    @Override
    public void onBindViewHolder(MenuAdapter.DefaultViewHolder holder, int position) {
        holder.setTitle(mDatas.get(position).getKeyNick());
        holder.setTextColor(mDatas.get(position).isActived());
    }

    /**
     * 修改卡片状态
     *
     * @param deviceID
     * @param keyID
     * @param actived
     */
    public void modifyCardState(String deviceID, int keyID, final int actived) {
        AppOperate.modifyCardState(deviceID, keyID, actived, this, new Report() {
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
     * 移除卡片
     *
     * @param deviceID
     * @param keyID
     */
    public void deleteCard(String deviceID, int keyID) {
        AppOperate.deleteCard(deviceID, keyID, this, new Report() {
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
