package com.example.latte.eye.database;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

/**
 * 功能的抽取
 */
public class DatabaseManager {
    private DaoSession mDaoSession = null;
    private UsersDao mDao = null;

    private DatabaseManager() {
    }

    public DatabaseManager init(Context context) {
        initDao(context);
        return this;
    }
    private static final class Holder {
        private static final DatabaseManager INSTANCE = new DatabaseManager();
    }

    public static DatabaseManager getInstance() {
        return Holder.INSTANCE;
    }

    private void initDao(Context context) {
        final ReleaseOpenHelper helper = new ReleaseOpenHelper(context, "fast_eye.db");//fast_eye.db数据库的名字
        final Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
        mDao = mDaoSession.getUsersDao();
    }

    public final UsersDao getDao() {
        return mDao;
    }
}
