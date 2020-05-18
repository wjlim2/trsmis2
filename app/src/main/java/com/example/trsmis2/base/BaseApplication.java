package com.example.trsmis2.base;

import android.app.Application;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class BaseApplication extends Application {

    private static volatile BaseApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Hawk.init(this).build();
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
