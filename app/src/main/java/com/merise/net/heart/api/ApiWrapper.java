package com.merise.net.heart.api;


import android.content.Context;

import com.merise.net.heart.api.util.MultipartBody;
import com.merise.net.heart.api.util.PageResponseFunc;
import com.merise.net.heart.api.util.ReplyPage;
import com.merise.net.heart.api.util.ReplyPageResponseFunc;
import com.merise.net.heart.api.util.ResponseFunc;
import com.merise.net.heart.api.util.RetrofitUtil;
import com.merise.net.heart.api.util.RxObserver;
import com.merise.net.heart.api.util.SuggestionPageResponseFunc;
import com.merise.net.heart.bean.Agreement;
import com.merise.net.heart.bean.Announcement;
import com.merise.net.heart.bean.Article;
import com.merise.net.heart.bean.AuthPermission;
import com.merise.net.heart.bean.AuthUser;
import com.merise.net.heart.bean.Card;
import com.merise.net.heart.bean.CommonUser;
import com.merise.net.heart.bean.Community;
import com.merise.net.heart.bean.Device;
import com.merise.net.heart.bean.DoorBell;
import com.merise.net.heart.bean.Fingerprint;
import com.merise.net.heart.bean.FoucsInfo;
import com.merise.net.heart.bean.Function;
import com.merise.net.heart.bean.NotifyState;
import com.merise.net.heart.bean.Page;
import com.merise.net.heart.bean.RecordBean;
import com.merise.net.heart.bean.RecordDetail;
import com.merise.net.heart.bean.Rent;
import com.merise.net.heart.bean.Repair;
import com.merise.net.heart.bean.ReplyBean;
import com.merise.net.heart.bean.Response;
import com.merise.net.heart.bean.SendMessageBean;
import com.merise.net.heart.bean.Sound;
import com.merise.net.heart.bean.UpdateBean;
import com.merise.net.heart.bean.UserInfo;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangdawang on 2016/8/30 0030.
 */
public class ApiWrapper extends RetrofitUtil {
    private static final java.lang.String TAG = "ApiWrapper";
    private static ApiWrapper apiWrapper;

    private ApiWrapper() {
    }


    public static ApiWrapper getApiWrapper() {
        if (apiWrapper == null) {
            synchronized (ApiWrapper.class) {
                if (null == apiWrapper) {
                    apiWrapper = new ApiWrapper();
                }
            }
        }
        return apiWrapper;
    }

    /**
     * 登录
     *
     * @param username       用户名
     * @param password       登陆密码
     * @param registrationID 极光所需注册id
     * @return
     */
    public Observable<Response<UserInfo>> login(String username, String password,
                                                String Imei, String registrationID, String Model, String versionName) {
        Observable<Response<UserInfo>> observable = getApiService().login(username, password, Imei, registrationID, Model, versionName);
        return observable;
    }


    /**
     * 开门
     *
     * @return
     */
    public Observable<Response> openDoor(String deviceID) {
        Observable observable = getApiService().openDoor(deviceID);
        return observable;
    }

    /**
     * 设防撤防
     *
     * @param deviceID
     * @param fortify
     * @return
     */
    public Observable<Response> defenceSetting(String deviceID, int fortify) {
        Observable observable = getApiService().defenceSetting(deviceID, fortify);
        return observable;
    }

    /**
     * 开门方式设置
     *
     * @param deviceID
     * @param opendoorWayID
     * @return
     */
    public Observable<Response> opendoorWaySetting(String deviceID, int opendoorWayID) {
        Observable observable = getApiService().opendoorSetting(deviceID, opendoorWayID);
        return observable;
    }

    /**
     * 获取卡片列表
     *
     * @param deviceID
     * @param page
     * @param rows
     * @return
     */
    public Observable<Response<Page<List<Card>>>> getCardList(String deviceID, int page, int rows) {
        Observable<Response<Page<List<Card>>>> observable = getApiService().getCardList(deviceID, page, rows);
        return observable;
    }

    /**
     * 修改卡片状态
     *
     * @param deviceID
     * @param keyID
     * @param actived
     * @return
     */
    public Observable<Response> modifyCardState(String deviceID, int keyID, int actived) {
        Observable observable = getApiService().modifyCardState(deviceID, keyID, actived);
        return observable;
    }

