package com.lib.sqlite.demo;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.lib.sqlite.demo.db.LongModelDao;
import com.yline.sqlite.SqliteManager;
import com.yline.sqlite.common.AbstractSafelyDao;
import com.yline.sqlite.dao.DaoManager;

import java.util.HashMap;

/**
 * @author yline 2018/1/26 -- 13:09
 * @version 1.0.0
 */
public class IApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SqliteManager.init(this, new DaoManager.OnSQLiteLifeCallback() {
            @Override
            public void onCreate(SQLiteDatabase db) {
                LongModelDao.createTable(db, true);
            }

            @Override
            public void onDaoCreate(HashMap<String, AbstractSafelyDao> daoHashMap, SQLiteDatabase db) {
                LongModelDao.attachSession(daoHashMap, db);
            }

            @Override
            public void onUpdate(SQLiteDatabase db) {
            }
        });
    }
}
