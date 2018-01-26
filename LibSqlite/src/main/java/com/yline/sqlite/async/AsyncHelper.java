package com.yline.sqlite.async;

import com.yline.sqlite.common.AbstractSafelyDao;
import com.yline.sqlite.dao.DaoManager;

import java.util.List;

/**
 * 异步处理，数据库
 *
 * @author yline 2018/1/26 -- 10:12
 * @version 1.0.0
 */
public class AsyncHelper<Key, Model> {
    private AbstractSafelyDao<Key, Model> mAbstractSafelyDao;

    public AsyncHelper(final AbstractSafelyDao<Key, Model> abstractSafelyDao) {
        this.mAbstractSafelyDao = abstractSafelyDao;
    }

    public void load(final Key key, final OnCompleteListener completeListener, final OnResultListener<Model> resultListener) {
        DaoManager.getInstance().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                Model model = mAbstractSafelyDao.load(key);
                if (null != completeListener) {
                    completeListener.onAsyncComplete(Type.Load);
                }
                if (null != resultListener) {
                    resultListener.onAsyncResult(model);
                }
            }
        });
    }

    public void loadAll(final OnCompleteListener completeListener, final OnResultListener<List<Model>> resultListener) {
        DaoManager.getInstance().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                List<Model> modelList = mAbstractSafelyDao.loadAll();
                if (null != completeListener) {
                    completeListener.onAsyncComplete(Type.LoadAll);
                }
                if (null != resultListener) {
                    resultListener.onAsyncResult(modelList);
                }
            }
        });
    }

    public void insert(final Model model, final OnCompleteListener completeListener, final OnResultListener<Long> resultListener) {
        DaoManager.getInstance().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                long rowId = mAbstractSafelyDao.insert(model);
                if (null != completeListener) {
                    completeListener.onAsyncComplete(Type.Insert);
                }
                if (null != resultListener) {
                    resultListener.onAsyncResult(rowId);
                }
            }
        });
    }

    public void insertInTx(final Iterable<Model> models, final OnCompleteListener completeListener) {
        DaoManager.getInstance().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                mAbstractSafelyDao.insertInTx(models);
                if (null != completeListener) {
                    completeListener.onAsyncComplete(Type.InsertInTx);
                }
            }
        });
    }

    public void insertInTx(final Iterable<Model> models, final boolean cache, final OnCompleteListener completeListener) {
        DaoManager.getInstance().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                mAbstractSafelyDao.insertInTx(models, cache);
                if (null != completeListener) {
                    completeListener.onAsyncComplete(Type.InsertInTxCache);
                }
            }
        });
    }

    public void insertOrReplace(final Model model, final OnCompleteListener completeListener, final OnResultListener<Long> resultListener) {
        DaoManager.getInstance().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                long rowId = mAbstractSafelyDao.insertOrReplace(model);
                if (null != completeListener) {
                    completeListener.onAsyncComplete(Type.InsertOrReplace);
                }
                if (null != resultListener) {
                    resultListener.onAsyncResult(rowId);
                }
            }
        });
    }

    public void insertOrReplaceInTx(final Iterable<Model> models, final OnCompleteListener completeListener) {
        DaoManager.getInstance().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                mAbstractSafelyDao.insertOrReplaceInTx(models);
                if (null != completeListener) {
                    completeListener.onAsyncComplete(Type.InsertOrReplaceInTx);
                }
            }
        });
    }

    public void insertOrReplaceInTx(final Iterable<Model> models, final boolean cache, final OnCompleteListener completeListener) {
        DaoManager.getInstance().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                mAbstractSafelyDao.insertOrReplaceInTx(models, cache);
                if (null != completeListener) {
                    completeListener.onAsyncComplete(Type.InsertOrReplaceInTxCache);
                }
            }
        });
    }

    public void count(final OnCompleteListener completeListener, final OnResultListener<Long> resultListener) {
        DaoManager.getInstance().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                long count = mAbstractSafelyDao.count();
                if (null != completeListener) {
                    completeListener.onAsyncComplete(Type.Count);
                }
                if (null != resultListener) {
                    resultListener.onAsyncResult(count);
                }
            }
        });
    }

    public void deleteAll(final OnCompleteListener completeListener) {
        DaoManager.getInstance().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                mAbstractSafelyDao.deleteAll();
                if (null != completeListener) {
                    completeListener.onAsyncComplete(Type.DeleteAll);
                }
            }
        });
    }

    public interface OnCompleteListener {
        /**
         * 异步加载完成回调
         *
         * @param type 操作的类型
         */
        void onAsyncComplete(Type type);
    }

    public interface OnResultListener<Result> {
        /**
         * 异步加载完成，并携带回某些数据
         *
         * @param result {rowId, 或 数据集}
         */
        void onAsyncResult(Result result);
    }

    public enum Type {
        Load, LoadAll,
        Insert, InsertInTx, InsertInTxCache,
        InsertOrReplace, InsertOrReplaceInTx, InsertOrReplaceInTxCache,
        Count, DeleteAll
    }
}
