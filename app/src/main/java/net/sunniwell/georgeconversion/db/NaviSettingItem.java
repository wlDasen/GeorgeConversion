package net.sunniwell.georgeconversion.db;

/**
 * Created by admin on 2017/11/8.
 * Navigtion设置的主要条目数据类
 */

public class NaviSettingItem {
    /**
     * item标题
     */
    private String itemTitle;
    /**
     * item默认值
     */
    private String itemValue;
    /**
     * item分组归属
     */
    private String group;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    @Override
    public String toString() {
        return "[NaviSettingItem]title:" + this.itemTitle + ",value:" + this.itemValue
                + ",group:" + this.group;
    }
}
