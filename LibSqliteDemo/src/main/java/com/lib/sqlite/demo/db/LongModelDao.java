package com.lib.sqlite.demo.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.yline.sqlite.common.AbstractSafelyDao;
import com.yline.sqlite.common.Column;

import java.util.HashMap;

/**
 * Int -- byte[] 数据库
 *
 * @author yline 2018/1/29 -- 9:58
 * @version 1.0.0
 */
public class LongModelDao extends AbstractSafelyDao<Long, LongModel> {
    public static final String TABLE_NAME = "LongModelDao";

    private static class Table {
        private final static Column Key = new Column(0, Long.class, "key", true, "key");
        private final static Column Value = new Column(1, Object.class, "value", false, "value");
    }

    public LongModelDao(SQLiteDatabase db) {
        super(db, TABLE_NAME, new Column[]{Table.Key, Table.Value}, new Column[]{Table.Key});
    }

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "if not exists" : "";
        String sql = String.format("create table %s %s(%s long primary key, %s blob);",
                constraint, TABLE_NAME, Table.Key.getColumnName(), Table.Value.getColumnName());
        db.execSQL(sql);
    }

    public static void attachSession(HashMap<String, AbstractSafelyDao> hashMap, SQLiteDatabase db) {
        hashMap.put(TABLE_NAME, new LongModelDao(db));
    }

    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = String.format("drop table %s %s", ifExists ? "if exists" : "", TABLE_NAME);
        db.execSQL(sql);
    }

    @Override
    public Long getKey(LongModel longModel) {
        return longModel.getKey();
    }

    @Override
    protected Long readKey(Cursor cursor) {
        return cursor.isNull(Table.Key.getOrdinal()) ? null : cursor.getLong(Table.Key.getOrdinal());
    }

    @Override
    protected LongModel readModel(Cursor cursor) {
        long key = cursor.isNull(Table.Key.getOrdinal()) ? -1 : cursor.getLong(Table.Key.getOrdinal());
        byte[] value = cursor.isNull(Table.Value.getOrdinal()) ? null : cursor.getBlob(Table.Value.getOrdinal());
        return new LongModel(key, value);
    }

    @Override
    protected boolean bindValues(SQLiteStatement stmt, LongModel longModel) {
        stmt.bindLong(1 + Table.Key.getOrdinal(), longModel.getKey());

        byte[] value = longModel.getValue();
        if (null != value) {
            stmt.bindBlob(1 + Table.Value.getOrdinal(), longModel.getValue());
        }

        return true;
    }
}
