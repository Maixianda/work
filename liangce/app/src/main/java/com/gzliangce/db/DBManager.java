package com.gzliangce.db;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.gzliangce.AppContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaron on 3/5/16.
 */
public class DBManager {
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private static DBManager dbManager;

    public static DBManager getInstance() {
        if (null == dbManager) {
            dbManager = new DBManager(AppContext.me().getApplicationContext());
        }
        return dbManager;
    }

    public static DBManager getInstance(Context context) {
        if (null == dbManager) {
            dbManager = new DBManager(context);
        }
        return dbManager;
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public DBManager(Context context) {
        helper = new DatabaseHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add conversationHistory list
     *
     * @param conversationHistories
     */
    public void add(List<ConversationHistory> conversationHistories) {
        db.beginTransaction();  //开始事务
        try {
            for (ConversationHistory conversationHistory : conversationHistories) {
                db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME + " VALUES(null, ?, ?, ?, ?, ?, ?, ?)",
                        new Object[]{conversationHistory.icon, conversationHistory.name,
                                conversationHistory.lastMessage, conversationHistory.time,
                                conversationHistory.memberId, conversationHistory.conversationId,
                                conversationHistory.unreadCount});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * add conversationHistory
     *
     * @param avimConversation , unreadCount
     */
    public void add(AVIMConversation avimConversation, int unreadCount) {
        db.beginTransaction();  //开始事务
        try {
            if (avimConversation != null) {
                if (judgeExist(avimConversation)) {
                    updateConversation(avimConversation, unreadCount);
                } else {
                    db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME + " VALUES(null, ?, ?)",
                            new Object[]{avimConversation.getConversationId(), unreadCount});
                }
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * update conversationHistory
     *
     * @param avimConversation,unreadCount
     */
    public void updateConversation(AVIMConversation avimConversation, int unreadCount) {
        ContentValues cv = new ContentValues();
        cv.put("conversationId", avimConversation.getConversationId());
        cv.put("unreadCount", unreadCount);
        db.update(DatabaseHelper.TABLE_NAME, cv, "conversationId = ?", new String[]{avimConversation.getConversationId()});
    }

    /**
     * delete old conversationHistory
     *
     * @param conversationId
     */
    public void deleteOldConversation(String conversationId) {
        db.delete(DatabaseHelper.TABLE_NAME, "conversationId = ?", new String[]{conversationId});
    }

    /**
     * delete table
     */
    public void dropTable() {
        try {
            db.delete(DatabaseHelper.TABLE_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * query all ConversationHistory, return list
     *
     * @return List<ConversationHistory>
     */
    public List<ConversationHistory> query() {
        ArrayList<ConversationHistory> conversationHistories = new ArrayList<>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            ConversationHistory conversationHistory = new ConversationHistory();
            conversationHistory.id = c.getInt(c.getColumnIndex("id"));
            conversationHistory.conversationId = c.getString(c.getColumnIndex("conversationId"));
            conversationHistory.unreadCount = c.getInt(c.getColumnIndex("unreadCount"));
            conversationHistories.add(conversationHistory);
        }
        c.close();
        return conversationHistories;
    }

    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);
        return c;
    }


    /**
     * query exist conversation, return cursor
     *
     * @return Cursor
     */
    public boolean judgeExist(AVIMConversation avimConversation) {
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME + " where conversationId = ?", new String[]{avimConversation.getConversationId()});
        if (c.moveToFirst()) {
            return true;
        }
        return false;
    }


    /**
     * close database
     */

    public void closeDB() {
        db.close();
    }
}