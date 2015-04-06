package com.mole.life.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by redmole on 2015/3/22.
 */
@Table(name = "sys_config")
public class SysConfig extends EntityBase {
    public static final String COL_VERSION = "_version";
    public static final String COL_NEW_VERSION = "_new_version";
    public static final String COL_HOME_WEB = "_home_web";
    public static final String COL_DONWLOAD_URL = "_donwload_url";
        public static final String COL_LIFE_INIT_URL = "_life_init_url";
    public static final String COL_LIFE_INIT_time = "_life_init_time";

    @Column(column = COL_VERSION)
    public String version;

    @Column(column = COL_NEW_VERSION)
    public String newWersion;

    @Column(column = COL_HOME_WEB)
    public String homeWeb;

    @Column(column = COL_DONWLOAD_URL)
    public String donwloadUrl;

    @Column(column = COL_LIFE_INIT_URL)
    public String siteInitUrl;

    @Column(column = COL_LIFE_INIT_time)
    public String siteInitTime;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHomeWeb() {
        return homeWeb;
    }

    public void setHomeWeb(String homeWeb) {
        this.homeWeb = homeWeb;
    }

    public String getDonwloadUrl() {
        return donwloadUrl;
    }

    public void setDonwloadUrl(String donwloadUrl) {
        this.donwloadUrl = donwloadUrl;
    }

    public String getNewWersion() {
        return newWersion;
    }

    public void setNewWersion(String newWersion) {
        this.newWersion = newWersion;
    }

    public String getSiteInitUrl() {
        return siteInitUrl;
    }

    public void setSiteInitUrl(String siteInitUrl) {
        this.siteInitUrl = siteInitUrl;
    }

    public String getSiteInitTime() {
        return siteInitTime;
    }

    public void setSiteInitTime(String siteInitTime) {
        this.siteInitTime = siteInitTime;
    }

    @Override
    public String toString() {
        return "SysConfig{" +
                "version='" + version + '\'' +
                ", newWersion='" + newWersion + '\'' +
                ", homeWeb='" + homeWeb + '\'' +
                ", donwloadUrl='" + donwloadUrl + '\'' +
                ", siteInitUrl='" + siteInitUrl + '\'' +
                ", siteInitTime='" + siteInitTime + '\'' +
                '}';
    }
}
