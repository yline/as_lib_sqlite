package com.lib.sqlite.demo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.lib.sqlite.demo.common.AbstractSQLiteTest;
import com.lib.sqlite.demo.dao.DaoManager;
import com.lib.sqlite.demo.dao.model.StringModel;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.Random;

// @RunWith(AndroidJUnit4.class)
public class SQLiteValueTest extends AbstractSQLiteTest<String, StringModel> {
    public static final String TAG = "xxx-SQLiteValueTest";

    protected Random mRandom;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        mDao = DaoManager.getStringModelDao();
        mRandom = new Random();
    }

    @Override
    protected void assertModel(StringModel tom, StringModel joe) {
        Assert.assertEquals(tom.getKey(), joe.getKey());
        Assert.assertEquals(tom.getValue(), joe.getValue());
    }

    @Override
    protected String createRandomPK() {
        return mRandom.nextInt() + "";
    }

    @Override
    protected StringModel createModel(String pk) {
        String value = pk + "-value";
        return new StringModel(pk, value);
    }

    @After
    public void tearDown() throws Exception {
        mDao = null;
    }
}
