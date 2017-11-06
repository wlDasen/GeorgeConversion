package net.sunniwell.georgeconversion.util;

import android.util.Log;

import net.sunniwell.georgeconversion.db.Money;

import java.util.Comparator;

/**
 * Created by admin on 2017/10/24.
 */

public class PinyinComparator implements Comparator<Money> {
    private static final String TAG = "jpd-Ppycpt";

    @Override
    public int compare(Money sortData, Money t1) {
        if ("#".equals(sortData.getFirstLetter())) {
            return -1;
        } else if ("#".equals(t1.getFirstLetter())){
            return 1;
        } else {
            return sortData.getLetters().compareTo(t1.getLetters());
        }
    }
}
