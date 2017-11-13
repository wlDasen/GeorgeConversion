package net.sunniwell.georgeconversion.db;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2017/11/13.
 */

public class ColorBean extends DataSupport {
    private int position;
    private int normalResourceId;
    private int selectResourceId;
    private String colorStr;
    private boolean isDefault;

    public int getNormalResourceId() {
        return normalResourceId;
    }

    public void setNormalResourceId(int normalResourceId) {
        this.normalResourceId = normalResourceId;
    }

    public int getSelectResourceId() {
        return selectResourceId;
    }

    public void setSelectResourceId(int selectResourceId) {
        this.selectResourceId = selectResourceId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getColorStr() {
        return colorStr;
    }

    public void setColorStr(String colorStr) {
        this.colorStr = colorStr;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return "[Color]position:" + position + ",normalResourceId:" + normalResourceId +
                ",selectResourceId:" + selectResourceId + ",colorStr:" + colorStr +
                ",isDefault:" + isDefault;
    }
}
