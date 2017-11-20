package com.merise.net.heart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.Rent;
import com.merise.net.heart.bean.Repair;
import com.merise.net.heart.listener.OnItemClickListener;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.view.ClearEditView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:xiangyang
 * 日期:2016/10/25
 */
public class HouseRentActivity extends BaseActivity implements OnItemClickListener, XRecyclerView.LoadingListener {
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
    @BindView(R.id.search_btn)
    ImageButton searchBtn;
    @BindView(R.id.pub_line)
    ImageView pubLine;
    @BindView(R.id.searchEV)
    ClearEditView searchEV;
    @BindView(R.id.searchRL)
    RelativeLayout searchRL;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.emptyView)
    TextView emptyView;
    private List<Rent> mDatas = new ArrayList<>();
    private MyRecyclerViewAdapter adapter;
    private static final int REQUESTCODE = 0x01;

    private int defaultPage = 1;
    private static final int defaultRows = 20;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_house_rent;
    }

    private static final int REFRESH = 0x01;
    private static final int LOADMORE = 0x02;

    private SearchViewHolder searchViewHolder;

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.rent_room);
        int left = (int) getResources().getDimension(R.dimen.title_text_padding_left);
        right.setPadding(left, 0, 0, 0);
        right.setVisibility(View.VISIBLE);
        right.setText(R.string.rent);
        right.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLoadingListener(this);
//        recyclerView.setEmptyView(emptyView);
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(-1)) {
                    TLog.log(TAG, "已经到顶了");
                    searchRL.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() > 0) {
                    if (newState == recyclerView.SCROLL_STATE_DRAGGING) {
                        searchRL.setVisibility(View.GONE);
                    }
                    if (newState == recyclerView.SCROLL_STATE_SETTLING) {
                        searchRL.setVisibility(View.GONE);
                    }
                    if (newState == recyclerView.SCROLL_STATE_IDLE) {
                        searchRL.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        getHouseLease(1, defaultRows, "", REFRESH);
    }

    /**
     * 清空搜索框
     */
    private void clearEditView() {
        searchEV.setText("");
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            clearEditView();
            switch (msg.what) {
                case REFRESH:
                    defaultPage = 1;
                    if (adapter == null) {
                        adapter = new MyRecyclerViewAdapter();
                        adapter.setOnItemClickListener(HouseRentActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                        recyclerView.setNoMore(false);
                        recyclerView.refreshComplete();
                    }
                    break;
                case LOADMORE:
                    adapter.notifyDataSetChanged();
                    recyclerView.loadMoreComplete();
                    break;
            }
            if(adapter.getItemCount()==1){
                emptyView.setVisibility(View.VISIBLE);
            }else{
                emptyView.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 获取租赁信息
     *
     * @param page
     * @param rows
     * @param address
     */
    private void getHouseLease(int page, int rows, String address, final int what) {
        AppOperate.getHouseLease(page, rows, address, this, new Report() {
            @Override
            public void onSucces(Object o) {
                if (what == REFRESH) {
                    mDatas.clear();
                    mDatas.addAll((List<Rent>) o);
                }else{
                    List<Rent> list = (List<Rent>) o;
                    if(list.size()==0){
                        recyclerView.setNoMore(true);
                        adapter.notifyDataSetChanged();
                        return;
                    }else {
                        for (Rent rent : list) {
                            if (!mDatas.contains(rent)) {
                                mDatas.add(rent);
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

    @OnClick({R.id.back, R.id.search_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.search_btn:
                String condition = searchEV.getText().toString().trim();
                getHouseLease(1, defaultRows, condition, REFRESH);
                break;
            case R.id.right:
                gotoActivityForResult(RentActivity.class, REQUESTCODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getHouseLease(1, defaultRows, "", REFRESH);
                }
            });
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        TLog.log(TAG, position + "" + searchViewHolder.getText());
        getHouseLease(1, defaultRows, searchViewHolder.getText(), REFRESH);
    }

    @Override
    public void onRefresh() {
        getHouseLease(1, defaultRows, "", REFRESH);
    }

    @Override
    public void onLoadMore() {
        getHouseLease(++defaultPage, defaultRows, "", LOADMORE);
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter {
        private OnItemClickListener onItemClickListener;

        public OnItemClickListener getOnItemClickListener() {
            return onItemClickListener;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(HouseRentActivity.this, R.layout.item_rent, null);
            if (viewType == 0) {
                view = View.inflate(HouseRentActivity.this, R.layout.layout_search_view, null);
                return new SearchViewHolder(view);
            }
            return new ViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 0;
            }
            return 1;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (position == 0) {
                searchViewHolder = (SearchViewHolder) holder;
                searchViewHolder.setOnItemClickListener(onItemClickListener);
            }
            if (position > 0) {
                ((ViewHolder) holder).address.setText(mDatas.get(position - 1).getAddress());
                ((ViewHolder) holder).date.setText(mDatas.get(position - 1).getCreateTime());
                String message = "<font color=\"#e43e3d\"><big><big><big>" + mDatas.get(position - 1).getPrice() + "</big></big></big></font>";
                ((ViewHolder) holder).price.setText(Html.fromHtml(message));
                String url = mDatas.get(position - 1).getUrl();
                String[] imgs;
                if (url != null) {
                    TLog.log(TAG, "position:" + position);
                    imgs = url.split(",");
                    Glide.with(getBaseContext())
                            .load(Constant.TestUrl + "/" + imgs[0])
                            .placeholder(R.drawable.default_error)
                            .into(((ViewHolder) holder).roomImg);
                }
                ((ViewHolder) holder).rootRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Rent rent = mDatas.get(position - 1);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.OBJ, rent);
                        gotoActivity(RentDetailActivity.class, false, bundle);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size() + 1;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.roomImg)
        ImageView roomImg;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.rootRL)
        RelativeLayout rootRL;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.search_btn)
        ImageButton searchBtn;
        @BindView(R.id.searchEV)
        ClearEditView searchEV;
        OnItemClickListener onItemClickListener;


        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            searchBtn.setOnClickListener(this);
        }

        public String getText() {
            return searchEV.getText().toString().trim();
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
