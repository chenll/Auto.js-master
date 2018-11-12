package com.stardust.auojs.inrt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
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
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.stardust.Event.ScriptEvent;
import com.stardust.Event.VolumeUpEvent;
import com.stardust.auojs.inrt.adapter.AppSelectAdapter;
import com.stardust.auojs.inrt.adapter.RvLogAdapter;
import com.stardust.auojs.inrt.bean.NewTaskBean;
import com.stardust.auojs.inrt.bean.NewTaskResponse;
import com.stardust.auojs.inrt.launch.GlobalProjectLauncher;
import com.stardust.autojs.core.http.MutableOkHttp;
import com.stardust.view.accessibility.AccessibilityService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;
import okhttp3.Response;


public class AppSelectActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AppSelectAdapter mAppSelectAdapter;
    private List<NewTaskBean> mNewTaskBeans;
    private RecyclerView mRvLog;
    private RvLogAdapter mRvLogAdapter;
    private List<String> mLogs;

    private QMUITipDialog tipDialog;
    private EditText mEtSign;
    private Disposable mDisposable;
    private boolean isStarted;


    private Queue<NewTaskBean> queue = new LinkedList<NewTaskBean>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appselect);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initViews();
        mAppSelectAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (1 == 1) {
                return;
            }
            if (!checkAccessibility()) {
                return;
            }
            if (!checkDrawOverlays()) {
                return;
            }

        });
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
                if (mNewTaskBeans == null || mNewTaskBeans.isEmpty()) {
                    Toast.makeText(AppSelectActivity.this, "暂无可执行任务", Toast.LENGTH_LONG).show();
                    return;
                }
                queue.clear();
                for (NewTaskBean newTaskBean : mNewTaskBeans) {
                    newTaskBean.setExecuted(false);
                    queue.offer(newTaskBean);
                }
                mAppSelectAdapter.notifyDataSetChanged();
                isStarted = true;
                runTask();
            }
        });


    }

    private void initViews() {
        mEtSign = (EditText) findViewById(R.id.et_sign);
        mEtSign.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("userName", ""));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("App列表");
        setSupportActionBar(toolbar);

