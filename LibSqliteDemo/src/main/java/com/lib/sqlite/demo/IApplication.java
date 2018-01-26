package com.lib.sqlite.demo;

import android.app.Application;

import com.yline.sqlite.SqliteManager;

/**
 * @author yline 2018/1/26 -- 13:09
 * @version 1.0.0
 */
public class IApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SqliteManager.init(this);
    }
}
