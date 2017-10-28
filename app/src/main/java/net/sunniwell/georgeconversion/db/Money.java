package net.sunniwell.georgeconversion.db;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2017/10/25.
 */

public class Money extends DataSupport {
    private String name;
    private String code;
    private boolean isMain4Money;
    private double base1CNYToCurrent;
    private double base1CurrentToCNY;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public Money(String name, String code, boolean isMain4Money, double base1CNYToCurrent,
                 double base1CurrentToCNY) {
        this.name = name;
        this.code = code;
        this.isMain4Money = isMain4Money;
        this.base1CNYToCurrent = base1CNYToCurrent;
        this.base1CurrentToCNY = base1CurrentToCNY;
    }

    public double getBase1CNYToCurrent() {
        return base1CNYToCurrent;
    }

    public void setBase1CNYToCurrent(double base1CNYToCurrent) {
        this.base1CNYToCurrent = base1CNYToCurrent;
    }

    public boolean isMain4Money() {
        return isMain4Money;
    }

    public void setMain4Money(boolean main4Money) {
        isMain4Money = main4Money;
    }

    public double getBase1CurrentToCNY() {
        return base1CurrentToCNY;
    }

    public void setBase1CurrentToCNY(double base1CurrentToCNY) {
        this.base1CurrentToCNY = base1CurrentToCNY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "[Money]name:" + name + ",code:" + code + ",isMain4Money:" + isMain4Money
                + ",base1CNYToCurrent:" + base1CNYToCurrent + ",base1CurrentToCNY:" + base1CurrentToCNY;
    }
}
