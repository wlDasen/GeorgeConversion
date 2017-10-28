package net.sunniwell.georgeconversion.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

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
