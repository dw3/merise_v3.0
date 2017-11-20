package com.merise.net.heart.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesTools {
	public Context mContext = null;

	public SharedPreferencesTools(Context context) {
		mContext = context;
	}

	/**
	 * @category 软件配置Const
	 * */
	public void saveSharedPreferences(String key, int values) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				Const.SHAREPREFRENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, values);
		editor.commit();

	}
	/**
	 * @category 软件配置
	 * */
	public void saveSharedPreferences(String key, boolean b){
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				Const.SHAREPREFRENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}
	/**
	 * @category 软件配置
	 * */
	public void saveSharedPreferences(String key, String values) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				Const.SHAREPREFRENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, values);
		editor.commit();

	}
	/**
	 * @category 读取我们添加到SharedPreference对象中的数据
	 */
	public boolean readSharedPreferencesBoolean(String keyName) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				Const.SHAREPREFRENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(keyName, false);
	}
	/**
	 * @category 读取我们添加到SharedPreference对象中的数据
	 */
	public int readSharedPreferencesInt(String keyName) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				Const.SHAREPREFRENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(keyName, 0);
	}

	/**
	 * @category 读取我们添加到SharedPreference对象中的数据
	 */
	public String readSharedPreferencesString(String keyName) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				Const.SHAREPREFRENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getString(keyName, "");
	}

	/**
	 * 移除我们添加到SharedPreference对象中的数据
	 *
	 * @param keyName
	 */
	public void removeSharedPreferences(String keyName) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				Const.SHAREPREFRENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.remove(keyName);
		editor.commit();
	}

}
