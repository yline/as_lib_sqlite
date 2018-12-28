package com.lib.sqlite.demo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lib.sqlite.demo.IApplication;
import com.lib.sqlite.demo.dao.model.ByteModel;
import com.lib.sqlite.demo.dao.model.ByteModelDao;
import com.lib.sqlite.demo.dao.model.LongModel;
import com.lib.sqlite.demo.dao.model.LongModelDao;
import com.lib.sqlite.demo.dao.model.StringModel;
import com.lib.sqlite.demo.dao.model.StringModelDao;
import com.yline.sqlite.async.AsyncHelper;
import com.yline.sqlite.SQLiteManager;

public class DaoManager {
	private static DaoSession instance; // 管理数据库操作对象
	
	private static void initDaoSession(SQLiteDatabase db) {
		instance = new DaoSession(db);
	}
	
	private static DaoSession getDaoSession() {
		if (null == instance) {
			synchronized (DaoSession.class) {
				init(IApplication.getInstance());
			}
		}
		return instance;
	}
	
	private static void init(Context context) {
		SQLiteManager.init(context, new SQLiteManager.OnSQLiteLifeCallback() {
			@Override
			public void onCreate(SQLiteDatabase db) {
				StringModelDao.createTable(db, true);
				ByteModelDao.createTable(db, true);
				LongModelDao.createTable(db, true);
			}
			
			@Override
			public void onDaoCreated(SQLiteDatabase db) {
				initDaoSession(db);
			}
			
			@Override
			public void onUpdate(SQLiteDatabase db) {
                /*
                StringModelDao.dropTable(db, true);
                ByteModelDao.dropTable(db, true);*/
			}
		});
	}
	
	public static StringModelDao getStringModelDao() {
		return getDaoSession().getStringModelDao();
	}
	
	public static AsyncHelper<String, StringModel> getStringModelAsync() {
		return getDaoSession().getStringModelAsync();
	}
	
	public static ByteModelDao getByteModelDao() {
		return getDaoSession().getByteModelDao();
	}
	
	public static AsyncHelper<String, ByteModel> getByteModelAsync() {
		return getDaoSession().getByteModelAsync();
	}
	
	public static LongModelDao getLongModelDao() {
		return getDaoSession().getLongModelDao();
	}
	
	public static AsyncHelper<Long, LongModel> getLongModelAsync() {
		return getDaoSession().getLongModelAsync();
	}
}
