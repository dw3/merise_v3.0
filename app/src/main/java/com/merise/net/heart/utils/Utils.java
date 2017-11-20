package com.merise.net.heart.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.framewok.util.TLog;

import static android.R.attr.versionCode;

/**
 * 工具类
 *
 * @author ethan
 */
@SuppressLint({"SdCardPath", "DefaultLocale"})
public class Utils {
    /**
     * 土司
     *
     * @param context
     * @param msg
     */
    public static void showTip(Context context, String msg) {
        if (msg != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null || str.trim().length() == 0 || str.trim().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 是否是手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobile(String mobiles) {
        // Pattern p = Pattern
        // .compile("^((13[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$");
        Pattern p = Pattern.compile("^1[34578][0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 是否是固话
     *
     * @param tel
     * @return
     */
    public static boolean isTelePhone(String tel) {
        // Pattern p = Pattern
        // .compile("1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}");
        Pattern p = Pattern.compile("^(0[1-9]{2,3}-)[0-9]{7,8}$");
        Matcher m = p.matcher(tel);
        return m.matches();
    }

    /**
     * 邮件地址验证
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String patten = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern regex = Pattern.compile(patten);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    /**
     * 设备网络状态监测
     *
     * @param context
     * @return
     */
    public static boolean hasNet(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }
        return true;
    }

    /**
     * 转换dip为px
     *
     * @param context
     * @param dip
     * @return
     */
    public static int Dip2Px(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 转换px为dip
     *
     * @param context
     * @param px
     * @return
     */
    public static int Px2Dip(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    /**
     * 转换sp为px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int Sp2Px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 转换px为sp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int Px2Sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 获取图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void reg(final Context cont, Bitmap photodata, String regData) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // 将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
            photodata.compress(Bitmap.CompressFormat.PNG, 100, baos);
            baos.close();
            byte[] buffer = baos.toByteArray();
            System.out.println("图片的大小：" + buffer.length);

            // 将图片的字节流数据加密成base64字符输出
            // String photo = Base64.encodeToString(buffer, 0, buffer.length,
            // Base64.DEFAULT);
            // String p = new String(buffer);
            // photo=URLEncoder.encode(photo,"UTF-8");
            // RequestParams params = new RequestParams();
            // params.put("photo", photo);
            // params.put("name", "woshishishi");// 传输的字符数据
            // String url =
            // "http://10.0.2.2:8080/IC_Server/servlet/RegisterServlet1";

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param imgPath
     * @param bitmap
     * @param imgFormat 图片格式
     * @return
     */
    public static String imgToBase64(String imgPath, Bitmap bitmap,
                                     String imgFormat) {
        if (imgPath != null && imgPath.length() > 0) {
            bitmap = readBitmap(imgPath);
        }
        if (bitmap == null) {
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Bitmap readBitmap(String imgPath) {
        try {
            return BitmapFactory.decodeFile(imgPath);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * @param base64Data
     * @param imgName
     * @param imgFormat  图片格式
     */
    public static void base64ToBitmap(String base64Data, String imgName,
                                      String imgFormat) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        File myCaptureFile = new File("/sdcard/", imgName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myCaptureFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        boolean isTu = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        if (isTu) {
            // fos.notifyAll();
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取软件版名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionName
            versionName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 判断应用是处于前台运行还是后台运行
     *
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
            Log.i(context.getPackageName(), "应用未启动" + appProcess.processName);
        }
        return false;
    }

    /**
     * 判断应用是否启动
     *
     * @param context
     * @return
     */
    public static boolean isRunning(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcessInfos = am
                .getRunningAppProcesses();
        for (RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.processName.equals(context.getPackageName())) {
                System.out.println("在运行...");
                return true;
            }
        }
        return false;
    }

    /**
     * 获取栈顶的activity
     * <p>
     * info.topActivity.getShortClassName(); //类名
     * info.topActivity.getClassName(); //完整类名
     * info.topActivity.getPackageName(); //包名
     *
     * @return
     */
    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        if (runningTaskInfos != null)
            return (runningTaskInfos.get(0).topActivity).getClassName();
        else
            return null;
    }

    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION-"
            + UUID.nameUUIDFromBytes("androidkit".getBytes());


    /**
     * 将表示此设备在该程序上的唯一标识符写入程序文件系统中。
     *
     * @param installation 保存唯一标识符的File对象。
     * @return 唯一标识符。
     * @throws IOException IO异常。
     */
    private static String readInstallationFile(File installation)
            throws IOException {
        RandomAccessFile accessFile = new RandomAccessFile(installation, "r");
        byte[] bs = new byte[(int) accessFile.length()];
        accessFile.readFully(bs);
        accessFile.close();
        return new String(bs);
    }


    /**
     * 获取设备的型号
     *
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取设备的厂商
     *
     * @return
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 根据ip地址获取详细地址
     *
     * @param context
     * @param ip
     * @return
     */
    static String result = null;

    public static void getAddressByIP(final Context context, final String ip,
                                      final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = "http://whois.pconline.com.cn/ip.jsp?ip=" + ip;
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    Message msg = Message.obtain();
                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buff = new byte[100];
                        int rc = 0;
                        while ((rc = is.read(buff, 0, 100)) > 0) {
                            baos.write(buff, 0, rc);
                        }
                        byte[] data = baos.toByteArray();
                        result = new String(data, "GBK").trim();
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        msg.setData(bundle);
                    }
                    handler.handleMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String getAppName(Context context, int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
