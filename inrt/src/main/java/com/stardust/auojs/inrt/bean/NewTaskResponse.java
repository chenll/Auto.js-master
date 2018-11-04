package com.stardust.auojs.inrt.bean;

import java.util.List;

public class NewTaskResponse {
    private String code;
    private String msg;
    private List<NewTaskBean> datalist;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NewTaskBean> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<NewTaskBean> datalist) {
        this.datalist = datalist;
    }
}
