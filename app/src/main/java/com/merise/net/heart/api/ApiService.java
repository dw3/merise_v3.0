package com.merise.net.heart.api;


import com.merise.net.heart.api.util.MultipartBody;
import com.merise.net.heart.api.util.ReplyPage;
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
import com.merise.net.heart.utils.Const;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wangdawang on 2016/8/30 0030.
 */
public interface ApiService {

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return retrofit框架的网络访问接口类
     * 0表示带验证码登录
     * 采用注解和方法来定义一个请求该如何被创建
     */
    @POST("merise/mobile/user/login")
    Observable<Response<UserInfo>> login(@Query(Const.NAME) String username,
                                         @Query(Const.PASSWORD) String password,
                                         @Query(Const.IMEI) String Imei,
                                         @Query(Const.REGISTRATIONID) String registrationID,
                                         @Query(Const.MODEL) String Model,
                                         @Query(Const.APPVERSION) String versionName);

    @POST("merise/mobile/user/login")
    Observable<Response<UserInfo>> login0(@Query(Const.NAME) String username,
                                          @Query(Const.PASSWORD) String password,
                                          @Query(Const.IMEI) String Imei,
                                          @Query(Const.REGISTRATIONID) String registrationID,
                                          @Query(Const.MODEL) String Model,
                                          @Query(Const.BINDCODE) String bindCode);


    @POST("/merise/mobile/deviceLog/getDeviceLogList")
    Observable<Response<Page<List<RecordBean>>>> getRecordList(@Query(Const.DEVICEID) String deviceId,
                                                               @Query(Const.LOGTYPE) String logtype,
                                                               @Query(Const.PAGE) int page,
                                                               @Query(Const.ROWS) String pagesize);

    //门铃记录和门磁报警查看用
    @POST("/merise/mobile/deviceLog/getDeviceLogList")
    Observable<Response<Page<List<DoorBell>>>> getRecordListDoor(@Query(Const.DEVICEID) String deviceId,
                                                                 @Query(Const.LOGTYPE) String logtype,
                                                                 @Query(Const.PAGE) int page,
                                                                 @Query(Const.ROWS) String pagesize);


    //门铃下拉刷新用
    @POST("/merise/mobile/deviceLog/getDeviceLogList")
    Observable<Response<Page<List<RecordBean>>>> getRecordList1(@Query(Const.DEVICEID) String deviceId,
                                                                @Query(Const.LOGTYPE) String logtype,
                                                                @Query(Const.PAGE) int page,
                                                                @Query(Const.LOGID) String firstVisibleID,
                                                                @Query(Const.ROWS) String pagesize);

    //门铃门磁下拉刷新用
    @POST("/merise/mobile/deviceLog/getDeviceLogList")
    Observable<Response<Page<List<DoorBell>>>> getRecordListDoor1(@Query(Const.DEVICEID) String deviceId,
                                                                  @Query(Const.LOGTYPE) String logtype,
                                                                  @Query(Const.PAGE) int page,
                                                                  @Query(Const.LOGID) String firstVisibleID,
                                                                  @Query(Const.ROWS) String pagesize);


    //点击打开记录图片
    @POST("/merise/mobile/deviceLog/getDeviceLogDetail")
    Observable<Response<RecordDetail>> getRecordDetail(@Query(Const.DEVICEID) String deviceId,
                                                       @Query(Const.LOGID) String logId,
                                                       @Query(Const.LOGTYPE) String logtype);

    //获取设备设防状态信息
    @POST("/merise/mobile/userRoleDevice/getDeviceUserHintList")
    Observable<Response<NotifyState>> getNotifyState(@Query(Const.DEVICEID) String deviceId);


    //更改设备设防信息(开门，门铃，报警，红外提示开关)
    @POST("/merise/mobile/userRoleDevice/updateDeviceUserHint")
    Observable<Response> notifySetting(@Query(Const.DEVICEID) String deviceId,
                                       @Query(Const.ACTIVED) String activeId,
                                       @Query(Const.PERMISSIONID) String permissionId);


    //获取登录设备列表
    @POST("/merise/mobile/user/getMobiles")
    Observable<Response<List<CommonUser>>> getLoginUsers();


    //删除登录设备
    @POST("/merise/mobile/user/deleteMobile")
    Observable<Response> deleteMobile(@Query(Const.ID) String ID);


    /**
     * 获取设备列表
     *
     * @return
     */
    @POST("/merise/mobile/device/getDeviceList")
    Observable<Response<List<Device>>> getDeviceList();


    /**
     * 修改密码
     *
     * @return
     */
    @POST("/merise/mobile/user/changePassword")
    Observable<Response> updatePassword(@Query(Const.NEWPASSWORD) String newPassword);


