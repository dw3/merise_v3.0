package com.merise.net.heart.base;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.activities.RepairActivity;
import com.merise.net.heart.adapter.GridviewAdapter;
import com.merise.net.heart.api.util.MultipartBody;
import com.merise.net.heart.utils.ImageTools;
import com.merise.net.heart.view.CustomGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者:xiangyang
 * 日期:2016/12/1
 */
public abstract class BaseGridviewActivity extends BaseActivity {
    public ArrayList<String> selectedPhotos = new ArrayList<>();
    public GridviewAdapter gridAdapter;
    public int maxPhotos = 6;
    public List<String> uploadFiles = new ArrayList<>();
    @BindView(R.id.gridView)
    CustomGridView gridView;
    Activity activity;
    public static final int REQUESTCODE = 0x01;

    @Override
    public void initData() {
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gridView.setNumColumns(cols);

        activity = this;
        // preview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = (String) parent.getItemAtPosition(position);
                if (GridviewAdapter.DEFAULTURL.equals(imgs)) {
                    selectedPhotos.remove(GridviewAdapter.DEFAULTURL);
                    PhotoPicker.builder()
                            .setPhotoCount(maxPhotos)
                            .setShowCamera(true)
                            .setSelected(selectedPhotos)
                            .setPreviewEnabled(true)
                            .start(activity);
                } else {
                    selectedPhotos.remove(GridviewAdapter.DEFAULTURL);
                    PhotoPreview.builder()
                            .setPhotos(selectedPhotos)
                            .setCurrentItem(position)
                            .start(activity);
                }
            }
        });
        selectedPhotos.add(GridviewAdapter.DEFAULTURL);
        gridAdapter = new GridviewAdapter(selectedPhotos, this, true);
        gridView.setAdapter(gridAdapter);
    }

    /**
     * 将file文件转化为MultipartBody
     *
     * @param filePaths
     * @return
     */
    public MultipartBody filesToMultipartBody(List<String> filePaths) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (String path : filePaths) {
            File file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            builder.addFormDataPart("files", file.getName(), requestBody);
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    /**
     * 将 file转化为Parts
     *
     * @param key
     * @param filePaths
     * @return
     */
    public static List<MultipartBody.Part> files2Parts(String key,
                                                       List<String> filePaths) {
        List<MultipartBody.Part> parts = new ArrayList<>(filePaths.size());
        for (String filePath : filePaths) {
            TLog.log(TAG, filePath);
            File file = new File(filePath);
            TLog.log(TAG, "文件大小:" + file.length());
            // 根据类型及File对象创建RequestBody（okhttp的类）
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            // 将RequestBody封装成MultipartBody.Part类型（同样是okhttp的）
            MultipartBody.Part part = MultipartBody.Part.
                    createFormData(key, file.getName(), requestBody);
            // 添加进集合
            parts.add(part);
        }
        return parts;
    }


    /**
     * 重新刷新adapter
     *
     * @param paths
     */
    public void loadAdpater(ArrayList<String> paths) {
        if (selectedPhotos != null && selectedPhotos.size() > 0) {
            selectedPhotos.clear();
        }
        if (paths.contains(GridviewAdapter.DEFAULTURL)) {
            paths.remove(GridviewAdapter.DEFAULTURL);
        }
        paths.add(GridviewAdapter.DEFAULTURL);
        selectedPhotos.addAll(paths);
        if (selectedPhotos.size() == maxPhotos + 1) {
            selectedPhotos.remove(maxPhotos);
        }
        gridAdapter.notifyDataSetChanged();
    }

    /**
     * 压缩并保存图片
     *
     * @param files
     */
    public void compressAndSave(List<String> files) {
        for (String filePath : files) {
            Bitmap bitmap = ImageTools.getImage(filePath, 100, 100, 200);
            String path = Environment.getExternalStorageDirectory().getPath()
                    + "/heartCache/cache/" + getName() + ".png";
            ImageTools.savePhotoToSDCard(bitmap, path);
            uploadFiles.add(path);
        }
    }

    /**
     * 随机获取八位字符串
     *
     * @return
     */
    private String getName() {
        StringBuffer str = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            str.append(random.nextInt(10));
        }
        return String.valueOf(str);
    }
}
