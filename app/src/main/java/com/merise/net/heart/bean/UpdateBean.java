package com.merise.net.heart.bean;

/**
 * Created by wangdawang on 2016/10/21 0021.
 */
public class UpdateBean {

    /**
     * name : V1.2.2
     * url : /download/android.apk
     * versionCode : 15
     */

    private String name;
    private String url;
    private String versionCode;


    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
