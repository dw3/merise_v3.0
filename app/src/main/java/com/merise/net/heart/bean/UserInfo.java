package com.merise.net.heart.bean;

/**
 * Created by Administrator on 2016/9/8.
 */
public class UserInfo {
    private int id;
    private int userID;
    private String faceImg;
    private String hxName;
    private String registrationID;
    private String name;
    private String functionCustom;
    private int type;
    private String mobile;
    /**
     * roles : XY
     * roleIDs : 25
     */

    private String roles;
    private String roleIDs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFaceImg() {
        return faceImg;
    }

    public void setFaceImg(String faceImg) {
        this.faceImg = faceImg;
    }

    public String getHxName() {
        return hxName;
    }

    public void setHxName(String hxName) {
        this.hxName = hxName;
    }

    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunctionCustom() {
        return functionCustom;
    }

    public void setFunctionCustom(String functionCustom) {
        this.functionCustom = functionCustom;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "faceImg='" + faceImg + '\'' +
                ", id=" + id +
                ", userID=" + userID +
                ", hxName='" + hxName + '\'' +
                ", registrationID='" + registrationID + '\'' +
                ", name='" + name + '\'' +
                ", functionCustom='" + functionCustom + '\'' +
                ", type=" + type +
                ", mobile='" + mobile + '\'' +
                ", roles='" + roles + '\'' +
                ", roleIDs='" + roleIDs + '\'' +
                '}';
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getRoleIDs() {
        return roleIDs;
    }

    public void setRoleIDs(String roleIDs) {
        this.roleIDs = roleIDs;
    }
}