    /**
     * 获取验证码
     * （修改手机号）
     * (注册获取)
     * (首页忘记密码)
     *
     * @return
     */
    @POST("/merise/mobile/validCode/getValidCode2")
    Observable<Response> getValidateCode(@Query(Const.NAME) String name,
                                         @Query(Const.MOBILE) String mobile,
                                         @Query(Const.CODETYPE) String codetype);

    /**
     * 首页忘记密码第一步
     */
    @POST("/merise/mobile/user/backPassword")
    Observable<Response> findPasswordFirstStep(@Query(Const.NAME) String name,
                                               @Query(Const.VALIDCODE) String validateCodeVal);

    /**
     * 首页忘记密码第二步
     */
    @POST("/merise/mobile/user/updatePassword")
    Observable<Response> rebuildPassword(@Query(Const.PASSWORD) String newPassword);


    /**
     * 注册
     * registrationID暂时默认一个号码
     *
     * @return
     */
    @POST("/merise/mobile/user/register2")
    Observable<Response> regist(@Query(Const.NAME) String name,
                                @Query(Const.MOBILE) String mobile,
                                @Query(Const.PASSWORD) String password,
                                @Query(Const.REGISTRATIONID) String registrationID,
                                @Query(Const.VALIDCODE) String validateCodeVal);


    /**
     * 修改手机号（账号）
     * （修改手机号）
     */
    @POST("/merise/mobile/user/changeAccount")
    Observable<Response> changeAccount(@Query(Const.OLD) String oldname,
                                       @Query(Const.NEW) String mobile,
                                       @Query(Const.VALIDCODE) String validcode);


    /**
     * 紧急号码设置
     *
     * @return
     */
    @POST("/merise/mobile/userRoleDevice/setEmergencyNumber")
    Observable<Response> setEmergency(@Query(Const.DEVICEID) String deviceID,
                                      @Query(Const.EMERGENCYNUM) String phone);

    /**
     * 意见建议获取问题
     *
     * @return
     */
    @POST("/merise/mobile/userFeedBack/getUserFeedBackList")
    Observable<Response<ReplyPage<List<ReplyBean>>>> getOpinions(@Query(Const.PAGE) int page,
                                                                 @Query(Const.ROWS) int pagesize);

    /**
     * 意见建议获取回复
     *
     * @return
     */
    @POST("/merise/mobile/userFeedBack/getUserFeedBackList")
    Observable<Response<ReplyPage<List<ReplyBean>>>> getReply(@Query(Const.PAGE) int page,
                                                              @Query(Const.ROWS) int pagesize);

    /**
     * 删除意见建议
     *
     * @return
     */
    @POST("/merise/mobile/userFeedBack/deleteFeedBack")
    Observable<Response> deleteQuestion(@Query(Const.QUESTIONID) int questionID);


    /**
     * 获取用户协议
     *
     * @return
     */
    @POST("/merise/mobile/user/getAgreement")
    Observable<Response<Agreement>> getAgreement();


    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    @POST("/merise/mobile/version/getVersion")
    Observable<Response<UpdateBean>> checkUpdate(@Query(Const.type) String type);

    /**
     * 提交意见建议
     *
     * @return
     */
    @POST("/merise/mobile/user/insertFeeBack")
    Observable<Response<SendMessageBean>> sendOpinnion(@Query(Const.CONTENT) String content);


    /**
     * 获取广告轮播图片
     *
     * @return
     */
    @POST()
    Observable<Response> getBannerImage();


    /**
     * 开门
     *
     * @return
     */
    @POST("/merise/mobile/device/openDoor")
    Observable<Response> openDoor(@Query("deviceID") String deviceID);

    /**
     * 设防撤防
     *
     * @param deviceID
     * @param fortify
     * @return
     */
    @POST("/merise/mobile/device/updateDeviceFortify")
    Observable<Response> defenceSetting(@Query("deviceID") String deviceID, @Query("isfortify") int fortify);

    /**
     * 开门方式设置
     *
     * @param deviceID
     * @param openDoorWayID
     * @return
     */
    @POST("/merise/mobile/device/updateDeviceOpenWay")
    Observable<Response> opendoorSetting(@Query("deviceID") String deviceID, @Query("openDoorWayID") int openDoorWayID);

    /**
     * 获取卡片列表
     *
     * @param deviceID
     * @param page
     * @param rows
     * @return
     */
    @POST("/merise/mobile/device/getDeviceKeyList")
    Observable<Response<Page<List<Card>>>> getCardList(@Query("deviceID") String deviceID, @Query("page") int page, @Query("rows") int rows);

