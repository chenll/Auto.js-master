package com.stardust.auojs.inrt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.stardust.Event.ScriptEvent;
import com.stardust.auojs.inrt.adapter.AppSelectAdapter;
import com.stardust.auojs.inrt.bean.AppBean;

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
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_apps);


        //创建布局管理
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManage);


        //创建适配器
        mAppBeans = new ArrayList<>();
        initDatas();
        mAppSelectAdapter = new AppSelectAdapter(R.layout.itme_app, mAppBeans);
        //给RecyclerView设置适配器
        mRecyclerView.setAdapter(mAppSelectAdapter);
        mAppSelectAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(AppSelectActivity.this, LogActivity.class);
            Bundle b = new Bundle();
            b.putParcelable("appbean", mAppSelectAdapter.getItem(position));
            intent.putExtras(b);
            startActivity(intent);
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

        Log.e("aaa","");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
