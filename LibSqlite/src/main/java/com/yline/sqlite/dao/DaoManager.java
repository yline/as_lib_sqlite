package com.yline.sqlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yline.sqlite.common.AbstractSafelyDao;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 数据库管理类
 *
 * @author yline 2018/1/25 -- 18:59
 * @version 1.0.0
 */
public class DaoManager {
    private static final int DEFAULT_VERSION = 1;
    private static final String TAG = "xxx-sqlite";
    private static boolean isDebug;

    private Context mContext;
    private OnSQLiteLifeCallback mSQLiteCallback;

    private ExecutorService mExecutorService;
    private DaoOpenHelper mDaoOpenHelper;
    private DaoSession mDaoSession;

    private DaoManager() {
    }

    public static DaoManager getInstance() {
        return DaoManagerHolder.getInstance();
    }

    public ExecutorService getExecutorService() {
        if (null == mExecutorService) {
            this.mExecutorService = Executors.newSingleThreadExecutor();
        }
        return mExecutorService;
    }

    public DaoSession getDaoSession() {
        if (null == mDaoSession) {
            if (null == mDaoOpenHelper) {
                if (null == mContext) {
                    throw new ExceptionInInitializerError("LibSQLite has not init");
                }
                mDaoOpenHelper = new DaoOpenHelper(mContext, DEFAULT_VERSION, mSQLiteCallback);
            }
            mDaoSession = new DaoSession(mDaoOpenHelper.getWritableDatabase(), mSQLiteCallback);
        }
        return mDaoSession;
    }

    /* ------------------------------------ 数据初始化 ----------------------------------- */
    public static void init(Context context, OnSQLiteLifeCallback lifeCallback) {
        init(context, DEFAULT_VERSION, lifeCallback);
    }

    public static void init(Context context, int daoVersion, OnSQLiteLifeCallback lifeCallback) {
        DaoManager.getInstance().setDaoHelper(context, daoVersion, lifeCallback);
    }

    private void setDaoHelper(Context context, int daoVersion, OnSQLiteLifeCallback lifeCallback) {
        this.mContext = context;
        this.mSQLiteCallback = lifeCallback;
    }

    private static class DaoManagerHolder {
        private static DaoManager sInstance;

        private synchronized static DaoManager getInstance() {
            if (null == sInstance) {
                sInstance = new DaoManager();
            }
            return sInstance;
        }
    }

    public interface OnSQLiteLifeCallback {
        /**
         * 创建数据表
         */
        void onCreate(SQLiteDatabase db);

        /**
         * 创建数据表，管理类
         */
        void onDaoCreate(HashMap<String, AbstractSafelyDao> daoHashMap, SQLiteDatabase db);

        /**
         * 更新数据表时的操作
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
