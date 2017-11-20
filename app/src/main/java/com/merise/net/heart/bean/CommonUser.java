package com.merise.net.heart.bean;

/**
 * Created by wangdawang on 2016/10/14 0014.
 */
public class CommonUser {

    /**
     * actived : 0
     * id : 901
     * imei : 86AE7AAA-6FA0-42E1-ADC8-C6A0704CBAF9
     * lastLoginTime : 2016-08-19 11:26:36
     * loginIP : 192.168.2.109
     * model : iPhone 5s
     * userID : 1242
     */

    private int actived;
    private int id;
    private String imei;
    private String lastLoginTime;
    private String loginIP;
    private String model;
    private int userID;
    private int type;// 登录终端类型（pc端，移动端）

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getActived() {
        return actived;
    }

    public void setActived(int actived) {
        this.actived = actived;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}