    /**
     * 修改卡片状态
     *
     * @param deviceID
     * @param keyID
     * @param actived
     * @return
     */
    @POST("/merise/mobile/device/updateDeviceKeyStates")
    Observable<Response> modifyCardState(@Query("deviceID") String deviceID, @Query("keyID") int keyID, @Query("actived") int actived);

    /**
     * 移除卡片
     *
     * @param deviceID
     * @param keyID
     * @return
     */
    @POST("/merise/mobile/device/deleteDeviceKey")
    Observable<Response> deleteCard(@Query("deviceID") String deviceID, @Query("keyID") int keyID);

    /**
     * 获取指纹列表
     *
     * @param deviceID
     * @param page
     * @param rows
     * @return
     */
    @POST("/merise/mobile/device/getDeviceFingerList")
    Observable<Response<Page<List<Fingerprint>>>> getFingerprintList(@Query("deviceID") String deviceID, @Query("page") int page, @Query("rows") int rows);

    /**
     * 修改指纹状态
     *
     * @param deviceID
     * @param fingerID
     * @param actived
     * @return
     */
    @POST("/merise/mobile/device/updateDeviceFingerStates")
    Observable<Response> modifyFingerprintState(@Query("deviceID") String deviceID, @Query("fingerID") int fingerID, @Query("actived") int actived);

    /**
     * 删除指纹信息
     *
     * @param deviceID
     * @param fingerID
     * @return
     */
    @POST("/merise/mobile/device/deleteDeviceFinger")
    Observable<Response> deleteFingerprint(@Query("deviceID") String deviceID, @Query("fingerID") int fingerID);

    //{"stateCode":423,"message":"门禁机正在执行『读取卡信息』操作，请稍候重试"}

    /**
     * 添加卡片
     *
     * @param deviceID
     * @param keyNick
     * @return
     */
    @POST("/merise/mobile/device/insertDeviceKey")
    Observable<Response> addCard(@Query("deviceID") String deviceID, @Query("keyNick") String keyNick);

    /**
     * 修改卡片
     *
     * @param deviceID
     * @param keyID
     * @param keyNick
     * @return
     */
    @POST("/merise/mobile/device/updateDeviceKeyNick")
    Observable<Response> modifyCardNick(@Query("deviceID") String deviceID, @Query("keyID") int keyID, @Query("keyNick") String keyNick);

    /**
     * 修改指纹备注
     *
     * @param deviceID
     * @param fingerID
     * @param fingerprintNick
     * @return
     */
    @POST("/merise/mobile/device/updateDeviceFingerNick")
    Observable<Response> modifyFingerprintNick(@Query("deviceID") String deviceID, @Query("fingerID") int fingerID, @Query("fingerprintNick") String fingerprintNick);

    /**
     * 添加指纹
     *
     * @param deviceID
     * @param fingerprintNick
     * @return
     */
    @POST("/merise/mobile/device/insertDeviceFinger")
    Observable<Response> addFingerprint(@Query("deviceID") String deviceID, @Query("fingerprintNick") String fingerprintNick);

    /**
     * 获取授权用户列表
     *
     * @param deviceID
     * @return
     */
    @POST("/merise/mobile/userRoleDevice/getDeviceUserList")
    Observable<Response<List<AuthUser>>> getAuthUserList(@Query("deviceID") String deviceID);

    /**
     * 启用禁用授权用户
     *
     * @param userID
     * @param deviceID
     * @param actived
     * @return
     */
    @POST("/merise/mobile/userRoleDevice/updateDeviceUserActived")
    Observable<Response> modifyAuthUserState(@Query("userID") String userID, @Query("deviceID") String deviceID, @Query("actived") int actived);

    /**
     * 删除授权用户
     *
     * @param userID
     * @param deviceID
     * @return
     */
    @POST("/merise/mobile/userRoleDevice/deleteDeviceUser")
    Observable<Response> deleteAuthUser(@Query("userID") String userID, @Query("deviceID") String deviceID);

    /**
     * 新增或者修改授权用户
     *
     * @param deviceID
     * @param userName
     * @param userNick
     * @param permissionIDs
     * @return
     */
    @POST("/merise/mobile/userRoleDevice/addDeviceUser")
    Observable<Response> modifyOrAddAuthUser(@Query("deviceID") String deviceID, @Query("userName") String userName,
                                             @Query("userNick") String userNick, @Query("permissionIDs") String permissionIDs);

    /**
     * 获取某个用户的权限
     *
     * @param deviceID
     * @param userID
     * @return
     */
    @POST("/merise/mobile/userRoleDevice/getDeviceUserRole")
    Observable<Response<List<AuthPermission>>> getAuthUserPermissions(@Query("deviceID") String deviceID, @Query("userID") String userID);

