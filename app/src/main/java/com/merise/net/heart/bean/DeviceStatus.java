package com.merise.net.heart.bean;

import java.io.Serializable;

/**
 * 作者:xiangyang
 * 日期:2016/12/7
 */
public class DeviceStatus implements Serializable {

    /**
     * field : doorMagnet
     * id : 1
     * name : 门磁状态
     * remark : 0关，1开
     * type : 1
     */

    private String field;
    private int id;
    private String name;
    private String remark;
    private int type;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DeviceStatus{" +
                "field='" + field + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", type=" + type +
                '}';
    }
}
