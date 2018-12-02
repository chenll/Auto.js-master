auto();
var window = floaty.window(<img w = "40" h = "40" src = "data:image/png;base64,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"/>);
window.setPosition(device.width - com.stardust.DensityUtil.dp2px(context, 40), device.height / 2 - com.stardust.DensityUtil.dp2px(context, 40));
window.setAdjustEnabled(true);

var appAutoMessage = com.stardust.auojs.inrt.AppAutoMgr.getCurrentAppAutoMessage();
if (appAutoMessage == null) {
    toast("获取本地配置异常");
    exit();
}

var appName = appAutoMessage.getAppName();
var pName = appAutoMessage.getPackageName();
//主页面
var atyMain = appAutoMessage.getActivityMain();
//可能干扰主页面的activity
var atyDisturb = appAutoMessage.getActivityDisturb();
//列表标题id
var targetTextId = appAutoMessage.getTargetTextId();
//首页上下滚动view的id
var rollViewId = appAutoMessage.getRollViewId();
var dialogIds = appAutoMessage.getDialogIds();

android.util.Log.e("aaa", "-----------------" + appName + "-----------------");
android.util.Log.e("aaa", pName + "");
android.util.Log.e("aaa", atyMain + "");
android.util.Log.e("aaa", atyDisturb + "");
android.util.Log.e("aaa", targetTextId + "");
android.util.Log.e("aaa", rollViewId + "");
android.util.Log.e("aaa", dialogIds + "");
android.util.Log.e("aaa", "----------------------------------");


toast("[" + appName + "]脚本开始运行");


//解冻
shell("pm enable " + pName, true);
//关闭
var result = shell("am force-stop " + pName, true);
//打开
if (result.code != 0) {
    toast("执行失败！请检查是否授予Root权限");
    org.greenrobot.eventbus.EventBus.getDefault().post(new com.stardust.Event.VolumeUpEvent());
    exit();
}

var task = com.stardust.auojs.inrt.AppAutoMgr.getCurrentTask();
//打开app
if (!app.launchApp(appName)) {
    toast("[" + appName + "]打开失败,请检查你是否安装");
    exit();
}
var time = new Date().getTime();
while (global.currentActivity() != atyMain && global.currentActivity() != atyDisturb) {
    click("允许");
    closeDialog();
    if (new Date().getTime() - time >= 2000) {
        click("跳过");
    }
    if (new Date().getTime() - time >= 15000) {
        break;
    }
    sleep(200);
}

//是否当前应用 监听线程
//var thread = threads.start(function(){
//    while(true){
//       if(currentPackage()!=pName){
//            if (!app.launchApp(appName)) {
//                 break;
//            }
//       }
//       sleep(1000);
//    }
//});

if (getListView() == null) {
    toast("任务运行异常1001")
    exitTask();
}
//剩余次数
var residueDegree = task.getTotalNumber();
//错误最多尝试100次
var timeRetry = new Date().getTime();
while (residueDegree > 0) {
//    if (currentPackage() != pName) {
//        toast("您已离开 [" + appName + "] 任务结束");
//        exitTask();
//    }
    if (new Date().getTime() - timeRetry >= 10000) {
        toast("任务运行异常1003");
        exitTask();
    }
    signIn();
    var textViews = id(targetTextId).find();
    if (textViews != null && textViews.length != 0) {
        android.util.Log.e("aaa", "textview个数" + textViews.length);
        for (var j = 0; j < textViews.length; j++) {
            android.util.Log.e("aaa", j + "===========" + textViews.length);
            var textView = textViews[j];
            if (textView != null && textView.parent() != null) {
                if (executeTask(textView)) {
                    residueDegree--;
                    org.greenrobot.eventbus.EventBus.getDefault().post(new com.stardust.Event.TaskRunningEvent(pName,residueDegree));
                    timeRetry = new Date().getTime();
                } else {
                    android.util.Log.e("aaa", "点击失败");
                }
            } else {
                closeDialog();
                android.util.Log.e("aaa", "textview集合查找item失败");
            }
            if (residueDegree <= 0) {
                break;
            }
        }
        if (residueDegree > 0) {
            var listView = getListView();
            if (listView == null) {
                goBack();
                sleep(500);
            } else {
                //            scrollDown(1);
                if (pName == "cn.weli.story") {
                    shell("input swipe " + 300 + " " + (device.height) * 2 / 3 + " " + 300 + " " + (device.height) / 5 + " " + getSwipeTimes(), true);
                } else {
                    shell("input swipe " + 300 + " " + (device.height) * 4 / 5 + " " + 300 + " " + (device.height) / 4 + " " + getSwipeTimes(), true);
                }
            }
            //        swipe(300, (device.height) * 4 / 5, 300, (device.height) / 4, task.getSlidingSpeed());
        }
    } else {
        android.util.Log.e("aaa", "未找到相关的textview集合");
        getListView();
    }
}


toast("[" + appName + "]任务运行结束");
exitTask();


