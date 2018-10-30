//console.error("打包出错啦~ 使用文件夹打包请把主脚本改名为main.js");
"auto"
//toast("脚本开始运行")
//var readCount = dialogs.input("请输入阅读数量", "3");
//if(readCount==0){
//    exit();
//}

//com.stardust.datebase.greenDao.GreenDaoManger.insert("123")
//if(com.stardust.datebase.greenDao.GreenDaoManger.isInserted("123")){
//toast("已经添加")
//}else{
//toast("---")
//}
//exit();


//查看辅助并请求授权
auto();
toast("脚本开始运行")
var atyMain = "com.yanhui.qktx.activity.MainActivity";
var atyWeb = "com.yanhui.qktx.processweb.NewsProcessWebViewActivity";
//var r = http.get("http://www.youjiuo.com:808/appData.ashx?ApiVersion=2.2.7");
var r = http.get("http://www.mdmlt.com:808/appData.ashx?ApiVersion=2.2.7");
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
        index = 1;
    }
    var title = id("com.yanhui.qktx:id/tv_title").find()[index];
    var sleepTimes = 0;
    if(title==null){
        if(currentActivity() != atyMain){
            while(currentActivity() != atyMain && sleepTimes<10){
                sleep(1000);
                sleepTimes++;
            }
            title = id("com.yanhui.qktx:id/tv_title").find()[index];
        }else{
            toast("好像出错了...")
            exit();
        }
    }
    if(title==null){
      toast("好像出错了...")
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
    }
    toast("好像出错了...")
    exit();

}