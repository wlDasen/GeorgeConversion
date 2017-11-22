package net.sunniwell.georgeconversion.util;

import android.util.Log;

import net.sunniwell.georgeconversion.db.MoneyRealRateBean;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by admin on 2017/11/22.
 */

public class JSONParserUtil {
    private static final String TAG = "jpd-JSONParserUtil";

    public static synchronized MoneyRealRateBean parseRealRateJSON(String jsonData) {
        Log.d(TAG, "parseRealRateJSON: jsonData:" + jsonData);
        MoneyRealRateBean moneyRealRateBean = new MoneyRealRateBean();
        try {
            JSONObject rateObj = new JSONObject(jsonData);
            String reason = rateObj.getString("reason");
            int errorCode = rateObj.getInt("error_code");
            moneyRealRateBean.setReason(rateObj.getString("reason"));
            moneyRealRateBean.setErrorCode(errorCode);
            if (errorCode == 0) { // 正常获取到汇率数据,封装bean
                JSONArray result = rateObj.getJSONArray("result");
                int length = result.length();
                MoneyRealRateBean.Result[] results = new MoneyRealRateBean.Result[length];
                for (int i = 0; i < length; i++) {
                    JSONObject obj = result.getJSONObject(i);
                    MoneyRealRateBean.Result slt = moneyRealRateBean.new Result();
                    slt.setCurrencyF(obj.getString("currencyF"));
                    slt.setCurrencyF_Name(obj.getString("currencyF_Name"));
                    slt.setCurrencyT(obj.getString("currencyT"));
                    slt.setCurrencyT_Name(obj.getString("currencyT_Name"));
                    slt.setCurrencyFD(obj.getString("currencyFD"));
                    slt.setExchange(obj.getString("exchange"));
                    slt.setResult(obj.getString("result"));
                    slt.setUpdateTime(obj.getString("updateTime"));
                    results[i] = slt;
                }
                moneyRealRateBean.setResults(results);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moneyRealRateBean;
    }
}