    /**
     * 删除卡片
     *
     * @param deviceID
     * @param keyID
     * @return
     */
    public Observable<Response> deleteCard(String deviceID, int keyID) {
        Observable observable = getApiService().deleteCard(deviceID, keyID);
        return observable;
    }

    /**
     * 获取指纹列表
     *
     * @param deviceID
     * @param page
     * @param rows
     * @return
     */
    public Observable<Response<Page<List<Fingerprint>>>> getFingerprintList(String deviceID, int page, int rows) {
        Observable observable = getApiService().getFingerprintList(deviceID, page, rows);
        return observable;
    }

    /**
     * 删除指纹信息
     *
     * @param deviceID
     * @param fingerID
     * @return
     */
    public Observable<Response> deleteFingerprint(String deviceID, int fingerID) {
        Observable observable = getApiService().deleteFingerprint(deviceID, fingerID);
        return observable;
    }

    /**
     * 修改指纹状态
     *
     * @param deviceID
     * @param fingerID
     * @param actived
     * @return
     */
    public Observable<Response> modifyFingerprint(String deviceID, int fingerID, int actived) {
        Observable observable = getApiService().modifyFingerprintState(deviceID, fingerID, actived);
        return observable;
    }

    /**
     * 添加卡片
     *
     * @param deviceID
     * @param keyNick
     * @return
     */
    public Observable<Response> addCard(String deviceID, String keyNick) {
        Observable observable = getApiService().addCard(deviceID, keyNick);
        return observable;
    }

    /**
     * 修改卡片备注
     *
     * @param deviceID
     * @param keyID
     * @param keyNick
     * @return
     */
    public Observable<Response> modifyCardNick(String deviceID, int keyID, String keyNick) {
        Observable observable = getApiService().modifyCardNick(deviceID, keyID, keyNick);
        return observable;
    }

    /**
     * 修改指纹备注
     *
     * @param deviceID
     * @param fingerID
     * @param fingerprintNick
     * @return
     */
    public Observable<Response> modifyFingerprintNick(String deviceID, int fingerID, String fingerprintNick) {
        Observable observable = getApiService().modifyFingerprintNick(deviceID, fingerID, fingerprintNick);
        return observable;
    }

    /**
     * 添加指纹
     *
     * @param deviceID
     * @param fingerprintNick
     * @return
     */
    public Observable<Response> addFingerprint(String deviceID, String fingerprintNick) {
        Observable observable = getApiService().addFingerprint(deviceID, fingerprintNick);
        return observable;
    }

    /**
     * 获取授权用户
     *
     * @param deviceID
     * @return
     */
    public Observable<Response<List<AuthUser>>> getAuthUserList(String deviceID) {
        Observable observable = getApiService().getAuthUserList(deviceID);
        return observable;
    }

    /**
     * 删除授权用户
     *
     * @param userID
     * @param deviceID
     * @return
     */
    public Observable<Response> deleteAuthUser(String userID, String deviceID) {
        Observable observable = getApiService().deleteAuthUser(userID, deviceID);
        return observable;
    }

    /**
     * 启用禁用授权用户
     *
     * @param userID
     * @param deviceID
     * @param actived
     * @return
     */
    public Observable<Response> modifyAuthUserState(String userID, String deviceID, int actived) {
        Observable observable = getApiService().modifyAuthUserState(userID, deviceID, actived);
        return observable;
    }

    /**
     * 添加或者修改授权用户
     *
     * @param deviceID
     * @param userName
     * @param userNick
     * @param permissionIDs
     * @return
     */
    public Observable<Response> modifyOrAddAuthUser(String deviceID, String userName, String userNick, String permissionIDs) {
        Observable observable = getApiService().modifyOrAddAuthUser(deviceID, userName, userNick, permissionIDs);
        return observable;
    }

    /**
     * 获取授权用户权限
     *
     * @param deviceID
     * @param userID
     * @return
     */
    public Observable<Response<List<AuthPermission>>> getAuthUserPermissions(String deviceID, String userID) {
        Observable observable = getApiService().getAuthUserPermissions(deviceID, userID);
        return observable;
    }

