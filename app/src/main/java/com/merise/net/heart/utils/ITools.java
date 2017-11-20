package com.merise.net.heart.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import com.bigkoo.alertview.AlertView;
import com.merise.net.heart.bean.Function;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
public class ITools {
    private static String[] getDefaultArray(String[] single, String[] all, String[] userHabit) {

        return null;
    }

    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static String getNewString(String targetStr, String replaceStr) {
        String[] oldStr = targetStr.split(",");
        String newStr = "";
        if (oldStr != null) {
            for (String s : oldStr) {
                if (!s.equals(replaceStr)) {
                    newStr += s + ",";
                }
            }
        }
        return newStr.substring(0, newStr.length() > 0 ? (newStr.length() - 1) : 0);
    }

    /**
     * 去两个字符的交集
     */
    public static List getArray(String array1, String array2) {
        String[] str1 = array1.split(",");
        String[] str2 = array2.split(",");
        List<String> result = new ArrayList<>();
        for (int i = 0; i < str1.length; i++) {
            for (int j = 0; j < str2.length; j++) {
                if (str1[i].equals(str2[j])) {
                    result.add(str1[i]);
                }
            }
        }
        return result;
    }

    /**
     * 获取首页用户操作习惯
     *
     * @return
     */
    public static List<Function> getUserHabbit(String allFuns, String customFuns, List<Function> funList) {
        String[] allFunsArray = null;
        if (allFuns == null || allFuns.length() == 0) {
            return null;
        }
        if (allFuns != null && allFuns.length() != 0) {
            allFunsArray = allFuns.split(",");
        }
        String[] customFunsArray = null;
        if (customFuns != null && customFuns.length() != 0) {
            customFunsArray = customFuns.split(",");
        }
        List<Function> result = new ArrayList<>();
        if (customFunsArray == null || customFunsArray.length == 0) {
            int count = allFunsArray.length > 4 ? 4 : allFunsArray.length;
            for (int i = 0; i < count; i++) {
                for (int j = 0; j < funList.size(); j++) {
                    if (allFunsArray[i].equals(String.valueOf(funList.get(j).getId()))) {
                        result.add(funList.get(j));
                    }
                }
            }
        } else {
            for (int i = 0; i < customFunsArray.length; i++) {
                for (int j = 0; j < funList.size(); j++) {
                    if (customFunsArray[i].equals(String.valueOf(funList.get(j).getId()))) {
                        for (int k = 0; k < allFunsArray.length; k++) {
                            if (allFunsArray[k].equals(customFunsArray[i])) {
                                result.add(funList.get(j));
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static String[] arrContrast(String[] arr1, String[] arr2) {
        List<String> list = new LinkedList();
        for (String str : arr1) { // 处理第一个数组,list里面的值为1,2,3,4
            if (!list.contains(str)) {
                list.add(str);
            }
        }
        for (String str : arr2) { // 如果第二个数组存在和第一个数组相同的值，就删除
            if (list.contains(str)) {
                list.remove(str);
            }
        }
        String[] result = {}; // 创建空数组
        return list.toArray(result); // List to Array
    }

    /**
     * 获取actionbar的像素高度，默认使用android官方兼容包做actionbar兼容
     *
     * @return
     */
    public static int getActionBarHeight(Context context) {
        int actionBarHeight = 0;
        final TypedValue tv = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, context.getResources().getDisplayMetrics());
            }
        } else {
            // 使用android.support.v7.appcompat包做actionbar兼容的情况
            if (context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize,
                    tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, context.getResources().getDisplayMetrics());
            }

        }
        return actionBarHeight;
    }

    public static void showLoginAlert(Context context) {
        final AlertView alertView = new AlertView("提示", "请登录", null, null, null, context, AlertView.Style.Alert, null);
        alertView.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertView.dismiss();
            }
        }, 2000);
    }

    public static void showAlert(Context context, String title, String message, String cancel) {
        final AlertView alertView = new AlertView(title, message, cancel, null, null, context, AlertView.Style.Alert, null);
        alertView.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertView.dismiss();
            }
        }, 2000);
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 震动
     *
     * @param context
     */
    public static void vibrator(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        // 下边是可以使震动有规律的震动   -1：表示不重复 0：循环的震动
        long[] pattern = {100, 300};
        vibrator.vibrate(pattern, -1);
    }

    /**
     * 获取当前系统时间并格式化
     *
     * @return
     */
    public static String getCurrentSimpleDateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String NDateTime = formatter.format(date);
        return NDateTime;
    }
}
