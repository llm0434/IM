package com.example.weichuanqi.im.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.weichuanqi.im.model.bean.Group;
import com.example.weichuanqi.im.model.bean.Invite;
import com.example.weichuanqi.im.model.bean.User;
import com.example.weichuanqi.im.utils.Constant;
import com.example.weichuanqi.im.utils.SPUtil;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMucSharedFile;

import java.util.List;

/**
 * 全局事件监听器
 */
public class EventListener {

    private Context mContext;
    private final LocalBroadcastManager mlbm;


    public EventListener(Context context) {
        this.mContext = context;

        //本地广播管理者
        mlbm = LocalBroadcastManager.getInstance(mContext);

        //注册联系人监听
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String username) {
                //数据更新
                Model.getInstance().getDBHelper().getContactDao().saveContact(new User(username),true);
                //发送联系人变化的广播
                mlbm.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
            }

            @Override
            public void onContactDeleted(String username) {

                //删除联系人
                Model.getInstance().getDBHelper().getContactDao().deleteContact(username);
                //同时删除联系人邀请信息
                Model.getInstance().getDBHelper().getInviteDao().deleteInvition(username);
                //发送联系人变化的广播
                mlbm.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
            }

            @Override
            public void onContactInvited(String username, String reason) {

                //新的邀请
                Invite invite = new Invite();
                invite.setUser(new User(username));
                invite.setReason(reason);
                invite.setStatus(Invite.InvitationStatus.NEW_INVITE);
                //添加邀请信息
                Model.getInstance().getDBHelper().getInviteDao().addInvition(invite);

                //出现新的邀请，红点
                SPUtil.getInstance().save(SPUtil.IS_NEW_INVITE, true);

                //发送广播
                mlbm.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
            }

            @Override
            public void onFriendRequestAccepted(String username) {
                Invite invitionInfo = new Invite();
                invitionInfo.setUser(new User(username));
                invitionInfo.setStatus(Invite.InvitationStatus.INVITE_ACCEPT_BY_PEER);//接收好友邀请

                Model.getInstance().getDBHelper().getInviteDao().addInvition(invitionInfo);
                //红点的处理
                SPUtil.getInstance().save(SPUtil.IS_NEW_INVITE, true);

                //发送邀请信息变化的广播
                mlbm.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
            }

            @Override
            public void onFriendRequestDeclined(String username) {
                   //红点的处理
                SPUtil.getInstance().save(SPUtil.IS_NEW_INVITE, true);
                //发送邀请信息变化的广播
                mlbm.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
            }
        });


        //群消息变化的监听
        EMClient.getInstance().groupManager().addGroupChangeListener(new EMGroupChangeListener() {
            //收到群邀请
            @Override
            public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
                //1.数据更新
                Invite invite = new Invite();
                invite.setReason(reason);
                invite.setGroup(new Group(groupId,groupName,inviter));
                invite.setStatus(Invite.InvitationStatus.NEW_GROUP_INVITE);
                Model.getInstance().getDBHelper().getInviteDao().addInvition(invite);
                //2.红点的处理
                SPUtil.getInstance().save(SPUtil.IS_NEW_INVITE,true);
                //3.发送广播
                mlbm.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));

            }

            @Override
            public void onRequestToJoinReceived(String groupId, String groupName, String applicant, String reason) {

                //1.数据更新
                Invite invite = new Invite();
                invite.setReason(reason);
                invite.setGroup(new Group(groupId,groupName,applicant));
                invite.setStatus(Invite.InvitationStatus.NEW_GROUP_APPLICATION);
                Model.getInstance().getDBHelper().getInviteDao().addInvition(invite);
                //2.红点的处理
                SPUtil.getInstance().save(SPUtil.IS_NEW_INVITE,true);
                //3.发送广播
                mlbm.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
            }

            @Override
            public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
                //1.数据更新
                Invite invite = new Invite();
                invite.setGroup(new Group(groupId,groupName,accepter));
                invite.setStatus(Invite.InvitationStatus.GROUP_APPLICATION_ACCEPTED);
                Model.getInstance().getDBHelper().getInviteDao().addInvition(invite);
                //2.红点的处理
                SPUtil.getInstance().save(SPUtil.IS_NEW_INVITE,true);
                //3.发送广播
                mlbm.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
            }

            @Override
            public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {

                //1.数据更新
                Invite invite = new Invite();
                invite.setReason(reason);
                invite.setGroup(new Group(groupId,groupName,decliner));
                invite.setStatus(Invite.InvitationStatus.GROUP_APPLICATION_DECLINED);
                Model.getInstance().getDBHelper().getInviteDao().addInvition(invite);
                //2.红点的处理
                SPUtil.getInstance().save(SPUtil.IS_NEW_INVITE,true);
                //3.发送广播
                mlbm.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
            }

            @Override
            public void onInvitationAccepted(String groupId, String invitee, String reason) {
                //1.数据更新
                Invite invite = new Invite();
                invite.setReason(reason);
                invite.setGroup(new Group(groupId,groupId,invitee));
                invite.setStatus(Invite.InvitationStatus.GROUP_INVITE_ACCEPTED);
                Model.getInstance().getDBHelper().getInviteDao().addInvition(invite);

                //2.红点的处理
                SPUtil.getInstance().save(SPUtil.IS_NEW_INVITE,true);
                //3.发送广播
                mlbm.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
            }

            @Override
            public void onInvitationDeclined(String groupId, String invitee, String reason) {
                //1.数据更新
                Invite invite = new Invite();
                invite.setReason(reason);
                invite.setGroup(new Group(groupId,groupId,invitee));
                invite.setStatus(Invite.InvitationStatus.GROUP_INVITE_DECLINED);
                Model.getInstance().getDBHelper().getInviteDao().addInvition(invite);
                //2.红点的处理
                SPUtil.getInstance().save(SPUtil.IS_NEW_INVITE,true);
                //3.发送广播
                mlbm.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
            }

            @Override
            public void onUserRemoved(String groupId, String groupName) {

            }

            @Override
            public void onGroupDestroyed(String groupId, String groupName) {


            }

            @Override
            public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {

                //1.数据更新
                Invite invite = new Invite();
                invite.setReason(inviteMessage);
                invite.setGroup(new Group(groupId,groupId,inviter));
                invite.setStatus(Invite.InvitationStatus.GROUP_INVITE_ACCEPTED);
                Model.getInstance().getDBHelper().getInviteDao().addInvition(invite);
                //2.红点的处理
                SPUtil.getInstance().save(SPUtil.IS_NEW_INVITE,true);
                //3.发送广播
                mlbm.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
            }

            /**
             *   一下方法均未使用
             */

            @Override
            public void onMuteListAdded(String groupId, List<String> mutes, long muteExpire) {


            }

            @Override
            public void onMuteListRemoved(String groupId, List<String> mutes) {


            }

            @Override
            public void onAdminAdded(String groupId, String administrator) {


            }

            @Override
            public void onAdminRemoved(String groupId, String administrator) {


            }

            @Override
            public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {


            }

            @Override
            public void onMemberJoined(String groupId, String member) {


            }

            @Override
            public void onMemberExited(String groupId, String member) {


            }

            @Override
            public void onAnnouncementChanged(String groupId, String announcement) {


            }

            @Override
            public void onSharedFileAdded(String groupId, EMMucSharedFile sharedFile) {


            }

            @Override
            public void onSharedFileDeleted(String groupId, String fileId) {


            }
        });



    }




}
