package com.lib.sqlite.demo.dao;

import android.database.sqlite.SQLiteDatabase;

import com.lib.sqlite.demo.dao.model.ByteModel;
import com.lib.sqlite.demo.dao.model.ByteModelDao;
import com.lib.sqlite.demo.dao.model.LongModel;
import com.lib.sqlite.demo.dao.model.LongModelDao;
import com.lib.sqlite.demo.dao.model.StringModel;
import com.lib.sqlite.demo.dao.model.StringModelDao;
import com.yline.sqlite.SQLiteManager;
import com.yline.sqlite.async.AsyncHelper;
import com.yline.sqlite.common.AbstractSafelyDao;

import java.util.HashMap;

/**
 * 对 ***Dao 进行统一管理；单个数据库管理
 *
 * @author yline 2018/1/26 -- 9:22
 * @version 1.0.0
 */
class DaoSession {
	private ByteModelDao byteModelDao;
	private LongModelDao longModelDao;
	private StringModelDao stringModelDao;
	
	DaoSession(SQLiteDatabase db) {
		byteModelDao = ByteModelDao.attachSession(db);
		longModelDao = LongModelDao.attachSession(db);
		stringModelDao = StringModelDao.attachSession(db);
	}
	
	LongModelDao getLongModelDao() {
		return longModelDao;
	}
	
	AsyncHelper<Long, LongModel> getLongModelAsync() {
		return new AsyncHelper<>(longModelDao);
	}
	
	ByteModelDao getByteModelDao() {
		return byteModelDao;
	}
	
	AsyncHelper<String, ByteModel> getByteModelAsync() {
		return new AsyncHelper<>(byteModelDao);
	}
	
	StringModelDao getStringModelDao() {
		return stringModelDao;
	}
	
	AsyncHelper<String, StringModel> getStringModelAsync() {
		return new AsyncHelper<>(stringModelDao);
	}
}
