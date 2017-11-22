package net.sunniwell.georgeconversion.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import net.sunniwell.georgeconversion.view.CustomDialog;

/**
 * Created by admin on 2017/11/22.
 */

public class UpgradeUtil {
    private static final String TAG = "jpd-UpgradeUtil";
    private Activity mActivity;
    private String localVersionName = "";
    private int localVersionCode = 0;
    private String serverVersionName = "";
    private int serverVersionCode = 0;
    private int isForce = 0;
    private String description = "";
    private String apkUrl = "";

    public static UpgradeUtil from(Activity activity) {
        Log.d(TAG, "from: ");
        return new UpgradeUtil(activity);
    }
    
    private UpgradeUtil(Activity activity) {
        Log.d(TAG, "UpgradeUtil: ");
        mActivity = activity;
        getAppLocalInfo();
    }

    private void getAppLocalInfo() {
        PackageManager pm = mActivity.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(mActivity.getPackageName(), 0);
            localVersionCode = info.versionCode;
            localVersionName = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UpgradeUtil serverVersionCode(int code) {
        this.serverVersionCode = code;
        return this;
    }
    public UpgradeUtil serverVersionName(String name) {
        this.serverVersionName = name;
        return this;
    }
    public UpgradeUtil serverIsForce(int isForce) {
        this.isForce = isForce;
        return this;
    }
    public UpgradeUtil description(String description) {
        this.description = description;
        return this;
    }
    public UpgradeUtil apkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
        return this;
    }
    public void update() {
        if (!localVersionName.equals(serverVersionName)) { // 与服务器版本不一致需要升级
            if (isWifiConnected()) {

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("升级提示");
                builder.setMessage("已经是最新版本！");
                builder.setPositiveButton("确定", null);
                builder.show();
            }
        } else { // 与服务器版本一致不需要升级
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: ");
                    CustomDialog dialog = new CustomDialog(mActivity);
                    dialog.show();
                }
            });
        }
    }
    private boolean isWifiConnected() {
        ConnectivityManager manager = (ConnectivityManager)mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }

        return false;
    }
}