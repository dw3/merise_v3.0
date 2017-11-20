package com.merise.net.heart.bean;

/**
 * Created by wangdawang on 2016/10/9 0009.
 */
public class RecordDetail {

    private String imageUrl;
    private String images;
    private String logDate;
    private String logDate1;

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

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
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
        return "RecordDetail{" +
                "imageUrl='" + imageUrl + '\'' +
                ", images='" + images + '\'' +
                ", logDate='" + logDate + '\'' +
                ", logDate1='" + logDate1 + '\'' +
                '}';
    }
}
