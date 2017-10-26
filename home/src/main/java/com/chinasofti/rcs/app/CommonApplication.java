package com.chinasofti.rcs.app;

import android.app.Application;
import android.content.Context;
import com.chinasofti.common.utils.log.YLog;
import com.didi.virtualapk.PluginManager;

public class CommonApplication extends Application{

    private static final String TAG = "CommonApplication";
    private static boolean isDebug = true;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        PluginManager.getInstance(base).init();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        YLog.init(isDebug,"rcs");
    }
}
