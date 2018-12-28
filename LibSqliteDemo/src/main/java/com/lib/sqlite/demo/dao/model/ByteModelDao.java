package com.lib.sqlite.demo.dao.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.yline.sqlite.common.AbstractSafelyDao;
import com.yline.sqlite.common.Column;

import java.util.HashMap;

/**
 * 数据库存储，String -- byte[]，操作表
 *
 * @author yline 2018/1/26 -- 9:49
 * @version 1.0.0
 */
public class ByteModelDao extends AbstractSafelyDao<String, ByteModel> {
    public static final String TABLE_NAME = "ByteModelDao";

    private static class Table {
        private final static Column Key = new Column(0, String.class, "key", true, "key");
        private final static Column Value = new Column(1, Object.class, "value", false, "value");
    }

    private ByteModelDao(SQLiteDatabase db) {
        super(db, TABLE_NAME, new Column[]{Table.Key, Table.Value}, new Column[]{Table.Key});
    }

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "if not exists" : "";
        String sql = String.format("create table %s %s(%s text primary key, %s blob);",
                constraint, TABLE_NAME, Table.Key.getColumnName(), Table.Value.getColumnName());
        db.execSQL(sql);
    }

    public static ByteModelDao attachSession(SQLiteDatabase db) {
    	return new ByteModelDao(db);
    }

    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = String.format("drop table %s %s", ifExists ? "if exists" : "", TABLE_NAME);
        db.execSQL(sql);
    }

    @Override
    public String getKey(ByteModel byteModel) {
        return byteModel.getKey();
    }

    @Override
    protected String readKey(Cursor cursor) {
        return cursor.isNull(Table.Key.getOrdinal()) ? null : cursor.getString(Table.Key.getOrdinal());
    }

    @Override
    protected ByteModel readModel(Cursor cursor) {
        String key = cursor.isNull(Table.Key.getOrdinal()) ? null : cursor.getString(Table.Key.getOrdinal());
        byte[] value = cursor.isNull(Table.Value.getOrdinal()) ? null : cursor.getBlob(Table.Value.getOrdinal());
        return new ByteModel(key, value);
    }

    @Override
    protected boolean bindValues(SQLiteStatement stmt, ByteModel byteModel) {
        String key = byteModel.getKey();
        if (null != key) {
            stmt.bindString(1 + Table.Key.getOrdinal(), key);
        }

        byte[] value = byteModel.getValue();
        if (null != value) {
            stmt.bindBlob(1 + Table.Value.getOrdinal(), value);
        }

        return true;
    }
}
