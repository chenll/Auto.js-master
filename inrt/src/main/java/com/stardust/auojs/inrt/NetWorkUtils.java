package com.stardust.auojs.inrt;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.stardust.HttpConstant;
import com.stardust.auojs.inrt.bean.UpDateBean;
import com.stardust.autojs.core.http.MutableOkHttp;
import com.stardust.utils.FuctionUtils;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Request;
import okhttp3.Response;

public class NetWorkUtils {

    public static UpDateBean getUpDateBean(Context context,  String versionCode) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Request request = new Request.Builder().url(HttpConstant.URL_UPDATA + "?sign=" + MD5Security.getMD5(format.format(date) + "-mcw") + "&apkVersion=" + versionCode).build();
        Log.e("aaa", request.url().toString());
        try {
            Response response = new MutableOkHttp().client().newCall(request).execute();
            if (response == null || !response.isSuccessful() || response.body() == null) {
                return null;
            }
            String responseStr = response.body().string();
            JSONObject jsonObject = new JSONObject(responseStr);
            if (jsonObject == null || !jsonObject.has("apkMd5") || !jsonObject.has("downloadLink")) {
                return null;
            }
            final String apkmd5 = jsonObject.optString("apkMd5", "");
            final String downloadLink = jsonObject.optString("downloadLink", "");
            if (TextUtils.isEmpty(apkmd5) || TextUtils.isEmpty(downloadLink)) {
                return null;
            }
            File file = new File(FuctionUtils.getDiskCacheDir(context, "inrt.apk"));
            if (file != null && file.exists()) {
                String md5 = MD5Security.getFileMD5(file);
                Log.e("aaa", "md5-->" + md5);
                if (!TextUtils.isEmpty(md5) && md5.equalsIgnoreCase(apkmd5)) {
                    FuctionUtils.clientInstall(FuctionUtils.getDiskCacheDir(context, "inrt.apk"));
                }
                file.delete();

            }
            return new UpDateBean(downloadLink, apkmd5);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
