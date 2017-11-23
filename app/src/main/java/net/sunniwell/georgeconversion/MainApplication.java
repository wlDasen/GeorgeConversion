package net.sunniwell.georgeconversion;

import android.app.Application;
import android.content.Context;

import com.liulishuo.filedownloader.FileDownloader;

import org.litepal.LitePal;

/**
 * Created by admin on 2017/10/12.
 */

public class MainApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
