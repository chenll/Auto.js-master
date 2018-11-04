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

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.stardust.Event.ScriptEvent;
import com.stardust.auojs.inrt.adapter.AppSelectAdapter;
import com.stardust.auojs.inrt.bean.AppBean;
import com.stardust.auojs.inrt.launch.GlobalProjectLauncher;
import com.stardust.view.accessibility.AccessibilityService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class AppSelectActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AppSelectAdapter mAppSelectAdapter;
    private List<AppBean> mAppBeans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appselect);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);

        }

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


            AppAutoMgr.CURRENTPACKAGENAME = mAppSelectAdapter.getItem(position).getAppPackageName();
            GlobalProjectLauncher.getInstance().launch(AppSelectActivity.this);
//            Intent intent = new Intent(AppSelectActivity.this, LogActivity.class);
//            Bundle b = new Bundle();
//            b.putParcelable("appbean", mAppSelectAdapter.getItem(position));
//            intent.putExtras(b);
//            startActivity(intent);


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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScriptEvent(ScriptEvent event) {

        Log.e("aaa", "");
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
                }).show();
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
                }).addAction("取消", (dialog, index) -> {

        }).show();

        return false;
    }
}
