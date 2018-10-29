package com.stardust;

import com.google.gson.Gson;

public class GsonParse {

    public GsonParse() {

    }

    public TaskBean parse(String str) {
        Gson gson = new Gson();
        TaskBean taskBean = gson.fromJson(str, TaskBean.class);
        return taskBean;
    }
}
