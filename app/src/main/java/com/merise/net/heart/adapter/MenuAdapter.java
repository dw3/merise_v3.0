/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.merise.net.heart.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.application.XYApplication;
import com.merise.net.heart.listener.OnBindViewHolderListener;
import com.merise.net.heart.listener.OnItemTypeListener;
import com.merise.net.heart.listener.OnItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;


public class MenuAdapter<T> extends SwipeMenuAdapter<MenuAdapter.DefaultViewHolder> {

    private static final java.lang.String TAG = "MenuAdapter";
    private List<T> mDatas;
    private String type;
    private OnItemClickListener mOnItemClickListener;

    public static final int ACTIVED = 1;
    public static final int UNACTIVED = 0;
    private OnItemTypeListener onItemTypeListener;
    private OnBindViewHolderListener onBindViewHolderListener;

    public MenuAdapter(List<T> mDatas, Class<T> clazz) {
        this.mDatas = mDatas;
        type = clazz.getSimpleName();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnGetItemTypeListener(OnItemTypeListener onItemTypeListener) {
        this.onItemTypeListener = onItemTypeListener;
    }

    public void setOnBindViewHolderListenr(OnBindViewHolderListener onBindViewHolderListener) {
        this.onBindViewHolderListener = onBindViewHolderListener;
    }

    @Override
    public int getItemCount() {
        TLog.log(TAG, "getItemCount");
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        TLog.log(TAG, "getItemViewType");
        return onItemTypeListener.getItemType(position);
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        TLog.log(TAG, "onCreateContentView");
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swiprecycler_view, parent, false);
    }

    @Override
    public MenuAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        TLog.log(TAG, "onCompatCreateViewHolder");
        return new DefaultViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(MenuAdapter.DefaultViewHolder holder, int position) {
        TLog.log(TAG, "onBindViewHolder");
        onBindViewHolderListener.onBindViewHolder(holder, position);
        holder.setOnItemClickListener(mOnItemClickListener);
    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        OnItemClickListener mOnItemClickListener;
        ImageButton imageButton;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.lv_tv);
            imageButton = (ImageButton) itemView.findViewById(R.id.set);
            imageButton.setOnClickListener(this);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setTitle(String title) {
            this.tvTitle.setText(title);
        }

        public void setTextColor(boolean actived) {
            TLog.log("setTextColor", "actived:" + actived);
            if (actived) {
                this.tvTitle.setTextColor(XYApplication.getInstance().getResources().getColor(R.color.deviceNameTextColor));
            } else {
                this.tvTitle.setTextColor(XYApplication.getInstance().getResources().getColor(R.color.red));
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}

