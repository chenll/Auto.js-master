package com.stardust.datebase.greenDao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.stardust.datebase.greenDao.bean.History;

import com.stardust.datebase.greenDao.HistoryDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig historyDaoConfig;

    private final HistoryDao historyDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        historyDaoConfig = daoConfigMap.get(HistoryDao.class).clone();
        historyDaoConfig.initIdentityScope(type);

        historyDao = new HistoryDao(historyDaoConfig, this);

        registerDao(History.class, historyDao);
    }
    
    public void clear() {
        historyDaoConfig.clearIdentityScope();
    }

    public HistoryDao getHistoryDao() {
        return historyDao;
    }

}
