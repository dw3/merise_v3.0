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
import com.merise.net.heart.adapter.RecordsAdapter;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.bean.RecordBean;
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
public class RecordOpenDoorFragment extends BaseFragment {

    @BindView(R.id.record_listview)
    PullToRefreshListView recordListview;
    //    @BindView(R.id.tv_no_record)
    public static TextView tvNoRecord;
    public static int page = 1;
    public static String pageRows = "20";
    public static boolean isRefresh;
    private int firstVisibleID = -1;
    public List<RecordBean> mListItems = new ArrayList<>();
    public static boolean isLoadMore;// 是否为下拉加载更多
    public Context mContext;
    public String deviceId;

    public RecordOpenDoorFragment(String deviceId) {
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
        recordListview.setMode(PullToRefreshBase.Mode.BOTH);
    }


    @Override
    public void initData() {
        getRecordList(page);
        //下拉刷新监听
        recordListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                TLog.log(TAG, "进入下拉刷新");

                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy年MM月dd日  HH:mm");
                String date = format.format(new Date());
                recordListview.getLoadingLayoutProxy().setLastUpdatedLabel(
                        "最近更新时间:" + date);
                tvNoRecord.setVisibility(View.GONE);
                TLog.log(TAG, "进入" + recordListview.isHeaderShown());
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
                intent.putExtra(Const.LOGTYPE, Const.OPENDOOR);
                intent.putExtra(Const.DEVICEID, RecordActivity.deviceID);
                intent.putExtra("List", (Serializable) mListItems);
                startActivity(intent);
            }
        });
    }

    private void getRecordList1(final int s) {
        //下拉刷新用
        if (isLoadMore && firstVisibleID != -1) {
            AppOperate.getRecordList(deviceId, Const.OPENDOOR, s,
                    pageRows, (RxAppCompatActivity) mContext, new Report() {
                        @Override
                        public void onSucces(Object o) {
                            isRefresh = false;
                            TLog.log(TAG, "下拉刷新成功" + o.toString());
                            recordListview.onRefreshComplete();
                            List<RecordBean> recordBean = (List<RecordBean>) o;
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
                            TLog.log(TAG, "下拉刷新失败" + o.toString());
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

        AppOperate.getRecordList(deviceId, Const.OPENDOOR, page,
                pageRows, (RxAppCompatActivity) mContext, new Report() {
                    @Override
                    public void onSucces(Object o) {
                        isLoadMore = false;
                        recordListview.onRefreshComplete();
                        List<RecordBean> recordBean = (List<RecordBean>) o;
                        if (recordBean != null && recordBean.size() > 0) {
                            changePage(Const.OPENDOOR);
                        }
                        TLog.log(TAG, "page-----" + RecordOpenDoorFragment.page);
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
        if (type.equals(Const.OPENDOOR) && RecordOpenDoorFragment.isRefresh) {
            RecordOpenDoorFragment.page += 1;
        }
    }

    /*
    获取开门记录数据列表
     */

    private void getRecordList(final int page) {
        TLog.log(TAG, "deviceID---------recordActivity" + deviceId);
        AppOperate.getRecordList(deviceId, Const.OPENDOOR, page,
                pageRows, (RxAppCompatActivity) mContext, new Report() {
                    @Override
                    public void onSucces(Object o) {
                        recordListview.onRefreshComplete();
                        List<RecordBean> recordBean = (List<RecordBean>) o;
                        if (recordBean.size() == 0) {
                            Toast.makeText(getContext(), "没有开门记录", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (int i = 0; i < recordBean.size(); i++) {
                            if (i == 0 && page == 1) {
                                firstVisibleID = recordBean.get(i).getId();
                                TLog.log(TAG, "firstVisibleID------" + firstVisibleID);
                            }
                        }
                        mListItems = recordBean;
                        RecordsAdapter openDoorAdapter = new RecordsAdapter(Const.OPENDOOR, getContext(), mListItems);
                        recordListview.setAdapter(openDoorAdapter);
                        openDoorAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Object o) {
                        TLog.log(TAG + "获取数据失败", o.toString());
                    }
                });
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
