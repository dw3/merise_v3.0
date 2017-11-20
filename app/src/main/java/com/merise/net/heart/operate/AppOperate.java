package com.merise.net.heart.operate;


import android.content.Context;

import com.android.framewok.util.TLog;
import com.merise.net.heart.api.ApiWrapper;
import com.merise.net.heart.api.util.MultipartBody;
import com.merise.net.heart.api.util.ReplyPage;
import com.merise.net.heart.api.util.RxHelper;
import com.merise.net.heart.api.util.RxSubscriber;
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
import com.merise.net.heart.bean.NotifyState;
import com.merise.net.heart.bean.Page;
import com.merise.net.heart.bean.RecordBean;
import com.merise.net.heart.bean.RecordDetail;
import com.merise.net.heart.bean.Repair;
import com.merise.net.heart.bean.ReplyBean;
import com.merise.net.heart.bean.Response;
import com.merise.net.heart.bean.SendMessageBean;
import com.merise.net.heart.bean.Sound;
import com.merise.net.heart.bean.UpdateBean;
import com.merise.net.heart.bean.UserInfo;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.GouUtils;
import com.merise.net.heart.utils.SharedPreferencesTools;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

import rx.Observable;

import static com.merise.net.heart.application.XYApplication.context;

/**
 * Created by Administrator on 2016/9/9.
 */
public class AppOperate {
    private static final java.lang.String TAG = "AppOperate";
    public static SharedPreferencesTools spt;

