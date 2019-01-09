package com.example.weichuanqi.im.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.weichuanqi.im.model.SQL.ContactSQL;
import com.example.weichuanqi.im.model.SQL.GroupSQL;
import com.example.weichuanqi.im.model.SQL.InviteSQL;
import com.example.weichuanqi.im.model.SQL.UserSQL;
import com.example.weichuanqi.im.model.bean.Group;
import com.example.weichuanqi.im.model.bean.Invite;
import com.example.weichuanqi.im.model.bean.User;
import com.example.weichuanqi.im.model.db.ContactInviteDB;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 *
 */

public class InviteDao {

    public InviteDao(ContactInviteDB db) {
        this.contactInviteDB = db;
    }

    private ContactInviteDB contactInviteDB ;




    //添加邀请
    public void addInvition(Invite invite){
        //获取数据库连接
        SQLiteDatabase db = contactInviteDB.getReadableDatabase();

        ContentValues values = new ContentValues();

        User user = invite.getUser();
        if (user != null){  //联系人
            values.put(InviteSQL.COL_USER_ID,invite.getUser().getId());
            values.put(InviteSQL.COL_USER_NAME,invite.getUser().getName());

        }else {                //群组
            values.put(InviteSQL.COL_GROUP_ID,invite.getGroup().getGroupId());
            values.put(InviteSQL.COL_GROUP_NAME,invite.getGroup().getGroupName());
            //没有主键，设置主键
            values.put(InviteSQL.COL_USER_ID,invite.getUser().getId());
        }
        values.put(InviteSQL.COL_REASON,invite.getReason());
        values.put(InviteSQL.COL_STATUS,invite.getStatus().ordinal());//枚举类型转换为序号

        db.replace(InviteSQL.TABLE_NAME,null,values);

    }


    //获取所有邀请信息
    public List<Invite> getAllInvition(){
        SQLiteDatabase database = contactInviteDB.getReadableDatabase();

        Cursor cursor = database.rawQuery(InviteSQL.QUERY_ALL_INVITION, null);

        List<Invite> mInfoList = new ArrayList<>();
        while (cursor.moveToNext()){
            Invite invite = new Invite();

            invite.setReason(cursor.getString(cursor.getColumnIndex(InviteSQL.COL_REASON)));
            invite.setStatus(getInvitionStatusByInt(cursor.getInt(cursor.getColumnIndex(InviteSQL.COL_STATUS))));


            //判断是否为联系人或者是群组
            //如果group_id为空，则为联系人，反之为群组
            String group_id = cursor.getString(cursor.getColumnIndex(InviteSQL.COL_GROUP_ID));
            if (group_id == null){
                //联系人信息
                User info = new User();
                info.setId(cursor.getString(cursor.getColumnIndex(InviteSQL.COL_USER_ID)));
                info.setName(cursor.getString(cursor.getColumnIndex(InviteSQL.COL_USER_NAME)));
                info.setNick(cursor.getString(cursor.getColumnIndex(InviteSQL.COL_USER_NAME)));

                invite.setUser(info);

            }else {
                //群组信息

                Group groupInfo = new Group();

                groupInfo.setGroupId(cursor.getString(cursor.getColumnIndex(GroupSQL.COL_GROUP_ID)));
                groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(GroupSQL.COL_GROUP_NAME)));
                groupInfo.setInviter(cursor.getString(cursor.getColumnIndex(GroupSQL.COL_GROUP_INVITER)));

                invite.setGroup(groupInfo);

            }


            //添加本次循环的邀请信息
            mInfoList.add(invite);
        }

        //关闭资源
        cursor.close();

        return mInfoList;


    }



    //将int类型的状态转换为枚举类型
    public Invite.InvitationStatus  getInvitionStatusByInt(int intStatus){
        if (intStatus == Invite.InvitationStatus.NEW_INVITE.ordinal()) {
            return Invite.InvitationStatus.NEW_INVITE;
        }

        if (intStatus == Invite.InvitationStatus.INVITE_ACCEPT.ordinal()) {
            return Invite.InvitationStatus.INVITE_ACCEPT;
        }

        if (intStatus == Invite.InvitationStatus.INVITE_ACCEPT_BY_PEER.ordinal()) {
            return Invite.InvitationStatus.INVITE_ACCEPT_BY_PEER;
        }

        if (intStatus == Invite.InvitationStatus.NEW_GROUP_INVITE.ordinal()) {
            return Invite.InvitationStatus.NEW_GROUP_INVITE;
        }

        if (intStatus == Invite.InvitationStatus.NEW_GROUP_APPLICATION.ordinal()) {
            return Invite.InvitationStatus.NEW_GROUP_APPLICATION;
        }

        if (intStatus == Invite.InvitationStatus.GROUP_INVITE_ACCEPTED.ordinal()) {
            return Invite.InvitationStatus.GROUP_INVITE_ACCEPTED;
        }

        if (intStatus == Invite.InvitationStatus.GROUP_APPLICATION_ACCEPTED.ordinal()) {
            return Invite.InvitationStatus.GROUP_APPLICATION_ACCEPTED;
        }

        if (intStatus == Invite.InvitationStatus.GROUP_INVITE_DECLINED.ordinal()) {
            return Invite.InvitationStatus.GROUP_INVITE_DECLINED;
        }

        if (intStatus == Invite.InvitationStatus.GROUP_APPLICATION_DECLINED.ordinal()) {
            return Invite.InvitationStatus.GROUP_APPLICATION_DECLINED;
        }

        if (intStatus == Invite.InvitationStatus.GROUP_ACCEPT_INVITE.ordinal()) {
            return Invite.InvitationStatus.GROUP_ACCEPT_INVITE;
        }

        if (intStatus == Invite.InvitationStatus.GROUP_ACCEPT_APPLICATION.ordinal()) {
            return Invite.InvitationStatus.GROUP_ACCEPT_APPLICATION;
        }

        if (intStatus == Invite.InvitationStatus.GROUP_REJECT_APPLICATION.ordinal()) {
            return Invite.InvitationStatus.GROUP_REJECT_APPLICATION;
        }

        if (intStatus == Invite.InvitationStatus.GROUP_REJECT_INVITE.ordinal()) {
            return Invite.InvitationStatus.GROUP_REJECT_INVITE;
        }

        return null;



    }


    //删除邀请人信息
    public void deleteInvition(String id){

        if (id == null){
            return;
        }

        //获取数据库连接
        SQLiteDatabase di = contactInviteDB.getReadableDatabase();

        //执行删除语句
        di.delete(InviteSQL.TABLE_NAME,InviteSQL.COL_USER_ID+"=?",new String[]{id});


    }


    //更新邀请状态
    public void updateInvitionStatus(Invite.InvitationStatus invitationStatus,String id) {

        if (id == null){
            return;
        }

        //获取数据库连接
        SQLiteDatabase ui = contactInviteDB.getReadableDatabase();

        //执行更新操作
        ContentValues values = new ContentValues();
        values.put(InviteSQL.COL_STATUS,invitationStatus.ordinal());

        ui.update(InviteSQL.TABLE_NAME,values,InviteSQL.COL_USER_ID+"=?",new String[]{id});


    }








}
