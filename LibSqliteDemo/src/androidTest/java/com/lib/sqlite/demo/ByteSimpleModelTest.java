package com.lib.sqlite.demo;

import com.lib.sqlite.demo.common.AbstractByteModelTest;
import com.yline.sqlite.SQLiteIOUtils;
import com.yline.sqlite.helper.ByteModel;
import com.yline.sqlite.helper.StringModel;

import org.junit.Assert;

/**
 * NetCacheModel 装载 SimpleModel
 *
 * @author yline 2017/9/14 -- 14:26
 * @version 1.0.0
 */
public class ByteSimpleModelTest extends AbstractByteModelTest {
    @Override
    protected void assertObject(byte[] tom, byte[] joe) {
        if (null == tom && null == joe) {
            Assert.assertEquals(true, true);
            return;
        }

        Object tomObject = SQLiteIOUtils.byte2Object(tom);
        Object joeObject = SQLiteIOUtils.byte2Object(tom);

        Assert.assertTrue(tomObject instanceof StringModel); // 断言一波
        Assert.assertTrue(joeObject instanceof StringModel); // 断言一波

        Assert.assertEquals(((StringModel) tomObject).getKey(), ((StringModel) joeObject).getKey());
        Assert.assertEquals(((StringModel) tomObject).getValue(), ((StringModel) joeObject).getValue());
    }

    @Override
    protected ByteModel createModel(String pk) {
        byte[] modelByte = SQLiteIOUtils.object2Byte(new StringModel(pk, pk + "-value"));
        return new ByteModel(pk, modelByte);
    }
}
