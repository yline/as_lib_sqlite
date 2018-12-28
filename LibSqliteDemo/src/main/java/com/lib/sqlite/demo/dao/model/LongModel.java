package com.lib.sqlite.demo.dao.model;

/**
 * @author yline 2018/1/29 -- 9:57
 * @version 1.0.0
 */
public class LongModel {
    private long key;
    private byte[] value;

    public LongModel(long key, byte[] value) {
        this.key = key;
        this.value = value;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