    /**
     * 自动回锁时间的设置
     *
     * @param deviceID
     * @param autoCloseTime
     * @return
     */
    public Observable<Response> autoCloseTimeSetting(String deviceID, int autoCloseTime) {
        Observable observable = getApiService().autoCloseTimeSetting(deviceID, autoCloseTime);
        return observable;
    }

    /**
     * 获取门铃信息
     *
     * @param deviceID
     * @return
     */
    public Observable<Response<Sound>> getSoundVolume(String deviceID) {
        Observable observable = getApiService().getSoundVolume(deviceID);
        return observable;
    }

    /**
     * 设置门铃
     *
     * @param deviceID
     * @param sound
     * @param volume
     * @return
     */
    public Observable<Response> setSoundVolume(String deviceID, int sound, int volume) {
        Observable observable = getApiService().setSoundVolume(deviceID, sound, volume);
        return observable;
    }

    /**
     * 修改门禁备注
     *
     * @param deviceID
     * @param name
     * @return
     */
    public Observable<Response> modifyDoorNick(String deviceID, String name) {
        Observable observable = getApiService().modifyDoorNick(deviceID, name);
        return observable;
    }

    /**
     * 获取公告信息
     *
     * @param page
     * @param rows
     * @return
     */
    public Observable<Response<Page<List<Announcement>>>> getPropertyAnnouncement(int page, int rows, int type) {
        Observable observable = getApiService().getPropertyAnnouncement(page, rows, type);
        return observable;
    }

    /**
     * 获取交流列表
     *
     * @return
     */
    public Observable<Response> getCommunityList(int page, int rows) {
        Observable observable = getApiService().getCommunityList(page, rows);
        return observable;
    }

    /**
     * 新增物业报修
     *
     * @param repairType
     * @param attaChmentType
     * @param content
     * @param multipartBody
     * @return
     */
    public Observable<Response> insertPropertyRepair(int repairType, int attaChmentType, String content, int parentType, int areaID, MultipartBody multipartBody) {
        Observable observable;
        observable = getApiService().insertPropertyRepair(repairType, attaChmentType, content, parentType, areaID, multipartBody);
        return observable;
    }

    /**
     * 新增社区交流
     *
     * @param content
     * @param type
     * @param time
     * @param place
     * @param scope
     * @param parts
     * @return
     */
    public Observable<Response> insertArticle(String content, int type, String time, String place, int scope, int attaChmentType, int parentType, List<MultipartBody.Part> parts) {
        Observable observable;
        observable = getApiService().insertArticle(content, type, time, place, scope, attaChmentType, parentType, parts);
        return observable;
    }

    public Observable<Response> insertArticle(String content, int type, String time, String place, int scope, int attaChmentType, int parentType, MultipartBody multipartBody) {
        Observable observable = getApiService().insertArticle(content, type, time, place, scope, attaChmentType, parentType, multipartBody);
        return observable;
    }

    /**
     * 上传头像
     *
     * @param multipartBody
     * @return
     */
    public Observable<Response> upHeadImage(MultipartBody multipartBody) {
        Observable observable = getApiService().upHeadImage(multipartBody);
        return observable;
    }

    /**
     * 签名
     *
     * @return
     */
    public Observable<Response> updateUserSign(String sign) {
        Observable observable = getApiService().updateUserSign(sign);
        return observable;
    }

    /**
     * 更改用户名
     *
     * @return
     */
    public Observable<Response> updateUserName(String userName) {
        Observable observable = getApiService().updateUserName(userName);
        return observable;
    }

    /**
     * 邮箱
     *
     * @return
     */
    public Observable<Response> updateUserEmail(String email) {
        Observable observable = getApiService().updateUserEmail(email);
        return observable;
    }

    /**
     * 昵称
     *
     * @return
     */
    public Observable<Response> updateUserNickName(String nickName) {
        Observable observable = getApiService().updateUserNickName(nickName);
        return observable;
    }

    /**
     * 退出登录
     *
     * @return
     */
    public Observable<Response> logout() {
        Observable observable = getApiService().logout();
        return observable;
    }

    /**
     * 点赞
     *
     * @param articleID
     * @return
     */
    public Observable<Response> insertLaud(int articleID) {
        Observable observable = getApiService().insertLaud(articleID);
        return observable;
    }

