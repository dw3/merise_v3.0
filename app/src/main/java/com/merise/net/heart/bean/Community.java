package com.merise.net.heart.bean;

import java.io.Serializable;

/**
 * 作者:xiangyang
 * 日期:2016/10/25
 */
public class Community implements Serializable {

    private int id;
    private String name;

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
}
