package com.merise.net.heart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.merise.net.heart.R;
import com.merise.net.heart.adapter.GridviewAdapter;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.Article;
import com.merise.net.heart.bean.ImgsBean;
import com.merise.net.heart.bean.Repair;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.ITools;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.view.CustomGridView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPreview;

/**
 * 作者:xiangyang
 * 日期:2016/10/25
 */
public class RepairRecordActivity extends BaseActivity implements XRecyclerView.LoadingListener {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.recyclerView)
    XRecyclerView recyclerView;
    //    @BindView(R.id.swipRefreshLayout)
//    SwipeRefreshLayout swipRefreshLayout;
    @BindView(R.id.root)
    RelativeLayout root;
    private List<Repair> mDatas = new ArrayList<>();

    private MyRecyclerViewAdapter adapter;
    private static final int REFRESH = 0x01;
    private static final int LOADMORE = 0x02;
    private LinearLayoutManager layoutManager;

    private int defaultPage = 1;
    private static final int defaultRows = 20;

    private static final int REQUESTCODE = 0x03;
    private int currentPosition;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_repair_record;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.repair_record);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLoadingListener(this);
    }

    @Override
    public void initData() {
        getListWuyeRepair(1, defaultRows, 0, REFRESH);
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH:
                    defaultPage = 1;
                    if (adapter == null) {
                        adapter = new MyRecyclerViewAdapter();
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                        recyclerView.refreshComplete();
                    }
                    break;
                case LOADMORE:
                    adapter.notifyDataSetChanged();
                    recyclerView.loadMoreComplete();
                    break;
            }
        }
    };

    /**
     * 查询报修列表
     *
     * @param page
     * @param rows
     * @param type 1个人，2公共 其他
     */
    private void getListWuyeRepair(int page, int rows, int type, final int what) {
        AppOperate.getListWuyeRepair(page, rows, type, this, new Report() {
            @Override
            public void onSucces(Object o) {
                if (what == REFRESH) {
                    mDatas.clear();
                    mDatas = (List<Repair>) o;
                    recyclerView.setNoMore(false);
                } else {
                    List<Repair> list = new ArrayList<>();
                    if (list.size() == 0) {
                        recyclerView.setNoMore(true);
                        adapter.notifyDataSetChanged();
                        return;
                    } else {
                        for (Repair report : list) {
                            if (!mDatas.contains(report)) {
                                mDatas.add(report);
                            }
                        }
                    }
                }
                handler.sendEmptyMessage(what);
            }

            @Override
            public void onError(Object o) {
                recyclerView.loadMoreComplete();
                recyclerView.refreshComplete();
                SnackbarUtil.LongSnackbar(root, (String) o
                        , SnackbarUtil.Alert).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        getListWuyeRepair(1, defaultRows, 0, REFRESH);
    }

    @Override
    public void onLoadMore() {
        getListWuyeRepair(++defaultPage, defaultRows, 0, LOADMORE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode && resultCode == RESULT_OK) {
            mDatas.get(currentPosition).setRemark(data.getStringExtra(Constant.REMARK));
            mDatas.get(currentPosition).setSatisfaction(data.getIntExtra(Constant.SATISFACTION,0));
            mDatas.get(currentPosition).setEndTime(ITools.getCurrentSimpleDateFormat(new Date()));
            adapter.notifyItemChanged(currentPosition + 1);
        }
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(RepairRecordActivity.this, R.layout.item_repair_record, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            TLog.log(TAG, "position:" + position);
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.date.setText(mDatas.get(position).getAreaName() + mDatas.get(position).getCreateTime());
            int repairType = mDatas.get(position).getRepairType();
            if (repairType == 1) {
                viewHolder.title.setText(R.string.personal_repair);
            } else if (repairType == 2) {
                viewHolder.title.setText(R.string.common_repair);
            }
            viewHolder.content.setText(mDatas.get(position).getContent());
            int userID = mDatas.get(position).getUserID();
            if (userID == app.userInfo.getId() && mDatas.get(position).getRemark() == null) { //// TODO: 2016/12/1  这里应该是受理了之后才能评价 等着改吧
                viewHolder.followRl.setVisibility(View.VISIBLE);
            } else {
                viewHolder.followRl.setVisibility(View.GONE);
            }
            String imgUrl = mDatas.get(position).getImgs();
            List<String> urlList = new ArrayList<>();
            if (imgUrl != null) {
                String imgArray[] = imgUrl.split(",");
                if (imgArray != null) {
                    for (String img : imgArray) {
                        urlList.add(img);
                    }
                }
            }
            TLog.log(TAG, "images大小:" + urlList.size());
//            if (images != null&&images.size()>0) {
            viewHolder.gridView.setNumColumns(urlList.size() >= 3 ? 3 : urlList.size());
            viewHolder.gridView.setAdapter(new GridviewAdapter(urlList, RepairRecordActivity.this,false));
//            } else {
//                viewHolder.gridView.setAdapter(null);
//            }
            final ArrayList<String> pictureUrl = new ArrayList<>();
            if (urlList != null) {
                for (String url : urlList) {
                    pictureUrl.add(Constant.TestUrl + "/" + url);
                }
            }
            viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.LIST, pictureUrl);
                    bundle.putInt(Constant.INDEX, index);
                    bundle.putSerializable(Constant.OBJ, mDatas.get(position));
                    gotoActivity(RepairRecordDetailActivity.class, false, bundle);
                }
            });
            viewHolder.followBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.KEYID, mDatas.get(position).getId());
                    bundle.putInt(Constant.STATUS, mDatas.get(position).getStatus());
                    TLog.log(TAG, "position:" + position);
//                    gotoActivity(EvaluateActivity.class, false, bundle);
                    currentPosition = position;
                    gotoActivityForResult(EvaluateActivity.class, REQUESTCODE, bundle);
                }
            });
            viewHolder.contentRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.LIST, pictureUrl);
                    bundle.putSerializable(Constant.OBJ, mDatas.get(position));
                    TLog.log(TAG, "position:" + position);
                    gotoActivity(RepairRecordDetailActivity.class, false, bundle);
                }
            });
        }


        @Override
        public int getItemCount() {
            return mDatas.size();
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.rlTile)
        RelativeLayout rlTile;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.detailRl)
        RelativeLayout detailRl;
        @BindView(R.id.gridView)
        CustomGridView gridView;
        @BindView(R.id.followBtn)
        ImageButton followBtn;
        @BindView(R.id.followRl)
        RelativeLayout followRl;
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.contentRL)
        RelativeLayout contentRL;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
