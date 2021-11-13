package com.example.testapplication.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.testapplication.model.bean.GroupInfo;
import com.example.testapplication.model.bean.InvationInfo;
import com.example.testapplication.model.bean.ToDoInfo;
import com.example.testapplication.model.bean.UserInfo;
import com.example.testapplication.utils.Constant;
import com.example.testapplication.utils.SpUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class EventListener {


    private Context mContext;

    private final LocalBroadcastManager mLBM;

    public EventListener(Context context){
        mContext = context;

        mLBM = LocalBroadcastManager.getInstance(mContext);
        //注册一个联系人变化的监听
        EMClient.getInstance().contactManager().setContactListener(emContactListener);
        //注册群变化的监听
        EMClient.getInstance().groupManager().addGroupChangeListener(eMGroupChangeListener);
        //注册接收消息的监听
        EMClient.getInstance().chatManager().addMessageListener(msgListener);

    }
    private EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            for(EMMessage message:messages){
                ToDoInfo toDoInfo = new ToDoInfo();
                Log.e("hsdmessage",((EMTextMessageBody)message.getBody()).getMessage());

                String ss = ((EMTextMessageBody)message.getBody()).getMessage() ;

                String[] sss = ss.split("&&&");

                toDoInfo.setToDo_title(sss[0]);
                toDoInfo.setToDo_place(sss[1]);
                toDoInfo.setToDo_content(sss[2]);
                toDoInfo.setStartTime(sss[3]);
                toDoInfo.setEndTime(sss[4]);
                toDoInfo.setToDo_sender(sss[6]);
                toDoInfo.setToDo_receiverGroup(sss[7]);
                toDoInfo.setToDO_somethingElse(sss[8]);
                toDoInfo.setToDo_ifTop(Integer.parseInt(sss[9]));
                toDoInfo.setModel_type(Integer.parseInt(sss[10]));
                toDoInfo.setType(2);//未处理标识
                Model.getInstance().getTodoTableDao().saveToDo(toDoInfo);


                //红点处理
                SpUtils.getInstance().save(SpUtils.IS_NEW_MESSAGE,true);
                //
                mLBM.sendBroadcast(new Intent(Constant.RECEIVING_MESSAGE));

            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    };


    private final EMGroupChangeListener eMGroupChangeListener = new EMGroupChangeListener() {
        //收到群邀请
        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setReason(reason);
            invationInfo.setGroup(new GroupInfo(groupName,groupId,inviter));
            invationInfo.setStatus(InvationInfo.InvitationStatus.NEW_GROUP_INVITE);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

            //红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到群申请通知
        @Override
        public void onRequestToJoinReceived(String groupId, String groupName, String applicant, String reason) {

            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setReason(reason);
            invationInfo.setGroup(new GroupInfo(groupName,groupId,applicant));
            invationInfo.setStatus(InvationInfo.InvitationStatus.NEW_GROUP_APPLICATION);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);
            //红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }
        //群申请被接受
        @Override
        public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {

            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setGroup(new GroupInfo(groupName,groupId,accepter));
            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);
            //红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }
        //群申请被拒绝
        @Override
        public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {

            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setReason(reason);
            invationInfo.setGroup(new GroupInfo(groupName,groupId,decliner));
            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);
            //红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }
        //群邀请被同意
        @Override
        public void onInvitationAccepted(String groupId, String invitee, String reason) {

            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setReason(reason);
            invationInfo.setGroup(new GroupInfo(groupId,groupId,invitee));
            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);
            //红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }
        //群邀请被拒绝
        @Override
        public void onInvitationDeclined(String groupId, String invitee, String reason) {

            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setReason(reason);
            invationInfo.setGroup(new GroupInfo(groupId,groupId,invitee));
            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_DECLINED);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);
            //红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }
        //群成员被删除
        @Override
        public void onUserRemoved(String groupId, String groupName) {


        }
        //群被解散
        @Override
        public void onGroupDestroyed(String groupId, String groupName) {

        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {

            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setReason(inviteMessage);
            invationInfo.setGroup(new GroupInfo(groupId,groupId,inviter));
            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);
            //红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onMuteListAdded(String groupId, List<String> mutes, long muteExpire) {

        }

        @Override
        public void onMuteListRemoved(String groupId, List<String> mutes) {

        }

        @Override
        public void onWhiteListAdded(String groupId, List<String> whitelist) {

        }

        @Override
        public void onWhiteListRemoved(String groupId, List<String> whitelist) {

        }

        @Override
        public void onAllMemberMuteStateChanged(String groupId, boolean isMuted) {

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
    } ;


    private final EMContactListener emContactListener = new EMContactListener() {
        //联系人增加后
        @Override
        public void onContactAdded(String username) {

            Model.getInstance().getDbManager().getContactTableDao().saveContact(new UserInfo(username),true);
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }
        //联系人删除后
        @Override
        public void onContactDeleted(String username) {

            Model.getInstance().getDbManager().getContactTableDao().deleteContactByHxId(username);
            Model.getInstance().getDbManager().getInviteTableDao().removeInvitation(username);

            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));

        }
        //联系热接收到新邀请
        @Override
        public void onContactInvited(String username, String reason) {

            InvationInfo invitationInfo = new InvationInfo();

            invitationInfo.setUser(new UserInfo(username));
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvationInfo.InvitationStatus.NEW_INVITE);

            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);

            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
        //别人同意了
        @Override
        public void onFriendRequestAccepted(String username) {

            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setUser(new UserInfo(username));
            invitationInfo.setStatus(InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);

            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);

            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
        //拒绝了
        @Override
        public void onFriendRequestDeclined(String username) {

            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));

        }
    };


}
