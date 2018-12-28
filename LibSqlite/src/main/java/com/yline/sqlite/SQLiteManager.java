package com.yline.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yline.sqlite.common.AbstractSafelyDao;
import com.yline.sqlite.helper.DaoOpenHelper;

import java.util.HashMap;

/**
 * 数据库管理类
 *
 * @author yline 2018/1/25 -- 18:59
 * @version 1.0.0
 */
public class SQLiteManager {
	private static final int DEFAULT_VERSION = 1;
	private static final String TAG = "xxx-sqlite";
	private static boolean isDebug;
	
	/* ------------------------------------ 数据初始化 ----------------------------------- */
	public static void init(Context context, OnSQLiteLifeCallback lifeCallback) {
		init(context, DEFAULT_VERSION, lifeCallback);
	}
	
	public static void init(Context context, int daoVersion, OnSQLiteLifeCallback lifeCallback) {
		SQLiteManager.getInstance().initInner(context, daoVersion, lifeCallback);
	}
	
	private void initInner(Context context, int daoVersion, OnSQLiteLifeCallback lifeCallback) {
		DaoOpenHelper daoOpenHelper = new DaoOpenHelper(context, daoVersion, lifeCallback);
		if (null != lifeCallback) {
			lifeCallback.onDaoCreated(daoOpenHelper.getWritableDatabase());
		}
	}
	
	private SQLiteManager() {
	}
	
	private static SQLiteManager instance;
	
	private static SQLiteManager getInstance() {
		if (null == instance) {
			synchronized (SQLiteManager.class) {
				instance = new SQLiteManager();
			}
		}
		return instance;
	}
	
	public interface OnSQLiteLifeCallback {
		/**
		 * 创建数据表
		 * @param db 数据库
		 */
		void onCreate(SQLiteDatabase db);
		
		/**
		 * 创建数据表，管理类
		 * @param db 数据库
		 */
		void onDaoCreated(SQLiteDatabase db);
		
		/**
		 * 更新数据表时的操作
		 * @param db 数据库
		 */
		void onUpdate(SQLiteDatabase db);
	}
	
	public static void v(String tag, String msg) {
		if (isDebug) {
			Log.v(TAG, tag + ": " + msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if (isDebug) {
			Log.e(TAG, tag + ": " + msg);
		}
	}
}
