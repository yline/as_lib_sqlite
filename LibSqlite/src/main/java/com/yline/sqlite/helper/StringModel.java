package com.yline.sqlite.helper;

import java.io.Serializable;

/**
 * 数据库存储，String -- String
 *
 * @author yline 2018/1/25 -- 19:31
 * @version 1.0.0
 */
public class StringModel implements Serializable {
    private String key;
    private String value;

    public StringModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
