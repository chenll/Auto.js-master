package com.stardust.auojs.inrt.bean;

import java.util.ArrayList;

public class AppAutoMessage {

    private String appName;
    private String packageName;
    private String activityMain;
    private String activityDisturb;
    private String targetTextId;
    private String rollViewId;
    private ArrayList<String> dialogIds;



    private String exitBtnId;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActivityMain() {
        return activityMain;
    }

    public void setActivityMain(String activityMain) {
        this.activityMain = activityMain;
    }

    public String getActivityDisturb() {
        return activityDisturb;
    }

    public void setActivityDisturb(String activityDisturb) {
        this.activityDisturb = activityDisturb;
    }

    public String getTargetTextId() {
        return targetTextId;
    }

    public void setTargetTextId(String targetTextId) {
        this.targetTextId = targetTextId;
    }

    public String getRollViewId() {
        return rollViewId;
    }

    public void setRollViewId(String rollViewId) {
        this.rollViewId = rollViewId;
    }

    public String[]  getDialogIds() {

        if(dialogIds==null||dialogIds.isEmpty()){
            return  null;
        }
        String[] array = (String[])dialogIds.toArray(new String[dialogIds.size()]);

        return array;
    }

    public void setDialogIds(ArrayList<String> dialogIds) {
        this.dialogIds = dialogIds;
    }
    public String getExitBtnId() {
        return exitBtnId;
    }

    public void setExitBtnId(String exitBtnId) {
        this.exitBtnId = exitBtnId;
    }
    @Override
    public String toString() {
        return "AppAutoMessage{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", activityMain='" + activityMain + '\'' +
                ", activityDisturb='" + activityDisturb + '\'' +
                ", targetTextId='" + targetTextId + '\'' +
                ", rollViewId='" + rollViewId + '\'' +
                ", dialogIds=" + dialogIds +
                '}';
    }
}
