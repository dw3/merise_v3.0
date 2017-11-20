package com.merise.net.heart.bean;

/**
 * Created by wangdawang on 2016/10/19 0019.
 */
public class SendMessageBean {

    /**
     * feedBackID : 111
     */

    private int feedBackID;

    public int getFeedBackID() {
        return feedBackID;
    }

    public void setFeedBackID(int feedBackID) {
        this.feedBackID = feedBackID;
    }

    @Override
    public String toString() {
        return "SendMessageBean{" +
                "feedBackID=" + feedBackID +
                '}';
    }
}
