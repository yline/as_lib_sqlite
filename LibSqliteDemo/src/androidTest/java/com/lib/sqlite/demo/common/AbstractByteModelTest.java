package com.lib.sqlite.demo.common;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.lib.sqlite.demo.dao.DaoManager;
import com.lib.sqlite.demo.dao.model.ByteModel;

import org.junit.Assert;
import org.junit.Before;

import java.util.Random;

/**
 * 数据库在 NetCache基础上测试
 *
 * @author yline 2017/9/14 -- 14:20
 * @version 1.0.0
 */
public abstract class AbstractByteModelTest extends AbstractSQLiteTest<String, ByteModel> {
    protected Random mRandom;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        mDao = DaoManager.getByteModelDao();
        mRandom = new Random();
    }

    @Override
    protected void assertModel(ByteModel tom, ByteModel joe) {
        Assert.assertEquals(tom.getKey(), joe.getKey());
        assertObject(tom.getValue(), joe.getValue());
    }

    protected abstract void assertObject(byte[] tom, byte[] joe);

    @Override
    protected String createRandomPK() {
        return mRandom.nextInt() + "";
    }
}
