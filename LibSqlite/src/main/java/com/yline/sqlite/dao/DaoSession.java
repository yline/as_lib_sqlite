package com.yline.sqlite.dao;

import android.database.sqlite.SQLiteDatabase;

import com.yline.sqlite.async.AsyncHelper;
import com.yline.sqlite.common.AbstractSafelyDao;

import java.util.HashMap;

/**
 * 对 ***Dao 进行统一管理；单个数据库管理
 *
 * @author yline 2018/1/26 -- 9:22
 * @version 1.0.0
 */
public class DaoSession {
    private HashMap<String, AbstractSafelyDao> mDaoHashMap;

    public DaoSession(SQLiteDatabase db, DaoManager.OnSQLiteLifeCallback lifeCallback) {
        this.mDaoHashMap = new HashMap<>();
        if (null != lifeCallback) {
            lifeCallback.onDaoCreate(mDaoHashMap, db);
        }
    }

    public AbstractSafelyDao getModelDao(String key) {
        return mDaoHashMap.get(key);
    }

    public AsyncHelper getModelDaoAsync(String key) {
        AbstractSafelyDao safelyDao = mDaoHashMap.get(key);
        if (null != safelyDao) {
            return new AsyncHelper<>(safelyDao);
        } else {
            return null;
        }
    }

    public <Key, Model> AsyncHelper<Key, Model> getModelDaoAsync(String key, Class<Key> keyClz, Class<Model> valueClz) {
        AbstractSafelyDao<Key, Model> safelyDao = mDaoHashMap.get(key);
        if (null != safelyDao) {
            return new AsyncHelper<>(safelyDao);
        } else {
            return null;
        }
    }
}
