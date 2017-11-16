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

    public static Response sendPostByOkHttp(String url, String key, String from, String to) {
        Log.d(TAG, "sendPostByOkHttp: url:" + url + ",from:" + from + ",to:" + to);
        OkHttpClient client = new OkHttpClient();
        Log.d(TAG, "sendPostByOkHttp: 1");
        RequestBody body = new FormBody.Builder()
                            .add("key", key)
                            .add("from", from)
                            .add("to", to).build();
        Log.d(TAG, "sendPostByOkHttp: 2");
        Request request = new Request.Builder().url(url).post(body).build();
        Log.d(TAG, "sendPostByOkHttp: 3");
        Response response = null;
        try {
            Log.d(TAG, "sendPostByOkHttp: ");
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "sendPostByOkHttp: ");
        return response;
    }
    public static String sendRequestByHttpURLConnection(String requestUrl, String key, String from, String to) {
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
            Log.d(TAG, "run: before get inputstream..");
            InputStream is = conn.getInputStream();
            Log.d(TAG, "run: after get inputstream..");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            Log.d(TAG, "run: begin receive...");
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            Log.d(TAG, "run: after receive...");
            byte[] byteArray = baos.toByteArray();
            response = new String(byteArray);
            Log.d(TAG, "run: recvData:" + response);
            Log.d(TAG, "run: finish connect....");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return response;
    }

//    public static void sendJSONPOSTByVolley(Context context, String url, Response.Listener<String> listener
//        , Response.ErrorListener errorListener, final String key, final String from, final String to) {
//        Log.d(TAG, "sendJSONRequestByVolley: ");
//        RequestQueue queue = Volley.newRequestQueue(context);
//        StringRequest request = new StringRequest(Request.Method.POST, url, listener,
//                errorListener) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> map = new HashMap<>();
//                map.put("key", key);
//                map.put("from", from);
//                map.put("to", to);
//                return map;
//            }
//        };
//        queue.add(request);
//    }
}
