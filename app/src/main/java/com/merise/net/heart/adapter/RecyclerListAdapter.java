/*
 * Copyright (C) 2015 Paul Burke
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

import android.content.Context;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.bean.DeviceStatus;
import com.merise.net.heart.bean.Function;
import com.merise.net.heart.listener.OnItemClickListener;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.view.helper.ItemTouchHelperAdapter;
import com.merise.net.heart.view.helper.ItemTouchHelperViewHolder;
import com.merise.net.heart.view.helper.OnStartDragListener;

import java.util.ArrayList;
import java.util.List;

import static com.merise.net.heart.application.XYApplication.currentIndex;
import static com.merise.net.heart.application.XYApplication.devices;


/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchHelperAdapter {

    private List<Function> mItems = new ArrayList<>();
    private List<Function> myFuns = new ArrayList<>();
    private List<Function> otherFuns = new ArrayList<>();
    private Context context;
    private OnStartDragListener mDragStartListener;
    private OnItemClickListener onItemClickListener;

    private View root;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecyclerListAdapter(Context context, OnStartDragListener dragStartListener, List<Function> myFuns, List<Function> otherFuns, View view) {
        this.context = context;
        mDragStartListener = dragStartListener;
        this.myFuns = myFuns;
        this.otherFuns = otherFuns;
        this.root = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1_main_item, parent, false);
            MyChannelHeaderViewHolder itemViewHolder = new MyChannelHeaderViewHolder(view);
            return itemViewHolder;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2_main_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || (position == myFuns.size() + 1)) {
            return 0;
        }
        return 1;
    }

    class MyChannelHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView edit;

        public MyChannelHeaderViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv);
            edit = (TextView) itemView.findViewById(R.id.tv_btn_edit);
        }
    }

    private void initImage(ItemViewHolder holder, int id) {
        List<DeviceStatus> statuses = devices.get(currentIndex).getStatus();
        boolean online = false;
        for (DeviceStatus status : statuses) {
            if (status.getField().equals(Constant.ONLINE)) {
                int type = status.getType();
                if (type == 1) {
                    online = true;
                } else {
                    online = false;
                }
                break;
            }
        }
        switch (id) {
            case 1:
                if (online)
                    holder.handleView.setImageResource(R.drawable.open_ic);
                else
                    holder.handleView.setImageResource(R.drawable.close);
                break;
            case 2:
                if (online)
                    holder.handleView.setImageResource(R.drawable.video_ic);
                else
                    holder.handleView.setImageResource(R.drawable.video);
                break;
            case 3:
                if (online)
                    holder.handleView.setImageResource(R.drawable.phone_one_ic);
                else
                    holder.handleView.setImageResource(R.drawable.call);
                break;
            case 4:
                if (online)
                    holder.handleView.setImageResource(R.drawable.defense_ic);
                else
                    holder.handleView.setImageResource(R.drawable.defense);
                break;
            case 5:
                if (online)
                    holder.handleView.setImageResource(R.drawable.home_ic);
                else
                    holder.handleView.setImageResource(R.drawable.home);
                break;
            case 6:
                if (online)
                    holder.handleView.setImageResource(R.drawable.phone_ic);
                else
                    holder.handleView.setImageResource(R.drawable.phone);
                break;
            case 7:
                if (online)
                    holder.handleView.setImageResource(R.drawable.pro_ic);
                else
                    holder.handleView.setImageResource(R.drawable.pro);
                break;
            case 8:
                if (online)
                    holder.handleView.setImageResource(R.drawable.door_ic);
                else
                    holder.handleView.setImageResource(R.drawable.door);
                break;
            case 9:
                if (online)
                    holder.handleView.setImageResource(R.drawable.record_ic);
                else
                    holder.handleView.setImageResource(R.drawable.record);
                break;
            case 10:
                if (online)
                    holder.handleView.setImageResource(R.drawable.finger_ic);
                else
                    holder.handleView.setImageResource(R.drawable.finger);
                break;
            case 11:
                if (online)
                    holder.handleView.setImageResource(R.drawable.card_ic);
                else
                    holder.handleView.setImageResource(R.drawable.card);
                break;
            case 12:
                if (online)
                    holder.handleView.setImageResource(R.drawable.user_ic);
                else
                    holder.handleView.setImageResource(R.drawable.user);
                break;
            case 13:
                if (online)
                    holder.handleView.setImageResource(R.drawable.ring_ic);
                else
                    holder.handleView.setImageResource(R.drawable.ring);
                break;
            case 14:
                if (online)
                    holder.handleView.setImageResource(R.drawable.clock_ic);
                else
                    holder.handleView.setImageResource(R.drawable.clock);
                break;
            case 15:
                if (online)
                    holder.handleView.setImageResource(R.drawable.modify_ic);
                else
                    holder.handleView.setImageResource(R.drawable.modify);
                break;
            case 16:
                if (online)
                    holder.handleView.setImageResource(R.drawable.codes);
                else
                    holder.handleView.setImageResource(R.drawable.codes_d);
                break;
            case 17:
                if (online)
                    holder.handleView.setImageResource(R.drawable.shake_b);
                else
                    holder.handleView.setImageResource(R.drawable.shake_d);
                break;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position == 0) {
            ((MyChannelHeaderViewHolder) viewHolder).title.setText(R.string.my_application);
        }
        if (position == myFuns.size() + 1) {
            ((MyChannelHeaderViewHolder) viewHolder).title.setText(R.string.all_application);
        }
        if (position != 0 && position != (myFuns.size() + 1)) {
            final ItemViewHolder holder = (ItemViewHolder) viewHolder;
            if (position > myFuns.size() + 1) {
                holder.textView.setText(otherFuns.get(position - 2 - myFuns.size()).getName());
                int id = otherFuns.get(position - 2 - myFuns.size()).getId();
                initImage(holder, id);
            } else {
                holder.textView.setText(myFuns.get(position - 1).getName());
                int id = myFuns.get(position - 1).getId();
                initImage(holder, id);
            }
            holder.setOnItemClickListener(onItemClickListener);
            holder.setmDragStartListener(mDragStartListener);
        }
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition > 0 && fromPosition < myFuns.size() + 1 && toPosition > 0 && toPosition < myFuns.size() + 1) {
            Function item = myFuns.get(fromPosition - 1);
            myFuns.remove(fromPosition - 1);
            myFuns.add(toPosition - 1, item);
            notifyItemMoved(fromPosition, toPosition);
        }
        if (fromPosition > 0 && fromPosition <= myFuns.size() && toPosition >= myFuns.size() + 1) {
            Function item = myFuns.get(fromPosition - 1);
            myFuns.remove(fromPosition - 1);
            otherFuns.add(toPosition - 2 - myFuns.size(), item);
            notifyItemMoved(fromPosition, toPosition);
        }
        if (fromPosition > myFuns.size() + 1 && toPosition > 0 && toPosition <= myFuns.size() + 1) {
            if (myFuns.size() >= 4) {
                String message = context.getResources().getString(R.string.maxItem);
                SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Warning).show();
                return true;
            }
            Function item = otherFuns.get(fromPosition - 2 - myFuns.size());
            otherFuns.remove(fromPosition - 2 - myFuns.size());
            myFuns.add(toPosition - 1, item);
            notifyItemMoved(fromPosition, toPosition);
        }
        if (fromPosition > myFuns.size() + 1 && toPosition > myFuns.size() + 1) {
            Function item = otherFuns.get(fromPosition - 2 - myFuns.size());
            otherFuns.remove(fromPosition - 2 - myFuns.size());
            otherFuns.add(toPosition - 2 - myFuns.size(), item);
            notifyItemMoved(fromPosition, toPosition);
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return myFuns.size() + 2 + otherFuns.size();
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder, View.OnClickListener {

        public final TextView textView;
        public final ImageView handleView;
        private LinearLayout itemRoot;
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        private OnStartDragListener mDragStartListener;

        public void setmDragStartListener(OnStartDragListener mDragStartListener) {
            this.mDragStartListener = mDragStartListener;
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.name);
            handleView = (ImageView) itemView.findViewById(R.id.image);
            itemRoot = (LinearLayout) itemView.findViewById(R.id.item_root);
            itemRoot.setOnClickListener(this);
        }

        @Override
        public void onItemSelected() {
            TLog.log("RecyclerListAdapter", "onItemSelected...");
            if (mDragStartListener != null)
                mDragStartListener.onStartDrag(this);
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            TLog.log("RecyclerListAdapter", "onItemClear...");
            itemView.setBackgroundResource(R.drawable.item_click_bg_selector);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
