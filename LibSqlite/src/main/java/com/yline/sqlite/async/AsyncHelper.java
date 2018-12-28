package com.yline.sqlite.async;

import com.yline.sqlite.common.AbstractSafelyDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步处理，数据库
 *
 * @author yline 2018/1/26 -- 10:12
 * @version 1.0.0
 */
public class AsyncHelper<Key, Model> {
	/* ----------------------------- 获取执行的线程池，允许外部设置 ----------------------------- */
	private static ExecutorService mExecutorService;
	
	public static void setExecutorService(ExecutorService executor){
		mExecutorService = executor;
	}
	
	private static ExecutorService getExecutorService() {
		if (null == mExecutorService) {
			synchronized (ExecutorService.class) {
				mExecutorService = Executors.newSingleThreadExecutor();
			}
		}
		return mExecutorService;
	}
	
	/* ----------------------------- 调用的API ----------------------------- */
	private AbstractSafelyDao<Key, Model> mAbstractSafelyDao;
	
	public AsyncHelper(final AbstractSafelyDao<Key, Model> abstractSafelyDao) {
		this.mAbstractSafelyDao = abstractSafelyDao;
	}
	
	public void load(final Key key, final OnResultListener<Model> resultListener) {
		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				Model model = mAbstractSafelyDao.load(key);
				if (null != resultListener) {
					resultListener.onAsyncResult(Type.Load, model);
				}
			}
		});
	}
	
	public void loadAll(final OnResultListener<List<Model>> resultListener) {
		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				List<Model> modelList = mAbstractSafelyDao.loadAll();
				if (null != resultListener) {
					resultListener.onAsyncResult(Type.LoadAll, modelList);
				}
			}
		});
	}
	
	public void insert(final Model model, final OnResultListener<Long> resultListener) {
		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				long rowId = mAbstractSafelyDao.insert(model);
				if (null != resultListener) {
					resultListener.onAsyncResult(Type.Insert, rowId);
				}
			}
		});
	}
	
	public void insertInTx(final Iterable<Model> models, final OnResultListener<Void> resultListener) {
		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				mAbstractSafelyDao.insertInTx(models);
				if (null != resultListener) {
					resultListener.onAsyncResult(Type.InsertInTx, null);
				}
			}
		});
	}
	
	public void insertInTx(final Iterable<Model> models, final boolean cache, final OnResultListener<Void> resultListener) {
		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				mAbstractSafelyDao.insertInTx(models, cache);
				if (null != resultListener) {
					resultListener.onAsyncResult(Type.InsertInTxCache, null);
				}
			}
		});
	}
	
	public void insertOrReplace(final Model model, final OnResultListener<Long> resultListener) {
		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				long rowId = mAbstractSafelyDao.insertOrReplace(model);
				if (null != resultListener) {
					resultListener.onAsyncResult(Type.InsertOrReplace, rowId);
				}
			}
		});
	}
	
	public void insertOrReplaceInTx(final Iterable<Model> models, final OnResultListener<Void> resultListener) {
		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				mAbstractSafelyDao.insertOrReplaceInTx(models);
				if (null != resultListener) {
					resultListener.onAsyncResult(Type.InsertOrReplaceInTx, null);
				}
			}
		});
	}
	
	public void insertOrReplaceInTx(final Iterable<Model> models, final boolean cache, final OnResultListener<Void> resultListener) {
		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				mAbstractSafelyDao.insertOrReplaceInTx(models, cache);
				if (null != resultListener) {
					resultListener.onAsyncResult(Type.InsertOrReplaceInTxCache, null);
				}
			}
		});
	}
	
	public void count(final OnResultListener<Long> resultListener) {
		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				long count = mAbstractSafelyDao.count();
				if (null != resultListener) {
					resultListener.onAsyncResult(Type.Count, count);
				}
			}
		});
	}
	
	public void deleteAll(final OnResultListener<Void> resultListener) {
		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				mAbstractSafelyDao.deleteAll();
				if (null != resultListener) {
					resultListener.onAsyncResult(Type.DeleteAll, null);
				}
			}
		});
	}
	
	public interface OnResultListener<Result> {
		/**
		 * 异步加载完成，并携带回某些数据
		 *
		 * @param type 操作的类型
		 * @param result {rowId, 或 数据集}
		 */
		void onAsyncResult(Type type, Result result);
	}
}
