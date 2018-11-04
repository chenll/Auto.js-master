package com.stardust.auojs.inrt;

import android.text.TextUtils;

import com.stardust.auojs.inrt.bean.AppAutoMessage;

import java.util.ArrayList;

public class AppAutoMgr {

    public static String CURRENTPACKAGENAME;

    public static AppAutoMessage getCurrentAppAutoMessage() {
        return getAppAutoMessage(CURRENTPACKAGENAME);
    }


    private static AppAutoMessage getAppAutoMessage(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }

        String[] appitem = App.getApplication().getResources().getStringArray(App.getApplication().getResources().getIdentifier(packageName, "array", App.getApplication().getPackageName()));

        if (appitem == null || appitem.length == 0) {
            return null;
        }
        AppAutoMessage appAutoMessage = new AppAutoMessage();
        appAutoMessage.setPackageName(packageName);
        appAutoMessage.setAppName(appitem[0]);
        appAutoMessage.setActivityMain(appitem[1]);
        appAutoMessage.setActivityDisturb(appitem[2]);
        appAutoMessage.setTargetTextId(appitem[3]);
        appAutoMessage.setRollViewId(appitem[4]);
        String ids = appitem[5];
        if (!TextUtils.isEmpty(ids)) {
            ArrayList<String> strings = new ArrayList<>();
            if (ids.contains("\\|")) {
                strings.add(ids);
            } else {
                for (String s : ids.split("\\|")) {
                    strings.add(s);
                }
            }
            appAutoMessage.setDialogIds(strings);

        }
        appAutoMessage.setExitBtnId(appitem[6]);

        return appAutoMessage;
    }

}
