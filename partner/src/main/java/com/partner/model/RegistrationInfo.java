package com.partner.model;

import java.io.Serializable;

/**
 * Created by yuym on 2015/7/4.
 */
public class RegistrationInfo implements Serializable{
    private long createTime;

    private long lastModTime;

    private int userId;

    private String userenrollCellphone;

    private int userenrollInfoId;

    private String userenrollInfoParent;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastModTime() {
        return lastModTime;
    }

    public void setLastModTime(long lastModTime) {
        this.lastModTime = lastModTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserenrollCellphone() {
        return userenrollCellphone;
    }

    public void setUserenrollCellphone(String userenrollCellphone) {
        this.userenrollCellphone = userenrollCellphone;
    }

    public int getUserenrollInfoId() {
        return userenrollInfoId;
    }

    public void setUserenrollInfoId(int userenrollInfoId) {
        this.userenrollInfoId = userenrollInfoId;
    }

    public String getUserenrollInfoParent() {
        return userenrollInfoParent;
    }

    public void setUserenrollInfoParent(String userenrollInfoParent) {
        this.userenrollInfoParent = userenrollInfoParent;
    }
}
