package com.merise.net.heart.bean;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by wangdawang on 2016/9/30 0030.
 */
public class RecordBean implements Serializable {


    /**
     * id : 111555
     * imageUrl : 201609/2059A0FD4D42/29194011.jpg
     * images : http://www.merise.net/photo/201609/2059A0FD4D42/29194011.jpg
     * logDate : 1475149212000
     * logDate1 : 2016-09-29 19:40:12
     * name : 米睿
     * way : 手机开门
     */

    private int id;
    private String imageUrl;
    private String images;
    private long logDate;
    private String logDate1;
    private String name;
    private String way;

    public static RecordBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), RecordBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public long getLogDate() {
        return logDate;
    }

    public void setLogDate(long logDate) {
        this.logDate = logDate;
    }

    public String getLogDate1() {
        return logDate1;
    }

    public void setLogDate1(String logDate1) {
        this.logDate1 = logDate1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }
}
