package com.stardust.auojs.inrt.bean;

import java.util.List;

public class NewTaskResponse {
    private String code;
    private String msg;


    private String imeiId;
    private List<NewTaskBeanById> datalist;

    public String getImeiId() {
        return imeiId;
    }

    public void setImeiId(String imeiId) {
        this.imeiId = imeiId;
    }

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

    public List<NewTaskBeanById> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<NewTaskBeanById> datalist) {
        this.datalist = datalist;
    }
}
