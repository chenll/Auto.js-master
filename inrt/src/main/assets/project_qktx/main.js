//查看辅助并请求授权
auto();
toast("[趣看天下]脚本开始运行")
var atyMain = "com.yanhui.qktx.activity.MainActivity";
var atyWeb = "com.yanhui.qktx.processweb.NewsProcessWebViewActivity";
var targetTextId = "com.yanhui.qktx:id/tv_title";

var task = getRunningTask();
//打开app
var isSussed = app.launchApp("趣看天下");
if (!isSussed) {
    toast("[趣看天下]打开失败,请检查你是否安装");
    exit();
}

//等待进入页面
waitForActivity(atyMain, [period = 200]);

closeHomeDialog();
var viewpager = id("com.yanhui.qktx:id/activity_main_viewpager").findOne().bounds();
var index = 0;
for (var i = 0; i < task.getTotalNumber(); i++) {
    executeTask();
}
toast("任务运行结束")
exit();


//获取任务配置
function getTask() {
    var r = http.get("http://api.u9er.com/appData.ashx?ApiVersion=2.2.7");
    if (r == null || r.statusCode != 200) {
        return null;
    }
    var taskbeanresulr = com.stardust.GsonParse.parse(r.body.string());
    if (taskbeanresulr == null || taskbeanresulr.getData() == null || taskbeanresulr.getData().isEmpty()) {
        return null;
    }
    return taskbeanresulr.getData().get(0);

}


//关闭首页弹窗
function closeHomeDialog() {
    var imgClose = id("com.yanhui.qktx:id/img_close").find();
    if (imgClose != null && imgClose[0] != null) {
        imgClose[0].click();
    }
}


function executeTask() {
    //等待进入页面
    //    waitForActivity("com.yanhui.qktx.activity.MainActivity", [period = 200]);
    sleep(1000);
    closeHomeDialog();
    if (index == -1) {
        swipe(300, viewpager.bottom - 1, 300, viewpager.top + 1, task.getSlidingSpeed());
        index = 0;
    }
    var title = id(targetTextId).find()[index];


    if (title == null) {
        if (currentActivity() != atyMain) {
            var sleepTimes = 0;
            while (currentActivity() != atyMain && sleepTimes < 10) {
                sleep(1000);
                sleepTimes++;
            }
            title = id(targetTextId).find()[index];
        } else {
            var titleGetTimes = 0;
            while (title == null && titleGetTimes < 3) {
                swipe(300, viewpager.bottom - 1, 300, viewpager.top + 100, task.getSlidingSpeed());
                titleGetTimes++;
                index = 0;
                title = id(targetTextId).find()[index];
            }
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
    //    if (currentActivity() == atyMain) {
    title.parent().click();
    if (titleStr != null && titleStr != "") {
        com.stardust.datebase.greenDao.GreenDaoManger.insert(titleStr);
    }
    //点到最下面一个了
    if (title.parent().bounds().bottom == viewpager.bottom) {
        index = -1
    } else {
        index++;
    }
    sleep(1000);
    //SingleSlideTimes 滑动次数
    for (var i = 0; i < task.getSingleSlideTimes(); i++) {
        if (currentActivity() == atyWeb) {
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
    //    }
    toast("好像出错了..." + index)
    exit();

}