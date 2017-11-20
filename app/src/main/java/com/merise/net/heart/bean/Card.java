package com.merise.net.heart.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/20.
 */
public class Card implements Serializable {

    private String keyNick;
    private String flag;
    private int id;
    private boolean actived;

    public String getKeyNick() {
        return keyNick;
    }

    public void setKeyNick(String keyNick) {
        this.keyNick = keyNick;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActived() {
        return actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    @Override
    public String toString() {
        return "Card{" +
                "actived=" + actived +
                ", keyNick='" + keyNick + '\'' +
                ", flag='" + flag + '\'' +
                ", id=" + id +
                '}';
    }
}
