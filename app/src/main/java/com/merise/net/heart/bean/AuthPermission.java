package com.merise.net.heart.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/28.
 */
public class AuthPermission implements Serializable {

    private int permissionID;
    private int roleID;
    private String name;

    public int getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(int permissionID) {
        this.permissionID = permissionID;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AuthPermission{" +
                "name='" + name + '\'' +
                ", permissionID=" + permissionID +
                ", roleID=" + roleID +
                '}';
    }
}
