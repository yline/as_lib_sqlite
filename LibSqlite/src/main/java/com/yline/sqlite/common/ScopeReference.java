package com.yline.sqlite.common;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 数据引用、缓存类
 *
 * @author yline 2018/1/25 -- 18:54
 * @version 1.0.0
 */
public class ScopeReference<Key, Module> {
    private final HashMap<Key, Reference<Module>> mHashMap;
    private final ReentrantLock mReentrantLock;

    public ScopeReference() {
        mHashMap = new HashMap<>();
        mReentrantLock = new ReentrantLock();
    }

    public Module get(Key key) {
        Reference<Module> reference;

        mReentrantLock.lock();
        try {
            reference = mHashMap.get(key);
        } finally {
            mReentrantLock.unlock();
        }

        if (null != reference) {
            return reference.get();
        } else {
            return null;
        }
    }

    public Module getNoLock(Key key) {
        Reference<Module> reference = mHashMap.get(key);
        if (null != reference) {
            return reference.get();
        } else {
            return null;
        }
    }

    public void put(Key key, Module module) {
        mReentrantLock.lock();
        try {
            mHashMap.put(key, new WeakReference<>(module));
        } finally {
            mReentrantLock.unlock();
        }
    }

    public void putNoLock(Key key, Module module) {
        mHashMap.put(key, new WeakReference<>(module));
    }

    public void remove(Key key) {
        mReentrantLock.lock();
        try {
            mHashMap.remove(key);
        } finally {
            mReentrantLock.unlock();
        }
    }

    public void remove(Iterable<Key> keys) {
        mReentrantLock.lock();
        try {
            for (Key key : keys) {
                mHashMap.remove(key);
            }
        } finally {
            mReentrantLock.unlock();
        }
    }

    public void clear() {
        mReentrantLock.lock();
        try {
            mHashMap.clear();
        } finally {
            mReentrantLock.unlock();
        }
    }

    public void lock() {
        mReentrantLock.lock();
    }

    public void unLock() {
        mReentrantLock.unlock();
    }
}
