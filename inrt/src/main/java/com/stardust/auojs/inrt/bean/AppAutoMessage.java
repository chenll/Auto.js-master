package com.stardust.auojs.inrt.bean;

import android.preference.PreferenceManager;

import com.stardust.auojs.inrt.App;

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
    private ArrayList<String> signInIds;
    private long signInTime;

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

    public String[] getDialogIds() {

        if (dialogIds == null || dialogIds.isEmpty()) {
            return null;
        }
        String[] array = (String[]) dialogIds.toArray(new String[dialogIds.size()]);

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

    public String[] getSignInIds() {
        if (signInIds == null || signInIds.isEmpty()) {
            return null;
        }
        String[] array = (String[]) signInIds.toArray(new String[signInIds.size()]);
        return array;

    }

    public void setSignInIds(ArrayList<String> signInIds) {
        this.signInIds = signInIds;
    }

    public long getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(long signInTime) {
        this.signInTime = signInTime;
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

    public boolean isCanSign() {
        if (signInTime == 0 || signInIds == null || signInIds.isEmpty()) {
            return false;
        }
        long lastSignInTime = PreferenceManager.getDefaultSharedPreferences(App.getApplication()).getLong(packageName + "_lastSignintime", 0l);
        if (System.currentTimeMillis() - lastSignInTime < signInTime) {
            return false;
        }
        PreferenceManager.getDefaultSharedPreferences(App.getApplication()).edit().putLong(packageName + "_lastSignintime", System.currentTimeMillis()).commit();
        return true;
    }
}