    /**
     * 开门操作
     *
     * @param deviceID
     * @param context
     * @param report
     */
    public static void openDoor(String deviceID, RxAppCompatActivity context, final Report report) {
        Observable o = ApiWrapper.getApiWrapper().openDoor(deviceID).compose(new RxHelper<Response>("正在开门，请稍候...").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                TLog.log("tag", e);
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(o, rxSubscriber);
    }

    /**
     * 获取设备列表
     *
     * @param context
     * @param report
     */
    public static void getDeviceList(RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getDeviceList()
                .compose(new RxHelper<Response>("正在加载数据,请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber<List<Device>>() {

            @Override
            public void _onNext(List<Device> devices) {
                report.onSucces(devices);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }


    /**
     * 开门方式设置
     *
     * @param deviceID
     * @param opendoorWayID
     * @param context
     * @param report
     */
    public static void opendoorSetting(String deviceID, int opendoorWayID, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().opendoorWaySetting(deviceID, opendoorWayID)
                .compose(new RxHelper<Response>("正在修改开门方式，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 获取卡片列表
     *
     * @param deviceID
     * @param page
     * @param rows
     * @param report
     */
    public static void getCardList(String deviceID, int page, int rows, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getCardList(deviceID, page, rows)
                .compose(new RxHelper<Response>("初始化卡片列表，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber<List<Card>>() {

            @Override
            public void _onNext(List<Card> cards) {
                report.onSucces(cards);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe2(observable, rxSubscriber);
    }

    /**
     * 修改卡片状态
     *
     * @param deviceID
     * @param keyID
     * @param actived
     * @param context
     * @param report
     */
    public static void modifyCardState(String deviceID, int keyID, int actived, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().modifyCardState(deviceID, keyID, actived)
                .compose(new RxHelper<Response>("正在修改，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 移除卡信息
     *
     * @param deviceID
     * @param keyID
     * @param context
     * @param report
     */
    public static void deleteCard(String deviceID, int keyID, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().deleteCard(deviceID, keyID)
                .compose(new RxHelper<Response>("正在删除，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 获取指纹列表
     *
     * @param deviceID
     * @param page
     * @param rows
     * @param context
     * @param report
     */
    public static void getFingerprintList(String deviceID, int page, int rows, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getFingerprintList(deviceID, page, rows)
                .compose(new RxHelper<Response>("初始化指纹列表，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber<List<Fingerprint>>() {
            @Override
            public void _onNext(List<Fingerprint> o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe2(observable, rxSubscriber);
    }

    /**
     * 修改指纹状态
     *
     * @param deviceID
     * @param fingerID
     * @param actived
     * @param context
     * @param report
     */
    public static void modifyFingerprint(String deviceID, int fingerID, int actived, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().modifyFingerprint(deviceID, fingerID, actived)
                .compose(new RxHelper<Response>("正在修改，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 删除指纹信息
     *
     * @param deviceID
     * @param fingerID
     * @param context
     * @param report
     */
    public static void deleteFingerprint(String deviceID, int fingerID, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().deleteFingerprint(deviceID, fingerID)
                .compose(new RxHelper<Response>("正在删除，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 添加卡
     *
     * @param deviceID
     * @param keyNick
     * @param context
     * @param report
     */
    public static void addCard(String deviceID, String keyNick, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().addCard(deviceID, keyNick)
                .compose(new RxHelper<Response>("正在添加，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 修改卡片备注
     *
     * @param deviceID
     * @param keyID
     * @param keyNick
     * @param context
     * @param report
     */
    public static void modifyCardNick(String deviceID, int keyID, String keyNick, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().modifyCardNick(deviceID, keyID, keyNick)
                .compose(new RxHelper<Response>("正在修改，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 修改指纹备注
     *
     * @param deviceID
     * @param fingerID
     * @param fingerprintNick
     * @param context
     * @param report
     */
    public static void modifFingerprintNick(String deviceID, int fingerID, String fingerprintNick, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().modifyFingerprintNick(deviceID, fingerID, fingerprintNick)
                .compose(new RxHelper<Response>("正在修改，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 添加指纹
     *
     * @param deviceID
     * @param fingerprintNick
     * @param context
     * @param report
     */
    public static void addFingerprint(String deviceID, String fingerprintNick, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().addFingerprint(deviceID, fingerprintNick)
                .compose(new RxHelper<Response>("正在添加，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 获取授权用户列表
     *
     * @param deviceID
     * @param context
     * @param report
     */
    public static void getAuthUserList(String deviceID, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getAuthUserList(deviceID)
                .compose(new RxHelper<Response>("正在初始化用户列表，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber<List<AuthUser>>() {
            @Override
            public void _onNext(List<AuthUser> o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 启用禁用授权用户
     *
     * @param userID
     * @param deviceID
     * @param actived
     * @param context
     * @param report
     */
    public static void modifyAuthUserState(String userID, String deviceID, int actived, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().modifyAuthUserState(userID, deviceID, actived)
                .compose(new RxHelper<Response>("正在修改，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 删除授权用户
     *
     * @param userID
     * @param deviceID
     * @param context
     * @param report
     */
    public static void deleteAuthUser(String userID, String deviceID, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().deleteAuthUser(userID, deviceID)
                .compose(new RxHelper<Response>("正在删除，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 添加获取修改授权用户
     *
     * @param deviceID
     * @param userName
     * @param userNick
     * @param permissionIDs
     * @param context
     * @param report
     */
    public static void modifyOrAddAuthUser(String deviceID, String userName, String userNick,
                                           String permissionIDs, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().modifyOrAddAuthUser(deviceID, userName, userNick, permissionIDs)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 获取用户指定权限
     *
     * @param deviceID
     * @param userID
     */
    public static void getAuthPermissions(String deviceID, String userID, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getAuthUserPermissions(deviceID, userID)
                .compose(new RxHelper<Response>("正在初始化，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber<List<AuthPermission>>() {
            @Override
            public void _onNext(List<AuthPermission> o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 自动回锁时间设置
     *
     * @param deviceID
     * @param autoCloseTime
     * @param context
     * @param report
     */
    public static void autoCloseTimeSetting(String deviceID, int autoCloseTime, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().autoCloseTimeSetting(deviceID, autoCloseTime)
                .compose(new RxHelper<Response>("正在设置，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 获取门铃信息
     *
     * @param deviceID
     * @param context
     * @param report
     */
    public static void getSoundVolume(String deviceID, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getSoundVolume(deviceID)
                .compose(new RxHelper<Response>("正在初始化，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber<Sound>() {
            @Override
            public void _onNext(Sound o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 设置门铃信息
     *
     * @param deviceID
     * @param sound
     * @param volume
     * @param context
     * @param report
     */
    public static void setSoundVolume(String deviceID, int sound, int volume, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().setSoundVolume(deviceID, sound, volume)
                .compose(new RxHelper<Response>("正在设置，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 修改门禁备注
     *
     * @param deviceID
     * @param name
     * @param context
     * @param report
     */
    public static void modifyDoorNick(String deviceID, String name, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().modifyDoorNick(deviceID, name)
                .compose(new RxHelper<Response>("正在修改，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 获取物业公告
     *
     * @param page
     * @param rows
     * @param type
     * @param context
     * @param report
     */
    public static void getPropertyAnnouncement(int page, int rows, int type, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getPropertyAnnouncement(page, rows, type)
                .compose(new RxHelper<Response>("正在初始化，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber<List<Announcement>>() {
            @Override
            public void _onNext(List<Announcement> announcements) {
                report.onSucces(announcements);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe2(observable, rxSubscriber);
    }

    /**
     * 获取交流列表
     *
     * @param context
     * @param report
     */
    public static void getCommunityList(int page, int rows, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getCommunityList(page, rows)
                .compose(new RxHelper<Response>("正在初始化，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber<List<Article>>() {
            @Override
            public void _onNext(List<Article> articles) {
                report.onSucces(articles);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe2(observable, rxSubscriber);
    }

    /**
     * 新增物业报修
     *
     * @param repairType
     * @param attaChmentType
     * @param content
     * @param multipartBody
     * @param context
     * @param report
     */
    public static void insertPropertyRepair(int repairType, int attaChmentType, String content, int parentType, int areaID, MultipartBody multipartBody, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().insertPropertyRepair(repairType, attaChmentType, content, parentType, areaID, multipartBody)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
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
     * @param context
     * @param report
     */
    public static void insertArticle(String content, int type, String time, String place, int scope, int attaChmentType, int parentType, List<MultipartBody.Part> parts, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().insertArticle(content, type, time, place, scope, attaChmentType, parentType, parts)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    public static void insertArticle(String content, int type, String time, String place, int scope, int attaChmentType, int parentType, MultipartBody multipartBody, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().insertArticle(content, type, time, place, scope, attaChmentType, parentType, multipartBody)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 上传头像
     */

    public static void upHeadImage(MultipartBody multipartBody, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().upHeadImage(multipartBody)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 退出登录
     */
    public static void logout(RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().logout()
                .compose(new RxHelper<Response>("正在退出，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }


    /**
     * 昵称
     */
    public static void updateUserNickName(String nickName, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().updateUserNickName(nickName)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 邮箱
     */
    public static void updateUserEmail(String email, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().updateUserEmail(email)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }


    /**
     * 更改用户名
     */
    public static void updateUserName(String userName, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().updateUserName(userName)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 签名
     */
    public static void updateUserSign(String sign, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().updateUserSign(sign)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }


    /**
     * 点赞
     *
     * @param articleID
     * @param context
     * @param report
     */
    public static void insertLaud(int articleID, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().insertLaud(articleID)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 新增社区交流评论
     *
     * @param article
     * @param comment
     * @param context
     * @param report
     */
    public static void insertComment(int article, String comment, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().insertComment(article, comment)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 取消赞
     *
     * @param articleID
     * @param context
     * @param report
     */
    public static void deleteLaud(int articleID, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().deleteLaud(articleID)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 获取单个用户的动态
     *
     * @param page
     * @param rows
     * @param userID
     * @param context
     * @param report
     */
    public static void getOneUserArticleList(int page, int rows, int userID, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getOneUserArticleList(page, rows, userID)
                .compose(new RxHelper<Response>("正在处理,请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber<List<Article>>() {
            @Override
            public void _onNext(List<Article> articles) {
                report.onSucces(articles);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe2(observable, rxSubscriber);
    }

    /**
     * 获取区域列表
     *
     * @param context
     * @param report
     */
    public static void getMyAreaList(RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getMyAreaList()
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber<List<Community>>() {
            @Override
            public void _onNext(List<Community> communities) {
                report.onSucces(communities);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe2(observable, rxSubscriber);
    }

    /**
     * 获取物业报修列表
     *
     * @param page
     * @param rows
     * @param type
     * @param context
     * @param report
     */
    public static void getListWuyeRepair(int page, int rows, int type, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getListWuyeRepair(page, rows, type)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber<List<Repair>>() {
            @Override
            public void _onNext(List<Repair> repairs) {
                report.onSucces(repairs);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe2(observable, rxSubscriber);
    }

    /**
     * 关注
     *
     * @param focusID
     * @param type
     * @param context
     * @param report
     */
    public static void insertFocus(int focusID, int type, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().insertFocus(focusID, type)
                .compose(new RxHelper<Response>("正在处理,请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 取消关注
     *
     * @param focusID
     * @param type
     * @param context
     * @param report
     */
    public static void cancelFocus(int focusID, int type, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().cancelFocus(focusID, type)
                .compose(new RxHelper<Response>("正在处理，请稍后").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 评价
     *
     * @param id
     * @param satisfaction
     * @param remark
     * @param status
     * @param context
     * @param report
     */
    public static void insertEvaluate(int id, int satisfaction, String remark, int status, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().insertEvaluate(id, satisfaction, remark, status)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 获取租赁信息
     *
     * @param page
     * @param rows
     * @param address
     * @param context
     * @param report
     */
    public static void getHouseLease(int page, int rows, String address, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getHouseLease(page, rows, address)
                .compose(new RxHelper<Response>("正在处理，请稍后").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe2(observable, rxSubscriber);
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
     * @param context
     * @param report
     */
    public static void insertHouseLease(String title, int price, String address, String areaName,
                                        String remark, String phoneNumber, MultipartBody multipartBody,
                                        RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().insertHouseLease(title, price, address, areaName, remark, phoneNumber, multipartBody)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 获取功能模块
     *
     * @param context
     * @param report
     */
    public static void getFunctionList(RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getFunctionList()
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context, false));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe2(observable, rxSubscriber);
    }


    /**
     * 获取登录设备列表
     */
    public static void getLoginUsers(RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getLoginUsers()
                .compose(new RxHelper<Response>("正在处理数据").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber<List<CommonUser>>() {

            @Override
            public void _onNext(List<CommonUser> loginUserList) {
                report.onSucces(loginUserList);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 删除解绑常用设备列表
     */
    public static void deleteMobile(String ID, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().deleteMobile(ID)
                .compose(new RxHelper<Response>("正在删除").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {

            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
                TLog.log(TAG, "onerror----" + e.toString());
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 登录操作
     *
     * @param username
     * @param password
     * @param imei
     * @param model
     * @param type
     */
    public static void getLogin(String username, String password, String imei, String model,
                                int type, String versionName, final RxAppCompatActivity context, final Report report) {
        if (type == 1) {
            //被观察者的引用，决定了触发什么样的事件
            //因为这句话的缘故new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            final Observable<Response> observable = ApiWrapper.getApiWrapper()
                    //type为1表示普通登录，type为0表示带验证码登录
                    .login(username, password, imei,"15244214", model, versionName)
                    //弹一个正在登陆的对话框
                    .compose(new RxHelper<Response>("正在登陆，请稍后").io_main(context));

            //订阅执行对象
            RxSubscriber rxSubscriber = new RxSubscriber<UserInfo>() {
                @Override
                public void _onNext(UserInfo userInfo) {
                    TLog.log(TAG + "userInfo----" + userInfo);
                    report.onSucces(userInfo);
                }

                @Override
                public void _onError(String e) {
                    report.onError(e);
                }
            };
            ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
        }
    }

    /**
     * 获取开门记录数据
     *
     * @param deviceid
     * @param logtype
     * @param page
     * @param pagesize
     */
    public static void getRecordList(String deviceid, final String logtype,
                                     final int page, String pagesize, final RxAppCompatActivity context, final Report report) {

        Observable<Response<Page<List<RecordBean>>>> observable = ApiWrapper.getApiWrapper()
                .getRecordList(deviceid, logtype, page, pagesize)
                .compose(new RxHelper<Response<Page<List<RecordBean>>>>("正在获取数据").io_main(context, false));

        RxSubscriber rxSubscriber = new RxSubscriber<List<RecordBean>>() {
            @Override
            public void _onNext(List<RecordBean> recordBeen) {
                GouUtils.logE(TAG, "recordBean----" + recordBeen.size());
                report.onSucces(recordBeen);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribePage(observable, rxSubscriber);
    }


    /**
     * 获取记录数据
     * (下拉刷新用，page指定为1)
     *
     * @param deviceid
     * @param logtype
     * @param page
     * @param pagesize
     */
    public static void getRecordList1(String deviceid, final String logtype,
                                      final int page, String firstVisibleID, String pagesize,
                                      final RxAppCompatActivity context, final Report report) {

        Observable<Response<Page<List<RecordBean>>>> observable = ApiWrapper.getApiWrapper()
                .getRecordList1(deviceid, logtype, 1, firstVisibleID, pagesize)
                .compose(new RxHelper<Response<Page<List<RecordBean>>>>("正在获取数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber<List<RecordBean>>() {
            @Override
            public void _onNext(List<RecordBean> recordBeen) {
                GouUtils.logE(TAG, "下拉刷新recordBean----" + recordBeen.size());
                report.onSucces(recordBeen);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribesSuggestPage(observable, rxSubscriber);
    }


    /**
     * 获取门铃门磁刷新记录数据
     * (下拉刷新用，page指定为1)
     *
     * @param deviceid
     * @param logtype
     * @param page
     * @param pagesize
     */
    public static void getRecordListDoor1(String deviceid, final String logtype,
                                          final int page, String firstVisibleID, String pagesize,
                                          final RxAppCompatActivity context, final Report report) {

        Observable<Response<Page<List<DoorBell>>>> observable = ApiWrapper.getApiWrapper()
                .getRecordListDoor1(deviceid, logtype, 1, firstVisibleID, pagesize)
                .compose(new RxHelper<Response<Page<List<DoorBell>>>>("正在获取数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber<List<DoorBell>>() {
            @Override
            public void _onNext(List<DoorBell> recordBeen) {
                GouUtils.logE(TAG, "下拉刷新recordBean----" + recordBeen.size());
                report.onSucces(recordBeen);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribePage(observable, rxSubscriber);
    }

    /**
     * 获取门铃和门磁记录数据
     * (下拉刷新用，page指定为1)
     *
     * @param deviceid
     * @param logtype
     * @param page
     * @param pagesize
     */
    public static void getRecordListDoor(String deviceid, final String logtype,
                                         final int page, String pagesize,
                                         final RxAppCompatActivity context, final Report report) {

        Observable<Response<Page<List<DoorBell>>>> observable = ApiWrapper.getApiWrapper()
                .getRecordListDoor(deviceid, logtype, page, pagesize)
                .compose(new RxHelper<Response<Page<List<DoorBell>>>>("正在获取数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber<List<DoorBell>>() {
            @Override
            public void _onNext(List<DoorBell> recordBeen) {
                TLog.log(TAG, "门铃门磁" + recordBeen.size());
                report.onSucces(recordBeen);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribePage(observable, rxSubscriber);
    }


    /**
     * 获取记录详情
     *
     * @param deviceid
     * @param logtype
     */
    public static void getRecordInfo(String deviceid, final String logId,
                                     String logtype, final RxAppCompatActivity context, final Report report) {

        Observable<Response> observable = ApiWrapper.getApiWrapper()
                .getRecordDetail(deviceid, logId, logtype).
                        compose(new RxHelper<Response>("正在加载数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber<RecordDetail>() {

            @Override
            public void _onNext(RecordDetail o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }


    /**
     * 获取提示管理详情
     *
     * @param deviceid
     */
    public static void getNotifyState(String deviceid,
                                      final RxAppCompatActivity context, final Report report) {

        Observable<Response<NotifyState>> observable = ApiWrapper.getApiWrapper()
                .getNotifyState(deviceid).
                        compose(new RxHelper<Response<NotifyState>>("正在加载数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber<NotifyState>() {

            @Override
            public void _onNext(NotifyState o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }


    /**
     * 上传提示管理变更信息详情
     *
     * @param deviceid
     */
    public static void notifySetting(String deviceid, String activeId, String permissionId,
                                     final RxAppCompatActivity context, final Report report) {

        Observable<Response> observable = ApiWrapper.getApiWrapper()
                .notifySetting(deviceid, activeId, permissionId).
                        compose(new RxHelper<Response>("正在处理数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber() {

            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 设置紧急拨号号码
     *
     * @param deviceid
     */
    public static void setEmergency(String deviceid, String phone,
                                    final RxAppCompatActivity context, final Report report) {

        Observable<Response> observable = ApiWrapper.getApiWrapper()
                .setEmergency(deviceid, phone).
                        compose(new RxHelper<Response>("正在处理数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber() {

            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }


    /**
     * 设防撤防
     *
     * @param deviceID
     * @param fortify
     * @param context
     * @param report
     */
    public static void defenceSetting(String deviceID, int fortify, RxAppCompatActivity context, final Report report) {
        String message = fortify == 0 ? "即将撤防，请稍候" : "即将设防，请稍候";
        Observable observable = ApiWrapper.getApiWrapper().defenceSetting(deviceID, fortify)
                .compose(new RxHelper<Response>(message).io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }


    /**
     * 获取意见建议中意见数据
     *
     * @param page
     * @param pagesize
     */
    public static void getOpinions(final int page, int pagesize, final RxAppCompatActivity context, final Report report) {

        Observable<Response<ReplyPage<List<ReplyBean>>>> observable = ApiWrapper.getApiWrapper()
                .getOpinions(page, pagesize)
                .compose(new RxHelper<Response<ReplyPage<List<ReplyBean>>>>("正在获取数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber<List<ReplyBean>>() {
            @Override
            public void _onNext(List<ReplyBean> suggestionBean) {
                GouUtils.logE(TAG, "suggestionBean----" + suggestionBean.size());
                report.onSucces(suggestionBean);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribesSuggestPage(observable, rxSubscriber);
    }

    /**
     * 获取意见建议中回复数据
     *
     * @param page
     * @param pagesize
     */
    public static void getReply(final int page, int pagesize, final RxAppCompatActivity context, final Report report) {

        Observable<Response<ReplyPage<List<ReplyBean>>>> observable = ApiWrapper.getApiWrapper()
                .getReply(page, pagesize)
                .compose(new RxHelper<Response<ReplyPage<List<ReplyBean>>>>("正在获取数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber<List<ReplyBean>>() {
            @Override
            public void _onNext(List<ReplyBean> replyBean) {
                GouUtils.logE(TAG, "recordBean----" + replyBean.size());
                report.onSucces(replyBean);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribeReplyPage(observable, rxSubscriber);
    }

    /**
     * 提交意见建议
     */
    public static void sendOpinnion(String content, final RxAppCompatActivity context, final Report report) {

        Observable<Response<SendMessageBean>> observable = ApiWrapper.getApiWrapper()
                .sendOpinnion(content)
                .compose(new RxHelper<Response<SendMessageBean>>("正在获取数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber<SendMessageBean>() {
            @Override
            public void _onNext(SendMessageBean sendMessageBeanResponse) {
                report.onSucces(sendMessageBeanResponse);
                TLog.log(TAG, "提交意见成功给了sendMessageBeanResponse");
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }


    /**
     * 删除问题
     */
    public static void deleteQuestion(int questionID,
                                      final RxAppCompatActivity context, final Report report) {

        Observable<Response> observable = ApiWrapper.getApiWrapper()
                .deleteQuestion(questionID).
                        compose(new RxHelper<Response>("正在处理数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber() {

            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 修改密码
     */
    public static void updatePassword(String newPassword,
                                      final RxAppCompatActivity context, final Report report) {

        Observable<Response> observable = ApiWrapper.getApiWrapper()
                .updatePassword(newPassword).
                        compose(new RxHelper<Response>("正在处理数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber() {

            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }


    /**
     * 获取验证码
     * （修改手机号）
     * （注册）
     * （首页忘记密码）
     */
    public static void getValidateCode(String name, String mobile, String codetype,
                                       final RxAppCompatActivity context, final Report report) {

        Observable<Response> observable = ApiWrapper.getApiWrapper()
                .getValidateCode(name, mobile, codetype).
                        compose(new RxHelper<Response>("正在处理数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber() {


            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                TLog.log(TAG, "e----" + e.toString());
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 注册
     */
    public static void regist(String name, String mobile, String password, String registrationID, String validateCodeVal,
                              final RxAppCompatActivity context, final Report report) {

        Observable<Response> observable = ApiWrapper.getApiWrapper()
                .regist(name, mobile, password, registrationID, validateCodeVal).
                        compose(new RxHelper<Response>("正在注册").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber() {

            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                TLog.log(TAG, "e----" + e.toString());
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 首页忘记密码第一步
     */
    public static void findPasswordFirstStep(String name, String validateCodeVal,
                                             final RxAppCompatActivity context, final Report report) {

        Observable<Response> observable = ApiWrapper.getApiWrapper()
                .findPasswordFirstStep(name, validateCodeVal).
                        compose(new RxHelper<Response>("正在处理").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber() {

            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
//                TLog.log(TAG, "第一步失败");
//                TLog.log(TAG, "e----" + e.toString());
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 首页忘记密码第二步
     */
    public static void rebuildPassword(String newPassword,
                                       final RxAppCompatActivity context, final Report report) {

        Observable<Response> observable = ApiWrapper.getApiWrapper()
                .rebuildPassword(newPassword).
                        compose(new RxHelper<Response>("正在处理").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber() {

            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                TLog.log(TAG, "e----" + e.toString());
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 修改手机号 （保存修改）
     */
    public static void changeAccount(String oldname, String mobile, String validcode,
                                     final RxAppCompatActivity context, final Report report) {

        Observable<Response> observable = ApiWrapper.getApiWrapper()
                .changeAccount(oldname, mobile, validcode).
                        compose(new RxHelper<Response>("正在处理数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber() {

            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                TLog.log(TAG, "e----" + e.toString());
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }


    /**
     * 获取用户协议
     */
    public static void getAgreement(final RxAppCompatActivity context, final Report report) {

        Observable<Response<Agreement>> observable = ApiWrapper.getApiWrapper()
                .getAgreement().
                        compose(new RxHelper<Response<Agreement>>("正在处理数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber<Agreement>() {

            @Override
            public void _onNext(Agreement agreement) {
                report.onSucces(agreement);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 获取用户协议
     */
    public static void checkUpdate(String type, final RxAppCompatActivity context, final Report report) {

        Observable<Response<UpdateBean>> observable = ApiWrapper.getApiWrapper()
                .checkUpdate(type).
                        compose(new RxHelper<Response<UpdateBean>>("正在处理数据").io_main(context));

        RxSubscriber rxSubscriber = new RxSubscriber<UpdateBean>() {

            @Override
            public void _onNext(UpdateBean updateBean) {
                report.onSucces(updateBean);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 修改用户操作习惯
     *
     * @param deviceID
     * @param functionCustom
     * @param context
     * @param report
     */
    public static void updateFunctionCustom(int deviceID, String functionCustom, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().updateFunctionCustom(deviceID, functionCustom)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 激活设备
     *
     * @param sn
     * @param name
     * @param context
     * @param report
     */
    public static void activateDevice(String sn, String name, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().activateDevice(context,sn, name)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }


    /**
     * 获取二维码数据
     */
    public static void getVirtualKey(String deviceID, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getVirtualKey(deviceID)
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 获取粉丝列表
     */
    public static void getFansList(RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getFansList()
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 获取关注列表
     */
    public static void getFocusUserList(RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getFocusUserList()
                .compose(new RxHelper<Response>("正在处理，请稍候").io_main(context));
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }


    /**
     * 获取用户关注信息
     *
     * @param userID
     * @param context
     * @param report
     */
    public static void getOneUserInfo(int userID, RxAppCompatActivity context, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getOneUserInfo(userID)
                .compose(new RxHelper<Response>("正在处理,请处理").io_main(context, false));
        RxSubscriber rxSubscriber = new RxSubscriber<FoucsInfo>() {
            @Override
            public void _onNext(FoucsInfo o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }

    /**
     * 获取版本号
     *
     * @param type
     * @param report
     */
    public static void getVersion(int type, final Report report) {
        Observable observable = ApiWrapper.getApiWrapper().getVersion(type);
        RxSubscriber rxSubscriber = new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                report.onSucces(o);
            }

            @Override
            public void _onError(String e) {
                report.onError(e);
            }
        };
        ApiWrapper.getApiWrapper().toSubscribe(observable, rxSubscriber);
    }
}
