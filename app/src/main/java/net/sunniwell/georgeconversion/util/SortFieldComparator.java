package net.sunniwell.georgeconversion.util;

import net.sunniwell.georgeconversion.db.Money;

import java.util.Comparator;

/**
 * Created by admin on 2017/11/7.
 */

public class SortFieldComparator implements Comparator<Money> {
    private static final String TAG = "jpd-SFCom";

    @Override
    public int compare(Money sortData, Money t1) {
            return sortData.getSortField() - t1.getSortField();
    }
}
