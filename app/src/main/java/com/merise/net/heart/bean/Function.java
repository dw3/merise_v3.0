package com.merise.net.heart.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/11.
 */
public class Function implements Serializable{

    private String code;
    private int id;
    private int isDefault;
    private String name;
    private int scope;
    private int sort;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "Function{" +
                "code='" + code + '\'' +
                ", id=" + id +
                ", isDefault=" + isDefault +
                ", name='" + name + '\'' +
                ", scope=" + scope +
                ", sort=" + sort +
                '}';
    }
}
