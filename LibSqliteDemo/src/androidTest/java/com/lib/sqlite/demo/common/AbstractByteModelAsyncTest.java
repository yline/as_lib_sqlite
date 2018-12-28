package com.lib.sqlite.demo.common;

import android.util.Log;

import com.lib.sqlite.demo.dao.DaoManager;
import com.yline.sqlite.async.AsyncHelper;
import com.yline.sqlite.async.Type;
import com.yline.sqlite.SQLiteManager;
import com.lib.sqlite.demo.dao.model.ByteModel;

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
        operation = DaoManager.getByteModelAsync();
    }

    @Test
    public void testInsertAndLoad() throws Exception {
        final Key key = createRandomPK();
        final Model model = createModel(key);
	
	    DaoManager.getByteModelAsync().insert(model, new AsyncHelper.OnResultListener<Long>() {
		    @Override
		    public void onAsyncResult(Type type, Long aLong) {
			    Assert.assertEquals(type, Type.Insert);
			
			    Log.i(TAG, "testInsertAndLoad: ");
			
			    Assert.assertEquals(key, DaoManager.getByteModelDao().getKey(model));
			
			    Model loadModel = (Model) DaoManager.getByteModelDao().load(key);
			    assertModel(model, loadModel);
		    }
	    });
    }

    @Test
    public void testCount() throws Exception {
        final long countA = DaoManager.getByteModelDao().count();
	
	    DaoManager.getByteModelAsync().insert(createModel(createRandomPK()), new AsyncHelper.OnResultListener<Long>() {
		    @Override
		    public void onAsyncResult(Type type, Long aLong) {
			    Assert.assertEquals(type, Type.Insert);
			
			    long countB = DaoManager.getByteModelDao().count();
			    Log.i(TAG, "testCount: rowId = " + aLong + ", countA = " + countA + ", countB = " + countB);
			
			    Assert.assertEquals(countA + 1, countB);
		    }
	    });
    }

    @Test
    public void testDeleteAll() throws Exception {
	    DaoManager.getByteModelDao().insert(createModel(createRandomPK()));
	
	    DaoManager.getByteModelAsync().deleteAll(new AsyncHelper.OnResultListener<Void>() {
	        @Override
	        public void onAsyncResult(Type type, Void aVoid) {
                Assert.assertEquals(type, Type.DeleteAll);

                SQLiteManager.v(TAG, "testDeleteAll: ");

                Assert.assertEquals(0, DaoManager.getByteModelDao().count());
            }
        });
    }

    @Test
    public void testInsertInTx() throws Exception {
	    DaoManager.getByteModelAsync().deleteAll(new AsyncHelper.OnResultListener<Void>() {
	        @Override
	        public void onAsyncResult(Type type, Void aVoid) {
                Assert.assertEquals(type, Type.DeleteAll);

                long count = DaoManager.getByteModelDao().count();
                SQLiteManager.v(TAG, "testInsertInTx: count = " + count);
                Assert.assertEquals(0, count);
            }
        });

        // 如果插入的数据的Key重复，则会导致执行失败；不会抛异常
        final List<Model> modelList = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            modelList.add(createModel(createRandomPK()));
        }

        final long teaTime = System.currentTimeMillis();
	    DaoManager.getByteModelAsync().insertInTx(new ArrayList<ByteModel>(modelList), new AsyncHelper.OnResultListener<Void>() {
	        @Override
	        public void onAsyncResult(Type type, Void aVoid) {
		        Assert.assertEquals(type, Type.InsertInTx);
		
		        long count = DaoManager.getByteModelDao().count();
		        SQLiteManager.v(TAG, "testInsertInTx: teaTime = " + (System.currentTimeMillis() - teaTime) + ",count = " + count);
		        Assert.assertEquals(modelList.size(), count);
	        }
        });
    }

    @Test
    public void testInsertOrReplaceInTx() throws Exception {
	    DaoManager.getByteModelDao().deleteAll();

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
	    DaoManager.getByteModelAsync().insertOrReplaceInTx(new ArrayList<ByteModel>(listPartial), new AsyncHelper.OnResultListener<Void>() {
            @Override
            public void onAsyncResult(Type type, Void aVoid) {
                Assert.assertEquals(type, Type.InsertOrReplaceInTx);

                SQLiteManager.v(TAG, "testInsertOrReplaceInTx: SQLiteManager.count() = " + DaoManager.getByteModelDao().count() + ", listPartial.size() = " + listPartial.size());
                // Assert.assertEquals(listPartial.size(), SQLiteManager.count());

                SQLiteManager.v(TAG, "testInsertOrReplaceInTx: first teaTime = " + (System.currentTimeMillis() - teaTime));
                final long secondTeaTime = System.currentTimeMillis();

                // 第二次异步
	            DaoManager.getByteModelAsync().insertOrReplaceInTx(new ArrayList<ByteModel>(listAll), new AsyncHelper.OnResultListener<Void>() {
	                @Override
	                public void onAsyncResult(Type type, Void aVoid) {
		                Assert.assertEquals(type, Type.InsertOrReplaceInTx);
		
		                long daoCount = DaoManager.getByteModelDao().count();
		                SQLiteManager.v(TAG, "testInsertOrReplaceInTx: SQLiteManager.count() = " + daoCount + ", listPartial.size() = " + listAll.size());
		                SQLiteManager.v(TAG, "testInsertOrReplaceInTx: second teaTime = " + (System.currentTimeMillis() - secondTeaTime));
		
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
