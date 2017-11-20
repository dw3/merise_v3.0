package com.merise.net.heart.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.bigkoo.alertview.AlertView;
import com.merise.net.heart.R;
import com.merise.net.heart.adapter.RecyclerListAdapter;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.Function;
import com.merise.net.heart.listener.OnItemClickListener;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.ITools;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.view.helper.OnStartDragListener;
import com.merise.net.heart.view.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.merise.net.heart.application.XYApplication.devices;

/**
 * Created by Administrator on 2016/9/12.
 */
public class ItemActivity extends BaseActivity implements OnStartDragListener, OnItemClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.root)
    RelativeLayout root;

    private List<Function> myFuns = new ArrayList<>();
    private List<Function> otherFuns = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    public static RecyclerListAdapter mAdapter;

    private boolean isDraging;
    private Handler handler;
    private int count;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_item;
    }

    private List<Function> getOtherFuns() {
        String functions = app.devices.get(app.currentIndex).getFunctions();
        List<Function> otherFuns = new ArrayList();
        String[] otherArray;
        if (functions == null || functions.length() == 0) {
            return null;
        }
        otherArray = functions.split(",");
        for (int i = 0; i < otherArray.length; i++) {
            for (int j = 0; j < app.functionList.size(); j++) {
                Function fun = app.functionList.get(j);
                if (otherArray[i].equals(String.valueOf(fun.getId()))) {
                    otherFuns.add(fun);
                    for (int k = 0; k < myFuns.size(); k++) {
                        if (fun.getId() == myFuns.get(k).getId()) {
                            otherFuns.remove(fun);
                            break;
                        }
                    }
                }
            }
        }
        return otherFuns;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(app.devices.get(app.currentIndex).getName());
        int left = (int) getResources().getDimension(R.dimen.title_text_padding_left);
        left = 50;
        right.setVisibility(View.GONE);
        right.setPadding(left, 0, 0, 0);
        titleText.setText(R.string.defence_title);
        right.setText(R.string.finish);
        String functionCustom = spt.readSharedPreferencesString(String.valueOf(devices.get(app.currentIndex).getId()));
        myFuns = ITools.getUserHabbit(app.devices.get(app.currentIndex).getFunctions(), functionCustom, app.functionList);
        otherFuns = getOtherFuns();
    }

    @Override
    protected void onResume() {
        super.onResume();
        count = 8;
        if (app.devices.size() > 0 && app.currentIndex >= 0) {
            titleText.setText(app.devices.get(app.currentIndex).getName());
        }
        //摇一摇新增，退出摇一摇界面，如果当前门锁是打开的就执行一个8秒倒计时,然后就继续可以发送指令了
        if (app.devices.get(app.currentIndex).isDoorIsOpen()) {
            handler.postDelayed(countTime, 1000);
        }
    }

    //倒计时操作
    Runnable countTime = new Runnable() {
        @Override
        public void run() {
//            遍历myfun
            for (Function myfun :
                    myFuns) {
                if (myfun.getId() == 17) {
                    myfun.setName("" + count);
                    if (count >= 1) {
                        count--;
                    }
                    if (count == 0) {
                        myfun.setName("摇一摇");
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
//            遍历otherfun
            for (final Function ohterfun :
                    otherFuns) {
                if (ohterfun.getId() == 17) {
                    ohterfun.setName("" + count);
                    if (count >= 1) {
                        count--;
                    }
                    if (count == 0) {
                        ohterfun.setName("摇一摇");
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(countTime);
    }

    @Override
    public void initData() {
        handler = new Handler();
        mAdapter = new RecyclerListAdapter(ItemActivity.this, this, myFuns, otherFuns, root);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mAdapter.getItemViewType(position);
                return viewType == 0 ? layoutManager.getSpanCount() : 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @OnClick({R.id.back, R.id.right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.right:
                final String customFuns = getCustomFunctions(myFuns);
                spt.saveSharedPreferences(String.valueOf(app.devices.get(app.currentIndex).getId()), customFuns);
                right.setVisibility(View.GONE);
                isDraging = false;
                app.devices.get(app.currentIndex).setFunctions(customFuns + "," + getCustomFunctions(otherFuns));
//                AppOperate.updateFunctionCustom(app.devices.get(app.currentIndex).getId(), customFuns, this, new Report() {
//                    @Override
//                    public void onSucces(Object o) {
//                        finish();
//                        right.setVisibility(View.GONE);
//                        isDraging = false;
//                        app.devices.get(app.currentIndex).setFunctionCustom(customFuns);
//                    }
//
//                    @Override
//                    public void onError(Object o) {
//                        SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
//                    }
//                });
                break;
        }
    }

    private String getCustomFunctions(List<Function> list) {
        String funtions = "";
        for (int i = 0; i < list.size(); i++) {
            funtions += list.get(i).getId() + ",";
        }
        if (funtions.length() > 0) {
            funtions = funtions.substring(0, funtions.length() - 1);
        }
        return funtions;
    }

    @Override
    public void onItemClick(View v, int position) {
        int id = 0;
        if (position > 0 && position < myFuns.size() + 1) {
            id = myFuns.get(position - 1).getId();
        } else if (position > myFuns.size() + 1) {
            id = otherFuns.get(position - myFuns.size() - 2).getId();
        }
        TLog.log(TAG, "点击的id为:" + id);
        switch (id) {
            case 1:
                opendoor();
                break;
            case 2:
                gotoActivity(VideoActivity.class);
                break;
            case 3:
                takeACall();
                break;
            case 4:
                setDefence();
                break;
            case 5:

                break;
            case 6:
                gotoActivity(EmergencyCallActivity.class);
                break;
            case 7:
                gotoActivity(PromptManage.class);
                break;
            case 8:
                gotoActivity(OpenDoorSettingActivity.class);
                break;
            case 9:
                int typeID = app.devices.get(app.currentIndex).getTypeID();
                if (typeID == 3 || typeID == 1) {
                    gotoActivity(RecordUnitActivity.class);
                } else {
                    gotoActivity(RecordActivity.class);
                }
                break;
            case 10:
                gotoActivity(FingerprintManagementActivity.class);
                break;
            case 11:
                gotoActivity(CardManagementActivity.class);
                break;
            case 12:
                gotoActivity(AuthUserManagementActivity.class);
                break;
            case 13:
                gotoActivity(DoorbellSettingActivity.class);
                break;
            case 14:
                gotoActivity(TimeSettingActivity.class);
                break;
            case 15:
                gotoActivity(ModifyDoornickActivity.class);
                break;
            case 16:
                gotoActivity(QRCodeActivity.class);
                break;
            case 17:
                if (!(app.devices.get(app.currentIndex).isDoorIsOpen())) {
                    gotoActivity(SharkDoorActivity.class);
                }
                break;
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        TLog.log(TAG, "onStartDrag...");
        mItemTouchHelper.startDrag(viewHolder);
        if (!isDraging) {
            isDraging = true;
            right.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 开门
     */
    private void openDoor(String deviceID) {
        AppOperate.openDoor(deviceID, this, new Report() {
            @Override
            public void onSucces(Object o) {
                SnackbarUtil.LongSnackbar(root, getResources().getString(R.string.opendoor_success), SnackbarUtil.Confirm).show();
                app.devices.get(app.currentIndex).setDoorIsOpen(true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        app.devices.get(app.currentIndex).setDoorIsOpen(false);
                    }
                }, Integer.valueOf(app.devices.get(app.currentIndex).getAutoCloseTime()) * 1000);
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }

    /**
     * 开门
     */
    private void opendoor() {
        if (!app.devices.get(app.currentIndex).isDoorIsOpen()) {
            openDoor(String.valueOf(app.devices.get(app.currentIndex).getId()));
        } else {
            SnackbarUtil.LongSnackbar(root, getResources().getString(R.string.frequent_operation_try_agin_later), SnackbarUtil.Info).show();
        }
    }

    /**
     * 打电话
     */
    private void takeACall() {
        final String phoneNumber = app.devices.get(app.currentIndex).getEmergencyNumber();
        String message;
        String title = getResources().getString(R.string.hint);
        String content;
        String cancel = getResources().getString(R.string.cancel);
        if (phoneNumber == null) {
            message = getResources().getString(R.string.go_setting);
            content = null;
        } else {
            message = getResources().getString(R.string.dialing);
            content = getResources().getString(R.string.confirm_dialing) + phoneNumber;
        }
        new AlertView(title, content, cancel, new String[]{message}, null, ItemActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0 && phoneNumber != null) {
                    callPhone(phoneNumber);
                }
                if (position == 0 && phoneNumber == null) {
                    gotoActivity(EmergencyCallActivity.class);
                }
            }
        }).setCancelable(true).show();
    }

    private void callPhone(String phoneNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ItemActivity.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + phoneNumber);
                intent.setData(data);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phoneNumber);
            intent.setData(data);
            startActivity(intent);
        }
    }

    /**
     * 设防撤防
     */
    private void setDefence() {
        boolean isfortify = app.devices.get(app.currentIndex).getIsfortify() == 1 ? true : false;
        final String successMessage = isfortify ? getResources().getString(R.string.undefence_success) : getResources().getString(R.string.defence_success);
        final String failMessage = isfortify ? getResources().getString(R.string.undefence_fail) : getResources().getString(R.string.defence_fail);
        final int fortify = isfortify ? 0 : 1;
        AppOperate.defenceSetting(String.valueOf(app.devices.get(app.currentIndex).getId()), fortify, this, new Report() {
            @Override
            public void onSucces(Object o) {
                SnackbarUtil.LongSnackbar(root, successMessage, SnackbarUtil.Confirm).show();
                app.devices.get(app.currentIndex).setIsfortify(fortify);
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }

}
