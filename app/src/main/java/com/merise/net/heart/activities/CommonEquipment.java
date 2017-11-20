package com.merise.net.heart.activities;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.CommonUser;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.GouUtils;
import com.merise.net.heart.view.swipemenulistview.SwipeMenu;
import com.merise.net.heart.view.swipemenulistview.SwipeMenuCreator;
import com.merise.net.heart.view.swipemenulistview.SwipeMenuItem;
import com.merise.net.heart.view.swipemenulistview.SwipeMenuListView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/13 0013.
 */
public class CommonEquipment extends BaseActivity {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.mark)
    ImageView mark;
    @BindView(R.id.device_name)
    TextView deviceName;
    @BindView(R.id.login_time)
    TextView loginTime;
    @BindView(R.id.rl_device)
    RelativeLayout rlDevice;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.listview)
    SwipeMenuListView listview;
    @BindView(R.id.ll_other_device)
    LinearLayout llOtherDevice;


    protected static final int INIT = 0;
    private LoginUserAdapter adapter;
    private ArrayList<CommonUser> loginUserList = new ArrayList<CommonUser>();


    private int loginUserSize;
    //当前user的设备数据信息
    private CommonUser AloginUser;


    @Override
    protected int getLayoutId() {
        return R.layout.common_equipment;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.common_equipment);
        titleText.setTextSize(30);
    }

    @Override
    public void initData() {
        TLog.log(TAG, "getLoginUsers");
        getLoginUsers();
    }


    @Override
    public void initListener() {

    }

    @OnClick(R.id.back)
    public void onClick() {
        onBackPressed();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INIT:
                    initDataView();
                    break;
            }
        }
    };

    /**
     * 设置列表信息
     */
    private void initDataView() {
        setLocal();
        if (loginUserList.size() == 0) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
        }
        listview = (SwipeMenuListView) findViewById(R.id.listview);
        adapter = new LoginUserAdapter();
        listview.setAdapter(adapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem imgItem = new SwipeMenuItem(
                        CommonEquipment.this);
                imgItem.setIcon(R.drawable.pub_line);
                imgItem.setWidth(GouUtils.Dip2Px(CommonEquipment.this,
                        2));
                menu.addMenuItem(imgItem);
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        CommonEquipment.this);
                deleteItem.setWidth(GouUtils.Dip2Px(
                        CommonEquipment.this, 60));
                deleteItem.setIcon(R.drawable.del);
                menu.addMenuItem(deleteItem);
            }
        };
        listview.setMenuCreator(creator);
        listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                CommonUser loginUser = loginUserList.get(position);
                switch (index) {
                    case 1:
                        removeLoginUser(loginUser.getId());
                        break;
                }
                return false; //返回值为false表示点击之后item自动滑回初始位置。返回true则点击之后停留。
            }
        });
    }

    /**
     * 删除登录设备
     *
     * @param id
     */
    private void removeLoginUser(final int id) {
        AppOperate.deleteMobile(String.valueOf(id), (RxAppCompatActivity) this, new Report() {
            @Override
            public void onSucces(Object o) {
                for (int i = 0; i < loginUserList.size(); i++) {
                    if (id == loginUserList.get(i).getId())
                        loginUserList.remove(i);
                }
                adapter.notifyDataSetChanged();
                if (loginUserList.size() == 0) {
                    tv.setVisibility(View.GONE);
                } else {
                    tv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Object o) {

            }
        });


    }

    // 设置本机的信息
    private void setLocal() {
        deviceName.setText(GouUtils.getModel() + " " + GouUtils.getManufacturer());
        if (AloginUser != null) {
            if (AloginUser.getType() == 0) {
                mark.setBackgroundResource(R.drawable.pc_and);
            } else {
                mark.setBackgroundResource(R.drawable.phone_and);
            }
            loginTime.setText(AloginUser.getLastLoginTime() + "  "
                    + AloginUser.getLoginIP());
            deviceName.setText(AloginUser.getModel());
        }
    }


    /**
     * 请求数据
     */
    private void getLoginUsers() {
        AppOperate.getLoginUsers((RxAppCompatActivity) this, new Report() {
            @Override
            public void onSucces(Object o) {
                List list = (List) o;
                loginUserList = (ArrayList<CommonUser>) list;
                for (int i = 0; i < loginUserList.size(); i++) {
                    if (loginUserList.get(i).getImei().equals(GouUtils.getID(CommonEquipment.this))) {
                        AloginUser = loginUserList.get(i);
                    }
                }
                //初始化界面数据
                handler.sendEmptyMessage(INIT);

            }

            @Override
            public void onError(Object o) {
                TLog.log(TAG, "onError---" + o.toString());
            }
        });
    }


    class LoginUserAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return loginUserList.size();
        }

        @Override
        public Object getItem(int position) {
            return loginUserList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            viewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(CommonEquipment.this,
                        R.layout.login_device_management_item, null);
                holder = new viewHolder();
                holder.mark = (ImageView) convertView.findViewById(R.id.mark);
                holder.deviceName = (TextView) convertView
                        .findViewById(R.id.device_name);
                holder.loginTime = (TextView) convertView
                        .findViewById(R.id.login_time);
                convertView.setTag(holder);
            } else {
                holder = (viewHolder) convertView.getTag();
            }

            // actived = loginUserList.get(position).isActived();
            // if (actived)
            // holder.deviceName.setTextColor(Color.BLACK);
            // else
            // holder.deviceName.setTextColor(Color.RED);
            if (loginUserList.get(position).getType() == 0) {
                holder.mark.setBackgroundResource(R.drawable.pc_and);
            } else {
                holder.mark.setBackgroundResource(R.drawable.phone_and);
            }
            holder.deviceName.setText(loginUserList.get(position).getModel());

            holder.loginTime.setText(loginUserList.get(position).getLastLoginTime()
                    + "  " + loginUserList.get(position).getLoginIP());
            return convertView;
        }
    }

    static class viewHolder {
        private ImageView mark;
        private TextView deviceName;
        private TextView loginTime;
    }
}
