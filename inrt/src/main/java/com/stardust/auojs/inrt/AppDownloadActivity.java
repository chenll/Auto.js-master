package com.stardust.auojs.inrt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.stardust.HttpConstant;
import com.stardust.auojs.inrt.adapter.AppDownloadAdapter;
import com.stardust.auojs.inrt.bean.AppDownloadBean;
import com.stardust.auojs.inrt.bean.AppDownloadResponse;
import com.stardust.autojs.core.http.MutableOkHttp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;
import okhttp3.Response;

public class AppDownloadActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private ArrayList<AppDownloadBean> mAppDoloadBeans;
    private AppDownloadAdapter mAppDownloadAdapter;
    private Toolbar mToolbar;
    private QMUITipDialog tipDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_download);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("应用下载");
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_apps);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManage);
        mAppDoloadBeans = new ArrayList<AppDownloadBean>();
        mAppDownloadAdapter = new AppDownloadAdapter(R.layout.itme_app_download, mAppDoloadBeans);
        mRecyclerView.setAdapter(mAppDownloadAdapter);
        mAppDownloadAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String url = mAppDoloadBeans.get(position).getDownloadLink();
                if (!TextUtils.isEmpty(url)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
            }
        });
        tipDialog = new QMUITipDialog.Builder(AppDownloadActivity.this).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).setTipWord("正在加载数据，请稍等...").create();
        getDate();
    }

    public void getDate() {
        Observable.create((ObservableOnSubscribe<AppDownloadResponse>) emitter -> {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Request request = new Request.Builder().url(HttpConstant.URL_APP_DOWNLOADS + "?sign=" + MD5Security.getMD5(format.format(date) + "-mcw")).build();
            Log.e("aaa", request.url().toString());
            Response response = new MutableOkHttp().client().newCall(request).execute();
            if (response == null || !response.isSuccessful() || response.body() == null) {
                emitter.onError(new RuntimeException("下载列表获取失败,请稍后再试."));
                return;
            }
            try {
                Gson gson = new Gson();
                String responseStr = response.body().string();
                AppDownloadResponse taskBean = gson.fromJson(responseStr, AppDownloadResponse.class);
                if (taskBean == null || !"0000".equals(taskBean.getCode())) {
                    emitter.onError(new RuntimeException("下载列表获取失败,请稍后再试."));
                } else if (taskBean.getDatalist() == null || taskBean.getDatalist().isEmpty()) {
                    emitter.onError(new RuntimeException("暂无应用可下载,请稍后再试"));
                } else {
                    emitter.onNext(taskBean);
                }

            } catch (Exception e) {
                emitter.onError(e);
            }
            emitter.onComplete();

        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AppDownloadResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        tipDialog.show();
                    }

                    @Override
                    public void onNext(AppDownloadResponse appDownloadResponse) {
                        mAppDoloadBeans.addAll(appDownloadResponse.getDatalist());
                        mAppDownloadAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        tipDialog.dismiss();
                        Toast.makeText(AppDownloadActivity.this, TextUtils.isEmpty(e.getMessage()) ? "下载列表获取失败,请稍后再试." : e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        tipDialog.dismiss();

                    }
                });

    }


}
