
module.exports = function(runtime, global){
    global.toast = function(text){
        runtime.toast(text);
    }

    global.toastLog = function(text){
        runtime.toast(text);
        global.log(text);
    }

    global.sleep = runtime.sleep.bind(runtime);

    global.isStopped = function(){
        return runtime.isStopped();
    }

    global.isShuttingDown = global.isShopped;

    global.notStopped = function(){
        return !isStopped();
    }

    global.isRunning = global.notStopped;

    global.exit = runtime.exit.bind(runtime);


    global.stop = global.exit;

    global.setClip = function(text){
        runtime.setClip(text);
    }

    global.getClip = function(text){
       return runtime.getClip();
    }

    global.currentPackage = function(){
        global.auto();
        return runtime.info.getLatestPackage();
    }

    global.currentActivity = function(){
        global.auto();
        return runtime.info.getLatestActivity();
    }

    global.waitForActivity = function(activity, period){
        period = period || 200;
        while(global.currentActivity() != activity){
            sleep(period);
        }
    }

    global.waitForPackage = function(packageName, period){
        period = period || 200;
        while(global.currentPackage() != packageName){
            sleep(period);
        }
    }

    global.random = function(min, max){
        if(arguments.length == 0){
            return Math.random();
        }
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }

    global.setScreenMetrics = runtime.setScreenMetrics.bind(runtime);

    global.requiresApi = runtime.requiresApi.bind(runtime);
    global.requiresAutojsVersion = function(version){
        if(typeof(version) == 'number'){
            if(compare(version, app.autojs.versionCode) > 0){
                throw new Error("需要Auto.js版本号" + version + "以上才能运行");
            }
        }else{
            if(compareVersion(version, app.autojs.versionName) > 0){
                throw new Error("需要Auto.js版本" + version + "以上才能运行");
            }
        }
    }

    var buildTypes = {
        release: 100,
        beta: 50,
        alpha: 0
    }

    function compareVersion(v1, v2){
        v1 = parseVersion(v1);
        v2 = parseVersion(v2);
        log(v1, v2);
        return v1.major != v2.major ? compare(v1.major, v2.major) :
               v1.minor != v2.minor ? compare(v1.minor, v2.minor) :
               v1.revision != v2.revision ? compare(v1.revision, v2.revision) :
               v1.buildType != v2.buildType ? compare(v1.buildType, v2.buildType) :
               compare(v1.build, v2.build);
    }

    function compare(a, b){
        return a > b ? 1 :
               a < b ? -1:
               0;
    }

    function parseVersion(v){
        var m = /(\d+)\.(\d+)\.(\d+)[ ]?(Alpha|Beta)?(\d*)/.exec(v);
        if(!m){
            throw new Error("版本格式不合法: " + v);
        }
        return {
            major: parseInt(m[1]),
            minor: parseInt(m[2]),
            revision: parseInt(m[3]),
            buildType: buildType(m[4]),
            build: m[5] ? parseInt(m[5]) : 1
        };
    }

    function buildType(str){
        if(str == 'Alpha'){
            return buildTypes.alpha;
        }
        if(str == 'Beta'){
            return buildTypes.beta;
        }
        return buildTypes.release;
    }
    //=================================一下是新增方法
    //获取任务配置
    global.getRunningTask = function () {
        var response = http.get("http://api.u9er.com/appData.ashx?ApiVersion=2.2.7");
        if (response == null || response.statusCode != 200) {
            toast("数据初始化失败,稍后再试...");
            exit();
            return null;
        }
        var taskResponse = com.stardust.GsonParse.parse(response.body.string());
        if (taskResponse == null || taskResponse.getData() == null || taskResponse.getData().isEmpty()) {
            toast("数据初始化失败,稍后再试...");
            exit();
            return null;
        }
        return taskResponse.getData().get(0);
    }


    global.waitForActivityWithTimeOut = function(activity, timeout, period) {
        period = period || 200;
        var time = new Date().getTime();
        while (global.currentActivity() != activity) {
            android.util.Log.e("aaa","waitForActivityWithTimeOut:\ncurrent-->"+global.currentActivity()+"\nwait----->"+activity);
            if (timeout && new Date().getTime() - time >= timeout) {
                return false;
            };
            sleep(period);
        };
        return true;
    };

}