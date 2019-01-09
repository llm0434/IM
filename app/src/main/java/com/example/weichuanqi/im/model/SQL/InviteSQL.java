package com.example.weichuanqi.im.model.SQL;

/**
 * 邀请表语句
 */
public class InviteSQL {

    public static final String TABLE_NAME = "tab_invite";          // 邀请表名
    public static final String COL_USER_ID = "user_id";            // 用户id
    public static final String COL_USER_NAME = "user_name";        // 用户名
    public static final String COL_GROUP_ID = "group_id";          // 群组id
    public static final String COL_GROUP_NAME = "group_name";      // 群组名
    public static final String COL_REASON = "reason";              // 邀请原因
    public static final String COL_STATUS = "status";              // 邀请状态


    //建表语句

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + COL_USER_ID + " text primary key,"
            + COL_USER_NAME + " text,"
            + COL_GROUP_ID + " text,"
            + COL_GROUP_NAME + " text,"
            + COL_REASON + " text,"
            + COL_STATUS + " text);";



    //查询所有邀请人信息
    public static final String QUERY_ALL_INVITION = "select * from " + TABLE_NAME;


}
