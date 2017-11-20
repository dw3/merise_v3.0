package com.merise.net.heart.api.util;


import android.content.Context;

import com.android.framewok.util.DialogHelper;
import com.android.framewok.util.TLog;
import com.merise.net.heart.api.ApiService;
import com.merise.net.heart.application.XYApplication;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.utils.Constant;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.merise.net.heart.application.XYApplication.context;

//http://blog.csdn.net/zsf442553199/article/details/51752576?locationNum=8
/**
 * Created by wangdawang on 2016/8/30 0030.
 */
public class RetrofitUtil {
    private static final java.lang.String TAG = "RetrofitUtil";
    private static Retrofit retrofit;
    private static ApiService apiService;
    private static OkHttpClient client;

    private static Class<?> currentClass;
    private static Map<Class<?>,List<Call>> callsMap = new ConcurrentHashMap<>();
    private static Retrofit getRetrofit() {
        TLog.log(TAG, "getRetrofit...");
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            //connectTimeout(10, TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS).
            client = new OkHttpClient.Builder()
                    .cookieJar(new CookiesManager())
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(mTokenInterceptor)
                    .addInterceptor(logging).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.TestUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static void cancelAllRequest() {
        DialogHelper.stopProgressDlg();
        if (client != null)
            client.dispatcher().cancelAll();
    }
    /*
    token拦截请求器
     */
    public static Interceptor mTokenInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();


            Response response = chain.proceed(request);
            TLog.log(TAG,chain.request().url()+"");
            if(String.valueOf(chain.request().url()).contains("/merise/mobile/device/getVirtualKey")) {
                String token = response.header(Const.QRTOKEN, null);
                if (token != null) {
                    XYApplication.spt.saveSharedPreferences(Const.QRTOKEN, token);
                }
                TLog.log(TAG, "interceptor===qrtoken----" + token);
            }
            return response;
        }
    };

    public static Interceptor mNetworkInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            okhttp3.Call call = client.newCall(chain.request());
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });
            call.cancel();
            return null;
        }
    };


    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = getRetrofit().create(ApiService.class);
        }
        return apiService;
    }

    /**
     * 自动管理Cookies
     */
    private static class CookiesManager implements CookieJar {
        public final PersistentCookieStore cookieStore = new PersistentCookieStore(XYApplication.getInstance());

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    TLog.log(TAG, item.path());
                    cookieStore.add(url, item);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies;
        }
    }
}
