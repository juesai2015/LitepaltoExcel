package com.example.admin.litepaltoexcel;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by admin on 2018/6/23.
 */

public class IApplication extends Application {

    private static Context context;
    private static Application instans;

    @Override
    public void onCreate() {
        super.onCreate();
        instans = this;
        context = getApplicationContext();
        initLitePal();
    }

    private void initLitePal() {
        LitePal.initialize(this);
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(base);
//    }


}
