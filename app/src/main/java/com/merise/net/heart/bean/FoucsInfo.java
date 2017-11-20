package com.merise.net.heart.bean;

import java.io.Serializable;

/**
 * 作者:xiangyang
 * 日期:2016/11/29
 */
public class FoucsInfo implements Serializable {

    private String isFocus;
    private String nickName;
    private String sign;
    private int focus;
    private int fans;

    public String getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(String isFocus) {
        this.isFocus = isFocus;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }
}
