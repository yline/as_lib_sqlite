package com.yline.sqlite.common;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.yline.sqlite.dao.DaoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <Key>   缓存的 关键词
 * @param <Model> 缓存的数据
 * @author yline 2018/1/25 -- 18:49
 * @version 1.0.0
 */
public abstract class AbstractDao<Key, Model> implements ExecuteDbCallback<Key, Model> {
    static final int Error = -1;
    static final String TAG = "AbstractSafelyDao";
    private final SQLiteDatabase mDb;

    private final String mTableName;
    private final String[] mPKColumns;

    private final ScopeReference<Key, Model> mScope;
    private final TableStatements mStatements;

    public AbstractDao(SQLiteDatabase db, String tableName, Column[] allColumns, Column[] pkColumns) {
        this.mDb = db;
        this.mTableName = tableName;

        String[] totalColumns = new String[allColumns.length];
        for (int i = 0; i < allColumns.length; i++) {
            totalColumns[i] = allColumns[i].getColumnName();
        }

        this.mPKColumns = new String[pkColumns.length];
        for (int i = 0; i < pkColumns.length; i++) {
            this.mPKColumns[i] = allColumns[i].getColumnName();
        }

        this.mScope = new ScopeReference<>();
        this.mStatements = new TableStatements(tableName, db, totalColumns);
    }

    @Override
    public Model load(Key key) {
        if (mPKColumns.length != 1) {
            DaoManager.v(TAG, "load: pk length is error");
            return null;
        }

        // read cache
        Model module = mScope.get(key);
        if (null != module) {
            return module;
        }

        // read sql
        String sql = String.format("select distinct * from %s where %s=?", mTableName, mPKColumns[0]);
        Cursor cursor = mDb.rawQuery(sql, new String[]{key.toString()});
        return loadUniqueAndCloseCursor(cursor);
    }

    @Override
    public List<Model> loadAll() {
        String sql = String.format("select * from %s", mTableName); // read sql
        Cursor cursor = mDb.rawQuery(sql, null);
        return loadAllAndCloseCursor(cursor);
    }

    @Override
    public long insert(Model model) {
        return insertSafely(model, true);
    }

    @Override
    public void insertInTx(Iterable<Model> models) {
        insertInTxSafely(models, true);
    }

    @Override
    public void insertInTx(Iterable<Model> models, boolean cache) {
        insertInTxSafely(models, cache);
    }

    @Override
    public long insertOrReplace(Model model) {
        return insertOrReplaceSafely(model, true);
    }

    @Override
    public void insertOrReplaceInTx(Iterable<Model> models) {
        insertOrReplaceInTxSafely(models, true);
    }

    @Override
    public void insertOrReplaceInTx(Iterable<Model> models, boolean cache) {
        insertOrReplaceInTxSafely(models, cache);
    }

    @Override
    public long count() {
        SQLiteStatement statement = mStatements.getCountStatement();
        synchronized (statement) {
            return statement.simpleQueryForLong();
        }
    }

    @Override
    public void deleteAll() {
        mDb.execSQL(String.format("delete from '%s'", mTableName));
        mScope.clear();
    }

    /**
     * 读取表中 所有数据
     *
     * @param cursor 数据表 游标
     * @return 数据 队列
     */
    private List<Model> loadAllAndCloseCursor(Cursor cursor) {
        try {
            int count = cursor.getCount();
            if (0 == count) {
                return new ArrayList<>();
            }

            List<Model> resultList = new ArrayList<>(count);
            if (cursor.moveToFirst()) {
                try {
                    mScope.lock();
                    do {
                        resultList.add(loadCurrent(cursor, false));
                    } while (cursor.moveToNext());
                } finally {
                    mScope.unLock();
                }
            }

            return resultList;
        } finally {
            cursor.close();
        }
    }

