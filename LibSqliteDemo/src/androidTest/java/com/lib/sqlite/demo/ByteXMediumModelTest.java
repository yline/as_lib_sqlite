package com.lib.sqlite.demo;

import android.support.test.runner.AndroidJUnit4;

import com.lib.sqlite.demo.common.AbstractByteModelTest;
import com.lib.sqlite.demo.model.XMediumModel;
import com.yline.sqlite.SQLiteIOUtils;
import com.yline.sqlite.helper.ByteModel;

import org.junit.Assert;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * 中等复杂度，model，单元测试
 * @author yline 2017/9/14 -- 15:02
 * @version 1.0.0
 */
@RunWith(AndroidJUnit4.class)
public class ByteXMediumModelTest extends AbstractByteModelTest {
    @Override
    protected void assertObject(byte[] tom, byte[] joe) {
        if (null == tom && null == joe) {
            Assert.assertEquals(true, true);
            return;
        }

        Object tomObject = SQLiteIOUtils.byte2Object(tom);
        Object joeObject = SQLiteIOUtils.byte2Object(tom);

        Assert.assertTrue(tomObject instanceof XMediumModel); // 断言一波
        Assert.assertTrue(joeObject instanceof XMediumModel); // 断言一波

        Assert.assertEquals(((XMediumModel) tomObject).getUserId(), ((XMediumModel) joeObject).getUserId());
        Assert.assertEquals(((XMediumModel) tomObject).getUserList(), ((XMediumModel) joeObject).getUserList());
        Assert.assertEquals(((XMediumModel) tomObject).getUserIntegerList(), ((XMediumModel) joeObject).getUserIntegerList());
    }

    @Override
    protected ByteModel createModel(String pk) {
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strList.add(pk + "-str");
        }

        List<Integer> intList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            intList.add(i);
        }

        byte[] modelByte = SQLiteIOUtils.object2Byte( new XMediumModel(pk, strList, intList));
        return new ByteModel(pk,modelByte);
    }
}
