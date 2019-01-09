package com.example.weichuanqi.im.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.weichuanqi.im.model.SQL.ContactSQL;
import com.example.weichuanqi.im.model.SQL.InviteSQL;
import com.example.weichuanqi.im.model.SQL.UserSQL;
import com.example.weichuanqi.im.model.bean.User;
import com.example.weichuanqi.im.model.db.ContactInviteDB;
import com.example.weichuanqi.im.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactDao {


    public ContactDao(ContactInviteDB db) {
        this.contactInviteDB  = db;
    }

    private ContactInviteDB contactInviteDB = null;




    // 保存单个联系人
    public void saveContact(User user, boolean isMyContact) {

        if (user == null) {
            return;
        }

        // 获取数据库链接
        SQLiteDatabase db = contactInviteDB.getReadableDatabase();

        // 执行保存语句
        ContentValues values = new ContentValues();
        values.put(ContactSQL.COL_ID, user.getId());
        values.put(ContactSQL.COL_NAME, user.getName());
        values.put(ContactSQL.COL_NICK, user.getNick());
        values.put(ContactSQL.COL_IMAGE, user.getImage());
        values.put(ContactSQL.COL_IS_CONTACT, isMyContact ? 1 : 0);

        db.replace(ContactSQL.TABLE_NAME, null, values);
    }

    //保存多个联系人
    public void saveMoreContact(List<User> mList, boolean isMyContact) {

        if (mList == null) {
            return;
        }

        for (User info : mList) {
            saveContact(info, isMyContact);
        }
    }

    //获取所有联系人信息
    public List<User> getAllContacts() {
        //获取数据库操作对象
        SQLiteDatabase db = contactInviteDB.getReadableDatabase();
        //查找
        Cursor cursor = db.rawQuery(ContactSQL.QUERY_ALL_CONTACT, null);

        //联系人集合
        List<User> mInfos = new ArrayList<>();

        while (cursor.moveToNext()) {
            User user = new User();
            user.setId(cursor.getString(cursor.getColumnIndex(ContactSQL.COL_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(ContactSQL.COL_NAME)));
            user.setNick(cursor.getString(cursor.getColumnIndex(ContactSQL.COL_NICK)));
            user.setImage(cursor.getString(cursor.getColumnIndex(ContactSQL.COL_IMAGE)));

            mInfos.add(user);
        }

        //关闭资源
        cursor.close();

        return mInfos;
    }

    //通过ID获取用户联系人信息
    public List<User> getUserContactById(List<String> identifier) {

        if (identifier == null || identifier.size() <= 0) {
            return null;
        }

        List<User> mList = new ArrayList<>();
        for (String id : identifier) {
            User contactById = getContactById(id);

            mList.add(contactById);
        }

        return mList;
    }

    //通过ID获取单个联系人信息
    public User getContactById(String identifier) {

        if (identifier == null) {
            return null;
        }

        SQLiteDatabase database = contactInviteDB.getReadableDatabase();

        Cursor cursor = database.rawQuery(ContactSQL.QUERY_BY_ID, new String[]{identifier});
        User userInfo = null;
        if (cursor.moveToNext()) {
            userInfo = new User();

            userInfo.setId(cursor.getString(cursor.getColumnIndex(UserSQL.COL_ID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(UserSQL.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(UserSQL.COL_NICK)));
            userInfo.setImage(cursor.getString(cursor.getColumnIndex(UserSQL.COL_IMAGE)));

        }

        cursor.close();

        return userInfo;
    }



    //删除联系人信息
    public void deleteContact(String identifier){

        if (identifier == null){
            return;
        }

        SQLiteDatabase deleteCont = contactInviteDB.getReadableDatabase();

        deleteCont.delete(ContactSQL.TABLE_NAME,ContactSQL.COL_ID+"=?",new String[]{identifier});

    }

}
