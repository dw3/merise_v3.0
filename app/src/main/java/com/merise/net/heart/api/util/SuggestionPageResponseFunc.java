package com.merise.net.heart.api.util;


import com.android.framewok.util.TLog;
import com.merise.net.heart.bean.Response;
import com.merise.net.heart.exception.ApiException;

import rx.functions.Func1;

public class SuggestionPageResponseFunc<T> implements Func1<Response<ReplyPage<T>>, T> {

    private static final String TAG = "PageResponseFunc";

    @Override
    public T call(Response<ReplyPage<T>> response) {
        if (response != null) {
            TLog.log(TAG, response.toString());
        }
        if (response.stateCode == ApiException.LOGIN_205) {
            throw new ApiException(205);
        } else if (response.stateCode == ApiException.OPERATE_FAIL_201) {
            throw new ApiException(201);
        } else if (response.stateCode == ApiException.SYSTEM_ERROR_500) {
            throw new ApiException(500);
        } else if (response.stateCode == ApiException.NODATA_203) {
            throw new ApiException(203);
        }
        return response.data.rows;
    }
}