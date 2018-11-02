package com.stardust.auojs.inrt.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AppBean implements Parcelable {
    private String lable;
    private String appName;
    private String appPackageName;
    private String appVersion;
    private String appVersionName;

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lable);
        dest.writeString(this.appName);
        dest.writeString(this.appPackageName);
        dest.writeString(this.appVersion);
        dest.writeString(this.appVersionName);
    }

    public AppBean() {
    }

    protected AppBean(Parcel in) {
        this.lable = in.readString();
        this.appName = in.readString();
        this.appPackageName = in.readString();
        this.appVersion = in.readString();
        this.appVersionName = in.readString();
    }

    public static final Creator<AppBean> CREATOR = new Creator<AppBean>() {
        @Override
        public AppBean createFromParcel(Parcel source) {
            return new AppBean(source);
        }

        @Override
        public AppBean[] newArray(int size) {
            return new AppBean[size];
        }
    };
}
