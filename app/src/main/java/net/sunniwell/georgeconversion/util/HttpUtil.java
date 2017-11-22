package net.sunniwell.georgeconversion.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2017/10/25.
 */

public class HttpUtil {
    private static final String TAG = "jpd-HttpUtil";

    public static String getByURLConnection(String updateUrl) {
        Log.d(TAG, "GetByURLConnection: updateUrl:" + updateUrl);
        String response = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(updateUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            response = new String(byteArray);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return response;
    }
    public static String postByURLConnection(String requestUrl, String key, String from, String to) {
        HttpURLConnection conn = null;
        String response = null;
        try {
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            String writeData = String.format("key=%s&from=%s&to=%s", key, from, to);
            byte[] b = writeData.getBytes();
            os.write(b);
            os.flush();
            os.close();
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            response = new String(byteArray);
            Log.d(TAG, "run: recvData:" + response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return response;
    }
}
