package com.stardust;

import java.util.List;

public class TaskBean {


    /**
     * code : 0000
     * msg : ok
     * data : [{"TotalNumber":1,"SlidingSpeed":2,"WaitForTime ":3000,"SingleSlideTimes":4,"SlidingInterval":5000}]
     */

    private String code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * TotalNumber : 1
         * SlidingSpeed : 2
         * WaitForTime  : 3000
         * SingleSlideTimes : 4
         * SlidingInterval : 5000
         */

        private int TotalNumber;
        private int SlidingSpeed;
        private int WaitForTime;
        private int SingleSlideTimes;
        private int SlidingInterval;

        public int getTotalNumber() {
            return TotalNumber;
        }

        public void setTotalNumber(int TotalNumber) {
            this.TotalNumber = TotalNumber;
        }

        public int getSlidingSpeed() {
            return SlidingSpeed;
        }

        public void setSlidingSpeed(int SlidingSpeed) {
            this.SlidingSpeed = SlidingSpeed;
        }

        public int getWaitForTime() {
            return WaitForTime;
        }

        public void setWaitForTime(int WaitForTime) {
            this.WaitForTime = WaitForTime;
        }

        public int getSingleSlideTimes() {
            return SingleSlideTimes;
        }

        public void setSingleSlideTimes(int SingleSlideTimes) {
            this.SingleSlideTimes = SingleSlideTimes;
        }

        public int getSlidingInterval() {
            return SlidingInterval;
        }

        public void setSlidingInterval(int SlidingInterval) {
            this.SlidingInterval = SlidingInterval;
        }
    }
}
