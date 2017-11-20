package com.merise.net.heart.bean;

import java.util.List;

/**
 * Created by wangdawang on 2016/10/11 0011.
 */
public class NotifyState {

    /**
     * dismantleHint : true
     * doorBellHint : true
     * doorSensorHint : true
     * list : [{"actived":true,"name":"门铃提示","permissionID":2},{"actived":true,"name":"开门提示","permissionID":4},{"actived":false,"name":"电量提示","permissionID":6},{"actived":true,"name":"入侵报警","permissionID":7},{"actived":true,"name":"拆卸报警","permissionID":8}]
     * openDoorHint : true
     * powerHint : false
     */

    private boolean dismantleHint;
    private boolean doorBellHint;
    private boolean doorSensorHint;
    private boolean openDoorHint;
    private boolean powerHint;
    /**
     * actived : true
     * name : 门铃提示
     * permissionID : 2
     */

    private List<ListBean> list;

    public boolean isDismantleHint() {
        return dismantleHint;
    }

    public void setDismantleHint(boolean dismantleHint) {
        this.dismantleHint = dismantleHint;
    }

    public boolean isDoorBellHint() {
        return doorBellHint;
    }

    public void setDoorBellHint(boolean doorBellHint) {
        this.doorBellHint = doorBellHint;
    }

    public boolean isDoorSensorHint() {
        return doorSensorHint;
    }

    public void setDoorSensorHint(boolean doorSensorHint) {
        this.doorSensorHint = doorSensorHint;
    }

    public boolean isOpenDoorHint() {
        return openDoorHint;
    }

    public void setOpenDoorHint(boolean openDoorHint) {
        this.openDoorHint = openDoorHint;
    }

    public boolean isPowerHint() {
        return powerHint;
    }

    public void setPowerHint(boolean powerHint) {
        this.powerHint = powerHint;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private boolean actived;
        private String name;
        private int permissionID;

        public boolean isActived() {
            return actived;
        }

        public void setActived(boolean actived) {
            this.actived = actived;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPermissionID() {
            return permissionID;
        }

        public void setPermissionID(int permissionID) {
            this.permissionID = permissionID;
        }
    }

    @Override
    public String toString() {
        return "NotifyState{" +
                "dismantleHint=" + dismantleHint +
                ", doorBellHint=" + doorBellHint +
                ", doorSensorHint=" + doorSensorHint +
                ", openDoorHint=" + openDoorHint +
                ", powerHint=" + powerHint +
                ", list=" + list +
                '}';
    }
}
