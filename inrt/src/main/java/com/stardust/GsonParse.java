package com.stardust;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class GsonParse {

    public static TaskBean parse(String str) {
//        if (1 == 1) {
//            return addTest();
//        }
        Gson gson = new Gson();
        TaskBean taskBean = gson.fromJson(str, TaskBean.class);
        return taskBean;
    }

    private static TaskBean addTest() {

        TaskBean taskBean = new TaskBean();
        List<TaskBean.DataBean> data = new ArrayList<>();
        TaskBean.DataBean dataBean = new TaskBean.DataBean();
        dataBean.setSingleSlideTimes(2);
        dataBean.setSlidingInterval(600);
        dataBean.setSlidingSpeed(1000);
        dataBean.setTotalNumber(2);
        dataBean.setWaitForTime(1000);
        data.add(dataBean);
        taskBean.setData(data);
        return taskBean;

    }

}
