package com.merise.net.heart.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.bean.UpdateBean;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 更新类
 * Created by wangdawang on 2016/10/21 0021.
 */
public class UpdateManager {

	private Context mContext;
	private int serviceCode;
	private String versionName;
	private String updateUrl;

	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	protected static final int UPDATE = 3;
	protected static final int ENTER_HOME = 4;
	public static final int URL_ERROR = 5;
	public static final int IO_ERROR = 6;
	private String TAG = "UpdateManager";
	/* 记录进度条数量 */
	private int progress;
	/* 更新进度条 */
    /* 下载保存路径 */
	private String mSavePath;
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;


	public UpdateManager(Context mContext) {
		this.mContext = mContext;
	}


	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				// 正在下载
				case DOWNLOAD:
					// 设置进度条位置
					mProgress.setProgress(progress);
					break;
				case DOWNLOAD_FINISH:
					// 安装文件
					installApk();
					break;
				case IO_ERROR:
					GouUtils.showTip(mContext, "下载失败，请检查网络");
					break;
				case URL_ERROR:
					GouUtils.showTip(mContext, "下载失败，请检查url");
					break;
				case UPDATE:
					showNoticeDialog();
					break;
				case ENTER_HOME:
					Toast.makeText(mContext, R.string.soft_update_no,
							Toast.LENGTH_LONG).show();
					break;
				default:
					break;
			}
		}
	};

	/**
	 * /**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog() {
		// 构造对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(R.string.soft_update_info);
		// 更新
		builder.setPositiveButton(R.string.soft_update_updatebtn,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 显示下载对话框
						showDownloadDialog();
					}
				});
		// 稍后更新
		builder.setNegativeButton(R.string.soft_update_later,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}


	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog() {
		// 构造软件下载对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton(R.string.soft_update_cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 设置取消状态
						cancelUpdate = true;
					}
				});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 下载文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				TLog.log(TAG, "信息匹配-----" + updateUrl + "----" + versionName + "----" + serviceCode);
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					URL url = new URL(Constant.downBaseUrl
							+ updateUrl);
					TLog.log(TAG, "url===" + url);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath,
							versionName);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						handler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							handler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				handler.sendEmptyMessage(URL_ERROR);
				e.printStackTrace();
			} catch (IOException e) {
				TLog.log(TAG, "IO---E===" + e.toString());
				handler.sendEmptyMessage(IO_ERROR);
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	}

	;


	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, versionName);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}


	/**
	 * 检查软件是否有更新版本
	 *
	 * @return
	 */
	public void checkUpdate() {
		//当前版本号
		final int vCode = GouUtils.getVersionCode(mContext);
//        TLog.log(TAG, "vCode-----" + vCode);
		// 1.android;2.ios;3.设备
		AppOperate.checkUpdate("1", (RxAppCompatActivity) mContext, new Report() {
			@Override
			public void onSucces(Object o) {
				UpdateBean updateBean = (UpdateBean) o;
				String versionCode = updateBean.getVersionCode();
				serviceCode = Integer.valueOf(versionCode);
				versionName = updateBean.getName();
				updateUrl = updateBean.getUrl();
				Message msg = new Message();
				if (serviceCode > vCode) {
					msg.what = UPDATE;
				} else {
					msg.what = ENTER_HOME;
				}
				handler.sendMessage(msg);
			}

			@Override
			public void onError(Object o) {
				TLog.log(TAG, "checkUpdate----" + o.toString());
			}
		});
	}

}
