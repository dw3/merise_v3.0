package com.merise.net.heart.bean;

import java.io.Serializable;

/**
 * Created by wangdawang on 2016/10/10 0010.
 */
public class DoorBell implements Serializable {


    /**
     * id : 24914
     * imageUrl : 201608/2059A0FD4D42/31175116.jpg
     * images : http://www.merise.net/photo/201608/2059A0FD4D42/31175116.jpg
     * logDate : 1472637076000
     * logDate1 : 2016-08-31 17:51:16
     */

    private int id;
    private String imageUrl;
    private String images;
    private long logDate;
    private String logDate1;

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

    @Override
    public String toString() {
        return "DoorBell{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", images='" + images + '\'' +
                ", logDate=" + logDate +
                ", logDate1='" + logDate1 + '\'' +
                '}';
    }
}
