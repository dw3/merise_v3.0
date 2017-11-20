package com.merise.net.heart.utils;

/**
 * @author xiangyang
 * @version 1.0
 * @Date 2013 2013-00-00 00:00:00
 * @Copyright (c) 2010-2013, ZuQiangShen All Rights Reserved.
 */
public class Const {
    public static final int GETDATE = 0X0001;
    public static final int DISABLE = 0X0002;// 禁用
    public static final int DELETE = 0X0003;// 刪除
    public static final int CHANGE = 0X0004;// 修改
    public static final int GENERALNOTIFY = 0X0005;// 通用提醒
    public static final int GETDEVICESN = 0X0002;// 设备SN

    public static final String SHAREPREFRENCE = "shareprefrence";

    public static final int ACTIVITYRESULTCODE1 = 0X0001;

    public static final int ACCESSCARDMANAGER = 0X0001;// 门禁卡管理页面（跳转到修改备注）
    public static final int ROOMSMANAGER = 0X0002;// 房屋管理管理页面（跳转到修改备注）
    public static final int UPDATANAME = 0X0000;// 房屋管理管理页面（修改备注）
    public static final int UPDATEDOORCARDNICK = 0X0003;// 修改门禁备注
    // 各个字段名
    public static final String STATUSCODE = "statusCode"; // 状态码1
    public static final String STATECODE = "stateCode";// 状态码2
    public static final String ACCESSCARDS = "accessCards";// 门禁卡列表
    public static final String ACCESSCARD = "accessCard";// 门禁卡
    public static final String MESSAGE = "message";// 信息字段
    public static final String DEVICEID = "deviceID";// 设备id
    public static final String EMERGENCYREMARK = "emergencyremark";// 紧急拨号的备注信息
    public static final String EMERGENCYREMARK_NUM = "emergencyremarkNum";// 紧急拨号的备注信息
    public static final String KEYID = "keyID";// 门禁id
    public static final String CHANGE_USERNAME = "new_username";// 门禁id
    public static final String TYPEID = "typeID";// 判断单元门禁入户门禁字段
    public static final String KEYNICK = "keyNick";// 门禁昵称
    public static final String ROWS = "rows";// 每页显示条数
    public static final String PAGE = "page";// 当前页
    public static final String DATA = "data";// 数据
    public static final String TOTAL = "total";// 总数
    public static final String SN = "sn";// sn码
    public static final String NAME = "name";// 门禁名称
    public static final String AGREEMENT = "agreement";
    public static final String USERID = "userID";// 用户id
    public static final String NETHEADPATH = "newheadpath";// 头像网络获取的绝对路径

    public static final String OLDPASSWORD = "oldPassword";// 旧密码
    public static final String NEWPASSWORD = "newPassword";// 新密码

    public static final String OLD = "old";// 新版界面旧密码
    public static final String NEW = "new";// 新版界面新密码

