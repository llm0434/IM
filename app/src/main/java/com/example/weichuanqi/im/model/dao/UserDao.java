package com.example.weichuanqi.im.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.weichuanqi.im.model.SQL.UserSQL;
import com.example.weichuanqi.im.model.db.UserDB;
import com.example.weichuanqi.im.model.bean.User;

/**
 * User表操作类
 */


public class UserDao {



    private UserDB userDB = null;


    public UserDao(Context context) {
        userDB = new UserDB(context);
    }




    //添加用户
    public void addAccount(User user) {
        //获取数据库对象
        SQLiteDatabase db = userDB.getReadableDatabase();
        //添加
        ContentValues values = new ContentValues();
        values.put(UserSQL.COL_ID, user.getId());
        values.put(UserSQL.COL_NAME, user.getName());
        values.put(UserSQL.COL_NICK, user.getNick());
        values.put(UserSQL.COL_IMAGE, user.getImage());

        db.replace(UserSQL.TABLE_NAME, null, values);

    }


    //查询账户
    public User getAccountById(String id) {
        //获取数据库对象
        SQLiteDatabase mDatabase = userDB.getReadableDatabase();

        //查询
        Cursor cursor = mDatabase.rawQuery(UserSQL.QUERY_BY_ID, new String[]{id});

        //封装成对象
        User user = new User();
        if (cursor.moveToNext()) {

            user.setId(cursor.getString(cursor.getColumnIndex(UserSQL.COL_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(UserSQL.COL_NAME)));
            user.setNick(cursor.getString(cursor.getColumnIndex(UserSQL.COL_NICK)));
            user.setImage(cursor.getString(cursor.getColumnIndex(UserSQL.COL_IMAGE)));
        }

        //关闭资源
        cursor.close();

        return user;

    }


}
