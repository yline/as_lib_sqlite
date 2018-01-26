package com.yline.sqlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库 入口类
 *
 * @author yline 2018/1/25 -- 20:10
 * @version 1.0.0
 */
public class DaoOpenHelper extends SQLiteOpenHelper {
    private static final String DefaultSQLiteName = "LibSqlite.db";
    private DaoManager.OnSQLiteLifeCallback mLifeCallback;

    public DaoOpenHelper(Context context, int version, DaoManager.OnSQLiteLifeCallback callback) {
        this(context, DefaultSQLiteName, null, version, callback);
    }

    public DaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DaoManager.OnSQLiteLifeCallback callback) {
        super(context, name, factory, version);
        this.mLifeCallback = callback;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DaoManager.v("DaoOpenHelper", "onCreate");
        if (null != mLifeCallback) {
            mLifeCallback.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DaoManager.v("DaoOpenHelper", "onUpgrade old:" + oldVersion + ",new:" + newVersion);
        if (null != mLifeCallback) {
            mLifeCallback.onUpdate(db);
        }
    }
}
