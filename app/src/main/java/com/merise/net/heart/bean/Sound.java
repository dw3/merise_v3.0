package com.merise.net.heart.bean;

/**
 * Created by Administrator on 2016/9/29.
 */
public class Sound {

    private int volume;
    private int sound;
    private int openDoorWayID;
    private String name;
    private String serverAddress;
    private int adminID;
    private String admin;
    private int typeID;
    private int id;
    private String sn;
    private boolean isfortify;
    private boolean actived;

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public int getOpenDoorWayID() {
        return openDoorWayID;
    }

    public void setOpenDoorWayID(int openDoorWayID) {
        this.openDoorWayID = openDoorWayID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public boolean isIsfortify() {
        return isfortify;
    }

    public void setIsfortify(boolean isfortify) {
        this.isfortify = isfortify;
    }

    public boolean isActived() {
        return actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    @Override
    public String toString() {
        return "Sound{" +
                "actived=" + actived +
                ", volume=" + volume +
                ", sound=" + sound +
                ", openDoorWayID=" + openDoorWayID +
                ", name='" + name + '\'' +
                ", serverAddress='" + serverAddress + '\'' +
                ", adminID=" + adminID +
                ", admin='" + admin + '\'' +
                ", typeID=" + typeID +
                ", id=" + id +
                ", sn='" + sn + '\'' +
                ", isfortify=" + isfortify +
                '}';
    }
}
