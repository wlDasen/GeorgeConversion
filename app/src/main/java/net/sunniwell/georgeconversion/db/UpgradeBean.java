package net.sunniwell.georgeconversion.db;

/**
 * Created by admin on 2017/11/22.
 */

public class UpgradeBean {
    private String versionName;
    private int versionCode;
    private int isForce;
    private String description;
    private String apkUrl;

    @Override
    public String toString() {
        return "[UpgradeBean]versionName:" + versionName + ",versionCode:" + versionCode
                + ",isForce:" + isForce + ",description:" + description + ",apkUrl:" + apkUrl;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getIsForce() {
        return isForce;
    }

    public void setIsForce(int isForce) {
        this.isForce = isForce;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }
}
