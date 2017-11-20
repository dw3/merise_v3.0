package com.merise.net.heart.bean;

import java.io.Serializable;

/**
 * 作者:xiangyang
 * 日期:2016/11/29    点赞人
 */
public class DZUser implements Serializable {
    /**
     * dzID : 1242
     * dzName : 1327
     */

    private int dzID;
    private String dzName;

    public int getDzID() {
        return dzID;
    }

    public void setDzID(int dzID) {
        this.dzID = dzID;
    }

    public String getDzName() {
        return dzName;
    }

    public void setDzName(String dzName) {
        this.dzName = dzName;
    }

    @Override
    public String toString() {
        return "DZUser{" +
                "dzID=" + dzID +
                ", dzName='" + dzName + '\'' +
                '}';
    }
}
