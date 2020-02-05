package com.example.latte.eye.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

/**
 * GreenDao在提供了openHelper类，但是在每次的APP打开之后，
 * openHelper会将之前的APP的储存的数据删除掉，现在我们建立现在自己的配置类；
 */
public class ReleaseOpenHelper extends DaoMaster.OpenHelper {
    public ReleaseOpenHelper(Context context, String name) {
        super(context, name);
    }

    public ReleaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }
}
