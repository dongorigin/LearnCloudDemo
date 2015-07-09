package cn.dong.leancloudtest;

import android.app.Application;

/**
 * author DONG 2015/7/5.
 */
public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AVHelper.init(this);
        instance = this;
    }
}
