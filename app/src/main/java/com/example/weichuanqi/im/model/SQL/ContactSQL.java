package com.example.weichuanqi.im.model.SQL;

/**
 * 联系人表语句
 */
public class ContactSQL {

    public static final String TABLE_NAME = "tab_contact";          // 联系人表名
    public static final String COL_ID = "id";                       // id
    public static final String COL_NAME = "name";                   // 用户名
    public static final String COL_NICK = "nick";                   // 昵称
    public static final String COL_IMAGE = "image";                 // 头像
    public static final String COL_IS_CONTACT = "is_contact";       // 是否是联系人


    //建表语句
    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + COL_ID + " text primary key,"
            + COL_NAME + " text,"
            + COL_NICK + " text,"
            + COL_IMAGE + " text,"
            + COL_IS_CONTACT + " integer);";


    //查询我的联系人
    public static final String QUERY_MY_CONTACT = "select * from " + TABLE_NAME + " where " + COL_IS_CONTACT + " =1;";


    //根据ID查询联系人

    public static final String QUERY_BY_ID = "select * from " + TABLE_NAME + " where " + COL_ID + " =?;";

    //查询所有联系人
    public static final String QUERY_ALL_CONTACT = "select * from " + TABLE_NAME ;


}
