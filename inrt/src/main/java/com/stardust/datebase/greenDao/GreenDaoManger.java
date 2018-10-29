package com.stardust.datebase.greenDao;

import com.stardust.auojs.inrt.App;
import com.stardust.datebase.greenDao.bean.History;

public class GreenDaoManger {

    public static void insert(String str) {
        History history = new History();
        history.setName(str);
        App.getApplication().getDaoSession().getHistoryDao().insert(history);
    }

    public static boolean isInserted(String str) {
        History history = App.getApplication().getDaoSession().getHistoryDao().queryBuilder().where(HistoryDao.Properties.Name.eq(str)).unique();
        if (history == null) {
            return false;
        }
        return true;
    }
}
