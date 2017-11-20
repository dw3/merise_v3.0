package com.merise.net.heart.base;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.adapter.MenuAdapter;
import com.merise.net.heart.listener.OnBindViewHolderListener;
import com.merise.net.heart.listener.OnItemTypeListener;
import com.merise.net.heart.listener.OnItemClickListener;
import com.merise.net.heart.view.DividerItemDecoration;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/20.
 */
public abstract class BaseSwipmenuRecycleViewActivity<T> extends BaseActivity implements OnSwipeMenuItemClickListener,
        OnItemTypeListener, OnItemClickListener, OnBindViewHolderListener {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    public
    TextView titleText;
    @BindView(R.id.right)
    public
    Button right;
    @BindView(R.id.swipMenuRecyclerView)
    public
    SwipeMenuRecyclerView swipMenuRecyclerView;
    @BindView(R.id.swipRefreshLayout)
    SwipeRefreshLayout swipRefreshLayout;
    @BindView(R.id.root)
    public
    RelativeLayout root;
    public List<T> mDatas;

    public  MenuAdapter<T> adapter;
    //当前操作的位置
    public int currentPostion;

    public String deviceID;

    public static final int REQUESTCODE_MODIFY = 0x00;
    public static final int REQUESTCODE_ADD = 0x02;
    public static final String DOSTH = "doSth";

    public static final int INIT = 0x00;
    public static final int REFRESH = 0x01;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_swipmenu_recyclerview;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        right.setVisibility(View.VISIBLE);
        swipRefreshLayout.setEnabled(false);
        Drawable drawable = getResources().getDrawable(R.drawable.right_bg_selector);
        /// 这一步必须要做,否则不会显示.
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        right.setPadding(0,0,10,0);
//        right.setCompoundDrawables(null, null, drawable, null);
        right.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        swipMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        swipMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        swipMenuRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        swipMenuRecyclerView.setSwipeMenuItemClickListener(this);
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INIT:
                    TLog.log(TAG, "初始化");
                    initListAdapter();
                    initAdapter();
                    break;
                case REFRESH:
                    TLog.log(TAG, "更新");
                    initListAdapter();
                    initAdapter();
                    break;
            }
        }
    };

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        deviceID = String.valueOf(app.devices.get(app.currentIndex).getId());
    }

    public abstract void initListAdapter();

    public abstract void refreshListAdapter();

    @OnClick({R.id.back, R.id.right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.right:
                setOnRightBtnClickListner();
                break;
        }
    }

    public abstract void setOnRightBtnClickListner();

    public SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            int width = getResources().getDimensionPixelSize(R.dimen.swip_item_width_60dp);
            int imageWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
            SwipeMenuItem deleteItem = new SwipeMenuItem(getBaseContext())
                    .setWidth(width)
                    .setImage(R.drawable.delete)
                    .setHeight(height)
                    .setBackgroundDrawable(R.drawable.swip_rightbtn_click_bg_selector);
            SwipeMenuItem imageItem = new SwipeMenuItem(getBaseContext())
                    .setWidth(imageWidth)
                    .setImage(R.drawable.line)
                    .setHeight(height);
            SwipeMenuItem modifyItem = new SwipeMenuItem(getBaseContext())
                    .setWidth(width)
                    .setHeight(height)
                    .setImage(R.drawable.lock)
                    .setBackgroundDrawable(R.drawable.swip_rightbtn_click_bg_selector);
            if (viewType == MenuAdapter.ACTIVED) {
                modifyItem.setImage(R.drawable.unlock);
            } else {
                modifyItem.setImage(R.drawable.lock);
            }
            swipeRightMenu.addMenuItem(imageItem);
            swipeRightMenu.addMenuItem(modifyItem);
            swipeRightMenu.addMenuItem(imageItem);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
        }
    };

    @Override
    public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
    }

    /**
     * 初始化适配器
     */
    public void initAdapter() {
        adapter.setOnGetItemTypeListener(this);
        adapter.setOnItemClickListener(this);
        adapter.setOnBindViewHolderListenr(this);
        swipMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        swipMenuRecyclerView.setAdapter(adapter);
    }

    /**
     * 刷新item数据列表
     */
    public void refreshItem(int position) {
        adapter.notifyItemChanged(position);
        swipMenuRecyclerView.smoothCloseMenu();
    }

    /**
     * 移除item数据列表
     *
     * @param position
     */
    public void deleteItem(int position) {
        adapter.notifyItemRemoved(position);
        swipMenuRecyclerView.smoothCloseMenu();
    }
}
