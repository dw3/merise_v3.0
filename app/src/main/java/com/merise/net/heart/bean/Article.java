package com.merise.net.heart.bean;


import java.util.List;

/**
 * 作者:xiangyang
 * 日期:2016/10/17
 */
public class Article {


    private String createTime;//创建时间
    private String faceImg;//头像路劲
    private int status;//状态
    private int dz;//点赞数量
    private int type;// 1、状态，2、活动
    private int parentID;//动态ID
    private String dzNames;// 点赞人
    private int id;
    private String content;//内容
    private int author;//作者id
    private int hot;//浏览量
    private String time;
    private String place;
    private String dzIDs;//点赞人IDs
    private List<DZUser> dzUsers;




    public List<DZUser> getDzUsers() {
        return dzUsers;
    }

    public void setDzUsers(List<DZUser> dzUsers) {
        this.dzUsers = dzUsers;
    }

    private int fans;
    private int focus;
    private int isFocus; //0 关注 1 未关注

    public int getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(int isFocus) {
        this.isFocus = isFocus;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getDzIDs() {
        return dzIDs;
    }

    public void setDzIDs(String dzIDs) {
        this.dzIDs = dzIDs;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    private List<ImgsBean> imgs;//图片

    private List<CommentsBean> comments;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFaceImg() {
        return faceImg;
    }

    public void setFaceImg(String faceImg) {
        this.faceImg = faceImg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDz() {
        return dz;
    }

    public void setDz(int dz) {
        this.dz = dz;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDzNames() {
        return dzNames;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public void setDzNames(String dzNames) {
        this.dzNames = dzNames;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public List<ImgsBean> getImgs() {
        return imgs;
    }

    public void setImgs(List<ImgsBean> imgs) {
        this.imgs = imgs;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Article{" +
                "author=" + author +
                ", createTime='" + createTime + '\'' +
                ", faceImg='" + faceImg + '\'' +
                ", status=" + status +
                ", dz=" + dz +
                ", type=" + type +
                ", parentID=" + parentID +
                ", dzNames='" + dzNames + '\'' +
                ", id=" + id +
                ", content='" + content + '\'' +
                ", hot=" + hot +
                ", time='" + time + '\'' +
                ", place='" + place + '\'' +
                ", dzIDs='" + dzIDs + '\'' +
                ", fans=" + fans +
                ", focus=" + focus +
                ", isFocus=" + isFocus +
                ", ids=" + ids +
                ", imgs=" + imgs +
                ", comments=" + comments +
                '}';
    }
}
