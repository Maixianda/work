package com.gzliangce.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by aaron on 3/5/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "conversation.db";
    private static final int DATABASE_VERSION = 1;
    // 数据表名，一个数据库中可以有多个表（虽然本例中只建立了一个表）
    public static final String TABLE_NAME = "Conversation";

    public DatabaseHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 调用时间：数据库第一次创建时onCreate()方法会被调用

        // onCreate方法有一个 SQLiteDatabase对象作为参数，根据需要对这个对象填充表和初始化数据
        // 这个方法中主要完成创建数据库后对数据库的操作
        Log.e("TAG", "DatabaseHelper onCreate");

        // 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中）
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE [" + TABLE_NAME + "] (");
        sBuffer.append("[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append("[conversationId] TEXT,");
        sBuffer.append("[unreadCount] INTEGER)");

        // 执行创建表的SQL语句
        db.execSQL(sBuffer.toString());

        // 即便程序修改重新运行，只要数据库已经创建过，就不会再进入这个onCreate方法
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 调用时间：如果DATABASE_VERSION值被改为别的数,系统发现现有数据库版本不同,即会调用onUpgrade

        // onUpgrade方法的三个参数，一个 SQLiteDatabase对象，一个旧的版本号和一个新的版本号
        // 这样就可以把一个数据库从旧的模型转变到新的模型
        // 这个方法中主要完成更改数据库版本的操作
        Log.d("TAG", "DatabaseHelper onUpgrade");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        // 上述做法简单来说就是，通过检查常量值来决定如何，升级时删除旧表，然后调用onCreate来创建新表
        // 一般在实际项目中是不能这么做的，正确的做法是在更新数据表结构时，还要考虑用户存放于数据库中的数据不丢失
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // 每次打开数据库之后首先被执行
        Log.e("TAG", "DatabaseHelper onOpen");
    }
}

