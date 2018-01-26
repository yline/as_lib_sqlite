package com.yline.sqlite.common;

/**
 * 单列属性
 *
 * @author yline 2018/1/25 -- 18:56
 * @version 1.0.0
 */
public class Column {
    private final int ordinal;
    private final Class<?> type;
    private final String nickName;
    private final boolean primaryKey;
    private final String columnName;

    /**
     * @param ordinal    第几个
     * @param type       数据类型
     * @param nickName   昵称
     * @param primaryKey 是否 是 primaryKey
     * @param columnName 栏目名称
     */
    public Column(int ordinal, Class<?> type, String nickName, boolean primaryKey, String columnName) {
        this.ordinal = ordinal;
        this.type = type;
        this.nickName = nickName;
        this.primaryKey = primaryKey;
        this.columnName = columnName;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public Class<?> getType() {
        return type;
    }

    public String getNickName() {
        return nickName;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public String getColumnName() {
        return columnName;
    }
}
