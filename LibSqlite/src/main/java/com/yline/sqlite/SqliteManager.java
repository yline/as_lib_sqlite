package com.yline.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yline.sqlite.async.AsyncHelper;
import com.yline.sqlite.common.AbstractSafelyDao;
import com.yline.sqlite.dao.DaoManager;
import com.yline.sqlite.helper.ByteModel;
import com.yline.sqlite.helper.ByteModelDao;
import com.yline.sqlite.helper.StringModel;
import com.yline.sqlite.helper.StringModelDao;

import java.util.HashMap;

/**
 * 给外界使用的统一管理类
 * 获取的帮助类，可能为空，如果未初始化
 *
 * @author yline 2018/1/25 -- 18:45
 * @version 1.0.0
 */
public class SqliteManager {
    public static void init(Context context) {
        init(context, null);
    }

    public static void init(Context context, final DaoManager.OnSQLiteLifeCallback lifeCallback) {
        DaoManager.init(context, new DaoManager.OnSQLiteLifeCallback() {
            @Override
            public void onCreate(SQLiteDatabase db) {
                StringModelDao.createTable(db, true);
                ByteModelDao.createTable(db, true);
                if (null != lifeCallback) {
                    lifeCallback.onCreate(db);
                }
            }

            @Override
            public void onDaoCreate(HashMap<String, AbstractSafelyDao> daoHashMap, SQLiteDatabase db) {
                StringModelDao.attachSession(daoHashMap, db);
                ByteModelDao.attachSession(daoHashMap, db);
                if (null != lifeCallback) {
                    lifeCallback.onDaoCreate(daoHashMap, db);
                }
            }

            @Override
            public void onUpdate(SQLiteDatabase db) {
                /*
                StringModelDao.dropTable(db, true);
                ByteModelDao.dropTable(db, true);*/
                if (null != lifeCallback) {
                    lifeCallback.onUpdate(db);
                }
            }
        });
    }

    public static StringModelDao getStringModelDao() {
        return (StringModelDao) DaoManager.getInstance().getDaoSession().getModelDao(StringModelDao.TABLE_NAME);
    }

    public static AsyncHelper<String, StringModel> getStringModelAsync() {
        return DaoManager.getInstance().getDaoSession().getModelDaoAsync(StringModelDao.TABLE_NAME, String.class, StringModel.class);
    }

    public static ByteModelDao getByteModelDao() {
        return (ByteModelDao) DaoManager.getInstance().getDaoSession().getModelDao(ByteModelDao.TABLE_NAME);
    }

    public static AsyncHelper<String, ByteModel> getByteModelAsync() {
        return DaoManager.getInstance().getDaoSession().getModelDaoAsync(ByteModelDao.TABLE_NAME, String.class, ByteModel.class);
    }
}
