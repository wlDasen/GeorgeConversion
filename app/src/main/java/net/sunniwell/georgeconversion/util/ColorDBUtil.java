package net.sunniwell.georgeconversion.util;

import android.content.Context;
import android.util.Log;

import net.sunniwell.georgeconversion.R;
import net.sunniwell.georgeconversion.db.ColorBean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by admin on 2017/11/13.
 */

public class ColorDBUtil {
    private static final String TAG = "jpd-ColorDBUtil";

    public static void setDefaultColorList(Context context) {
        String[] colorList = context.getResources().getStringArray(R.array.color_list);
        int[] normalResourceList = context.getResources().getIntArray(R.array.normal_drawable_id);
        int[] selectResourceList = context.getResources().getIntArray(R.array.select_drawable_id);
        for (int i = 0; i < colorList.length; i++) {
            ColorBean colorBean = new ColorBean();
            colorBean.setPosition(i);
            colorBean.setColorStr(colorList[i]);
            colorBean.setNormalResourceId(normalResourceList[i]);
            colorBean.setSelectResourceId(selectResourceList[i]);
            if (i == 0) {
                colorBean.setDefault(true);
            } else {
                colorBean.setDefault(false);
            }
            Log.d(TAG, "setDefaultColorList: " + colorBean);
            colorBean.save();
        }
    }
    public static ColorBean getDefaultColor() {
        return DataSupport.where("isdefault == ?", "1").findFirst(ColorBean.class);
    }
    public static ColorBean getColorBeanAtPosition(String pos) {
        return DataSupport.where("position == ?", pos).findFirst(ColorBean.class);
    }
    public static void printColorBean() {
        List<ColorBean> list = DataSupport.findAll(ColorBean.class);
        for (int i = 0; i < list.size(); i++) {
            Log.d(TAG, "printColorBean: " + list.get(i));
        }
    }
}