function exitTask() {
    //    thread.interrupt();
    window.close();
    exitApp();
    exit();
}
function goBack() {
//  if (currentPackage() != pName ) {
//       return;
//    }
    back();
}
function getListViewTimeOut(maxTime) {
    if (rollViewId == null || rollViewId == "") {
        return className("android.widget.ListView").findOne(maxTime);
    } else {
        return id(rollViewId).findOne(maxTime);

    }

}
function getListView() {

    //className("android.widget.ListView")
    android.util.Log.e("aaa", "getListView--->开始查找0");
    closeDialog();
    var listViewTemp = getListViewTimeOut(1000);
    if (listViewTemp == null) {
        closeDialog();
    } else {
        return listViewTemp;
    }
    android.util.Log.e("aaa", "getListView--->开始查找1");

    listViewTemp = getListViewTimeOut(1000);
    if (listViewTemp == null) {
        goBack();
    } else {
        return listViewTemp;
    }
    android.util.Log.e("aaa", "getListView--->开始查找2");

    listViewTemp = getListViewTimeOut(1500);
    if (listViewTemp == null) {
        closeDialog();
    } else {
        return listViewTemp;
    }
    android.util.Log.e("aaa", "getListView--->开始查找3");
    listViewTemp = getListViewTimeOut(1500);
    if (listViewTemp == null) {
        goBack();
    } else {
        return listViewTemp;
    }

    android.util.Log.e("aaa", "getListView--->开始查找24");

    listViewTemp = getListViewTimeOut(1500);
    if (listViewTemp == null) {
        closeDialog();
    } else {
        return listViewTemp;
    }
    android.util.Log.e("aaa", "getListView--->开始查找5");

    listViewTemp = getListViewTimeOut(1500);
    if (listViewTemp == null) {
        goBack();
    } else {
        return listViewTemp;
    }
    android.util.Log.e("aaa", "getListView--->开始查找6");

    listViewTemp = getListViewTimeOut(1000);
    closeDialog();
    return listViewTemp;
}

//关闭首页弹窗
function closeDialog() {
    if (dialogIds == null || dialogIds.length == 0) {
        return;
    }
    for (var i = 0; i < dialogIds.length; i++) {
        closeDialogByid(dialogIds[i]);
    }
}

function closeDialogByid(viewId) {

    try{
        var imgClose = id(viewId).findOnce();
        if (imgClose != null) {
            sleep(600);
            imgClose.click();
            sleep(600);
        }
    }catch(erro){
                android.util.Log.e("aaa",  "===========" + erro);
    }

}


function executeTask(textView) {
    sleep(1000);
    if (getListView() == null) {
        toast("任务运行异常1002")
        exitTask();
    }
    try {
        var parent = textView.parent();
        if (parent == null) {
            closeDialog();
            return false;
        }
    } catch (erro) {
        return false;
    }
    //跳过广告
    var guangg = parent.findByText("广告");
    if (guangg != null && guangg.size() != 0) {
        android.util.Log.e("bbb", "跳过广告");
        timeRetry = new Date().getTime();
        return false;
    }
    //跳过下载
    var xiazai = parent.findByText("下载");
    if (xiazai != null && xiazai.size() != 0) {
        android.util.Log.e("bbb", "跳过下载");
        timeRetry = new Date().getTime();
        return false;
    }
    var titleStr = textView.text();
    if (com.stardust.datebase.greenDao.GreenDaoManger.isInserted(pName, titleStr)) {
        android.util.Log.e("bbb", "跳过已读");
        timeRetry = new Date().getTime();
        return false;
    }
    if (titleStr != null && titleStr != "" && click(titleStr)) {
        android.util.Log.e("aaa", titleStr + "");
        com.stardust.datebase.greenDao.GreenDaoManger.insert(pName, titleStr);
        for (var i = 0; i < task.getSingleSlideTimes(); i++) {
            sleep(1000);
            closeDialog();
            // getSlidingSpeed滑动速度
            // swipe(300, 1600, 300, 1020, task.getSlidingSpeed());
//            scrollDown(0);
            shell("input swipe " + 300 + " " + (device.height) * 4 / 5 + " " + 300 + " " + (device.height) / 3 + " " + getSwipeTimes(), true)

//                  var readAll = textContains("查看全文").findOnce();
//                  if(readAll!=null){
//                     readAll.click();
//                  }
            //swipe(300, (device.height) * 4 / 5, 300, (device.height) / 3, task.getSlidingSpeed());
            //SlidingInterval 每次滑动间隔时间
            sleep(getSlidingIntervalTimes());
        }
        sleep(getWaitTimes());
        closeDialog();
        goBack();
        sleep(1000);
        return true;
    }
    return false;

}


function getSlidingIntervalTimes(){
   return random(task.getSlidingInterval()/2, task.getSlidingInterval());
}

function getWaitTimes(){
   return random(task.getWaitForTime()/2, task.getWaitForTime());
}

function getSwipeTimes(){
   return random(task.getSlidingSpeed()/2, task.getSlidingSpeed());
}

function exitApp() {
    shell("am force-stop " + pName, true);
//    shell("pm disable "+pName,true);
//
//    if (exitBtnId == null || exitBtnId == "") {
//        back();
//        sleep(500);
//        back();
//    } else {
//        back();
//        android.util.Log.e("aaa", exitBtnId);
//        var btnEixt = id(exitBtnId).findOne(1000);
//        if (btnEixt != null) {
//            btnEixt.click();
//        } else {
//            sleep(500);
//            back();
//        }
//    }
}
//签到
function signIn() {
    if (appAutoMessage == null) {
            android.util.Log.e("bbb", "1111");

        return;
    }
    if (!appAutoMessage.isCanSign()) {
                android.util.Log.e("bbb", "2222");
        return;
    }
    var signIds = appAutoMessage.setSignInIds();
    if (signIds == null || signIds.length == 0) {
                android.util.Log.e("bbb", "3333");

        return;
    }
    for (var i = 0; i < signIds.length; i++) {
        try {
            var btn = id(signIds[i]).findOnce();
            if (btn != null) {
                if (i == 0 && !btn.click()) {
                                android.util.Log.e("bbb", "444");

                    break;
                } else {
                                android.util.Log.e("bbb", "555");

                    btn.click();
                }
            } else if (i == 0) {
                            android.util.Log.e("bbb", "666");

                break;
            }
        } catch (erro) {}

    }
}