    /**
     * 自动回锁时间设置
     *
     * @param deviceID
     * @param autoCloseTime
     * @return
     */
    @POST("/merise/mobile/device/updateDeviceTime")
    Observable<Response> autoCloseTimeSetting(@Query("deviceID") String deviceID, @Query("autoCloseTime") int autoCloseTime);

    /**
     * 获取门铃信息
     *
     * @param deviceID
     * @return
     */
    @POST("/merise/mobile/device/getSoundVolume")
    Observable<Response<Sound>> getSoundVolume(@Query("deviceID") String deviceID);

    /**
     * 设置门铃信息
     *
     * @param deviceID
     * @param sound
     * @param volume
     * @return
     */
    @POST("/merise/mobile/device/setSoundVolume")
    Observable<Response> setSoundVolume(@Query("deviceID") String deviceID, @Query("sound") int sound, @Query("volume") int volume);

    /**
     * 修改门禁备注
     *
     * @param deviceID
     * @param name
     * @return
     */
    @POST("/merise/mobile/device/updateDeviceName")
    Observable<Response> modifyDoorNick(@Query("deviceID") String deviceID, @Query("name") String name);

    /**
     * 获取公告信息
     *
     * @param page
     * @param rows
     * @param type 1是广告2是公告都需要传0(必填)
     * @return
     */
    @POST("/merise/mobile/message/getNoticeList")
    Observable<Response<Page<List<Announcement>>>> getPropertyAnnouncement(@Query("page") int page, @Query("rows") int rows, @Query("type") int type);

    /**
     * 获取交流列表
     *
     * @return
     */
    @POST("/merise/mobile/community/getArticleList")
    Observable<Response<Page<List<Article>>>> getCommunityList(@Query("page") int page, @Query("rows") int rows);

    /**
     * 新增物业报修
     *
     * @return
     */
    @POST("/merise/mobile/wuyeRepair/insertWuyeRepair")
    Observable<Response> insertPropertyRepair(@Query("repairType") int repairType,
                                              @Query("attaChmentType") int attaChmentType,
                                              @Query("content") String content,
                                              @Query("parentType") int parentType,
                                              @Query("areaID") int areaID,
                                              @Body MultipartBody multipartBody);


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
    @Multipart
    @POST("/merise/mobile/community/insertArticle")
    Observable<Response> insertArticle(@Query("content") String content,
                                       @Query("type") int type,
                                       @Query("time") String time,
                                       @Query("place") String place,
                                       @Query("scope") int scope,
                                       @Query("attaChmentType") int attaChmentType,
                                       @Query("parentType") int parentType,
                                       @Part List<MultipartBody.Part> parts);

    @POST("/merise/mobile/community/insertArticle")
    Observable<Response> insertArticle(@Query("content") String content,
                                       @Query("type") int type,
                                       @Query("time") String time,
                                       @Query("place") String place,
                                       @Query("scope") int scope,
                                       @Query("attaChmentType") int attaChmentType,
                                       @Query("parentType") int parentType,
                                       @Body MultipartBody multipartBody);

    /**
     * 点赞
     *
     * @param articleID
     * @return
     */
    @POST("/merise/mobile/community/insertLaud")
    Observable<Response> insertLaud(@Query("articleID") int articleID);

    /**
     * 新增社区交流评论
     *
     * @param article
     * @param comment
     * @return
     */
    @POST("/merise/mobile/community/insertComment")
    Observable<Response> insertComment(@Query("articleID") int article, @Query("comment") String comment);

    /**
     * 取消赞
     *
     * @param article
     * @return
     */
    @POST("/merise/mobile/community/deleteLaud")
    Observable<Response> deleteLaud(@Query("articleID") int article);

    /**
     * 查询单个用户动态列表
     *
     * @param page
     * @param rows
     * @param userID
     * @return
     */
    @POST("/merise/mobile/community/getOneUserArticleList")
    Observable<Response<Page<List<Article>>>> getOneUserArticleList(@Query("page") int page, @Query("rows") int rows, @Query("userID") int userID);

    /**
     * 获取社区列表
     *
     * @return
     */
    @POST("/merise/mobile/wuyeRepair/getMyAreaList")
    Observable<Response<Page<List<Community>>>> getMyAreaList();

    /**
     * 获取物业报修列表
     *
     * @param page
     * @param rows
     * @param type 1个人，2公共
     * @return
     */
    @POST("/merise/mobile/wuyeRepair/getListWuyeRepair")
    Observable<Response<Page<List<Repair>>>> getListWuyeRepair(@Query("page") int page, @Query("rows") int rows, @Query("type") int type);