//        ConsoleView consoleView = (ConsoleView) findViewById(R.id.console);
//        consoleView.setConsole((StardustConsole) AutoJs.getInstance().getGlobalConsole());
//        consoleView.findViewById(R.id.input_container).setVisibility(View.GONE);
//

        mRvLog = (RecyclerView) findViewById(R.id.rv_log);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvLog.setLayoutManager(linearLayoutManager);
        mLogs = new ArrayList<>();
        mRvLogAdapter = new RvLogAdapter(R.layout.itme_log, mLogs);
        mRvLog.setAdapter(mRvLogAdapter);


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_apps);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManage);
        mNewTaskBeans = new ArrayList<>();
        mAppSelectAdapter = new AppSelectAdapter(R.layout.itme_app, mNewTaskBeans);
        mRecyclerView.setAdapter(mAppSelectAdapter);


        tipDialog = new QMUITipDialog.Builder(AppSelectActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载数据，请稍等...")
                .create();
    }

    @Override
    public void onBackPressed() {
    }

    private void getTask(String sign) {
        Observable.create((ObservableOnSubscribe<NewTaskResponse>) emitter -> {

            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Request request = new Request.Builder().url("http://api.u9er.com/appData.ashx?sign=" + MD5Security.getMD5(format.format(date) + "-mcw") + "&key=" + sign + "&imei=" + getIMEI(AppSelectActivity.this)).build();
            Log.e("aaa", request.url().toString());
            Response response = new MutableOkHttp().client().newCall(request).execute();
            if (response == null || !response.isSuccessful() || response.body() == null) {
                emitter.onError(new RuntimeException("任务获取失败,请检查网络后再试."));
                return;
            }
            try {
                Gson gson = new Gson();
                NewTaskResponse taskBean = gson.fromJson(response.body().string(), NewTaskResponse.class);
//                NewTaskResponse taskBean = gson.fromJson("{\"code\":\"0000\",\"msg\":\"ok\",\"datalist\":[{\"f_PackageName\":\"com.kuaima.browser\",\"f_AppName\":\"快马小报\",\"f_AppVersion\":\"1.7.3\",\"totalNumber\":5,\"slidingSpeed\":3000,\"waitForTime\":5000,\"singleSlideTimes\":3,\"slidingInterval\":3000}]}", NewTaskResponse.class);
                if (taskBean == null) {
                    emitter.onError(new RuntimeException("任务获取失败,请稍后再试."));

                } else if (taskBean.getDatalist() == null || taskBean.getDatalist().isEmpty()) {
                    emitter.onError(new RuntimeException("暂无任务,请稍后再试"));
                } else {
                    emitter.onNext(taskBean);
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
                        mNewTaskBeans.addAll(integer.getDatalist());
                        mAppSelectAdapter.notifyDataSetChanged();
                        PreferenceManager.getDefaultSharedPreferences(AppSelectActivity.this).edit().putString("userName", sign).commit();
                        StringBuffer stringBuffer = new StringBuffer();
                        for (NewTaskBean newTaskBean : integer.getDatalist()) {
                            queue.offer(newTaskBean);
                            stringBuffer.append(newTaskBean.getF_AppName());
                            if (newTaskBean != integer.getDatalist().get(integer.getDatalist().size() - 1)) {
                                stringBuffer.append("\n");
                            }
                        }

//                        new QMUIDialog.MessageDialogBuilder(AppSelectActivity.this).setTitle("任务列表").setMessage(stringBuffer.toString())
//                                .addAction("开始执行", (dialog, index) -> {
//                                    dialog.dismiss();
//                                    mLogs.clear();
//                                    mRvLogAdapter.notifyDataSetChanged();
//                                    runTask();
//                                }).addAction("取消", (dialog, index) -> dialog.dismiss()).show();


                    }

                    @Override
                    public void onError(Throwable e) {
                        tipDialog.dismiss();
                        Toast.makeText(AppSelectActivity.this, TextUtils.isEmpty(e.getMessage()) ? "任务获取失败" : e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        tipDialog.dismiss();

                    }
                });

    }


    private void addLog(String str) {
        mLogs.add(str);
        mRvLogAdapter.notifyDataSetChanged();
    }

    private void runTask() {
        if (GlobalProjectLauncher.getInstance().isRunning()) {
            return;
        }
        NewTaskBean newTaskBean = queue.poll();
        if (newTaskBean == null) {
            addLog("任务全部执行结束");
            isStarted = false;
            return;
        }
        PackageInfo packageInfo = com.stardust.utils.AppUtils.getPackageInfo(AppSelectActivity.this, newTaskBean.getF_PackageName());
        if (packageInfo == null) {
            addLog("[" + newTaskBean.getF_AppName() + "]未安装");
            runTask();
            return;
        }
        if (!newTaskBean.getF_AppVersion().equals(packageInfo.versionName)) {
            addLog("暂不支持[" + newTaskBean.getF_AppName() + "]" + packageInfo.versionName + "版本不对");
            runTask();
            return;
        }
        AppAutoMgr.CURRENTPACKAGENAME = newTaskBean.getF_PackageName();
        AppAutoMgr.sNewTaskBean = newTaskBean;
        GlobalProjectLauncher.getInstance().launch(AppSelectActivity.this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScriptEvent(ScriptEvent event) {
        NewTaskBean taskBean = AppAutoMgr.sNewTaskBean;
        if (taskBean != null) {
            addLog("[" + taskBean.getF_AppName() + "]执行完成");
            for (NewTaskBean newTaskBean : mNewTaskBeans) {
                if (newTaskBean.getF_PackageName().equals(taskBean.getF_PackageName())) {
                    newTaskBean.setExecuted(true);
                    newTaskBean.setExecutedSussed(event.getException() == null);
                }
            }
            mAppSelectAdapter.notifyDataSetChanged();
        }
        runTask();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVolumeUp(VolumeUpEvent event) {
        queue.clear();
        isStarted = false;
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

        new QMUIDialog.MessageDialogBuilder(AppSelectActivity.this).setMessage("Android 7.1.1 以上, 请开启悬浮窗权限")
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

}
