package com.merise.net.heart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merise.net.heart.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangdawang on 2016/12/10 0010.
 */

public class FansAndConcernRecycleAdapter extends RecyclerView.Adapter<FansAndConcernRecycleAdapter.FansViewHolder> {

    private OnItemClickLitener myOnItemClickLitener;
    private List<String> mTextDatas;
    private LayoutInflater mInflater;

    public FansAndConcernRecycleAdapter(Context context, List<String> mTextDatas) {
        this.mTextDatas = mTextDatas;
        this.mInflater = LayoutInflater.from(context);
    }
    /*
  点击事件接口
  */
    public interface OnItemClickLitener {
        void OnItemClickLitener(View view, int position);
    }

    /*
    对外提供点击事件监听方法，方法中传入以便接口实现
     */
    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.myOnItemClickLitener = onItemClickLitener;
    }


    //---------------------------------------------------------------------------------------------------------------------

    //加载界面
    @Override
    public FansViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FansViewHolder myViewHolder = new FansViewHolder(mInflater.inflate(R.layout.activity_fans_item, parent, false));
        return myViewHolder;
    }

    //赋予数据施加监听
    @Override
    public void onBindViewHolder(final FansViewHolder holder, int position) {
        holder.nickNameTv.setText(mTextDatas.get(position));
        //如果外部设置了监听器
        if (myOnItemClickLitener != null) {
            //为ItemView设置监听器
            holder.wholeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取当前点击位置的position
                    int pos = holder.getLayoutPosition();
                    //再次回调，实现接口，这里处理外部重写OnItemClickLitener的具体逻辑
                    myOnItemClickLitener.OnItemClickLitener(v,pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mTextDatas.size();
    }


    //---------------------------------------------------------------------------------------------------------------------

    /*
    继承viewholder自动关联复用类
     */
    static class FansViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.head_information_image)
        ImageView headInformationImage;
        @BindView(R.id.nickName_tv)
        TextView nickNameTv;
        @BindView(R.id.mood_tv)
        TextView moodTv;
        @BindView(R.id.is_fans)
        ImageButton isFans;
        @BindView(R.id.whole_item)
        RelativeLayout wholeItem;

        public FansViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
