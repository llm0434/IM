package com.example.weichuanqi.im.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.weichuanqi.im.model.SQL.ContactSQL;
import com.example.weichuanqi.im.model.SQL.InviteSQL;

/**
 *   联系人 、 邀请信息数据库
 */
public class ContactInviteDB extends SQLiteOpenHelper {


    public ContactInviteDB(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContactSQL.CREATE_TABLE);
        db.execSQL(InviteSQL.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
