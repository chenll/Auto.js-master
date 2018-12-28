package com.stardust;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.stardust.auojs.inrt.App;
import com.stardust.auojs.inrt.MD5Security;
import com.stardust.auojs.inrt.NetWorkUtils;
import com.stardust.auojs.inrt.bean.UpDateBean;
import com.stardust.utils.AppUtils;
import com.stardust.utils.FuctionUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DownloadService extends Service {

    private int versionCode = -1;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;


    }

    @SuppressLint("CheckResult")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //安装辅助应用
        if (AppUtils.getPackageInfo(App.getApplication(), "com.u9er.mcw") == null) {
            FuctionUtils.installHelperApk(App.getApplication());
        }


        if (System.currentTimeMillis() - PreferenceManager.getDefaultSharedPreferences(mContext).getLong("lastUpdataTime", 0l) > 1000 * 60 * 5) {
            Observable.create((ObservableOnSubscribe<UpDateBean>) emitter -> {
                PreferenceManager.getDefaultSharedPreferences(mContext).edit().putLong("lastUpdataTime", System.currentTimeMillis()).commit();
                UpDateBean upDateBean = checkUpdate();
                if (upDateBean != null) {
                    emitter.onNext(upDateBean);
                }
                emitter.onComplete();
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(upDateBean -> {
                        if (upDateBean == null || TextUtils.isEmpty(upDateBean.getDownloadUrl()) || TextUtils.isEmpty(upDateBean.getApkMd5())) {
                            return;
                        }
                        Intent intent1 = App.getApplication().getPackageManager().getLaunchIntentForPackage("com.u9er.mcw");
                        if (intent1 != null) {
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            App.getApplication().startActivity(intent1);
                        }
                        File file = new File(FuctionUtils.getDiskCacheDir(mContext, "inrt.apk"));
                        if (file != null && file.exists()) {
                            file.delete();
                        }
                        FileDownloader.getImpl().create(upDateBean.getDownloadUrl()).setPath(FuctionUtils.getDiskCacheDir(mContext, "inrt.apk")).setListener(new FileDownloadSampleListener() {
                            @Override
                            protected void completed(BaseDownloadTask task) {
                                super.completed(task);
                                Log.e("aaa", "下载完成-->" + task.getTargetFilePath() + "");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String md5 = MD5Security.getFileMD5(new File(task.getTargetFilePath()));
                                        Log.e("aaa", "开始安装" + md5);
                                        if (!TextUtils.isEmpty(md5) && md5.equalsIgnoreCase(upDateBean.getApkMd5())) {
                                            Log.e("aaa", "开始安装");
                                            new Thread(() -> FuctionUtils.clientInstall(task.getTargetFilePath())).start();
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
                    });
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private UpDateBean checkUpdate() {
        if (versionCode == -1) {
            try {
                PackageInfo packageInfo = mContext.getApplicationContext().getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                versionCode = packageInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                versionCode = -1;
            }
        }
        if (versionCode == -1) {
            return null;
        }
        return NetWorkUtils.getUpDateBean(mContext, versionCode + "");

    }

}
