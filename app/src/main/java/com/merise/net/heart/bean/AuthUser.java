package com.merise.net.heart.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/28.
 */
public class AuthUser implements Serializable {
    private int id;
    private String name; //用戶名
    private String nick; //昵称
    private boolean actived;//是否有权限

    public boolean isActived() {
        return actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "actived=" + actived +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", nick='" + nick + '\'' +
                '}';
    }
}
