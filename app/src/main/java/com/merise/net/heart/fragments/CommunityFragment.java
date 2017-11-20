package com.merise.net.heart.fragments;

import android.content.pm.PackageStats;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.merise.net.heart.R;
import com.merise.net.heart.activities.CommunityActivity;
import com.merise.net.heart.activities.EvaluateActivity;
import com.merise.net.heart.activities.HouseRentActivity;
import com.merise.net.heart.activities.Lock9Activity;
import com.merise.net.heart.activities.PropertyAnnouncementActivity;
import com.merise.net.heart.activities.RepairRecordActivity;
import com.merise.net.heart.activities.RepairRecordDetailActivity;
import com.merise.net.heart.activities.RepairThePropertyActivity;
import com.merise.net.heart.activities.UnlockActivity;
import com.merise.net.heart.base.MyBaseFragment;
import com.merise.net.heart.listener.OnItemClickListener;
import com.merise.net.heart.utils.ITools;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.view.DividerGridItemDecoration;
import com.merise.net.heart.view.HomeSlideShowView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/6.
 */
public class CommunityFragment extends MyBaseFragment implements OnItemClickListener {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.banner)
    HomeSlideShowView banner;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.root)
    CoordinatorLayout root;

    private String[] mDatas;
    private int[] images;
    private MyRecycleViewAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
        back.setVisibility(View.GONE);
        titleText.setText(R.string.defence_title);
        banner.refreshView(null);
    }

    @Override
    public void initData() {
        mDatas = getResources().getStringArray(R.array.communityArray);
        images = new int[]{R.drawable.notice, R.drawable.news,
                R.drawable.repair, R.drawable.house_rent,
                R.drawable.article_sell, R.drawable.add_content
        };
        adapter = new MyRecycleViewAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
    }

    @Override
    public void onItemClick(View v, int position) {
//        SnackbarUtil.LongSnackbar(root, position + "", SnackbarUtil.Confirm).show();
        if (!app.isLogin && position != 1) {
            ITools.showLoginAlert(getActivity());
        } else {
            switch (position) {
                case 0:
                    gotoActivity(PropertyAnnouncementActivity.class);
                    break;
                case 1:
                    gotoActivity(CommunityActivity.class);
                    break;
                case 2:
                    gotoActivity(RepairThePropertyActivity.class);
                    break;
                case 3:
                    gotoActivity(HouseRentActivity.class);
                    break;
                case 4:
//                    gotoActivity(UnlockActivity.class);
                    break;
                case 5:
                    final AlertView alertView = new AlertView("提示", "更多服务暂未开放,敬请关注", "确定", null, null, getContext(), AlertView.Style.Alert, null);
                    alertView.show();
                    break;
            }
        }
    }

    private class MyRecycleViewAdapter extends RecyclerView.Adapter {
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getActivity(), R.layout.item2_main_item, null);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).image.setImageResource(images[position]);
            ((ViewHolder) holder).name.setText(mDatas[position]);
            ((ViewHolder) holder).setOnItemClickListener(onItemClickListener);
        }

        @Override
        public int getItemCount() {
            return images.length;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView name;
        private LinearLayout itemRoot;
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            itemRoot = (LinearLayout) itemView.findViewById(R.id.item_root);
            itemRoot.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
