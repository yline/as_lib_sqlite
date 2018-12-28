package com.lib.sqlite.demo.dao.model;

import java.io.Serializable;

/**
 * 数据库存储，String -- byte[]
 *
 * @author yline 2018/1/25 -- 19:36
 * @version 1.0.0
 */
public class ByteModel implements Serializable {
    private String key;
    private byte[] value;

    public ByteModel(String key, byte[] value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
