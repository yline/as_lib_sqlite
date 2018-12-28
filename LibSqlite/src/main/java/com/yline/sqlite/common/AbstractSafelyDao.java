package com.yline.sqlite.common;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.yline.sqlite.SQLiteManager;

/**
 * IExecuteDao 的 日志 + 入口判断
 *
 * @author yline 2017/9/21 -- 10:00
 * @version 1.0.0
 */
public abstract class AbstractSafelyDao<Key, Model> extends AbstractDao<Key, Model> {
	public static final int Error = -1;
	private static final String TAG = "AbstractSafelyDao";
	
	public AbstractSafelyDao(SQLiteDatabase db, String tableName, Column[] allColumns, Column[] pkColumns) {
		super(db, tableName, allColumns, pkColumns);
		SQLiteManager.v(TAG, "tableName = " + tableName + ", all = " + allColumns.length + ", pk = " + pkColumns.length);
	}
	
	@Override
	public Model load(Key key) {
		SQLiteManager.v(TAG, "load key = " + key);
		
		if (null == key) {
			SQLiteManager.e(TAG, "load key is null");
			return null;
		}
		
		return super.load(key);
	}
	
	@Override
	public long insert(Model model) {
		SQLiteManager.v(TAG, "insert model = " + model);
		
		if (null == model) {
			SQLiteManager.e(TAG, "insert model is null");
			return Error;
		}
		
		return super.insert(model);
	}
	
	@Override
	public void insertInTx(Iterable<Model> models) {
		SQLiteManager.v(TAG, "insertInTx models = " + models);
		
		if (null == models) {
			SQLiteManager.e(TAG, "insertInTx models is null");
			return;
		}
		super.insertInTx(models);
	}
	
	@Override
	public void insertInTx(Iterable<Model> models, boolean cache) {
		SQLiteManager.v(TAG, "insertInTx models = " + models + ", cache = " + cache);
		
		if (null == models) {
			SQLiteManager.e(TAG, "insertInTx models is null(cache)");
			return;
		}
		super.insertInTx(models, cache);
	}
	
	@Override
	public long insertOrReplace(Model model) {
		SQLiteManager.v(TAG, "insertOrReplace model = " + model);
		
		if (null == model) {
			SQLiteManager.e(TAG, "insertOrReplace model is null");
			return Error;
		}
		return super.insertOrReplace(model);
	}
	
	@Override
	public void insertOrReplaceInTx(Iterable<Model> models) {
		SQLiteManager.v(TAG, "insertOrReplaceInTx models = " + models);
		
		if (null == models) {
			SQLiteManager.e(TAG, "insertOrReplaceInTx models is null");
			return;
		}
		super.insertOrReplaceInTx(models);
	}
	
	@Override
	public void insertOrReplaceInTx(Iterable<Model> models, boolean cache) {
		SQLiteManager.v(TAG, "insertOrReplaceInTx models = " + models + ", cache = " + cache);
		
		if (null == models) {
			SQLiteManager.e(TAG, "insertOrReplaceInTx models is null(cache)");
			return;
		}
		super.insertOrReplaceInTx(models, cache);
	}
	
	@Override
	public abstract Key getKey(Model model);
	
	@Override
	protected abstract Key readKey(Cursor cursor);
	
	@Override
	protected abstract Model readModel(Cursor cursor);
	
	@Override
	protected abstract boolean bindValues(SQLiteStatement stmt, Model model);
}
