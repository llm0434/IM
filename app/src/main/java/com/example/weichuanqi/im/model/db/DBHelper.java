package com.example.weichuanqi.im.model.db;

import android.content.Context;

import com.example.weichuanqi.im.model.dao.ContactDao;
import com.example.weichuanqi.im.model.dao.InviteDao;

public class DBHelper {


    private ContactInviteDB contactInviteDB;
    private ContactDao contactDao = null;
    private InviteDao inviteDao = null;


    public DBHelper(Context context, String name) {
        contactInviteDB = new ContactInviteDB(context,name);

        contactDao = new ContactDao(contactInviteDB);

        inviteDao = new InviteDao(contactInviteDB);


    }



    //获取联系人的数据库操作类对象
    public ContactDao getContactDao(){


        return contactDao;
    }

    //获取邀请信息的数据库操作类对象
    public InviteDao getInviteDao(){

        return inviteDao;
    }


    public void close(){
        contactInviteDB.close();
    }



}
