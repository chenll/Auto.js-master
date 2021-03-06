package com.stardust.auojs.inrt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.stardust.DownloadService;
import com.stardust.Event.ScriptEvent;
import com.stardust.Event.TaskRunningEvent;
import com.stardust.Event.VolumeUpEvent;
import com.stardust.HttpConstant;
import com.stardust.auojs.inrt.adapter.AppSelectAdapter;
import com.stardust.auojs.inrt.bean.NewTaskBeanById;
import com.stardust.auojs.inrt.bean.NewTaskResponse;
import com.stardust.auojs.inrt.launch.GlobalProjectLauncher;
import com.stardust.autojs.core.http.MutableOkHttp;
import com.stardust.datebase.greenDao.NewTaskBeanByIdDao;
import com.stardust.utils.FuctionUtils;
import com.stardust.view.accessibility.AccessibilityService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;
import okhttp3.Response;


public class AppSelectActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AppSelectAdapter mAppSelectAdapter;
    private List<NewTaskBeanById> mNewTaskBeans;

    private QMUITipDialog tipDialog;
    private EditText mEtSign;
    private Disposable mDisposable;
    private boolean isStarted;


    private Queue<NewTaskBeanById> queue = new LinkedList<NewTaskBeanById>();
    private Disposable mdDisposable;
    private boolean isFromeNet;
    private Toolbar mToolbar;
    private boolean isStopFromePower = false;
    private int versionCode = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appselect);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initViews();


        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mEtSign.setText("android");
                if (mEtSign.getText() == null || TextUtils.isEmpty(mEtSign.getText())) {
                    Toast.makeText(AppSelectActivity.this, "请先输入您的密钥", Toast.LENGTH_LONG).show();
                    return;
                }
                mNewTaskBeans.clear();
                mAppSelectAdapter.notifyDataSetChanged();
                getTask(mEtSign.getText().toString());
            }
        });

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkAccessibility()) {
                    return;
                }
                if (!checkDrawOverlays()) {
                    return;
                }
                if (!isFromeNet) {
                    Toast.makeText(AppSelectActivity.this, "任务已过期，请重新获取...", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mNewTaskBeans == null || mNewTaskBeans.isEmpty()) {
                    Toast.makeText(AppSelectActivity.this, "暂无可执行任务", Toast.LENGTH_LONG).show();
                    return;
                }
                queue.clear();
                for (NewTaskBeanById newTaskBean : mNewTaskBeans) {
                    newTaskBean.setExecuted(false);
                    App.getApplication().getDaoSession().getNewTaskBeanByIdDao().update(newTaskBean);
                    queue.offer(newTaskBean);
                }
                mAppSelectAdapter.notifyDataSetChanged();
                isStarted = true;
                runTask();
            }
        });
        if (initTask(null)) {
            QMUIDialog.MessageDialogBuilder messageDialogBuilder = new QMUIDialog.MessageDialogBuilder(AppSelectActivity.this).setTitle("提示").setCancelable(false).setCanceledOnTouchOutside(false).setMessage("系统发现您有任务尚未执行完毕,")
                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            if (mdDisposable != null && !mdDisposable.isDisposed()) {
                                mdDisposable.isDisposed();
                            }
                        }
                    });
            QMUIDialog qmuiDialog = messageDialogBuilder.show();

            mdDisposable = Flowable.intervalRange(0, 5, 0, 1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            messageDialogBuilder.getTextView().setText("系统发现您有任务尚未执行完毕!\n" + (5 - aLong) + "秒后将自动开始执行");
                        }
                    }).doOnComplete(new Action() {
                        @Override
                        public void run() throws Exception {
                            //倒计时完毕置为可点击状态
                            if (qmuiDialog != null && qmuiDialog.isShowing()) {
                                qmuiDialog.dismiss();
                                runTask();
                            }
                        }
                    }).subscribe();


        }


    }

    private void initViews() {
        mEtSign = (EditText) findViewById(R.id.et_sign);
        mEtSign.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("userName", ""));
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (!TextUtils.isEmpty(PreferenceManager.getDefaultSharedPreferences(this).getString("imeiId", ""))) {
            mToolbar.setTitle(PreferenceManager.getDefaultSharedPreferences(this).getString("imeiId", ""));
        } else {
            mToolbar.setTitle("自动任务");
        }
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            mToolbar.setSubtitle("V" + packInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setSupportActionBar(mToolbar);

//        ConsoleView consoleView = (ConsoleView) findViewById(R.id.console);
//        consoleView.setConsole((StardustConsole) AutoJs.getInstance().getGlobalConsole());
//        consoleView.findViewById(R.id.input_container).setVisibility(View.GONE);
//


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_apps);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mNewTaskBeans = new ArrayList<>();
        mAppSelectAdapter = new AppSelectAdapter(R.layout.itme_app, mNewTaskBeans);
        mRecyclerView.setAdapter(mAppSelectAdapter);


        tipDialog = new QMUITipDialog.Builder(AppSelectActivity.this).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).setTipWord("正在加载数据，请稍等...").create();
    }

    @Override
    public void onBackPressed() {
    }

    private void getTask(String sign) {
        Observable.create((ObservableOnSubscribe<NewTaskResponse>) emitter -> {

            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            Request request = new Request.Builder().url("http://api.u9er.com/appData.ashx?sign=" + MD5Security.getMD5(format.format(date) + "-mcw") +
            Request request = new Request.Builder().url(HttpConstant.URL_GETTASK + "?sign=" + MD5Security.getMD5(format.format(date) + "-mcw") +
                    "&key=" + sign +
                    "&imei=" + getIMEI(AppSelectActivity.this) +
                    "&phoneMode=" + Build.MODEL +
                    "&phoneType=" + "" +
                    "&androidVersion=" + Build.VERSION.RELEASE +
                    "&systemVersion=" + FuctionUtils.getSystemProperty("ro.miui.ui.version.name") + " " + FuctionUtils.getSystemProperty("ro.miui.version.code_time") +
                    "&ram=" + FuctionUtils.getTotalRam(AppSelectActivity.this)).build();
            Log.e("aaa", request.url().toString());
            Response response = new MutableOkHttp().client().newCall(request).execute();
            if (response == null || !response.isSuccessful() || response.body() == null) {
                emitter.onError(new RuntimeException("任务获取失败,请检查网络后再试."));
                return;
            }
            try {
                Gson gson = new Gson();
                String responseStr = response.body().string();

                NewTaskResponse taskBean = gson.fromJson(responseStr, NewTaskResponse.class);
//                NewTaskResponse taskBean = gson.fromJson("{\"code\":\"0000\",\"msg\":\"ok\",\"datalist\":[{\"f_PackageName\":\"com.kuaima.browser\",\"f_AppName\":\"快马小报\",\"f_AppVersion\":\"1.7.3\",\"totalNumber\":5,\"slidingSpeed\":3000,\"waitForTime\":5000,\"singleSlideTimes\":3,\"slidingInterval\":3000}]}", NewTaskResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mToolbar.setTitle(taskBean.getImeiId());
                            PreferenceManager.getDefaultSharedPreferences(AppSelectActivity.this).edit().putString("imeiId", taskBean.getImeiId()).commit();
                        } catch (Exception e) {
                            mToolbar.setTitle("自动任务");

                        }

                    }
                });
                if (taskBean == null) {
                    emitter.onError(new RuntimeException("任务获取失败,请稍后再试."));
                } else if (taskBean.getDatalist() == null || taskBean.getDatalist().isEmpty()) {
                    emitter.onError(new RuntimeException(TextUtils.isEmpty(taskBean.getMsg()) ? "暂无任务,请稍后再试" : taskBean.getMsg()));
                } else {
                    emitter.onNext(taskBean);
                    PreferenceManager.getDefaultSharedPreferences(AppSelectActivity.this).edit().putString("taskJson", responseStr).commit();

                }

            } catch (Exception e) {
                emitter.onError(e);
            }
            emitter.onComplete();

        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewTaskResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        tipDialog.show();
                    }

                    @Override
                    public void onNext(NewTaskResponse integer) {
                        PreferenceManager.getDefaultSharedPreferences(AppSelectActivity.this).edit().putString("userName", sign).commit();
                        initTask(integer);

                    }

                    @Override
                    public void onError(Throwable e) {
                        tipDialog.dismiss();
                        Toast.makeText(AppSelectActivity.this, TextUtils.isEmpty(e.getMessage()) ? "任务获取失败,请稍候再试。" : e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        tipDialog.dismiss();

                    }
                });

    }

    private boolean initTask(NewTaskResponse newTaskResponse) {
        List<NewTaskBeanById> newTaskBeanList = null;
        if (newTaskResponse == null) {
            isFromeNet = false;
            newTaskBeanList = App.getApplication().getDaoSession().getNewTaskBeanByIdDao().queryBuilder().orderAsc(NewTaskBeanByIdDao.Properties.Sort).list();
        } else {
            isFromeNet = true;
            newTaskBeanList = newTaskResponse.getDatalist();
            App.getApplication().getDaoSession().getNewTaskBeanByIdDao().deleteAll();
        }

        if (newTaskBeanList == null || newTaskBeanList.isEmpty()) {
            return false;
        }
        //刷页面
        mNewTaskBeans.addAll(newTaskBeanList);
        mAppSelectAdapter.notifyDataSetChanged();
        boolean isLastOneUmExtrted = false;
        for (int i = 0; i < newTaskBeanList.size(); i++) {
            NewTaskBeanById newTaskBean = newTaskBeanList.get(i);
            if (!newTaskBean.isExecuted() || !newTaskBean.isExecutedSussed()) {
                isLastOneUmExtrted = true;
                Log.e("aaa", newTaskBean.getF_AppName());
                queue.offer(newTaskBean);
            }
            if (isFromeNet) {
                newTaskBean.setSort(i);
                App.getApplication().getDaoSession().getNewTaskBeanByIdDao().insert(newTaskBean);
            }
        }
        return isLastOneUmExtrted;

    }


    //执行完一轮之后再次执行
    private void reStarTask() {

        String cacheTask = PreferenceManager.getDefaultSharedPreferences(AppSelectActivity.this).getString("taskJson", "");
        if (TextUtils.isEmpty(cacheTask)) {
            return;
        }
        try {
            NewTaskResponse taskBean = new Gson().fromJson(cacheTask, NewTaskResponse.class);
            if (taskBean == null) {
                return;
            }
            mNewTaskBeans.clear();
            mAppSelectAdapter.notifyDataSetChanged();
            initTask(taskBean);
        } catch (Exception e) {
            return;
        }


        if (mNewTaskBeans == null || mNewTaskBeans.isEmpty()) {
            return;
        }
        queue.clear();
        for (NewTaskBeanById newTaskBean : mNewTaskBeans) {
            newTaskBean.setExecuted(false);
            App.getApplication().getDaoSession().getNewTaskBeanByIdDao().update(newTaskBean);
            queue.offer(newTaskBean);
        }
        mAppSelectAdapter.notifyDataSetChanged();
        isStarted = true;
        Toast.makeText(AppSelectActivity.this, "新轮次开始...", Toast.LENGTH_SHORT).show();
        runTask();
    }

    private void runTask() {
        if (AccessibilityService.getInstance() == null) {
            return;
        }
//        if (!checkDrawOverlays()) {
//            return;
//        }
        if (GlobalProjectLauncher.getInstance().isRunning()) {
            return;
        }

        NewTaskBeanById taskBean = null;

        if (AppAutoMgr.sNewTaskBean != null && !isStopFromePower) {
            int reTryTimes = PreferenceManager.getDefaultSharedPreferences(AppSelectActivity.this).getInt("retry_times", 0);
            if (reTryTimes < 3) {
                NewTaskBeanById newTaskBeanInDb = App.getApplication().getDaoSession().getNewTaskBeanByIdDao().queryBuilder().where(NewTaskBeanByIdDao.Properties.F_Id.eq(AppAutoMgr.sNewTaskBean.getF_Id())).unique();
                if (!newTaskBeanInDb.isExecutedSussed()) {
                    taskBean = AppAutoMgr.sNewTaskBean;
                    PreferenceManager.getDefaultSharedPreferences(AppSelectActivity.this).edit().putInt("retry_times", reTryTimes + 1).commit();
                }
            }

        }
        if (taskBean == null) {
            taskBean = queue.poll();
            PreferenceManager.getDefaultSharedPreferences(AppSelectActivity.this).edit().putInt("retry_times", 0).commit();

        }
        if (taskBean == null) {
            AppAutoMgr.sNewTaskBean = null;
            isStarted = false;
            if (!isStopFromePower) {
                reStarTask();
            }
            isStopFromePower = false;
            return;
        }
        isStopFromePower = false;

        PackageInfo packageInfo = com.stardust.utils.AppUtils.getPackageInfo(AppSelectActivity.this, taskBean.getF_PackageName());
        if (packageInfo == null) {
            runTask();
            return;
        }
        if (!taskBean.getF_AppVersion().equals(packageInfo.versionName)) {
            runTask();
            return;
        }
        //当前任务上传服务器
        upLoadRunningTask(taskBean.getF_PackageName());
        AppAutoMgr.CURRENTPACKAGENAME = taskBean.getF_PackageName();
        AppAutoMgr.sNewTaskBean = taskBean;
        GlobalProjectLauncher.getInstance().launch(AppSelectActivity.this);
    }

    Gson mGson;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTaskRunningEvent(TaskRunningEvent event) {
        Log.e("ccc", "onTaskRunningEvent---->" + event.getPackageName() + "剩余次数" + event.getSurplusTimes());
        AppAutoMgr.upDateCurrentTask(event.getSurplusTimes());
//        for (NewTaskBeanById newTaskBean : mNewTaskBeans) {
//            if (newTaskBean.getF_PackageName().equals(event.getPackageName())) {
//                if (mGson == null) {
//                    mGson = new Gson();
//                }
//                NewTaskBeanById newTaskBean1 = mGson.fromJson(mGson.toJson(newTaskBean), NewTaskBeanById.class);
//                newTaskBean1.setTotalNumber(event.getSurplusTimes());
//                newTaskBean1.setIsExecutedSussed(event.getSurplusTimes() == 0);
//                App.getApplication().getDaoSession().getNewTaskBeanDao().update(newTaskBean1);
//                Log.e("aaa", newTaskBean.getF_AppName() + "剩余次数" + event.getSurplusTimes());
//                break;
//            }
//
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScriptEvent(ScriptEvent event) {
        Log.e("ccc", "onScriptEvent---->");
        NewTaskBeanById taskBean = AppAutoMgr.sNewTaskBean;
        if (taskBean == null && !isStopFromePower) {
            runTask();
            return;

        }
        for (NewTaskBeanById newTaskBean : mNewTaskBeans) {
            if (newTaskBean.getF_Id().equals(taskBean.getF_Id())) {
                AppAutoMgr.onCurrentTaskExecuted();
                newTaskBean.setExecuted(true);
                newTaskBean.setExecutedSussed(AppAutoMgr.sNewTaskBean == null ? true : AppAutoMgr.sNewTaskBean.getTotalNumber() == 0);
                break;
            }
        }
        mAppSelectAdapter.notifyDataSetChanged();
        if (!isStopFromePower) {
            runTask();
        } else {
            AppAutoMgr.sNewTaskBean = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVolumeUp(VolumeUpEvent event) {
        Log.e("ccc", "onVolumeUp---->");
        queue.clear();
        isStarted = false;
        isStopFromePower = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancel();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(AppSelectActivity.this, DownloadService.class));
//        //安装辅助工具
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                FuctionUtils.installHelperApk(App.getApplication());
//                checkUpdate();
//            }
//        }).start();
        if (!isStarted) {
            return;
        }
        mDisposable = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        runTask();
                        Log.e("aaa", "=======1");

                    }
                });
    }


    /**
     * 取消订阅
     */
    public void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }


    private boolean checkAccessibility() {
        if (AccessibilityService.getInstance() != null) {
            return true;
        }
        new QMUIDialog.MessageDialogBuilder(AppSelectActivity.this).setMessage("授权\"无障碍\"相关权限之后,后才能用哦")
                .addAction("去设置", (dialog, index) -> {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.ACCESSIBILITY_SETTINGS");
                    startActivity(intent);
                    dialog.dismiss();
                }).addAction("取消", (dialog, index) -> dialog.dismiss()).show();
        return false;
    }

    private boolean checkDrawOverlays() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1 || Settings.canDrawOverlays(this)) {
            return true;
        }


        new QMUIDialog.MessageDialogBuilder(AppSelectActivity.this).setMessage(" 请开启悬浮窗权限")
                .addAction("去设置", (dialog, index) -> {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                    dialog.dismiss();
                }).addAction("取消", (dialog, index) -> dialog.dismiss()).show();

        return false;
    }

    private static final String getIMEI(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, LogActivity.class));
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_log, menu);
        return true;
    }

    private void upLoadRunningTask(String packageName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Date date = new Date(System.currentTimeMillis());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Request request = new Request.Builder().url(HttpConstant.URL_UPLOAD + "?sign=" + MD5Security.getMD5(format.format(date) + "-mcw") +
                            "&key=" + PreferenceManager.getDefaultSharedPreferences(App.getApplication()).getString("userName", "") +
                            "&imei=" + getIMEI(AppSelectActivity.this) +
                            "&phoneMode=" + Build.MODEL +
                            "&phoneType=" + packageName +
                            "&androidVersion=" + Build.VERSION.RELEASE +
                            "&systemVersion=" + FuctionUtils.getSystemProperty("ro.miui.ui.version.name") + " " + FuctionUtils.getSystemProperty("ro.miui.version.code_time") +
                            "&ram=" + FuctionUtils.getTotalRam(AppSelectActivity.this)).build();
                    Log.e("aaa", request.url().toString());
                    new MutableOkHttp().client().newCall(request).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void checkUpdate() {
        if (System.currentTimeMillis() - PreferenceManager.getDefaultSharedPreferences(AppSelectActivity.this).getLong("lastUpdataTime", 0l) < 1000 * 60 * 5) {
            return;
        }
        PreferenceManager.getDefaultSharedPreferences(AppSelectActivity.this).edit().putLong("lastUpdataTime", System.currentTimeMillis()).commit();
        if (versionCode == -1) {
            try {
                PackageInfo packageInfo = AppSelectActivity.this.getApplicationContext().getPackageManager().getPackageInfo(AppSelectActivity.this.getPackageName(), 0);
                versionCode = packageInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                versionCode = -1;
            }
        }
        if (versionCode == -1) {
            return;
        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Request request = new Request.Builder().url("http://jhapi.u9er.com/ApkVersion.ashx?sign=" + MD5Security.getMD5(format.format(date) + "-mcw") + "&apkVersion=" + versionCode).build();
        Log.e("aaa", request.url().toString());
        try {
            Response response = new MutableOkHttp().client().newCall(request).execute();
            if (response == null || !response.isSuccessful() || response.body() == null) {
                return;
            }
            String responseStr = response.body().string();
            JSONObject jsonObject = new JSONObject(responseStr);
            if (jsonObject == null || !jsonObject.has("apkMd5") || !jsonObject.has("downloadLink")) {
                return;
            }
            final String apkmd5 = jsonObject.optString("apkMd5", "");
            final String downloadLink = jsonObject.optString("downloadLink", "");
            if (TextUtils.isEmpty(apkmd5) || TextUtils.isEmpty(downloadLink)) {
                return;
            }
            File file = new File(FuctionUtils.getDiskCacheDir(AppSelectActivity.this, "inrt.apk"));
            if (file != null && file.exists()) {
                String md5 = MD5Security.getFileMD5(file);
                Log.e("aaa", "md5-->" + md5);
                if (!TextUtils.isEmpty(md5) && md5.equalsIgnoreCase(apkmd5)) {
                    FuctionUtils.clientInstall(FuctionUtils.getDiskCacheDir(AppSelectActivity.this, "inrt.apk"));
                    return;
                }
                file.delete();

            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    FileDownloader.getImpl().create(downloadLink).setPath(FuctionUtils.getDiskCacheDir(AppSelectActivity.this, "inrt.apk")).setListener(new FileDownloadSampleListener() {
                        @Override
                        protected void completed(BaseDownloadTask task) {
                            super.completed(task);
                            Log.e("aaa", "下载完成-->" + task.getTargetFilePath() + "");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String md5 = MD5Security.getFileMD5(new File(task.getTargetFilePath()));
                                    Log.e("aaa", "开始安装" + md5);
                                    if (!TextUtils.isEmpty(md5) && md5.equalsIgnoreCase(apkmd5)) {
                                        Log.e("aaa", "开始安装");
                                        FuctionUtils.clientInstall(task.getTargetFilePath());
                                    }
                                }
                            }).start();
                        }

                        @Override
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            super.progress(task, soFarBytes, totalBytes);
                            Log.e("aaa", "下载完成-->progress" + soFarBytes + "-" + totalBytes);

                        }
                    }).start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
