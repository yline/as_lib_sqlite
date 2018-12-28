package com.lib.sqlite.demo;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.lib.sqlite.demo.dao.DaoManager;
import com.lib.sqlite.demo.dao.model.LongModelDao;
import com.yline.sqlite.common.AbstractSafelyDao;
import com.yline.sqlite.SQLiteManager;

import java.util.HashMap;

/**
 * @author yline 2018/1/26 -- 13:09
 * @version 1.0.0
 */
public class IApplication extends Application {
	private static Application sInstance;
	
	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
	}
	
	public static Application getInstance() {
		return sInstance;
	}
}
