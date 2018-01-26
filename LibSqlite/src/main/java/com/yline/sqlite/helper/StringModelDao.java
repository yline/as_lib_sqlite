package com.yline.sqlite.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.yline.sqlite.common.AbstractSafelyDao;
import com.yline.sqlite.common.Column;

import java.util.HashMap;

/**
 * 数据库存储，String -- String; 操作
 *
 * @author yline 2018/1/26 -- 9:40
 * @version 1.0.0
 */
public class StringModelDao extends AbstractSafelyDao<String, StringModel> {
    public static final String TABLE_NAME = "StringModelDao";

    private static class Table {
        private final static Column Key = new Column(0, String.class, "key", true, "key");
        private final static Column Value = new Column(1, String.class, "value", false, "value");
    }

    private StringModelDao(SQLiteDatabase db) {
        super(db, TABLE_NAME, new Column[]{Table.Key, Table.Value}, new Column[]{Table.Key});
    }

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "if not exists" : "";
        String sql = String.format("create table %s %s(%s text primary key, %s text);",
                constraint, TABLE_NAME, Table.Key.getColumnName(), Table.Value.getColumnName());
        db.execSQL(sql);
    }

    public static void attachSession(HashMap<String, AbstractSafelyDao> hashMap, SQLiteDatabase db) {
        hashMap.put(TABLE_NAME, new StringModelDao(db));
    }

    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = String.format("drop table %s %s", ifExists ? "if exists" : "", TABLE_NAME);
        db.execSQL(sql);
    }

    @Override
    public String getKey(StringModel stringModel) {
        return stringModel.getKey();
    }

    @Override
    protected String readKey(Cursor cursor) {
        return cursor.isNull(Table.Key.getOrdinal()) ? null : cursor.getString(Table.Key.getOrdinal());
    }

    @Override
    protected StringModel readModel(Cursor cursor) {
        String key = cursor.isNull(Table.Key.getOrdinal()) ? null : cursor.getString(Table.Key.getOrdinal());
        String value = cursor.isNull(Table.Value.getOrdinal()) ? null : cursor.getString(Table.Value.getOrdinal());
        return new StringModel(key, value);
    }

    @Override
    protected boolean bindValues(SQLiteStatement stmt, StringModel stringModel) {
        String userId = stringModel.getKey();
        if (null != userId) {
            stmt.bindString(1 + Table.Key.getOrdinal(), userId);
        }

        String userName = stringModel.getValue();
        if (null != userId) {
            stmt.bindString(1 + Table.Value.getOrdinal(), userName);
        }

        return true;
    }
}
