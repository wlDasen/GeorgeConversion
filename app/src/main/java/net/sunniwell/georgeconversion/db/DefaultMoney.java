package net.sunniwell.georgeconversion.db;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2017/10/25.
 */

public class DefaultMoney extends DataSupport {
    private String name;
    private String code;
    private boolean isSelected;
    private double count;

    public DefaultMoney() {}
    public DefaultMoney(String name, String code, boolean isSelected, double count) {
        this.name = name;
        this.code = code;
        this.isSelected = isSelected;
        this.count = count;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public double getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
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
}
