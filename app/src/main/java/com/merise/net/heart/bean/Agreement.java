package com.merise.net.heart.bean;

/**
 * Created by wangdawang on 2016/10/21 0021.
 */
public class Agreement {

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Agreement{" +
                "content='" + content + '\'' +
                '}';
    }

    private String content;

}
