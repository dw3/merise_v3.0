package com.merise.net.heart.exception;



import com.android.framewok.util.TLog;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ApiException extends RuntimeException {
    public static final int OPERATE_FAIL_201 = 201;
    public static final int SYSTEM_ERROR_500 = 500;
    public static final int SUCCESS_200 = 200;
    public static final int NODATA_203 = 203;
    public static final int VALID_FAILE_202 = 202;
    public static final int LOGIN_205 = 205;
    private static final java.lang.String TAG = "ApiException";

    public ApiException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     * @param code
     * @return
     */
    public static String getApiExceptionMessage(int code){
        String message;
        switch (code) {
            case OPERATE_FAIL_201:
                message = "操作失败";
                break;
            case SYSTEM_ERROR_500:
                message = "系统错误";
                break;
            case LOGIN_205:
                message = "请登录";
                break;
            case SUCCESS_200:
                message = "操作成功";
                break;
            case NODATA_203:
                message = "查询数据为空";
                break;
            case VALID_FAILE_202:
                message = "验证失败";
                break;
            default:
                message = "未知错误";
                break;

        }
//        TLog.log(TAG,message);
        return message;
    }
}
