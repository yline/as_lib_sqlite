package com.yline.sqlite.async;

/**
 * 支持的操作类型[异步]
 * @author yline 2018/12/28 -- 16:02
 */
public enum Type {
	Load, LoadAll,
	Insert, InsertInTx, InsertInTxCache,
	InsertOrReplace, InsertOrReplaceInTx, InsertOrReplaceInTxCache,
	Count, DeleteAll
}
