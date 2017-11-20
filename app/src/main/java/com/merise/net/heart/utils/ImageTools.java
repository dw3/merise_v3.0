package com.merise.net.heart.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.framewok.util.TLog;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wangdawang on 2016/8/30 0030.
 */
public class ImageTools {
    private Bitmap bitmap;

    @Nullable
    public static Bitmap createBitmapFromURI(Uri uriOfPicture, Context context) {

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context
                    .getContentResolver().openInputStream(uriOfPicture));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (bitmap != null)
            return Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        else {
            return null;
        }
    }

    /**
     * Check the SD card
     *
     * @return
     */
    public static boolean checkSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static void saveFile(Bitmap bm, String path) throws IOException {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

    public static void saveBitmap(Bitmap bm, String path,Context context) {
        File f = new File(context.getFilesDir()+path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void saveBitmapToFile(Bitmap bitmap, String _file) {
        BufferedOutputStream os = null;
        try {
            File file = new File(_file);  //新建图片
            int end = _file.lastIndexOf(File.separator);
            String _filePath = _file.substring(0, end); //获取图片路径
            File filePath = new File(_filePath);
            if (!filePath.exists()) {  //如果文件夹不存在，创建文件夹
                filePath.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();  //创建图片文件
            } catch (IOException e) {
                e.printStackTrace();
            }
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, os);  //图片存成png格式。
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();  //关闭流
                } catch (IOException e) {
                    TLog.log("savePhotoToSDCard", "保存失败" + e.getMessage());
                }
            }
        }
    }

    /**
     * 将图片存放在指定路径
     *
     * @param photoBitmap
     * @param path
     */
    public static void savePhotoToSDCard(Bitmap photoBitmap, String path) {
        if (checkSDCardAvailable()) {
            TLog.log("savePhotoToSDCard", "开始保存");
            File photoFile = new File(path);
            if (!photoFile.exists()) {
                photoFile.getParentFile().mkdirs();
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 50,
                            fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
                TLog.log("savePhotoToSDCard", "保存成功");
            } catch (Exception e) {
                photoFile.delete();
                e.printStackTrace();
                TLog.log("savePhotoToSDCard", "保存失败" + e.getMessage());
            } finally {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 质量压缩法
     *
     * @param image
     * @return
     */
    private static Bitmap compressImage(Bitmap image, int maxLength) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        TLog.log("compressImage", baos.toByteArray().length + "");
        while (baos.toByteArray().length / 1024 > maxLength) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            TLog.log("compressImage", baos.toByteArray().length + "");
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//            TLog.log("compressImage", baos.toByteArray().length + "");
        }
        TLog.log("compressImage", baos.toByteArray().length + "");
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getImage(String srcPath, int reqWidth, int reqHeight, int maxLength) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        newOpts.inSampleSize = calculateInSampleSize(newOpts, reqWidth, reqHeight);// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap, maxLength);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 图片按比例大小压缩方法（根据Bitmap图片压缩）
     *
     * @param image
     * @return
     */
    public static Bitmap comp(Bitmap image, int reqWidth, int reqHeight, int maxLength) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        newOpts.inSampleSize = calculateInSampleSize(newOpts, reqWidth, reqHeight);// 设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;// 降低图片从ARGB888到RGB565
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap, maxLength);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 压缩倍数
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
