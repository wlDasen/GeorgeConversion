package net.sunniwell.georgeconversion.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import net.sunniwell.georgeconversion.interfaces.DialogClickCallback;
import net.sunniwell.georgeconversion.view.CustomDialog;

import java.io.File;

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
        if (localVersionName.equals(serverVersionName)) { // 与服务器版本一致不需要升级
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new CustomDialog(mActivity).setContent("已经是最新版本!").setDialogType(CustomDialog.CUSTOM_DIALOG_TYPE_WITH_CONFIRMBUTTON).show();
                }
            });
        } else { // 与服务器版本不一致需要升级
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: ");
                    String content = "发现新版本:" + serverVersionName + "是否下载更新?"
                            + "\n" + description;
                    new CustomDialog(mActivity, new DialogClickCallback() {
                        @Override
                        public void onConfirmButtonClicked() {
                            if (!isWifiConnected()) { // 非WIFI模式下下载
                                new CustomDialog(mActivity, new DialogClickCallback() {
                                    @Override
                                    public void onConfirmButtonClicked() {
                                        download();
                                    }
                                }).setContent("正在使用流量下载\n是否要继续下载?").setCancel(false).show();
                            } else { // WIFI模式下下载
                                download();
                            }
                        }
                    }).setContent(content).setCancel(false).show();
                }
            });
        }
    }
    private void download() {
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(apkUrl));
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        String fileDir = mActivity.getFilesDir().getAbsolutePath();
        Log.d(TAG, "download: fileDir:" + fileDir);
        req.setDestinationInExternalFilesDir(mActivity, fileDir, "george.apk");
        req.setTitle("George汇率");

        DownloadManager dm = (DownloadManager)mActivity.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = dm.enqueue(req);
        SharedPreferenceUtil.setLong(mActivity, "downloadid", downloadId);
        Log.d(TAG, "download: downloadId:" + downloadId);
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
    public void checkStatus() {
        long downloadid = SharedPreferenceUtil.getLong(mActivity, "downloadid", 0);
        Log.d(TAG, "checkStatus: id:" + downloadid);
        DownloadManager dm = (DownloadManager)mActivity.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadid);
        Cursor cursor = dm.query(query);
        if (cursor != null && cursor.moveToFirst()) {
            int statue = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
            switch (statue) {
                case DownloadManager.STATUS_PENDING:
                    Log.d(TAG, "download: STATUS_PENDING.");
                    break;
                case DownloadManager.STATUS_PAUSED:
                    Log.d(TAG, "download: STATUS_PAUSED.");
                    break;
                case DownloadManager.STATUS_RUNNING:
                    Log.d(TAG, "download: STATUS_RUNNING.");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.d(TAG, "download: STATUS_SUCCESSFUL.");
                    break;
                case DownloadManager.STATUS_FAILED:
                    Log.d(TAG, "download: STATUS_FAILED.");
                    break;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }
}