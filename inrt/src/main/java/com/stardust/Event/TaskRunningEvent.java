package com.stardust.Event;

public class TaskRunningEvent {

    public String packageName;
    public int surplusTimes;


    public TaskRunningEvent(String packageName, int surplusTimes) {
        this.packageName = packageName;
        this.surplusTimes = surplusTimes;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getSurplusTimes() {
        return surplusTimes;
    }

    public void setSurplusTimes(int surplusTimes) {
        this.surplusTimes = surplusTimes;
    }

}
