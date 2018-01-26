package com.lib.sqlite.demo.common;

import android.util.Log;

import com.yline.sqlite.SqliteManager;
import com.yline.sqlite.async.AsyncHelper;
import com.yline.sqlite.dao.DaoManager;
import com.yline.sqlite.helper.ByteModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 异步数据库，操作
 *
 * @author yline 2017/9/14 -- 19:38
 * @version 1.0.0
 */
public abstract class AbstractByteModelAsyncTest<Key extends String, Model extends ByteModel> {
    private String TAG = "xxx-";

    protected Random mRandom;

    private AsyncHelper operation;

    public AbstractByteModelAsyncTest() {
        this.TAG += getClass().getSimpleName();
    }

    @Before
    public void setUp() throws Exception {
        mRandom = new Random();

        Log.i(TAG, "setUp: ");
        operation = SqliteManager.getByteModelAsync();
    }

    @Test
    public void testInsertAndLoad() throws Exception {
        final Key key = createRandomPK();
        final Model model = createModel(key);

        SqliteManager.getByteModelAsync().insert(model, new AsyncHelper.OnCompleteListener() {
            @Override
            public void onAsyncComplete(AsyncHelper.Type type) {
                Assert.assertEquals(type, AsyncHelper.Type.Insert);
            }
        }, new AsyncHelper.OnResultListener<Long>() {
            @Override
            public void onAsyncResult(Long aLong) {
                Log.i(TAG, "testInsertAndLoad: ");

                Assert.assertEquals(key, SqliteManager.getByteModelDao().getKey(model));

                Model loadModel = (Model) SqliteManager.getByteModelDao().load(key);
                assertModel(model, loadModel);
            }
        });
    }

    @Test
    public void testCount() throws Exception {
        final long countA = SqliteManager.getByteModelDao().count();

        SqliteManager.getByteModelAsync().insert(createModel(createRandomPK()), new AsyncHelper.OnCompleteListener() {
            @Override
            public void onAsyncComplete(AsyncHelper.Type type) {
                Assert.assertEquals(type, AsyncHelper.Type.Insert);
            }
        }, new AsyncHelper.OnResultListener<Long>() {
            @Override
            public void onAsyncResult(Long aLong) {
                long countB = SqliteManager.getByteModelDao().count();
                Log.i(TAG, "testCount: rowId = " + aLong + ", countA = " + countA + ", countB = " + countB);

                Assert.assertEquals(countA + 1, countB);
            }
        });
    }

    @Test
    public void testDeleteAll() throws Exception {
        SqliteManager.getByteModelDao().insert(createModel(createRandomPK()));

        SqliteManager.getByteModelAsync().deleteAll(new AsyncHelper.OnCompleteListener() {
            @Override
            public void onAsyncComplete(AsyncHelper.Type type) {
                Assert.assertEquals(type, AsyncHelper.Type.DeleteAll);

                DaoManager.v(TAG, "testDeleteAll: ");

                Assert.assertEquals(0, SqliteManager.getByteModelDao().count());
            }
        });
    }

    @Test
    public void testInsertInTx() throws Exception {
        SqliteManager.getByteModelAsync().deleteAll(new AsyncHelper.OnCompleteListener() {
            @Override
            public void onAsyncComplete(AsyncHelper.Type type) {
                Assert.assertEquals(type, AsyncHelper.Type.DeleteAll);

                long count = SqliteManager.getByteModelDao().count();
                DaoManager.v(TAG, "testInsertInTx: count = " + count);
                Assert.assertEquals(0, count);
            }
        });

        // 如果插入的数据的Key重复，则会导致执行失败；不会抛异常
        final List<Model> modelList = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            modelList.add(createModel(createRandomPK()));
        }

        final long teaTime = System.currentTimeMillis();
        SqliteManager.getByteModelAsync().insertInTx(new ArrayList<ByteModel>(modelList), new AsyncHelper.OnCompleteListener() {
            @Override
            public void onAsyncComplete(AsyncHelper.Type type) {
                Assert.assertEquals(type, AsyncHelper.Type.InsertInTx);

                long count = SqliteManager.getByteModelDao().count();
                DaoManager.v(TAG, "testInsertInTx: teaTime = " + (System.currentTimeMillis() - teaTime) + ",count = " + count);
                Assert.assertEquals(modelList.size(), count);
            }
        });
    }

    @Test
    public void testInsertOrReplaceInTx() throws Exception {
        SqliteManager.getByteModelDao().deleteAll();

        final List<Model> listPartial = new ArrayList<>();
        final List<Model> listAll = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Model model = createModel(createRandomPK());
            if (i % 2 == 0) {
                listPartial.add(model);
            }
            listAll.add(model);
        }

        final long teaTime = System.currentTimeMillis();
        SqliteManager.getByteModelAsync().insertOrReplaceInTx(new ArrayList<ByteModel>(listPartial), new AsyncHelper.OnCompleteListener() {
            @Override
            public void onAsyncComplete(AsyncHelper.Type type) {
                Assert.assertEquals(type, AsyncHelper.Type.InsertOrReplaceInTx);

                DaoManager.v(TAG, "testInsertOrReplaceInTx: SQLiteManager.count() = " + SqliteManager.getByteModelDao().count() + ", listPartial.size() = " + listPartial.size());
                // Assert.assertEquals(listPartial.size(), SQLiteManager.count());

                DaoManager.v(TAG, "testInsertOrReplaceInTx: first teaTime = " + (System.currentTimeMillis() - teaTime));
                final long secondTeaTime = System.currentTimeMillis();

                // 第二次异步
                SqliteManager.getByteModelAsync().insertOrReplaceInTx(new ArrayList<ByteModel>(listAll), new AsyncHelper.OnCompleteListener() {
                    @Override
                    public void onAsyncComplete(AsyncHelper.Type type) {
                        Assert.assertEquals(type, AsyncHelper.Type.InsertOrReplaceInTx);

                        long daoCount = SqliteManager.getByteModelDao().count();
                        DaoManager.v(TAG, "testInsertOrReplaceInTx: SQLiteManager.count() = " + daoCount + ", listPartial.size() = " + listAll.size());
                        DaoManager.v(TAG, "testInsertOrReplaceInTx: second teaTime = " + (System.currentTimeMillis() - secondTeaTime));

                        Assert.assertEquals(listAll.size(), daoCount);
                    }
                });
            }
        });
    }

    protected abstract void assertModel(Model tom, Model joe);

    protected abstract Key createRandomPK();

    protected abstract Model createModel(Key pk);
}
