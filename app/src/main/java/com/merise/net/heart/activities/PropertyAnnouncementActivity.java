package com.merise.net.heart.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.base.CommonWebviewActivity;
import com.merise.net.heart.bean.Announcement;
import com.merise.net.heart.listener.OnItemClickListener;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/8.
 * 物业公告
 */
public class PropertyAnnouncementActivity extends BaseActivity implements OnItemClickListener, XRecyclerView.LoadingListener {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.recyclerView)
    XRecyclerView recyclerView;
    @BindView(R.id.root)
    RelativeLayout root;

    private static final int REFRESH = 0x00;
    private static final int LOADMORE = 0x01;
    private List<Announcement> mDatas = new ArrayList<>();
    private PropertyRecycleViewAdapter adapter;

    private int defaultPage = 1;
    private static final int deaultRows = 20;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_property_announcement;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.property_announcement);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLoadingListener(this);
    }

    @Override
    public void initData() {
        getPropertyAnnouncement(defaultPage, deaultRows, 1, REFRESH);
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
                        adapter = new PropertyRecycleViewAdapter();
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(PropertyAnnouncementActivity.this);
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


    private void getPropertyAnnouncement(int page, int rows, int type, final int what) {
        AppOperate.getPropertyAnnouncement(page, rows, type, this, new Report() {
            @Override
            public void onSucces(Object o) {
                if (what == REFRESH) {
                    mDatas.clear();
                    mDatas.addAll((List<Announcement>) o);
                    recyclerView.setNoMore(false);
                } else {
                    List<Announcement> list = (List<Announcement>) o;
                    if (list.size() == 0) {
                        recyclerView.setNoMore(true);
                        adapter.notifyDataSetChanged();
                        return;
                    } else {
                        for (Announcement a : list) {
                            if (!mDatas.contains(a)) {
                                mDatas.add(a);
                            }
                        }
                    }
                }
                handler.sendEmptyMessage(what);
            }

            @Override
            public void onError(Object o) {
                recyclerView.reset();
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }

    @Override
    public void onItemClick(View v, int position) {
//        SnackbarUtil.LongSnackbar(root, position + "", SnackbarUtil.Confirm).show();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY, mDatas.get(position).getHtml());
//        String html = mDatas.get(position).getHtml();
        gotoActivity(CommonWebviewActivity.class);
    }

    @Override
    public void onRefresh() {
        getPropertyAnnouncement(1, deaultRows, 1, REFRESH);
    }

    @Override
    public void onLoadMore() {
        getPropertyAnnouncement(++defaultPage, deaultRows, 1, LOADMORE);
    }

    private class PropertyRecycleViewAdapter extends RecyclerView.Adapter {
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TLog.log(TAG, "onCreateViewHolder");
            View view = View.inflate(PropertyAnnouncementActivity.this, R.layout.item_property_announcement_activity, null);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TLog.log(TAG, "onBindViewHolder");
            ((ViewHolder) holder).date.setText(mDatas.get(position).getCreateTime());
            ((ViewHolder) holder).title.setText(mDatas.get(position).getTitle());
            ((ViewHolder) holder).imgAd.setBackgroundResource(R.drawable.notice_label);
            ((ViewHolder) holder).content.setText(mDatas.get(position).getContent());
            ((ViewHolder) holder).setOnItemClickListener(onItemClickListener);
        }

        @Override
        public int getItemCount() {
            TLog.log(TAG, "getItemCount" + mDatas.size());
            return mDatas.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date;
        TextView title;
        TextView imgAd;
        TextView content;
        CardView cardView;
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            title = (TextView) itemView.findViewById(R.id.title);
            imgAd = (TextView) itemView.findViewById(R.id.imgAd);
            content = (TextView) itemView.findViewById(R.id.content);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition() - 1);
            }
        }
    }
}
