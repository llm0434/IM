package com.example.weichuanqi.im.model.SQL;

public class UserSQL {

    public static final String TABLE_NAME = "tab_user";         //  用户表名
    public static final String COL_ID = "id";                   //  id
    public static final String COL_NAME = "name";               //  用户名
    public static final String COL_NICK = "nick";               //  昵称
    public static final String COL_IMAGE = "image";             //  头像


    //建表语句
    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + COL_ID + " text primary key,"
            + COL_NAME + " text,"
            + COL_NICK + " text,"
            + COL_IMAGE + " text);";

    //根据ID 查询用户信息
    public static final String QUERY_BY_ID ="select * from " + TABLE_NAME + " where " + COL_ID + " =?;";






}
