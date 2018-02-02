package com.yline.sqlite.common;

import java.util.List;

/**
 * 所有的支持的数据操作
 * 1） load
 * 2） insert
 * 3）insertOrReplace
 * // 4） query
 * // 5） delete
 * // 6） update
 * 7） count
 * 7-1）getKey
 *
 * @author yline 2018/1/25 -- 18:47
 * @version 1.0.0
 */
public interface ExecuteDbCallback<Key, Model> {
    /**
     * 依据 Key 获取 Model
     * 默认读取缓存
     *
     * @param key 关键词
     * @return 单条数据
     */
    Model load(Key key);

    /**
     * 读取所有数据
     * 默认读取缓存(单条读取)
     *
     * @return 数据队列
     */
    List<Model> loadAll();

    /**
     * 插入单条数据，如果重复，则不会插入数据
     * 默认缓存写入
     *
     * @param model 单条数据
     * @return rowId
     */
    long insert(Model model);

    /**
     * 插入多条数据，开启事务，如果重复，则不会插入数据
     * 默认缓存写入
     *
     * @param models 数据队列
     */
    void insertInTx(Iterable<Model> models);

    /**
     * 插入多条数据，开启事务，如果重复，则不会插入数据
     *
     * @param models 数据队列
     * @param cache  是否写入缓存
     */
    void insertInTx(Iterable<Model> models, boolean cache);

    /**
     * 插入单条数据
     * 默认缓存写入
     *
     * @param model 单条数据
     * @return rowId
     */
    long insertOrReplace(Model model);

    /**
     * 插入多条数据，开启事务
     * 默认缓存写入
     *
     * @param models 数据队列
     */
    void insertOrReplaceInTx(Iterable<Model> models);

    /**
     * 插入多条数据，开启事务
     *
     * @param models 数据队列
     * @param cache  是否写入缓存
     */
    void insertOrReplaceInTx(Iterable<Model> models, boolean cache);

    /**
     * @return 行数
     */
    long count();

    /**
     * 清除所有数据
     */
    void deleteAll();

    /**
     * 执行某条语句，没有返回值
     *
     * @param sql      例如：update person set phone=?,name=? where personid=?
     * @param bindArgs 例如： new Object[]{176, "yline", 11}
     */
    void execSQL(String sql, Object[] bindArgs);

    /**
     * 执行语句，获取游标
     *
     * @param sql           例如：select * from TABLE where NAME like ? and AGE=?
     * @param selectionArgs 例如： new String[]{"%传智%", "4"}
     * @param callback      游标在回调中使用，较为安全
     */
    void rawQuery(String sql, String[] selectionArgs, AbstractDao.OnCursorCallback callback);
}
