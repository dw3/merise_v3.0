package com.merise.net.heart.bean;

import java.io.Serializable;

/**
 * 作者:xiangyang
 * 日期:2016/10/17
 */
public class Comment implements Serializable {

    /**
     * name : tom
     * comment : 福建大厦发几款撒谎
     */

    private String name;
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