    /**
     * 新增社区交流评论
     *
     * @param articleID
     * @param comment
     * @return
     */
    public Observable<Response> insertComment(int articleID, String comment) {
        Observable observable = getApiService().insertComment(articleID, comment);
        return observable;
    }

    /**
     * 取消赞
     *
     * @param articleID
     * @return
     */
    public Observable<Response> deleteLaud(int articleID) {
        Observable observable = getApiService().deleteLaud(articleID);
        return observable;
    }

    /**
     * 获取单个用户动态
     *
     * @param page
     * @param rows
     * @param userID
     * @return
     */
    public Observable<Response<Page<List<Article>>>> getOneUserArticleList(int page, int rows, int userID) {
        Observable observable = getApiService().getOneUserArticleList(page, rows, userID);
        return observable;
    }

    /**
     * 获取区域列表
     *
     * @return
     */
    public Observable<Response<Page<List<Community>>>> getMyAreaList() {
        Observable observable = getApiService().getMyAreaList();
        return observable;
    }

    /**
     * 获取物业报修信息
     *
     * @param page
     * @param rows
     * @param type
     * @return
     */
    public Observable<Response<Page<List<Repair>>>> getListWuyeRepair(int page, int rows, int type) {
        Observable observable;
        if (type > 0) {
            observable = getApiService().getListWuyeRepair(page, rows, type);
        } else {
            observable = getApiService().getListWuyeRepair(page, rows);
        }
        return observable;
    }

    /**
     * 添加关注
     *
     * @param focusID
     * @param type
     * @return
     */
    public Observable<Response> insertFocus(int focusID, int type) {
        Observable observable = getApiService().insertFocus(focusID, type);
        return observable;
    }

    /**
     * 取消关注
     *
     * @param focusID
     * @param type
     * @return
     */
    public Observable<Response> cancelFocus(int focusID, int type) {
        Observable observable = getApiService().cancelFocus(focusID, type);
        return observable;
    }

    /**
     * 评价
     *
     * @param id
     * @param satisfaction
     * @param remark
     * @return
     */
    public Observable<Response> insertEvaluate(int id, int satisfaction, String remark, int status) {
        Observable observable = getApiService().insertEvaluate(id, satisfaction, remark, status);
        return observable;
    }

    /**
     * 获取租赁信息
     *
     * @param page
     * @param rows
     * @param address
     * @return
     */
    public Observable<Response<Page<List<Rent>>>> getHouseLease(int page, int rows, String address) {
        Observable observable = getApiService().getHouseLease(page, rows, address);
        return observable;
    }

    /**
     * 新增房屋出租
     *
     * @param title
     * @param price
     * @param address
     * @param areaName
     * @param remark
     * @param phoneNumber
     * @param multipartBody
     * @return
     */
    public Observable<Response> insertHouseLease(String title, int price, String address, String areaName,
                                                 String remark, String phoneNumber, MultipartBody multipartBody) {
        Observable observable = getApiService().insertHouseLease(title, price, address, areaName, remark, phoneNumber, multipartBody);
        return observable;
    }

    /**
     * 获取功能列表
     *
     * @return
     */
    public Observable<Response<Page<List<Function>>>> getFunctionList() {
        Observable observable = getApiService().getFunctionList();
        return observable;
    }

    /**
     * 修改用户操作习惯
     *
     * @param deviceID
     * @param functionCustom
     * @return
     */
    public Observable<Response> updateFunctionCustom(int deviceID, String functionCustom) {
        Observable observable = getApiService().updateFunctionCustom(deviceID, functionCustom);
        return observable;
    }

    /**
     * 激活设备
     *
     * @param sn
     * @param name
     * @return
     */
    public Observable<Response> activateDevice(Context context,String sn, String name) {
        Observable observable = getApiService().activateDevice(sn, name);
        return observable;
    }

    /**
     * 获取用户关注信息
     *
     * @param userID
     * @return
     */
    public Observable<Response<FoucsInfo>> getOneUserInfo(int userID) {
        Observable<Response<FoucsInfo>> observable = getApiService().getOneUserInfo(userID);
        return observable;
    }