    public static final String FINGER = "finger";
    public static final String ISUPDATE = "isUpdate";
    public static final String FINGERID = "fingerID";// 指纹id
    public static final String ISLOCK = "isLock";// 是否上锁
    // public static final String SAFEDOOR = "safedoor";// SharedPreferences对象
    public static final String NICKNAME = "nickName";// 保存在SharedPreferences里面的nickName
    public static final String REGISTRATIONID = "registrationID";// 手机用户的唯一标识
    public static final String PASSWORD = "password";// 密码
    public static final String ADD = "add";// 添加
    public static final String QUICK_NAME = "quick_name";// 快速拨打人名称
    public static final String QUICK_CALL = "quick_call";// 快速拨打
    public static final String ISUPDATECONFIG = "isUpdateConf";
    public static final String PROPERTY = "property";
    public static final String ESTATEID = "estateID";
    public static final String FINGERPRINTNICK = "fingerprintNick";// 指纹备注
    public static final String OPENDOORHINT = "openDoorHint";// 开门标识
    public static final String DOORBELLHINT = "doorBellHint";// 门铃标识
    public static final String POWERHINT = "powerHint";// 电量标识
    public static final String DOORSENSORHINT = "doorSensorHint";// 门磁标识
    public static final String DISMANTLEHINT = "dismantleHint";// 拆卸标识
    public static final String CONTENT = "content";
    public static final String LOGID = "logID";
    public static final String LOGTYPE = "logType";
    public static final String OPENDOOR = "openDoor";// （方式）开门
    public static final String DOORBELL = "doorBell";// 门铃
    public static final String DOORSENSOR = "doorSensor";// 门磁提醒
    public static final String LOGDATE = "logDate";// 记录日期
    public static final String IMAGES = "images";// 抓拍的图片
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String VALIDCODE = "validCode";
    public static final String PHONE = "phone";
    public static final String AUTHUSER = "authuser";
    public static final String USERNAME = "userName";
    public static final String CHANGENAME = "Name";
    public static final String USERNICK = "userNick";
    public static final String ROLEIDS = "roleIDs";
    public static final String ROLEID = "roleID";
    public static final String ACTIVED = "actived";
    public static final String ALARM = "alarm";
    public static final String STOP_DOORBELL_ACTION = "stop_doorBell_action";
    public static final String STOP_OTHER_ACTION = "stop_other_action";
    public static final String STOP_OPENDOOR_ACTION = "stop_opendoor_action";
    public static final String ID = "id";
    public static final String OPENDOORWAYID = "openDoorWayID";
    public static final String ADMINID = "adminID";
    public static final String ISFORTIFY = "isfortify";
    public static final String AUTOCLOSETIME = "autoCloseTime";
    public static final String ALARMTIME = "alarmTime";
    public static final String DEVICENICK = "deviceNick";
    public static final String type = "type";
    public static final String messageID = "messageID";
    public static final String VOLUME = "volume";
    public static final String SOUND = "sound";
    public static final String VERSIONCODE = "versionCode";
    public static final String VERSIONURL = "url";
    public static final String VERSIONNAME = "name";
    public static final String TAG = "tag";
    public static final String VERSION = "version";

    public static final String REPEATPASSWORD = "repeatPassword";
    public static final String SERVERADDRESS = "serverAddress";
    public static final String IMAGE = "image";
    public static final String BELL = "bell";
    public static final String LOCK = "lock";
    public static final String MAGNET = "magnet";
    public static final String REPLY = "reply";
    public static final String CREATETIME = "createTime";
    public static final String PID = "pid";
    public static final String CLEARPUSHCOUNT = "clearPushCount";
    public static final String CLEAROPENDOORCOUNT = "clearOpendoorCount";
    public static final String CLEARDOORBELLCOUNT = "clearDoorbellCount";
    public static final String CLEAR_OPENDOOR_PUSH = "clear_opendoor_push";
    public static final String PERMISSIONID = "permissionID";
    public static final String PERMISSIONIDS = "permissionIDs";
    public static final String EMERGENCYNUM = "emergencyNumber";
    public static final String FEEDBACKID = "feedBackID";
    public static final String ROLES = "roles";
    public static final String UNREAD = "unread";
    public static final String SAVEUSERNAME = "saveUsername";
    public static final String IMEI = "imei";
    public static final String MODEL = "model";
    public static final String APPVERSION = "appVersion";
    public static final String BINDCODE = "bindCode";
    public static final String DOORMAGNET = "doorMagnet";
    public static final String ONLINE = "online";
    public static final String FINISHACTIVITY = "finishActivity";
    public static final String OLDNAME = "oldname";
    public static final String DEVICENAME = "deviceName";
    public static final String HEAD_PATH = "head_path";
    public static final String CODETYPE = "codeType";
    public static final String QUESTIONID = "questionID";
    public static final String ANSWER = "answer";
    public static final String TITLE = "title";
    public static final String QUESTION = "question";
    public static final String FILE = "file";
    public static final String FACEIMG = "faceImg";
    public static final String UPHEADIMG = "upheadimg";// 上传头像
    public static final String SIGN = "sign";// 上传签名
    public static final String QRTOKEN = "QRToken";// 二维码签名


    //HXPUSH
    public static final String HXNAME = "hxName";
    public static final String MESSAGEID = "messageID";
    public static final String URL = "url";
    public static final String TYPE = "type";
    public static final String HXALARM = "alarm";
    public static final String ALARMTEXT = "AlarmText";
    public static final String HXPUSHDOORBEll = "hxpushdoorbell";
    public static final String HXPUSHALARM = "hxpushalarm";
    public static final String HXPUSHUNLOGIN = "hxpushunlogin";
    public static final String HXPUSHOPENDOOR = "hxpushopendoor";
    public static final String HXPUSHOTHER = "hxpushother";
    public static final String UNLOGIN = "unlogin";
    public static final String OTHER = "other";
}
