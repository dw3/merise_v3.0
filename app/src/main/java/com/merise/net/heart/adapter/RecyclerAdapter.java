package com.merise.net.heart.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;

import java.util.List;

/**
 * Author: zhuliyuan
 * Time: 下午 5:36
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {


    private static final String TAG ="RecyclerAdapter" ;
    private List<String> dataList;

    public RecyclerAdapter(List<String> dataList) {
        TLog.log(TAG,dataList.size()+"-----");
        for(String str:dataList){
            TLog.log(TAG,str);
        }
        this.dataList = dataList;
    }

    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MyViewHolder holder, int position) {
        holder.tv.setText(dataList.get(position));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_record_detail);
        }
    }

    @Override
    public int getItemCount() {
        return (dataList == null || dataList.size() == 0) ? 0 : dataList.size();
    }

}
