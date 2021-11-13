package com.example.testapplication.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.testapplication.model.bean.GroupInfo;
import com.example.testapplication.model.bean.InvationInfo;
import com.example.testapplication.model.bean.UserInfo;
import com.example.testapplication.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

//邀请信息表的操作类

public class InviteTableDao {

    private DBHelper mHelper;

    public InviteTableDao(DBHelper helper){
        mHelper=helper;
    }

    public void addInvitation(InvationInfo invationInfo){
        SQLiteDatabase db = mHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(InviteTable.COL_REASON,invationInfo.getReason());
        values.put(InviteTable.COL_STATUS,invationInfo.getStatus().ordinal());

        UserInfo user = invationInfo.getUser();

        if(user!=null){
            values.put(InviteTable.COL_USER_HXID,invationInfo.getUser().getHxid());
            values.put(InviteTable.COL_USER_NAME,invationInfo.getUser().getName());
        }else {
            values.put(InviteTable.COL_GROUP_HXID,invationInfo.getGroup().getGroupId());
            values.put(InviteTable.COL_GROUP_NAME,invationInfo.getGroup().getGroupName());
            values.put(InviteTable.COL_USER_HXID,invationInfo.getGroup().getInvatePerson());
        }
        db.replace(InviteTable.TAB_NAME,null,values);

    }

    public List<InvationInfo> getInvitations(){
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String sql = "select * from " + InviteTable.TAB_NAME;
        Cursor cursor = db.rawQuery(sql, null);

        List<InvationInfo> invationInfos = new ArrayList<>();

        while (cursor.moveToNext()){
            InvationInfo invationInfo = new InvationInfo();

            invationInfo.setReason(cursor.getString(cursor.getColumnIndex(InviteTable.COL_REASON)));
            invationInfo.setStatus(int2InviteStatus(cursor.getInt(cursor.getColumnIndex(InviteTable.COL_STATUS))));
            String groupId = cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_HXID));
            if(groupId==null){
                UserInfo userInfo = new UserInfo();

                userInfo.setHxid(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_HXID)));
                userInfo.setName(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_NAME)));
                userInfo.setNick(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_NAME)));

                invationInfo.setUser(userInfo);
            }else{
                GroupInfo groupInfo = new GroupInfo();
                groupInfo.setGroupId(cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_HXID)));
                groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_NAME)));
                groupInfo.setInvatePerson(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_HXID)));

                invationInfo.setGroup(groupInfo);
            }

            invationInfos.add(invationInfo);
        }

        cursor.close();
        return invationInfos;
    }

    private InvationInfo.InvitationStatus int2InviteStatus(int intStatus){
        if(intStatus == InvationInfo.InvitationStatus.NEW_INVITE.ordinal()){
            return InvationInfo.InvitationStatus.NEW_INVITE;
        }

        if(intStatus == InvationInfo.InvitationStatus.INVITE_ACCEPT.ordinal()){
            return InvationInfo.InvitationStatus.INVITE_ACCEPT;
        }
        if(intStatus == InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER.ordinal()){
            return InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER;
        }
        if(intStatus == InvationInfo.InvitationStatus.NEW_GROUP_INVITE.ordinal()){
            return InvationInfo.InvitationStatus.NEW_GROUP_INVITE;
        }
        if(intStatus == InvationInfo.InvitationStatus.NEW_GROUP_APPLICATION.ordinal()){
            return InvationInfo.InvitationStatus.NEW_GROUP_APPLICATION;
        }
        if(intStatus == InvationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED.ordinal()){
            return InvationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED;
        }
        if(intStatus == InvationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED.ordinal()){
            return InvationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED;
        }
        if(intStatus == InvationInfo.InvitationStatus.GROUP_INVITE_DECLINED.ordinal()){
            return InvationInfo.InvitationStatus.GROUP_INVITE_DECLINED;
        }

        if(intStatus == InvationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED.ordinal()){
            return InvationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED;
        }
        if(intStatus == InvationInfo.InvitationStatus.GROUP_ACCEPT_INVITE.ordinal()){
            return InvationInfo.InvitationStatus.GROUP_ACCEPT_INVITE;
        }
        if(intStatus == InvationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION.ordinal()){
            return InvationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION;
        }
        if(intStatus == InvationInfo.InvitationStatus.GROUP_REJECT_APPLICATION.ordinal()){
            return InvationInfo.InvitationStatus.GROUP_REJECT_APPLICATION;
        }
        if(intStatus == InvationInfo.InvitationStatus.GROUP_REJECT_INVITE.ordinal()){
            return InvationInfo.InvitationStatus.GROUP_REJECT_INVITE;
        }


        return null;
    }

    public void removeInvitation(String hxId){
        if(hxId==null){
            return;
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();

        db.delete(InviteTable.TAB_NAME,InviteTable.COL_USER_HXID + "=?",new String[]{hxId});
    }

    public void updateInvitationStatus(InvationInfo.InvitationStatus invitationStatus,String hxId){
        if (hxId == null){
            return;
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(InviteTable.COL_STATUS,invitationStatus.ordinal());
        db.update(InviteTable.TAB_NAME,values,InviteTable.COL_USER_HXID + "=?",new String[]{hxId});

    }


}
