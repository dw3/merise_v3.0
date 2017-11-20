package com.merise.net.heart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.framewok.util.TLog;
import com.android.framewok.util.Util;
import com.bumptech.glide.Glide;
import com.merise.net.heart.R;
import com.merise.net.heart.bean.ImgsBean;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.ITools;

import java.util.ArrayList;
import java.util.List;

public class GridviewAdapter extends BaseAdapter {
    private List<String> images;
    private Context context;
    public static final String DEFAULTURL = "defaulturl";//默认的一张图片的地址

    private boolean imageFromLocal;//true 为选择本地图片，false为服务器返回的图片

    public GridviewAdapter(List<String> imgs, Context context, boolean imageFromLocal) {
        images = new ArrayList<>();
        this.images = imgs;
        this.context = context;
        this.imageFromLocal = imageFromLocal;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridviewHolder holder;
        if (convertView == null) {
            holder = new GridviewHolder();
            convertView = View.inflate(context, R.layout.item_image, null);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (GridviewHolder) convertView.getTag();
        }
        ////动态设置GridView图片的宽高,间距是1，每行两列，计算每个图片的宽度，高度与宽度一致
        int f = images.size() >= 3 ? 4 : images.size() + 1;
        int b = images.size() >= 3 ? 3 : images.size();
//            int width = (screenWidth - (f * ITools.Dp2Px(context, 1))) / b;
        int width = Util.getScreenWidth(context) / b;
        if (imageFromLocal) {
//            width = Util.getScreenWidth(context) / 3;
            width = ( Util.getScreenWidth(context) - (5 * ITools.Dp2Px(context, 5))) / 3;
        }
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, width);
        holder.image.setLayoutParams(param);
        String path = images.get(position);
        if (!imageFromLocal) {
            path = Constant.TestUrl + "/" + path;
        }
        if (path.equals(DEFAULTURL)) {
            Glide.with(context)
                    .load(R.drawable.photo)
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.default_error)
                    .centerCrop()
                    .crossFade()
                    .into(holder.image);
        } else {
            Glide.with(context)
                    .load(path)
                    .placeholder(R.drawable.default_error)
                    .into(holder.image);
        }
        return convertView;
    }

    static class GridviewHolder {
        ImageView image;
    }
}


