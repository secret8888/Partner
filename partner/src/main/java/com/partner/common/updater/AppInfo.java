package com.partner.common.updater;

import java.util.ArrayList;

/**
 * @author emilyu
 */
public class AppInfo {
    /**
     * 是否有新版本
     */
    public boolean newVersion = false;

    /**
     * 新版本version code
     */
    public String versionCode;

    /**
     * 新版本version name
     */
    public String versioName;

    /**
     * 应用大小
     */
    public String size = "";

    /**
     * 新功能说明
     */
    public ArrayList<String> updateTxt;

    /**
     * 下载Url
     */
    public String downloadUrl;
}
