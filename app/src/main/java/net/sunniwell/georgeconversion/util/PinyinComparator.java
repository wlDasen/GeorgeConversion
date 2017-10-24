package net.sunniwell.georgeconversion.util;

import net.sunniwell.georgeconversion.db.SortData;

import java.util.Comparator;

/**
 * Created by admin on 2017/10/24.
 */

public class PinyinComparator implements Comparator<SortData> {
    private static final String TAG = "jpd-Ppycpt";

    @Override
    public int compare(SortData sortData, SortData t1) {
//        Log.d(TAG, "compare: forward:" + sortData.getName() + ",back:" + t1.getName()
//                + ",res:" + sortData.getName().compareTo(t1.getName()));
        return sortData.getLetters().compareTo(t1.getLetters());
    }
}