    /**
     * 读取单条数据 并关闭游标
     *
     * @param cursor 游标
     * @return 单条数据
     */
    private Model loadUniqueAndCloseCursor(Cursor cursor) {
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else {
                return loadCurrent(cursor, true);
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * 读取数据
     *
     * @param cursor 数据表 游标
     * @param lock   操作 是否 锁处理
     * @return 单条数据
     */
    private Model loadCurrent(Cursor cursor, boolean lock) {
        Key key = readKey(cursor);
        if (null == key) {
            return null;
        }

        Model model = lock ? mScope.get(key) : mScope.getNoLock(key);
        if (null != model) {
            return model;
        } else {
            model = readModel(cursor);
            loadCache(key, model, lock);
            return model;
        }
    }

    /**
     * 存入缓存
     *
     * @param key   关键词
     * @param model 数据
     * @param lock  操作 是否 锁处理
     */
    private void loadCache(Key key, Model model, boolean lock) {
        if (null != key) {
            if (lock) {
                mScope.put(key, model);
            } else {
                mScope.putNoLock(key, model);
            }
        }
    }

    /**
     * 插入数据
     *
     * @param model 单条数据
     * @param cache 是否 缓存处理
     * @return rowId
     */
    private long insertSafely(Model model, boolean cache) {
        long rowId;
        if (mDb.isDbLockedByCurrentThread()) {
            rowId = executeInsert(model);
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            mDb.beginTransaction();
            try {
                rowId = executeInsert(model);
                mDb.setTransactionSuccessful();
            } finally {
                mDb.endTransaction();
            }
        }

        if (cache) {
            Key key = getKey(model);
            loadCache(key, model, true);
        }

        return rowId;
    }

    /**
     * 插入数据
     *
     * @param models 数据队列
     * @param cache  是否 缓存处理
     */
    private void insertInTxSafely(Iterable<Model> models, boolean cache) {
        mDb.beginTransaction();
        try {
            SQLiteStatement statement = mStatements.getInsertStatement();
            synchronized (statement) {
                mScope.lock();

                try {
                    boolean bindResult;
                    long rowId;
                    for (Model model : models) {
                        statement.clearBindings();
                        bindResult = bindValues(statement, model);
                        rowId = bindResult ? statement.executeInsert() : Error; // 不重复插入数据

                        if (Error != rowId && cache) {
                            Key key = getKey(model);
                            loadCache(key, model, false);
                        }
                    }
                } finally {
                    mScope.unLock();
                }
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }

    /**
     * 插入数据
     *
     * @param model 单条数据
     * @return rowId
     */
    private long executeInsert(Model model) {
        // sql insert
        SQLiteStatement statement = mStatements.getInsertStatement();
        synchronized (statement) {
            statement.clearBindings();
            boolean bindResult = bindValues(statement, model);
            return bindResult ? statement.executeInsert() : Error;
        }
    }

    /**
     * 插入 或 替换 数据队列
     *
     * @param models 数据队列
     * @param cache  是否缓存
     */
    private void insertOrReplaceInTxSafely(Iterable<Model> models, boolean cache) {
        mDb.beginTransaction();
        try {
            SQLiteStatement statement = mStatements.getInsertOrReplaceStatement();
            synchronized (statement) {
                mScope.lock();

                try {
                    for (Model model : models) {
                        statement.clearBindings();
                        bindValues(statement, model);
                        statement.executeInsert();

                        if (cache) {
                            Key key = getKey(model);
                            loadCache(key, model, false);
                        }
                    }
                } finally {
                    mScope.unLock();
                }
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }

    /**
     * @param model 单个数据
     * @param cache 是否 缓存 处理
     * @return rowId
     */
    private long insertOrReplaceSafely(Model model, boolean cache) {
        long rowId;
        if (mDb.isDbLockedByCurrentThread()) {
            rowId = executeInsertOrReplace(model);
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            mDb.beginTransaction();
            try {
                rowId = executeInsertOrReplace(model);
                mDb.setTransactionSuccessful();
            } finally {
                mDb.endTransaction();
            }
        }

        if (cache) {
            Key key = getKey(model);
            loadCache(key, model, true);
        }

        return rowId;
    }

    /**
     * 插入或替换 数据
     *
     * @param model 单条数据
     * @return rowId
     */
    private long executeInsertOrReplace(Model model) {
        // sql insert
        SQLiteStatement statement = mStatements.getInsertOrReplaceStatement();
        synchronized (statement) {
            statement.clearBindings();
            boolean bindResult = bindValues(statement, model);
            return bindResult ? statement.executeInsert() : Error;
        }
    }

    /**
     * 依据 内容 生成 Key
     *
     * @param model 单条内容
     * @return Key
     */
    public abstract Key getKey(Model model);

    /**
     * 依据 游标 读取到 Key
     *
     * @param cursor 游标
     * @return 关键词
     */
    protected abstract Key readKey(Cursor cursor);

    /**
     * 依据 游标 读取到 Model内容
     *
     * @param cursor 游标
     * @return 单条数据
     */
    protected abstract Model readModel(Cursor cursor);

    /**
     * 将数据 和 数据表绑定
     *
     * @param stmt  SQLite工具
     * @param model 单个数据
     * @return 是否绑定成功
     */
    protected abstract boolean bindValues(SQLiteStatement stmt, Model model);
}