    @POST("/merise/mobile/wuyeRepair/getListWuyeRepair")
    Observable<Response<Page<List<Repair>>>> getListWuyeRepair(@Query("page") int page, @Query("rows") int rows);

    /**
     * 关注
     *
     * @param focusID
     * @param type    类型(必传):1人员,2小区
     * @return
     */
    @POST("/merise/mobile/community/insertFocus")
    Observable<Response> insertFocus(@Query("focusID") int focusID, @Query("type") int type);


    /**
     * 取消关注
     *
     * @param focusID
     * @param type
     * @return
     */
    @POST("/merise/mobile/community/deleteFocus")
    Observable<Response> cancelFocus(@Query("focusID") int focusID, @Query("type") int type);

    /**
     * 评价
     *
     * @param id
     * @param satisfaction
     * @param remark
     * @return
     */
    @POST("/merise/mobile/wuyeRepair/updateRemark")
    Observable<Response> insertEvaluate(@Query("id") int id, @Query("satisfaction") int satisfaction, @Query("remark") String remark, @Query("status") int status);

    /**
     * 获取租赁列表
     *
     * @param page
     * @param rows
     * @param address
     * @return
     */
    @POST("/merise/mobile/unit/getHouseLease")
    Observable<Response<Page<List<Rent>>>> getHouseLease(@Query("page") int page, @Query("rows") int rows, @Query("address") String address);

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
    @POST("/merise/mobile/unit/insertHouseLease")
    Observable<Response> insertHouseLease(@Query("title") String title,
                                          @Query("price") int price,
                                          @Query("address") String address,
                                          @Query("areaName") String areaName,
                                          @Query("remark") String remark,
                                          @Query("phoneNumber") String phoneNumber,
                                          @Body MultipartBody multipartBody);

    /**
     * 获取功能列表
     *
     * @return
     */
    @POST("/merise/mobile/function/getFunctionList")
    Observable<Response<Page<List<Function>>>> getFunctionList();

    /**
     * 修改设备用户操作习惯
     *
     * @param deviceID
     * @param functionCustom
     * @return
     */
    @POST("/merise/mobile/device/updateFunctionCustom")
    Observable<Response> updateFunctionCustom(@Query("deviceID") int deviceID, @Query("functionCustom") String functionCustom);

    /**
     * 激活设备
     *
     * @param sn
     * @param name
     * @return
     */
    @POST("/merise/mobile/device/activateDevice")
    Observable<Response> activateDevice(@Query("sn") String sn, @Query("name") String name);


    /**
     * 获取用户关注信息
     *
     * @param userID
     * @return
     */
    @POST("/merise/mobile/community/getOneUserInfo")
    Observable<Response<FoucsInfo>> getOneUserInfo(@Query("userID") int userID);

    /**
     * 签名
     *
     * @return
     */
    @POST("/merise/mobile/user/updateUser")
    Observable<Response> updateUserSign(@Query(Const.SIGN) String sign);

    /**
     * 更改用户名
     *
     * @return
     */
    @POST("/merise/mobile/user/updateUser")
    Observable<Response> updateUserName(@Query(Const.CHANGENAME) String userName);


    /**
     * 粉丝列表
     *
     * @return
     */
    @POST("/merise/mobile/community/getFansList")
    Observable<Response> getFansList();

    /**
     * 关注列表
     *
     * @return
     */
    @POST("/merise/mobile/community/getFocusUserList")
    Observable<Response> getFocusUserList();


    /**
     * 昵称
     *
     * @return
     */
    @POST("/merise/mobile/user/updateUser")
    Observable<Response> updateUserNickName(@Query(Const.NICKNAME) String nickName);

    /**
     * 邮箱
     *
     * @return
     */
    @POST("/merise/mobile/user/updateUser")
    Observable<Response> updateUserEmail(@Query(Const.EMAIL) String email);

    /**
     * 获取二维码数据
     */
    @POST("/merise/mobile/device/getVirtualKey")
    Observable<Response> getVirtualKey(@Query(Const.DEVICEID) String deviceID);

    /**
     * 退出登录
     */
    @POST("/merise/mobile/user/logout")
    Observable<Response> logout();

    /*
    头像
     */
    @POST("/merise/mobile/user/setUserFaceImg")
    Observable<Response> upHeadImage(@Body MultipartBody multipartBody);

    /**
     * 获取版本信息
     *
     * @param type
     * @return
     */
    @POST("/merise/common/getVersion")
    Observable<Response> getVersion(@Query("type") int type);
}