    public <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        try {
            o.map(new ResponseFunc())
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T> void toSubscribe2(Observable<T> o, Subscriber<T> s) {
        try {
            o.map(new PageResponseFunc())
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void toSubscribe3(Observable<T> o, RxObserver<T> s) {
        try {
            o.map(new PageResponseFunc())
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取记录数据
     * (下拉刷新专用)
     *
     * @param deviceid
     * @param logtype
     * @param page
     * @param pagesize
     * @return
     */
    public Observable<Response<Page<List<RecordBean>>>> getRecordList1(String deviceid, final String logtype,
                                                                       final int page, String firstVisibleID, String pagesize) {
        Observable<Response<Page<List<RecordBean>>>> observable = getApiService().getRecordList1
                (deviceid, logtype, page, firstVisibleID, pagesize);
        return observable;
    }

    /**
     * 获取门铃门磁记录数据
     * (下拉刷新专用)
     *
     * @param deviceid
     * @param logtype
     * @param page
     * @param pagesize
     * @return
     */
    public Observable<Response<Page<List<DoorBell>>>> getRecordListDoor1(String deviceid, final String logtype,
                                                                         final int page, String firstVisibleID, String pagesize) {
        Observable<Response<Page<List<DoorBell>>>> observable = getApiService().getRecordListDoor1
                (deviceid, logtype, page, firstVisibleID, pagesize);
        return observable;
    }

    /**
     * 获取开门记录数据
     *
     * @param deviceid
     * @param logtype
     * @param page
     * @param pagesize
     * @return
     */
    public Observable<Response<Page<List<RecordBean>>>> getRecordList(String deviceid, final String logtype,
                                                                      final int page, String pagesize) {
        Observable<Response<Page<List<RecordBean>>>> observable = getApiService().getRecordList(deviceid, logtype, page, pagesize);
        return observable;
    }


    /**
     * 获取门铃和门磁记录数据
     *
     * @param deviceid
     * @param logtype
     * @param page
     * @param pagesize
     * @return
     */
    public Observable<Response<Page<List<DoorBell>>>> getRecordListDoor(String deviceid, final String logtype,
                                                                        final int page, String pagesize) {
        Observable<Response<Page<List<DoorBell>>>> observable = getApiService().getRecordListDoor(deviceid, logtype, page, pagesize);
        return observable;
    }


    /**
     * 获取记录查看详情数据图片
     *
     * @param deviceid
     * @param logId
     * @param logtype
     * @return
     */
    public Observable<Response<RecordDetail>> getRecordDetail(String deviceid, final String logId,
                                                              String logtype) {
        Observable<Response<RecordDetail>> observable = getApiService().getRecordDetail(deviceid, logId, logtype);
        return observable;
    }


    /**
     * 获取轮播图片
     *
     * @return
     */
    public Observable<Response> getBannerImage() {
        Observable<Response> observable = getApiService().getBannerImage();
        return observable;
    }

    /**
     * 修改密码
     *
     * @return
     */
    public Observable<Response> updatePassword(String newPassword) {
        Observable<Response> observable = getApiService().updatePassword(newPassword);
        return observable;
    }

    /**
     * 获取验证码
     * （修改手机号）
     * （注册）
     * （首页忘记密码）
     *
     * @return
     */
    public Observable<Response> getValidateCode(String name, String mobile, String codetype) {
        Observable<Response> observable = getApiService().getValidateCode(name, mobile, codetype);
        return observable;
    }

    /**
     * 注册
     *
     * @return
     */
    public Observable<Response> regist(String name, String mobile, String password, String registrationID, String validateCodeVal) {
        Observable<Response> observable = getApiService().regist(name, mobile, password, registrationID, validateCodeVal);
        return observable;
    }

    /**
     * 首页忘记密码第一步
     *
     * @return
     */
    public Observable<Response> findPasswordFirstStep(String name, String validateCodeVal) {
        Observable<Response> observable = getApiService().findPasswordFirstStep(name, validateCodeVal);
        return observable;
    }


    /**
     * 首页忘记密码第二步
     *
     * @return
     */
    public Observable<Response> rebuildPassword(String newPassword) {
        Observable<Response> observable = getApiService().rebuildPassword(newPassword);
        return observable;
    }

    /**
     * 修改手机号(账号)
     *
     * @return
     */
    public Observable<Response> changeAccount(String oldname, String mobile, String validcode) {
        Observable<Response> observable = getApiService().changeAccount(oldname, mobile, validcode);
        return observable;
    }


    /**
     * 设置紧急号码
     *
     * @return
     */
    public Observable<Response> setEmergency(String deviceId, String phone) {
        Observable<Response> observable = getApiService().setEmergency(deviceId, phone);
        return observable;
    }

    /**
     * 获取提示管理信息
     *
     * @return
     */
    public Observable<Response<NotifyState>> getNotifyState(String deviceId) {
        Observable<Response<NotifyState>> observable = getApiService().getNotifyState(deviceId);
        return observable;
    }

    /**
     * 提交设置提示管理信息
     *
     * @return
     */
    public Observable<Response> notifySetting(String deviceId, String activeId, String permissionId) {
        Observable<Response> observable = getApiService().notifySetting(deviceId, activeId, permissionId);
        return observable;
    }

    /**
     * 获取设备
     *
     * @return
     */
    public Observable<Response<List<Device>>> getDeviceList() {
        Observable<Response<List<Device>>> observable = getApiService().getDeviceList();
        return observable;
    }

    /**
     * 删除设备
     *
     * @return
     */
    public Observable<Response> deleteMobile(String ID) {
        Observable<Response> observable = getApiService().deleteMobile(ID);
        return observable;
    }

    /**
     * 获取用户协议
     *
     * @return
     */
    public Observable<Response<Agreement>> getAgreement() {
        Observable<Response<Agreement>> observable = getApiService().getAgreement();
        return observable;
    }

    /**
     * 获取二维码数据
     *
     * @return
     */
    public Observable<Response> getVirtualKey(String deviceID) {
        Observable<Response> observable = getApiService().getVirtualKey(deviceID);
        return observable;
    }

    /**
     * 获取二维码数据
     *
     * @return
     */
    public Observable<Response> getFansList() {
        Observable<Response> observable = getApiService().getFansList();
        return observable;
    }

    /**
     * 获取二维码数据
     *
     * @return
     */
    public Observable<Response> getFocusUserList() {
        Observable<Response> observable = getApiService().getFocusUserList();
        return observable;
    }

    /**
     * 获取更新版本信息
     *
     * @return
     */
    public Observable<Response<UpdateBean>> checkUpdate(String type) {
        Observable<Response<UpdateBean>> observable = getApiService().checkUpdate(type);
        return observable;
    }

    /**
     * 获取登录设备列表
     *
     * @return
     */
    public Observable<Response<List<CommonUser>>> getLoginUsers() {
        Observable<Response<List<CommonUser>>> observable = getApiService().getLoginUsers();
        return observable;
    }


    /**
     * 获取意见建议中建议数据
     */
    public Observable<Response<ReplyPage<List<ReplyBean>>>> getOpinions(int page, int pagesize) {
        Observable<Response<ReplyPage<List<ReplyBean>>>> observable = getApiService().getOpinions(page, pagesize);
        return observable;
    }

    /**
     * 获取意见建议中回复数据
     */
    public Observable<Response<ReplyPage<List<ReplyBean>>>> getReply(int page, int pagesize) {
        Observable<Response<ReplyPage<List<ReplyBean>>>> observable = getApiService().getReply(page, pagesize);
        return observable;
    }


    /**
     * 删除意见建议条目
     */
    public Observable<Response> deleteQuestion(int questionID) {
        Observable<Response> observable = getApiService().deleteQuestion(questionID);
        return observable;
    }


    /**
     * 提交意见建议条目
     */
    public Observable<Response<SendMessageBean>> sendOpinnion(String content) {
        Observable<Response<SendMessageBean>> observable = getApiService().sendOpinnion(content);
        return observable;
    }

    /**
     * 获取版本信息
     * @param type
     * @return
     */
    public Observable getVersion(int type) {
        Observable observable = getApiService().getVersion(type);
        return observable;
    }

    public <T> void toSubscribePage(Observable<T> o, Subscriber<T> s) {
        o.map(new PageResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 回复
     *
     * @param o
     * @param s
     * @param <T>
     */
    public <T> void toSubscribeReplyPage(Observable<T> o, Subscriber<T> s) {
        o.map(new ReplyPageResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 意见
     *
     * @param o
     * @param s
     * @param <T>
     */
    public <T> void toSubscribesSuggestPage(Observable<T> o, Subscriber<T> s) {
        o.map(new SuggestionPageResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

}
