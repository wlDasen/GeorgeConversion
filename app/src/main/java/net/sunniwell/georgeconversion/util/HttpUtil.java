package net.sunniwell.georgeconversion.util;

import android.util.Log;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by admin on 2017/10/25.
 */

public class HttpUtil {
    private static final String TAG = "jpd-HttpUtil";

    public static void sendRequest(String url, Callback callback) {
        Log.d(TAG, "sendRequest: url:" + url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
