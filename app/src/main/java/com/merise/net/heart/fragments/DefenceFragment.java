package com.merise.net.heart.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.android.framewok.util.Util;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.merise.net.heart.MainActivity;
import com.merise.net.heart.R;
import com.merise.net.heart.activities.ActivateActivity;
import com.merise.net.heart.activities.AuthUserManagementActivity;
import com.merise.net.heart.activities.CardManagementActivity;
import com.merise.net.heart.activities.DoorbellSettingActivity;
import com.merise.net.heart.activities.EmergencyCallActivity;
import com.merise.net.heart.activities.FingerprintManagementActivity;
import com.merise.net.heart.activities.ItemActivity;
import com.merise.net.heart.activities.LoginActivity;
import com.merise.net.heart.activities.ModifyDoornickActivity;
import com.merise.net.heart.activities.OpenDoorSettingActivity;
import com.merise.net.heart.activities.PromptManage;
import com.merise.net.heart.activities.RecordActivity;
import com.merise.net.heart.activities.RecordUnitActivity;
import com.merise.net.heart.activities.TimeSettingActivity;
import com.merise.net.heart.activities.UnlockActivity;
import com.merise.net.heart.activities.VideoActivity;
import com.merise.net.heart.base.MyBaseFragment;
import com.merise.net.heart.bean.Device;
import com.merise.net.heart.bean.DeviceStatus;
import com.merise.net.heart.bean.Function;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.ITools;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.view.HomeSlideShowView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.merise.net.heart.R.id.top;
import static com.merise.net.heart.R.id.viewPager;
import static com.merise.net.heart.application.XYApplication.devices;


/**
 * Created by Administrator on 2016/9/6.
 */
public class DefenceFragment extends MyBaseFragment implements SwipeRefreshLayout.OnRefreshListener, ExpandableListView.OnGroupExpandListener {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.banner)
    HomeSlideShowView banner;
    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;
    @BindView(R.id.swipRefreshLayout)
    SwipeRefreshLayout swipRefreshLayout;
    @BindView(R.id.root)
    CoordinatorLayout root;
    public static BaseExpandableListAdapter adapter;
    private static final int REQUESTCODE = 0x01;
//    private ArrayList<String> deviceList;
//    private ArrayList<String> children;

    String[] deviceTypeName = new String[]{};

    private static final String TYPE_ZERO = "0";
    private static final String TYPE_ONE = "1";
    private static final String TYPE_TWO = "2";
    private static final String TYPE_THREE = "3";


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_defence;
    }


    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
//        TLog.log(TAG,"right:"+right.getPaddingRight());
//        TLog.log(TAG,"left:"+right.getPaddingLeft());
//        TLog.log(TAG,"top:"+right.getPaddingTop());
//        TLog.log(TAG,"Bottom:"+right.getPaddingBottom());
        int left = (int) getActivity().getResources().getDimension(R.dimen.title_text_padding_left);
        right.setPadding(left, 0, 0, 0);
        titleText.setText(R.string.defence_title);
        back.setVisibility(View.GONE);
        swipRefreshLayout.setOnRefreshListener(DefenceFragment.this);
        deviceTypeName = getResources().getStringArray(R.array.deviceTypeName);
        expandableListView.setOnGroupExpandListener(DefenceFragment.this);
        expandableListView.setGroupIndicator(null);
        banner.refreshView(null);
