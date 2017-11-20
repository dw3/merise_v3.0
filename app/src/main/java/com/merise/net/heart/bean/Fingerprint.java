package com.merise.net.heart.bean;

/**
 * Created by Administrator on 2016/9/26.
 */
public class Fingerprint {


    private String flag;
    private int id;
    private String fingerprintNick;
    private boolean actived;

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

    public String getFingerprintNick() {
        return fingerprintNick;
    }

    public void setFingerprintNick(String fingerprintNick) {
        this.fingerprintNick = fingerprintNick;
    }

    public boolean isActived() {
        return actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    @Override
    public String toString() {
        return "Fingerprint{" +
                "actived=" + actived +
                ", flag='" + flag + '\'' +
                ", id=" + id +
                ", fingerprintNick='" + fingerprintNick + '\'' +
                '}';
    }
}
