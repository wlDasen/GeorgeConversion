package net.sunniwell.georgeconversion.util;

/**
 * Created by admin on 17/10/27.
 */

public class CalculateUtil {
    public static double[] calculate(double[] rates, double baseRate, double count) {
        double[] data = new double[4];
        for (int i = 0; i < rates.length; i++) {
            if (rates[i] == baseRate) {
                data[i] = count;
            } else {
                data[i] = count / baseRate * rates[i];
            }
        }

        return data;
    }
}