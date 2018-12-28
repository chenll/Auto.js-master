package com.stardust.auojs.inrt.bean;

import java.util.List;

public class AppDownloadResponse {
    private String code;
    private List<AppDownloadBean> datalist;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<AppDownloadBean> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<AppDownloadBean> datalist) {
        this.datalist = datalist;
    }
}