//        TLog.log(TAG,"right:"+right.getPaddingRight());
//        TLog.log(TAG,"left:"+right.getPaddingLeft());
//        TLog.log(TAG,"top:"+right.getPaddingTop());
//        TLog.log(TAG,"Bottom:"+right.getPaddingBottom());
//        if (app.isLogin) {
//            right.setText(R.string.activate);
//            swipRefreshLayout.setEnabled(true);
//        } else {
//            swipRefreshLayout.setEnabled(false);
//            right.setText(R.string.login);
//        }
        startPlay();
    }

    // 自动轮播的时间间隔
    private final static int TIME_INTERVAL = 5*1000;
    // 定时任务

    private Handler handler = new Handler();
    SlideShowTask slideShowTask = new SlideShowTask();
    /**
     * 开始轮播图切换
     */
    private void startPlay() {
        handler.postDelayed(slideShowTask,TIME_INTERVAL);
    }
    private void stopPlay() {
        handler.removeCallbacks(slideShowTask);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {
        @Override
        public void run() {
            banner.setCurrentItem();
            handler.postDelayed(this,TIME_INTERVAL);
        }
    }

    private void initFunctions() {
        TLog.log(TAG, "refresh2。。。");
        AppOperate.getFunctionList((RxAppCompatActivity) getContext(), new Report() {
            @Override
            public void onSucces(Object o) {
                app.functionList = (List<Function>) o;
                getDeviceList();
            }

            @Override
            public void onError(Object o) {
                closeSwipRefreshing();
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }

    @Override
    public void initData() {
        adapter = new ExpandableAdapter();
        expandableListView.setAdapter(adapter);
        if (app.isLogin) {
            initFunctions();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE && resultCode == getActivity().RESULT_OK) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getDeviceList();
                }
            });
        }
    }

    @OnClick({R.id.right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right:
                if (app.isLogin) { // 已经登录激活设备
                    gotoActivityForResult(ActivateActivity.class, REQUESTCODE);
                } else if (spt.readSharedPreferencesBoolean(Constant.IS_OPEN_FINGER) || app.timeForGesture > 0 && spt.readSharedPreferencesBoolean(Constant.IS_OPEN_GESTURE)) {
                    TLog.log(TAG, "是否进入这里");
                    gotoActivity(UnlockActivity.class); //如果手势密码开启或者指纹开启则进入相应界面登录
                } else {
                    gotoActivity(MainActivity.class);//直接接入登录界面登录
                }
                break;
        }
    }


    @Override
    public void onRefresh() {
        TLog.log(TAG, "refresh1。。。");
        if (app.functionList == null || app.functionList.size() == 0) {
            initFunctions();
        } else {
            getDeviceList();
        }
    }


    @Override
    public void onGroupExpand(int groupPosition) {
        app.currentIndex = groupPosition;
        for (int i = 0; i < devices.size(); i++) {
            if (groupPosition != i) {
                expandableListView.collapseGroup(i);
            }
        }
    }

    private void closeSwipRefreshing() {
        if (swipRefreshLayout.isRefreshing()) {
            swipRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 获取设备列表
     */
    private void getDeviceList() {
        AppOperate.getDeviceList((RxAppCompatActivity) getContext(), new Report() {
            @Override
            public void onSucces(Object o) {
                closeSwipRefreshing();
                TLog.log(TAG, "devices----" + o.toString());
                if (devices != null) {
                    devices.clear();
                    devices.addAll((List<Device>) o);
                } else {
                    devices = (List<Device>) o;
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Object o) {
                closeSwipRefreshing();
                TLog.log(TAG, "onError------" + o.toString());
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.green).show();
            }
        });
    }

    /**
     * 开门
     */
    private void openDoor(String deviceID) {
        AppOperate.openDoor(deviceID, (RxAppCompatActivity) getContext(), new Report() {
            @Override
            public void onSucces(Object o) {
                SnackbarUtil.LongSnackbar(root, getResources().getString(R.string.opendoor_success), SnackbarUtil.Confirm).show();
                devices.get(app.currentIndex).setDoorIsOpen(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        devices.get(app.currentIndex).setDoorIsOpen(false);
                    }
                }, Integer.valueOf(devices.get(app.currentIndex).getAutoCloseTime()) * 1000);
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }


    private class ExpandableAdapter extends BaseExpandableListAdapter implements View.OnClickListener {

        @Override
        public int getGroupCount() {
            return devices.size() == 0 ? 1 : devices.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (devices.size() == 0) {
                return 0;
            }
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (devices.size() == 0) {
                View view = View.inflate(parent.getContext(), R.layout.activate_item, null);
                view.findViewById(R.id.activate_device).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (app.isLogin) {
                            gotoActivityForResult(ActivateActivity.class, REQUESTCODE);
                        } else {
                            ITools.showLoginAlert(getActivity());
                        }
                    }
                });
                return view;
            }
            ViewHolder holder;
            if (convertView == null || convertView.getTag() == null) {//  解决holder为空的问题
                convertView = View.inflate(parent.getContext(), R.layout.item_device_top, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (isExpanded) {
                convertView.setBackgroundResource(R.color.clickColor);
            } else {
                convertView.setBackgroundResource(R.color.white);
            }
            holder.deviceName.setText(devices.get(groupPosition).getName());
            String typeID = String.valueOf(devices.get(groupPosition).getTypeID());
            TLog.log(TAG, "typeID:" + typeID);
            if (TYPE_ONE.equals(typeID)) {
                holder.deviceType.setText(deviceTypeName[0]);
            } else if (TYPE_TWO.equals(typeID)) {
                holder.deviceType.setText(deviceTypeName[1]);
            } else if (TYPE_THREE.equals(typeID)) {
                holder.deviceType.setText(deviceTypeName[2]);
            } else {
                holder.deviceType.setText(deviceTypeName[3]);
            }
            List<DeviceStatus> statuses = devices.get(groupPosition).getStatus();
            for (int i = 0; i < statuses.size(); i++) {
                if (statuses.get(i).getField().equals(Constant.ONLINE)) {
                    int type = statuses.get(i).getType();
                    TLog.log(TAG, "上线状态:" + type);
                    //0为离线 1为在线
                    if (type == 1) {
//                       holder.rootRL.setBackgroundColor(Color.GREEN);
                        holder.deviceName.setTextColor(getResources().getColor(R.color.deviceNameTextColor));
                        holder.deviceType.setTextColor(getResources().getColor(R.color.dodgerblue));
//                        holder.doorStatus.setImageResource(R.drawable.close_door);
                    } else {
                        holder.deviceName.setTextColor(getResources().getColor(R.color.color_b2b2b2));
                        holder.deviceType.setTextColor(getResources().getColor(R.color.color_b2b2b2));
                        holder.doorStatus.setImageResource(R.drawable.signal);
//                        holder.rootRL.setBackgroundColor(Color.RED);
                    }
                }
                if (statuses.get(i).getField().equals(Constant.DOORMAGNET)) {
                    int type = statuses.get(i).getType();
                    TLog.log(TAG, "门磁状态:" + type);
                    if (type == 1) {
                        holder.doorStatus.setImageResource(R.drawable.open_door);
                    } else {
                        holder.doorStatus.setImageResource(R.drawable.close_door);
                    }
                }
            }
//            String isOpen = app.devices.get(groupPosition).getIsOpen();
//            if (TYPE_ONE.equals(isOpen)) {
//                holder.doorStatus.setImageResource(R.drawable.open_door);
//            } else if (TYPE_ZERO.equals(isOpen)) {
//                holder.doorStatus.setImageResource(R.drawable.close_door);
//            } else {
//                holder.doorStatus.setImageResource(R.drawable.moni);
//            }
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
            String functionCustom = spt.readSharedPreferencesString(String.valueOf(devices.get(groupPosition).getId()));
//            List<Function> result = ITools.getUserHabbit(devices.get(groupPosition).getFunctions(), devices.get(groupPosition).getFunctionCustom(), app.functionList);
            List<Function> result = ITools.getUserHabbit(devices.get(groupPosition).getFunctions(), functionCustom, app.functionList);
            convertView = View.inflate(parent.getContext(), R.layout.item_device_child, null);
            LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.rootLL);
            linearLayout.removeAllViews();
            if (result == null) {
                return convertView;
            }
            for (int i = 0; i <= result.size(); i++) {
                View view = View.inflate(getContext(), R.layout.item_child, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Util.getScreenWidth(getContext()) / 5, ViewGroup.LayoutParams.MATCH_PARENT);
                ImageView img = (ImageView) view.findViewById(R.id.funOneImage);
                TextView textView = (TextView) view.findViewById(R.id.funOneName);
                if (i == result.size()) {
                    img.setImageResource(R.drawable.all);
                    textView.setText(R.string.all);
                    linearLayout.addView(view);
                    lp.gravity = Gravity.END; //// TODO: 2016/11/17
                    lp.leftMargin = (4 - result.size()) * Util.getScreenWidth(getContext()) / 5;
                    view.setId(R.id.more);
                    view.setLayoutParams(lp);
                    view.setOnClickListener(this);
                    break;
                }
                view.setLayoutParams(lp);
                if (result.get(i).getId() == 1) {
                    img.setImageResource(R.drawable.close);
                    view.setId(R.id.openDoor);
                }
                if (result.get(i).getId() == 2) {
                    img.setImageResource(R.drawable.video);
                    view.setId(R.id.videoReview);
                }
                if (result.get(i).getId() == 3) {
                    img.setImageResource(R.drawable.call);
                    view.setId(R.id.aKeyCall);
                }
                if (result.get(i).getId() == 4) {
                    int fortifyVal = app.devices.get(groupPosition).getIsfortify();
                    if (fortifyVal == 1) {
                        img.setImageResource(R.drawable.defense);
                    } else if (fortifyVal == 0) {
                        img.setImageResource(R.drawable.enable);
                    }
                    view.setId(R.id.fortifiedStatus);
                }
                if (result.get(i).getId() == 5) {
                    img.setImageResource(R.drawable.home);
                    view.setId(R.id.houseManage);
                }
                if (result.get(i).getId() == 6) {
                    img.setImageResource(R.drawable.phone);
                    view.setId(R.id.setEmergencyNumbers);
                }
                if (result.get(i).getId() == 7) {
                    img.setImageResource(R.drawable.pro);
                    view.setId(R.id.manageTips);
                }
                if (result.get(i).getId() == 8) {
                    img.setImageResource(R.drawable.door);
                    view.setId(R.id.openWay);
                }
                if (result.get(i).getId() == 9) {
                    img.setImageResource(R.drawable.record);
                    view.setId(R.id.viewRecord);
                }
                if (result.get(i).getId() == 10) {
                    img.setImageResource(R.drawable.finger);
                    view.setId(R.id.fingerprintManage);
                }
                if (result.get(i).getId() == 11) {
                    img.setImageResource(R.drawable.card);
                    view.setId(R.id.doorCardManage);
                }
                if (result.get(i).getId() == 12) {
                    img.setImageResource(R.drawable.user);
                    view.setId(R.id.userManage);
                }
                if (result.get(i).getId() == 13) {
                    img.setImageResource(R.drawable.ring);
                    view.setId(R.id.doorbellSet);
                }
                if (result.get(i).getId() == 14) {
                    img.setImageResource(R.drawable.clock);
                    view.setId(R.id.timeSet);
                }
                if (result.get(i).getId() == 15) {
                    img.setImageResource(R.drawable.modify);
                    view.setId(R.id.updateAccess);
                }
                if (result.get(i).getId() == 16) {
                    img.setImageResource(R.drawable.codes_d);
                    view.setId(R.id.QRCode);
                }
                if (result.get(i).getId() == 17) {
                    img.setImageResource(R.drawable.shake_d);
                    view.setId(R.id.sharkDoor);
                }
                String name = result.get(i).getName();
                textView.setText(name.substring(0, name.length() > 4 ? 4 : name.length()));
                view.setOnClickListener(this);
                linearLayout.addView(view);
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        @Override
        public void onClick(View v) {
            TLog.log(TAG, v.getId() + "");
            switch (v.getId()) {
                case R.id.openDoor:
                    opendoor();
                    break;
                case R.id.videoReview:
                    gotoActivity(VideoActivity.class);
                    break;
                case R.id.aKeyCall:
                    takeACall();
                    break;
                case R.id.fortifiedStatus:
                    setDefence();
                    break;
                case R.id.houseManage:
                    break;
                case R.id.setEmergencyNumbers:
                    gotoActivity(EmergencyCallActivity.class);
                    break;
                case R.id.manageTips:
                    gotoActivity(PromptManage.class);
                    break;
                case R.id.openWay:
                    gotoActivity(OpenDoorSettingActivity.class);
                    break;
                case R.id.viewRecord:
                    int typeID = devices.get(app.currentIndex).getTypeID();
                    if (typeID == 3 || typeID == 1) {
                        gotoActivity(RecordUnitActivity.class);
                    } else {
                        gotoActivity(RecordActivity.class);
                    }
                    break;
                case R.id.fingerprintManage:
                    gotoActivity(FingerprintManagementActivity.class);
                    break;
                case R.id.doorCardManage:
                    gotoActivity(CardManagementActivity.class);
                    break;
                case R.id.userManage:
                    gotoActivity(AuthUserManagementActivity.class);
                    break;
                case R.id.doorbellSet:
                    gotoActivity(DoorbellSettingActivity.class);
                    break;
                case R.id.timeSet:
                    gotoActivity(TimeSettingActivity.class);
                    break;
                case R.id.updateAccess:
                    gotoActivity(ModifyDoornickActivity.class);
                    break;
                case R.id.more:
                    gotoActivity(ItemActivity.class);
                    break;
            }
        }
    }

    static class ViewHolder {
        @BindView(R.id.deviceName)
        TextView deviceName;
        @BindView(R.id.deviceType)
        TextView deviceType;
        @BindView(R.id.doorStatus)
        ImageView doorStatus;
        @BindView(R.id.rootRL)
        RelativeLayout rootRL;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            TLog.log(TAG, "onResume...刷新");
            adapter.notifyDataSetChanged();
        }
        if (app.isLogin) {
            right.setText(R.string.activate);
            swipRefreshLayout.setEnabled(true);
        } else {
            swipRefreshLayout.setEnabled(false);
            right.setText(R.string.login);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        TLog.log(TAG, "setUserVisibleHint:" + isVisibleToUser);
        if (isVisibleToUser) {
            if (adapter != null) {
                TLog.log(TAG, "onResume...刷新");
                adapter.notifyDataSetChanged();
            }
            if (right != null && swipRefreshLayout != null) {
                if (app.isLogin) {
                    right.setText(R.string.activate);
                    swipRefreshLayout.setEnabled(true);
                } else {
                    swipRefreshLayout.setEnabled(false);
                    right.setText(R.string.login);
                }
            }
        }
    }

    /**
     * 打电话
     */
    private void takeACall() {
        final String phoneNumber = devices.get(app.currentIndex).getEmergencyNumber();
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
        new AlertView(title, content, cancel, new String[]{message}, null, getContext(), AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0 && phoneNumber != null) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + phoneNumber);
                    intent.setData(data);
                    startActivity(intent);
                }
                if (position == 0 && phoneNumber == null) {
                    gotoActivity(EmergencyCallActivity.class);
                }
            }
        }).setCancelable(true).show();
    }

    /**
     * 设防撤防
     */
    private void setDefence() {
        boolean isfortify = devices.get(app.currentIndex).getIsfortify() == 1 ? true : false;
        final String successMessage = isfortify ? getResources().getString(R.string.undefence_success) : getResources().getString(R.string.defence_success);
        final String failMessage = isfortify ? getResources().getString(R.string.undefence_fail) : getResources().getString(R.string.defence_fail);
        final int fortify = isfortify ? 0 : 1;
        AppOperate.defenceSetting(String.valueOf(devices.get(app.currentIndex).getId()), fortify, (RxAppCompatActivity) getContext(), new Report() {
            @Override
            public void onSucces(Object o) {
                SnackbarUtil.LongSnackbar(root, successMessage, SnackbarUtil.Confirm).show();
                devices.get(app.currentIndex).setIsfortify(fortify);
                expandableListView.collapseGroup(app.currentIndex);
                expandableListView.expandGroup(app.currentIndex);
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
        if (!devices.get(app.currentIndex).isDoorIsOpen()) {
            openDoor(String.valueOf(devices.get(app.currentIndex).getId()));
        } else {
            SnackbarUtil.LongSnackbar(root, getResources().getString(R.string.frequent_operation_try_agin_later), SnackbarUtil.Info).show();
        }
    }
}
