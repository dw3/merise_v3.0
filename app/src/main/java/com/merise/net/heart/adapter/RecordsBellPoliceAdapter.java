package com.merise.net.heart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.merise.net.heart.R;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.bean.DoorBell;

import java.util.List;

public class RecordsBellPoliceAdapter extends BaseAdapter {
    private String type;
    public Context mContext;
    private static final String TAG = "RecordFragment";

    public List<DoorBell> mListItems;


    public RecordsBellPoliceAdapter(String type, Context context, List<DoorBell> mListItems) {
        this.type = type;
        mContext = context;
        this.mListItems = mListItems;
    }


    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext,
                    R.layout.record_door_item, null);
            holder.date = (TextView) convertView
                    .findViewById(R.id.door_date_tv);
            holder.type = (TextView) convertView
                    .findViewById(R.id.door_type_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        if (type.equals(Const.DOORBELL)) {
            holder.date.setText(mListItems.get(position).getLogDate1());
            holder.type.setText("有人按了门铃");
        } else if (type.equals(Const.DOORSENSOR)) {
//            holder.name.setText(mListItems.get(position).getName());
            holder.date.setText(mListItems.get(position).getLogDate1());
            // holder.type.setText(mListItems.get(position).way);
            holder.type.setText("门磁已被打开");
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView date;
        TextView type;
    }
}