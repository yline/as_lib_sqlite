package com.yline.sqlite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yline.sqlite.SQLiteManager;

/**
 * 数据库 入口类
 *
 * @author yline 2018/1/25 -- 20:10
 * @version 1.0.0
 */
public class DaoOpenHelper extends SQLiteOpenHelper {
    private static final String DefaultSQLiteName = "LibSqlite.db";
    private SQLiteManager.OnSQLiteLifeCallback mLifeCallback;

    public DaoOpenHelper(Context context, int version, SQLiteManager.OnSQLiteLifeCallback callback) {
        this(context, DefaultSQLiteName, null, version, callback);
    }

    public DaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, SQLiteManager.OnSQLiteLifeCallback callback) {
        super(context, name, factory, version);
        this.mLifeCallback = callback;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SQLiteManager.v("DaoOpenHelper", "onCreate");
        if (null != mLifeCallback) {
            mLifeCallback.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SQLiteManager.v("DaoOpenHelper", "onUpgrade old:" + oldVersion + ",new:" + newVersion);
        if (null != mLifeCallback) {
            mLifeCallback.onUpdate(db);
        }
    }
}
