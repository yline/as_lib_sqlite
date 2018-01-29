package com.lib.sqlite.demo;

import com.lib.sqlite.demo.common.AbstractSQLiteTest;
import com.lib.sqlite.demo.db.LongModel;
import com.lib.sqlite.demo.db.LongModelDao;
import com.yline.sqlite.dao.DaoManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.Random;

/**
 * 外部拓展，实现方式
 *
 * @author yline 2018/1/29 -- 10:39
 * @version 1.0.0
 */
public class LongModelTest extends AbstractSQLiteTest<Long, LongModel> {
    private Random mRandom;

    @Before
    public void setUp() throws Exception {
        mDao = (LongModelDao) DaoManager.getInstance().getDaoSession().getModelDao(LongModelDao.TABLE_NAME);

        mRandom = new Random();
    }

    @Override
    protected void assertModel(LongModel tom, LongModel joe) {
        Assert.assertEquals(tom.getKey(), joe.getKey());

        String tomStr = new String(tom.getValue());
        String joeStr = new String(joe.getValue());
        Assert.assertEquals(tomStr, joeStr);
    }

    @Override
    protected Long createRandomPK() {
        return mRandom.nextLong();
    }

    @Override
    protected LongModel createModel(Long pk) {
        return new LongModel(pk, (pk + "value").getBytes());
    }

    @After
    public void tearDown() throws Exception {
        mDao = null;
    }
}
