package com.merise.net.heart.bean;

import java.io.Serializable;

/**
 * 作者:xiangyang
 * 日期:2016/10/26
 */
public class Repair implements Serializable {
    /**
     * id	Int	报修记录ID
     * content	String	报修内容
     * repairType	Int	1个人2公共
     * createTime	String	创建时间
     * status	Int	1已报修，2已受理，3维修中，4完成
     * areaName	String	小区名称
     * repairPersonnel	String	维修人员
     * repairContact	Int	维修联系方式
     * endTime	String	结束时间
     * remark	String	评语
     * satisfaction	int	满意度，1非常不满意，2不满意，3一般，4满意，5非常满意
     * acceptTime	String	受理时间
     * userName	String	报修人名称
     * areaID	Int	小区ID
     * attaChmentType	Int	1图片，2视频
     * imgs	String[]	报修记录图片
     */

    private String acceptTime;
    private int areaID;
    private String areaName;
    private int attaChmentType;
    private String content;
    private String createTime;
    private String endTime;
    private int id;
    private String imgs;
    private String remark;
    private String repairContact;
    private String repairPersonnel;
    private int repairType;
    private int satisfaction;
    private int status;
    private String userName;
    private int userID;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public int getAreaID() {
        return areaID;
    }

    public void setAreaID(int areaID) {
        this.areaID = areaID;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getAttaChmentType() {
        return attaChmentType;
    }

    public void setAttaChmentType(int attaChmentType) {
        this.attaChmentType = attaChmentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRepairContact() {
        return repairContact;
    }

    public void setRepairContact(String repairContact) {
        this.repairContact = repairContact;
    }

    public String getRepairPersonnel() {
        return repairPersonnel;
    }

    public void setRepairPersonnel(String repairPersonnel) {
        this.repairPersonnel = repairPersonnel;
    }

    public int getRepairType() {
        return repairType;
    }

    public void setRepairType(int repairType) {
        this.repairType = repairType;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Repair{" +
                "acceptTime='" + acceptTime + '\'' +
                ", areaID=" + areaID +
                ", areaName='" + areaName + '\'' +
                ", attaChmentType=" + attaChmentType +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", id=" + id +
                ", imgs='" + imgs + '\'' +
                ", remark='" + remark + '\'' +
                ", repairContact='" + repairContact + '\'' +
                ", repairPersonnel='" + repairPersonnel + '\'' +
                ", repairType=" + repairType +
                ", satisfaction=" + satisfaction +
                ", status=" + status +
                ", userName='" + userName + '\'' +
                ", userID=" + userID +
                '}';
    }
}
