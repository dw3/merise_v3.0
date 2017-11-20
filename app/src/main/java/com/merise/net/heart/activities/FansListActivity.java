package com.merise.net.heart.activities;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.merise.net.heart.R;
import com.merise.net.heart.adapter.FansAndConcernRecycleAdapter;
import com.merise.net.heart.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/12 0012.
 */

public class FansListActivity extends BaseActivity {


    @BindView(R.id.fans_recyclerview)
    RecyclerView fansRecyclerview;
    private List<String> mNameDatas = new ArrayList<>();
    private FansAndConcernRecycleAdapter noticeRecycleAdapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_fans);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        for (int i = 0; i < 10; i++) {
            mNameDatas.add(""+i);
        }
        noticeRecycleAdapter = new FansAndConcernRecycleAdapter(this, mNameDatas);
        fansRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        fansRecyclerview.setAdapter(noticeRecycleAdapter);
        fansRecyclerview.setItemAnimator(new DefaultItemAnimator());
        initEvent();
    }

    /**
     * 点击事件
     */
    private void initEvent() {
        noticeRecycleAdapter.setOnItemClickLitener(new FansAndConcernRecycleAdapter.OnItemClickLitener() {
            @Override
            public void OnItemClickLitener(View view, int position) {
                Toast.makeText(FansListActivity.this, "position---" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

