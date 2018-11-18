package com.stardust.auojs.inrt.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class NewTaskBean {


    /**
     * f_PackageName : com.yanhui.qktx
     * f_AppName : 趣看天下
     * f_AppVersion : 2.2.8
     * totalNumber : 5
     * slidingSpeed : 3000
     * waitForTime : 5000
     * singleSlideTimes : 6
     * slidingInterval : 3000
     */

    @Id
    private String f_PackageName;
    private String f_AppName;
    private String f_AppVersion;
    private int totalNumber;
    private int slidingSpeed;
    private int waitForTime;
    private int singleSlideTimes;
    private int slidingInterval;
    private boolean isExecuted = false;
    private boolean isExecutedSussed;



    private int sort;

    @Generated(hash = 896789543)
    public NewTaskBean(String f_PackageName, String f_AppName, String f_AppVersion,
            int totalNumber, int slidingSpeed, int waitForTime,
            int singleSlideTimes, int slidingInterval, boolean isExecuted,
            boolean isExecutedSussed, int sort) {
        this.f_PackageName = f_PackageName;
        this.f_AppName = f_AppName;
        this.f_AppVersion = f_AppVersion;
        this.totalNumber = totalNumber;
        this.slidingSpeed = slidingSpeed;
        this.waitForTime = waitForTime;
        this.singleSlideTimes = singleSlideTimes;
        this.slidingInterval = slidingInterval;
        this.isExecuted = isExecuted;
        this.isExecutedSussed = isExecutedSussed;
        this.sort = sort;
    }

    @Generated(hash = 1787269383)
    public NewTaskBean() {
    }

    public String getF_PackageName() {
        return f_PackageName;
    }

    public void setF_PackageName(String f_PackageName) {
        this.f_PackageName = f_PackageName;
    }

    public String getF_AppName() {
        return f_AppName;
    }

    public void setF_AppName(String f_AppName) {
        this.f_AppName = f_AppName;
    }

    public String getF_AppVersion() {
        return f_AppVersion;
    }

    public void setF_AppVersion(String f_AppVersion) {
        this.f_AppVersion = f_AppVersion;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getSlidingSpeed() {
        return slidingSpeed;
    }

    public void setSlidingSpeed(int slidingSpeed) {
        this.slidingSpeed = slidingSpeed;
    }

    public int getWaitForTime() {
        return waitForTime;
    }

    public void setWaitForTime(int waitForTime) {
        this.waitForTime = waitForTime;
    }

    public int getSingleSlideTimes() {
        return singleSlideTimes;
    }

    public void setSingleSlideTimes(int singleSlideTimes) {
        this.singleSlideTimes = singleSlideTimes;
    }

    public int getSlidingInterval() {
        return slidingInterval;
    }

    public void setSlidingInterval(int slidingInterval) {
        this.slidingInterval = slidingInterval;
    }

    public boolean isExecuted() {
        return isExecuted;
    }

    public void setExecuted(boolean executed) {
        isExecuted = executed;
    }

    public boolean isExecutedSussed() {
        return isExecutedSussed;
    }

    public void setExecutedSussed(boolean executedSussed) {
        isExecutedSussed = executedSussed;
    }

    public boolean getIsExecuted() {
        return this.isExecuted;
    }

    public void setIsExecuted(boolean isExecuted) {
        this.isExecuted = isExecuted;
    }

    public boolean getIsExecutedSussed() {
        return this.isExecutedSussed;
    }

    public void setIsExecutedSussed(boolean isExecutedSussed) {
        this.isExecutedSussed = isExecutedSussed;
    }
    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
