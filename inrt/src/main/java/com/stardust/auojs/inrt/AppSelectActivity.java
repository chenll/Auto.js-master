package com.stardust.auojs.inrt;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.stardust.Event.ScriptEvent;
import com.stardust.auojs.inrt.adapter.AppSelectAdapter;
import com.stardust.auojs.inrt.bean.AppBean;
import com.stardust.auojs.inrt.bean.NewTaskBean;
import com.stardust.auojs.inrt.bean.NewTaskResponse;
import com.stardust.auojs.inrt.launch.GlobalProjectLauncher;
import com.stardust.autojs.core.http.MutableOkHttp;
import com.stardust.view.accessibility.AccessibilityService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;
import okhttp3.Response;


public class AppSelectActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AppSelectAdapter mAppSelectAdapter;
    private List<AppBean> mAppBeans;
    private QMUITipDialog tipDialog;
    private EditText mEtSign;

    Queue<NewTaskBean> queue = new LinkedList<NewTaskBean>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appselect);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mEtSign = (EditText) findViewById(R.id.et_sign);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("App列表");
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_apps);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManage);
        //创建适配器
        mAppBeans = new ArrayList<>();
        initDatas();
        mAppSelectAdapter = new AppSelectAdapter(R.layout.itme_app, mAppBeans);
        //给RecyclerView设置适配器
        mRecyclerView.setAdapter(mAppSelectAdapter);
        mAppSelectAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (!checkAccessibility()) {
                return;
            }
            if (!checkDrawOverlays()) {
                return;
            }
//            AppAutoMgr.CURRENTPACKAGENAME = mAppSelectAdapter.getItem(position).getAppPackageName();
//            GlobalProjectLauncher.getInstance().launch(AppSelectActivity.this);
//            Intent intent = new Intent(AppSelectActivity.this, LogActivity.class);
//            Bundle b = new Bundle();
//            b.putParcelable("appbean", mAppSelectAdapter.getItem(position));
//            intent.putExtras(b);
//            startActivity(intent);


        });
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkAccessibility()) {
                    return;
                }
                if (!checkDrawOverlays()) {
                    return;
                }

                if (mEtSign.getText() == null) {
                    Toast.makeText(AppSelectActivity.this, "请先输入您的密钥", Toast.LENGTH_LONG).show();
                    return;
                }
                getTask(mEtSign.getText().toString());
//                new QMUIDialog.MessageDialogBuilder(AppSelectActivity.this)
//                        .setMessage("将依次执行【趣看天下】【蚂蚁头条】")
//                        .addAction("确定", (dialog, index) -> {
//
//                            dialog.dismiss();
//                        }).addAction("取消", (dialog, index) -> dialog.dismiss()).show();
            }
        });


        tipDialog = new QMUITipDialog.Builder(AppSelectActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载数据，请稍等")
                .create();


    }


    private void getTask(String sign) {
        Observable.create((ObservableOnSubscribe<NewTaskResponse>) emitter -> {
            Request request = new Request.Builder().url("http://api.u9er.com/appData.ashx?sign=" + MD5Security.getMD5(sign)).build();
            Response response = new MutableOkHttp().client().newCall(request).execute();
            if (response == null || !response.isSuccessful() || response.body() == null) {
                emitter.onError(new RuntimeException());
                return;
            }
            try {
                Gson gson = new Gson();
                NewTaskResponse taskBean = gson.fromJson(response.body().string(), NewTaskResponse.class);
                if (taskBean != null && taskBean.getDatalist() != null && !taskBean.getDatalist().isEmpty()) {
                    emitter.onNext(taskBean);
                } else {
                    emitter.onError(new RuntimeException());
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
                        for (NewTaskBean newTaskBean : integer.getDatalist()) {
                            queue.offer(newTaskBean);
                        }
                        runTask();
                        Toast.makeText(AppSelectActivity.this, "数据加载成功", Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onError(Throwable e) {
                        tipDialog.dismiss();
                        Toast.makeText(AppSelectActivity.this, "数据加载失败", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onComplete() {
                        tipDialog.dismiss();

                    }
                });

    }

    private void initDatas() {
        String[] apps = getResources().getStringArray(R.array.apps);
        for (String appLable : apps) {
            String[] appitem = getResources().getStringArray(getResources().getIdentifier(appLable, "array", getPackageName()));
            AppBean appBean = new AppBean();
            appBean.setLable(appLable);
            appBean.setAppName(appitem[0]);
            appBean.setAppPackageName(appitem[1]);
            appBean.setAppVersionName(appitem[2]);
            appBean.setAppVersion(appitem[3]);
            mAppBeans.add(appBean);
        }


    }

    private void runTask() {
        NewTaskBean newTaskBean = queue.poll();
        if (newTaskBean == null) {
            return;
        }
        AppAutoMgr.CURRENTPACKAGENAME = newTaskBean.getF_PackageName();
        AppAutoMgr.sNewTaskBean = newTaskBean;
        GlobalProjectLauncher.getInstance().launch(AppSelectActivity.this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScriptEvent(ScriptEvent event) {

//        Log.e("ddd",event.getScriptExecution()==null?"----":event.getScriptExecution()+"");

        runTask();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
}
