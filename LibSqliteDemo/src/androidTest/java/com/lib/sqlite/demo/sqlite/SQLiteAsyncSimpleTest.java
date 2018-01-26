package com.lib.sqlite.demo.sqlite;

import com.lib.sqlite.demo.common.AbstractByteModelAsyncTest;
import com.yline.sqlite.SQLiteIOUtils;
import com.yline.sqlite.helper.ByteModel;
import com.yline.sqlite.helper.StringModel;

import org.junit.Assert;

public class SQLiteAsyncSimpleTest extends AbstractByteModelAsyncTest<String, ByteModel> {

    @Override
    protected void assertModel(ByteModel tom, ByteModel joe) {
        Assert.assertEquals(tom.getKey(), joe.getKey());
        assertModel(tom.getValue(), joe.getValue());
    }

    private void assertModel(Object tom, Object joe){
        if (null == tom && null == joe) {
            Assert.assertEquals(true, true);
            return;
        }

        Assert.assertTrue(tom instanceof StringModel); // 断言一波
        Assert.assertTrue(joe instanceof StringModel); // 断言一波

        Assert.assertEquals(((StringModel) tom).getKey(), ((StringModel) joe).getKey());
        Assert.assertEquals(((StringModel) tom).getValue(), ((StringModel) joe).getValue());
    }

    protected String createRandomPK() {
        return mRandom.nextInt() + "";
    }

    protected ByteModel createModel(String pk) {
        byte[] modelByte = SQLiteIOUtils.object2Byte(new StringModel(pk, pk + "-value"));
        return new ByteModel(pk, modelByte);
    }
}
