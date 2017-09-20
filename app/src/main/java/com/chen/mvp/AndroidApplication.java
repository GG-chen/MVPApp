package com.chen.mvp;

import android.app.Application;
import android.content.Context;

import com.chen.mvp.api.RetrofitService;
import com.chen.mvp.injector.components.ApplicationComponent;
import com.chen.mvp.injector.components.DaggerApplicationComponent;
import com.chen.mvp.injector.modules.ApplicationModule;
import com.chen.mvp.local.dao.DaoMaster;
import com.chen.mvp.local.dao.DaoSession;
import com.chen.mvp.rxbus.RxBus;
import com.chen.mvp.utils.DownLoadHelper;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.greendao.database.Database;

/**
 * Created by chen on 2017/9/7.
 */

public class AndroidApplication extends Application {
    private static ApplicationComponent sAppComponent;
    private static Context sContext;
    private RxBus mRxBus = new RxBus();
    private String DB_NAME = "db_news";
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        initDataBase();
        initInjector();
        initConfig();
        initDownload();

    }

    private void initDataBase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(this, DB_NAME);
        Database db = mHelper.getWritableDb();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoSession = new DaoMaster(db).newSession();

    }

    private void initDownload() {
        FileDownloader.setupOnApplicationOnCreate(this);
        DownLoadHelper.init(sContext, mRxBus, mDaoSession.getVideoInfoDao());
    }

    private void initConfig() {
        /*if (BuildConfig.DEBUG) {
            LeakCanary.install((Application) getApplication());
            Logger.init("LogTAG");
        }*/
        RetrofitService.init();
    }


    private void initInjector() {
        sAppComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this, mDaoSession, mRxBus))
                .build();
    }

    public static ApplicationComponent getAppComponent() {
        return sAppComponent;
    }

    public static Context getContext() {
        return sContext;
    }

    public Context getApplication() {
        return sContext;
    }
}
