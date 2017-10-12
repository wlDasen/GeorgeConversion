package net.sunniwell.georgeconversion;

import android.app.Application;
import android.content.Context;

/**
 * Created by admin on 2017/10/12.
 */

public class MainApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
