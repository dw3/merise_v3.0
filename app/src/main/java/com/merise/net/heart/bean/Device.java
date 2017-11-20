package com.merise.net.heart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */
public class Device implements Serializable {


    private String functions;
    private String emergencyNumber;
    private String sn;
    private String serverAddress;
    private String hxName;
    private String deviceNick;
    private int isfortify;
    private String adminID;
    private double version;
    private int autoCloseTime;
    private int alarmTime;
    private int id;
    private String name;
    private int openDoorWayID;
    private String permissionID;
    private String roleID;
    private int typeID;
    private String phone;// 紧急电话
    private List<DeviceStatus> status;

    public List<DeviceStatus> getStatus() {
        return status;
    }

    public void setStatus(List<DeviceStatus> status) {
        this.status = status;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }
//    private String functionCustom;
//
//    public String getFunctionCustom() {
//        return functionCustom;
//    }
//
//    public void setFunctionCustom(String functionCustom) {
//        this.functionCustom = functionCustom;
//    }

    private boolean doorIsOpen;
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isDoorIsOpen() {
        return doorIsOpen;
    }

    public void setDoorIsOpen(boolean doorIsOpen) {
        this.doorIsOpen = doorIsOpen;
    }

    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getHxName() {
        return hxName;
    }

    public void setHxName(String hxName) {
        this.hxName = hxName;
    }

    public String getDeviceNick() {
        return deviceNick;
    }

    public void setDeviceNick(String deviceNick) {
        this.deviceNick = deviceNick;
    }

    public int getIsfortify() {
        return isfortify;
    }

    public void setIsfortify(int isfortify) {
        this.isfortify = isfortify;
    }

    public int getAutoCloseTime() {
        return autoCloseTime;
    }

    public void setAutoCloseTime(int autoCloseTime) {
        this.autoCloseTime = autoCloseTime;
    }

    public int getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(int alarmTime) {
        this.alarmTime = alarmTime;
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

    public int getOpenDoorWayID() {
        return openDoorWayID;
    }

    public void setOpenDoorWayID(int openDoorWayID) {
        this.openDoorWayID = openDoorWayID;
    }

    public String getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(String permissionID) {
        this.permissionID = permissionID;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    @Override
    public String toString() {
        return "Device{" +
                "adminID='" + adminID + '\'' +
                ", functions='" + functions + '\'' +
                ", emergencyNumber='" + emergencyNumber + '\'' +
                ", sn='" + sn + '\'' +
                ", serverAddress='" + serverAddress + '\'' +
                ", hxName='" + hxName + '\'' +
                ", deviceNick='" + deviceNick + '\'' +
                ", isfortify=" + isfortify +
                ", version=" + version +
                ", autoCloseTime=" + autoCloseTime +
                ", alarmTime=" + alarmTime +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", openDoorWayID=" + openDoorWayID +
                ", permissionID='" + permissionID + '\'' +
                ", roleID='" + roleID + '\'' +
                ", typeID=" + typeID +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", doorIsOpen=" + doorIsOpen +
                '}';
    }
}
