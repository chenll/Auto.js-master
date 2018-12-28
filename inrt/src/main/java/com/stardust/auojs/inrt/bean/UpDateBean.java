package com.stardust.auojs.inrt.bean;

public class UpDateBean {

    String downloadUrl;
    String apkMd5;

    public UpDateBean(String downloadUrl, String apkMd5) {
        this.downloadUrl = downloadUrl;
        this.apkMd5 = apkMd5;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getApkMd5() {
        return apkMd5;
    }

    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }
}
