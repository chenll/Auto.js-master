package com.stardust.datebase.greenDao;

import android.text.TextUtils;

import com.stardust.auojs.inrt.App;
import com.stardust.datebase.greenDao.bean.History;

public class GreenDaoManger {

    public static void insert(String packageName, String str) {
        if (TextUtils.isEmpty(packageName) || TextUtils.isEmpty(str)) {
            return;
        }
        History history = new History();
        history.setPackageName(packageName);
        history.setKey(str);
        history.setTime(System.currentTimeMillis());
        App.getApplication().getDaoSession().getHistoryDao().insert(history);
    }

    public static boolean isInserted(String packageName, String str) {
        if (TextUtils.isEmpty(packageName) || TextUtils.isEmpty(str)) {
            return false;
        }
        History history = App.getApplication().getDaoSession().getHistoryDao().queryBuilder()
                .where(HistoryDao.Properties.PackageName.eq(packageName), HistoryDao.Properties.Key.eq(str))
                .unique();
        if (history == null) {
            return false;
        }
        return true;
    }
}
