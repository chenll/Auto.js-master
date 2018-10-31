"auto"
auto();
toast("[蚂蚁头条]脚本开始运行");
var pName = "com.ldzs.zhangxin";
var atyMain = "com.weishang.wxrd.activity.MainActivity";
var atyWeb = "com.weishang.wxrd.activity.WebViewActivity";
var r = http.get("http://api.u9er.com/appData.ashx?ApiVersion=2.2.7");
if (r == null || r.statusCode != 200) {
    toast("数据初始失败,请检查网络,稍后再试...");
    exit();
}

var taskbeanresulr = com.stardust.GsonParse.parse(r.body.string());
if (taskbeanresulr == null || taskbeanresulr.getData() == null || taskbeanresulr.getData().isEmpty()) {
    toast("数据解析失败,稍后再试...");
    exit();
}

var task = taskbeanresulr.getData().get(0);
//打开app
if (!app.launchApp("蚂蚁头条")) {
    toast("[蚂蚁头条]打开失败,请检查你是否安装");
    exit();
}

var mainSleepTimes = 0;
if (currentActivity() != atyMain) {
    while (currentActivity() != atyMain && mainSleepTimes < 10) {
        sleep(1000);
        mainSleepTimes++;
        closeHomeDialog();
    }
} else {
    toast("好像出错了...")
    exit();
}




var viewpager = id("com.ldzs.zhangxin:id/vp_pager").findOne().bounds();

var index = 0;
for (var i = 0; i < task.getTotalNumber(); i++) {
    executeTask();
}
toast("任务运行结束")
exit();



//关闭首页弹窗
function closeHomeDialog() {
    var imgClose = id("com.ldzs.zhangxin:id/iv_close_packet").find();
    if (imgClose != null && imgClose[0] != null) {
        imgClose[0].click();
        sleep(1000);
    }

}

//关闭金币领取弹窗
function closeGoldialog() {
    var imgClose = id("com.ldzs.zhangxin:id/btn_close").find();
    if (imgClose != null && imgClose[0] != null) {
        imgClose[0].click();
        sleep(1000);
    }

}

function executeTask() {
    //等待进入页面
    closeGoldialog();
    sleep(1000);
    closeHomeDialog();
    if (index == -1) {
        swipe(300, viewpager.bottom - 1, 300, viewpager.top + 1, task.getSlidingSpeed());
        index = 0;
    }
    var title = id("com.ldzs.zhangxin:id/tv_article_title").find()[index];

    var titleGetTimes = 0;
    while (title == null && titleGetTimes < 3) {
        swipe(300, viewpager.bottom - 1, 300, viewpager.top + 100, task.getSlidingSpeed());
//            swipe(300, viewpager.bottom - 1, 300, viewpager.bottom-200, task.getSlidingSpeed());

        titleGetTimes++;
        index = 0;
        title = id("com.ldzs.zhangxin:id/tv_article_title").find()[index];
    }

    var sleepTimes = 0;
    if (title == null) {
        if (currentActivity() != atyMain) {
            while (currentActivity() != atyMain && sleepTimes < 10) {
                sleep(1000);
                sleepTimes++;
            }
            title = id("com.ldzs.zhangxin:id/tv_article_title").find()[index];
        } else {
            toast("好像出错了1...")
            exit();
        }
    }
    if (title == null) {
        toast("好像出错了2...")
        exit();
    }

    var titleStr = title.text();
    //跳过广告
    var guangg = title.parent().findByText("广告");
    if (guangg != null && guangg.size() != 0) {
        toast("发现广告，直接跳过");
        if (title.parent().bounds().bottom == viewpager.bottom) {
            index = -1
        } else {
            index++;
        }
        executeTask();
        return;
    }
    if (com.stardust.datebase.greenDao.GreenDaoManger.isInserted(titleStr)) {
        toast("已经点过了，直接跳过");
        if (title.parent().bounds().bottom == viewpager.bottom) {
            index = -1
        } else {
            index++;
        }
        executeTask();
        return;
    }
    if (currentActivity() == atyMain) {
        var titleBounds = title.bounds();
        title.parent().parent().click();
        if (titleStr != null && titleStr != "") {
            com.stardust.datebase.greenDao.GreenDaoManger.insert(titleStr);
        }
        //点到最下面一个了
        if (title.parent().parent().bounds().bottom == viewpager.bottom) {
            index = -1
        } else {
            index++;
        }
        sleep(1000);
        //SingleSlideTimes 滑动次数
        for (var i = 0; i < task.getSingleSlideTimes(); i++) {

            if (currentActivity() == atyWeb) {
                closeGoldialog();
                // getSlidingSpeed滑动速度
                swipe(300, 1600, 300, 1020, task.getSlidingSpeed());
                //SlidingInterval 每次滑动间隔时间
                sleep(task.getSlidingInterval());
            }
        }
        //getWaitForTime总阅读时间
        sleep(task.getWaitForTime());
        back();
        return;
    }
    toast("好像出错了1111...")
    exit();

}