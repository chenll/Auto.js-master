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
import com.stardust.autojs.core.http.MutableOkHttp;
import com.stardust.utils.AppUtils;
import com.stardust.utils.FuctionUtils;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;
import okhttp3.Response;

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
        if (System.currentTimeMillis() - PreferenceManager.getDefaultSharedPreferences(mContext).getLong("lastUpdataTime", 0l) > 1000 * 60 * 5) {
            Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
                if (AppUtils.getPackageInfo(App.getApplication(), "com.u9er.mcw") == null) {
                    FuctionUtils.installHelperApk(App.getApplication());
                    Thread.sleep(10000);
                }
                emitter.onNext(1);
                PreferenceManager.getDefaultSharedPreferences(mContext).edit().putLong("lastUpdataTime", System.currentTimeMillis()).commit();
                checkUpdate();
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> {
                        if (integer == 1) {
                            Intent intent1 = App.getApplication().getPackageManager().getLaunchIntentForPackage("com.u9er.mcw");
                            if (intent1 != null) {
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                App.getApplication().startActivity(intent1);
                            }
                        }

                    });
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void checkUpdate() {
        if (versionCode == -1) {
            try {
                PackageInfo packageInfo = mContext.getApplicationContext().getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
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
        Request request = new Request.Builder().url(HttpConstant.URL_UPDATA + "?sign=" + MD5Security.getMD5(format.format(date) + "-mcw") + "&apkVersion=" + versionCode).build();
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
            File file = new File(FuctionUtils.getDiskCacheDir(mContext, "inrt.apk"));
            if (file != null && file.exists()) {
                String md5 = MD5Security.getFileMD5(file);
                Log.e("aaa", "md5-->" + md5);
                if (!TextUtils.isEmpty(md5) && md5.equalsIgnoreCase(apkmd5)) {
                    FuctionUtils.clientInstall(FuctionUtils.getDiskCacheDir(mContext, "inrt.apk"));
                    return;
                }
                file.delete();

            }
            FileDownloader.getImpl().create(downloadLink).setPath(FuctionUtils.getDiskCacheDir(mContext, "inrt.apk")).setListener(new FileDownloadSampleListener() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
