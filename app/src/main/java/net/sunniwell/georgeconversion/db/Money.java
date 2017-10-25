package net.sunniwell.georgeconversion.db;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2017/10/25.
 */

public class Money extends DataSupport {
    private String name;
    private String code;

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
