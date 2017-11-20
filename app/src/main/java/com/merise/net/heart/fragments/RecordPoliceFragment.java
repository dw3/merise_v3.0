package com.merise.net.heart.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.framewok.base.BaseFragment;
import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.activities.RecordActivity;
import com.merise.net.heart.activities.RecordViewActivity;
import com.merise.net.heart.adapter.RecordsBellPoliceAdapter;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.bean.DoorBell;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.view.PullToReFreshListView.PullToRefreshBase;
import com.merise.net.heart.view.PullToReFreshListView.PullToRefreshListView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by wangdawang on 2016/7/12 0012.
 */
@SuppressLint("ValidFragment")
public class RecordPoliceFragment extends BaseFragment {

    @BindView(R.id.record_listview)
    PullToRefreshListView recordListview;
    public static TextView tvNoRecord;
    public static int page = 1;
    public static String pageRows = "20";
    public static boolean isRefresh;
    private int firstVisibleID = -1;
    public List<DoorBell> mListItems = new ArrayList<>();
    public static boolean isLoadMore;// 是否为下拉加载更多
    public Context mContext;
    public String deviceId;
    private List<DoorBell> firstrecordBean;

    public RecordPoliceFragment(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_record;
    }

    @Override
    public void initView(View view) {
        tvNoRecord = (TextView) view.findViewById(R.id.tv_no_record);
        ButterKnife.bind(this, view);
        //    * Mode.BOTH：同时支持上拉下拉
        recordListview.setMode(PullToRefreshBase.Mode.BOTH);
//        UIHelper.showLoading(getActivity(), "加载中...");
    }


    @Override
    public void initData() {
        getRecordList(page);
        //下拉刷新监听
        recordListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy年MM月dd日  HH:mm");
                String date = format.format(new Date());
                recordListview.getLoadingLayoutProxy().setLastUpdatedLabel(
                        "最近更新时间:" + date);
                tvNoRecord.setVisibility(View.GONE);
                if (recordListview.isHeaderShown()) {
                    isLoadMore = true;
                    getRecordList1(1);
                } else if (recordListview.isFooterShown()) {
                    isRefresh = true;
                    isLoadMore = false;
                    getRecordList2(page + 1);
                }
            }
        });

        //Item点击事件监听
        recordListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TLog.log(TAG, "position0-----" + position);
                Intent intent = new Intent(getActivity(), RecordViewActivity.class);
                int logid = mListItems.get(position - 1).getId();
                intent.putExtra(Const.LOGID, logid);
                intent.putExtra(Const.LOGTYPE, Const.DOORSENSOR);
                intent.putExtra(Const.DEVICEID, RecordActivity.deviceID);
                intent.putExtra("List", (Serializable) mListItems);
                startActivity(intent);
            }
        });
    }

    private void getRecordList1(final int s) {
        //下拉刷新用
        if (isLoadMore && firstVisibleID != -1) {
            AppOperate.getRecordListDoor(deviceId, Const.DOORSENSOR, s,
                    pageRows, (RxAppCompatActivity) mContext, new Report() {
                        @Override
                        public void onSucces(Object o) {
                            recordListview.onRefreshComplete();
                            List<DoorBell> recordBean = (List<DoorBell>) o;
                            for (int i = 0; i < recordBean.size(); i++) {
                                if (i == 0 && s == 1) {
                                    firstVisibleID = recordBean.get(i).getId();
                                    TLog.log(TAG, "firstVisibleID------" + firstVisibleID);
                                }
                            }
                            if (isLoadMore) {
                                mListItems = recordBean;
                            }
                        }

                        @Override
                        public void onError(Object o) {

                        }
                    });
        }
    }

    /**
     * 上啦加载用
     *
     * @param page
     */
    private void getRecordList2(final int page) {

        AppOperate.getRecordListDoor(deviceId, Const.DOORSENSOR, page,
                pageRows, (RxAppCompatActivity) mContext, new Report() {
                    @Override
                    public void onSucces(Object o) {
                        recordListview.onRefreshComplete();
                        List<DoorBell> recordBean = (List<DoorBell>) o;
                        if (recordBean != null && recordBean.size() > 0) {
                            changePage(Const.DOORSENSOR);
                        }
                        TLog.log(TAG, "page-----" + RecordDoorBellFragment.page);
                        for (int i = 0; i < recordBean.size(); i++) {
                            mListItems.add(recordBean.get(i));
                            TLog.log(TAG, "上啦加载的数量" + mListItems.size());
                        }
                    }

                    @Override
                    public void onError(Object o) {
                        TLog.log(TAG, "onError----" + o.toString());
                    }
                });


    }

    /**
     * 根据不同的类型改变页码
     *
     * @param type
     */
    private void changePage(String type) {
        if (type.equals(Const.DOORSENSOR) && RecordPoliceFragment.isRefresh) {
            RecordPoliceFragment.page += 1;
        }
    }

    /*
    获取开门记录数据列表
     */

    private void getRecordList(final int page) {
        TLog.log(TAG, "deviceID---------recordActivity" + deviceId);
        AppOperate.getRecordListDoor(deviceId, Const.DOORSENSOR, page,
                pageRows, (RxAppCompatActivity) mContext, new Report() {
                    @Override
                    public void onSucces(Object o) {
                        recordListview.onRefreshComplete();
                        firstrecordBean = (List<DoorBell>) o;
                        if (firstrecordBean.size() == 0) {
//                            Toast.makeText(getContext(), "没有报警记录", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (int i = 0; i < firstrecordBean.size(); i++) {
                            if (i == 0 && page == 1) {
                                firstVisibleID = firstrecordBean.get(i).getId();
                                TLog.log(TAG, "firstVisibleID------" + firstVisibleID);
                            }
                        }
                        mListItems = firstrecordBean;
                        RecordsBellPoliceAdapter bellPoliceAdapter = new RecordsBellPoliceAdapter(Const.DOORSENSOR, getContext(), mListItems);
                        recordListview.setAdapter(bellPoliceAdapter);
                        bellPoliceAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Object o) {
                        TLog.log(TAG + "获取数据失败", o.toString());
                    }
                });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (firstrecordBean == null || firstrecordBean.size() == 0) {
                Toast.makeText(getContext(), "没有报警记录", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        TLog.log(TAG, "onDetach...");
        page = 1;
        isRefresh = false;
    }
}
