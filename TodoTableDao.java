package com.example.testapplication.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.testapplication.model.bean.ToDoInfo;
import com.example.testapplication.model.db.DBHelper;
import com.example.testapplication.model.db.TodoDB;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

public class TodoTableDao {

    private final TodoDB mHelper;

    public TodoTableDao(Context context){
        mHelper = new TodoDB(context);
    }
    //获取所有已处理TODO
    public List<ToDoInfo> getAllToDos(String uid){
        //获取数据库连接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //执行查询语句
        String sql = "select * from " + ToDoTable.TAB_NAME +" where " + ToDoTable.COL_UID + "=? and " + ToDoTable.COL_TYPE + " =1";
        Cursor cursor = db.rawQuery(sql, new String[]{uid});

        List<ToDoInfo> toDoInfos = new ArrayList<>();
        while (cursor.moveToNext()){
            ToDoInfo toDoInfo = new ToDoInfo();
            toDoInfo.setToDo_id(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_todoId)));
            toDoInfo.setToDo_title(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_title)));
            toDoInfo.setToDo_place(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_place)));
            toDoInfo.setToDo_content(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_content)));
            toDoInfo.setStartTime(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_starTime)));
            toDoInfo.setEndTime(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_endTime)));
            toDoInfo.setColor(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_color)));
            toDoInfo.setToDo_sender(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_sender)));
            toDoInfo.setToDo_receiverGroup(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_receiverGroup)));
            toDoInfo.setToDO_somethingElse(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDO_somethingElse)));
            toDoInfo.setToDo_ifTop(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_toDo_ifTop)));
            toDoInfo.setModel_type(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_Model_type)));
            toDoInfo.setIfDone(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_IfDone)));
            toDoInfos.add(toDoInfo);
        }

        //关闭资源

        cursor.close();
        //返回数据
        return toDoInfos;
    }
    //获取所有未处理待办
    public List<ToDoInfo> getAllUnprocessedToDos(String uid){
        //获取数据库连接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //执行查询语句
        String sql = "select * from " + ToDoTable.TAB_NAME +" where " + ToDoTable.COL_UID + "=? and " + ToDoTable.COL_TYPE + " =2";//标识符为2表示未处理
        Cursor cursor = db.rawQuery(sql, new String[]{uid});

        List<ToDoInfo> toDoInfos = new ArrayList<>();
        while (cursor.moveToNext()){
            ToDoInfo toDoInfo = new ToDoInfo();
            toDoInfo.setToDo_id(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_todoId)));
            toDoInfo.setToDo_title(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_title)));
            toDoInfo.setToDo_place(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_place)));
            toDoInfo.setToDo_content(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_content)));
            toDoInfo.setStartTime(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_starTime)));
            toDoInfo.setEndTime(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_endTime)));
            toDoInfo.setColor(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_color)));
            toDoInfo.setToDo_sender(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_sender)));
            toDoInfo.setToDo_receiverGroup(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_receiverGroup)));
            toDoInfo.setToDO_somethingElse(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDO_somethingElse)));
            toDoInfo.setToDo_ifTop(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_toDo_ifTop)));
            toDoInfo.setModel_type(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_Model_type)));
            toDoInfo.setIfDone(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_IfDone)));

            toDoInfos.add(toDoInfo);
        }

        //关闭资源

        cursor.close();
        //返回数据
        return toDoInfos;
    }
    //获取所有最近删除的待办
    public List<ToDoInfo> getAllRecent(String uid){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select * from " + ToDoTable.TAB_NAME +" where " + ToDoTable.COL_UID + "=? and " + ToDoTable.COL_TYPE + " =3";//标识符为3标识最近删除
        Cursor cursor = db.rawQuery(sql, new String[]{uid});

        List<ToDoInfo> toDoInfos = new ArrayList<>();
        while (cursor.moveToNext()){
            ToDoInfo toDoInfo = new ToDoInfo();
            toDoInfo.setToDo_id(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_todoId)));
            toDoInfo.setToDo_title(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_title)));
            toDoInfo.setToDo_place(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_place)));
            toDoInfo.setToDo_content(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_content)));
            toDoInfo.setStartTime(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_starTime)));
            toDoInfo.setEndTime(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_endTime)));
            toDoInfo.setColor(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_color)));
            toDoInfo.setToDo_sender(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_sender)));
            toDoInfo.setToDo_receiverGroup(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDo_receiverGroup)));
            toDoInfo.setToDO_somethingElse(cursor.getString(cursor.getColumnIndex(ToDoTable.COL_toDO_somethingElse)));
            toDoInfo.setToDo_ifTop(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_toDo_ifTop)));
            toDoInfo.setModel_type(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_Model_type)));
            toDoInfo.setIfDone(cursor.getInt(cursor.getColumnIndex(ToDoTable.COL_IfDone)));
            toDoInfos.add(toDoInfo);
        }

        //关闭资源

        cursor.close();
        //返回数据
        return toDoInfos;
    }

    //保存事件、自建的待办type为1
    public void saveToDo(ToDoInfo toDoInfo){

        if(toDoInfo==null){
            return;
        }
        Log.e("wenti",toDoInfo.toString());
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ToDoTable.COL_toDo_title , toDoInfo.getToDo_title());
        values.put(ToDoTable.COL_toDo_place , toDoInfo.getToDo_place());
        values.put(ToDoTable.COL_toDo_content , toDoInfo.getToDo_content());
        values.put(ToDoTable.COL_starTime , toDoInfo.getStartTime());
        values.put(ToDoTable.COL_endTime , toDoInfo.getEndTime());
        values.put(ToDoTable.COL_color , toDoInfo.getColor());
        values.put(ToDoTable.COL_toDo_sender , toDoInfo.getToDo_sender());
        values.put(ToDoTable.COL_toDo_receiverGroup , toDoInfo.getToDo_receiverGroup());
        values.put(ToDoTable.COL_toDO_somethingElse , toDoInfo.getToDO_somethingElse());
        values.put(ToDoTable.COL_toDo_ifTop , toDoInfo.getToDo_ifTop());
        values.put(ToDoTable.COL_Model_type , toDoInfo.getModel_type());
        values.put(ToDoTable.COL_IfDone , toDoInfo.getIfDone());
        values.put(ToDoTable.COL_UID, EMClient.getInstance().getCurrentUser());
        values.put(ToDoTable.COL_TYPE, toDoInfo.getType());

        db.replace(ToDoTable.TAB_NAME,null,values);
    }

    public void changeChecked(int toDo_ID , int checkStatus){


        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ToDoTable.COL_IfDone,checkStatus);
        db.update(ToDoTable.TAB_NAME,values,ToDoTable.COL_todoId + "=?",new String[]{String.valueOf(toDo_ID)});
    }

    //将已勾选待办类型改为3，即最近删除
    public void changeType(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ToDoTable.COL_TYPE,3);
        db.update(ToDoTable.TAB_NAME,values,ToDoTable.COL_IfDone+"=?",new String[]{String.valueOf(1)});
    }
    //根据待办ID改成已处理
    public void changeTypeToProcessed(int toDo_ID){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ToDoTable.COL_TYPE,1);
        db.update(ToDoTable.TAB_NAME,values,ToDoTable.COL_todoId + "=?",new String[]{String.valueOf(toDo_ID)});
    }
    //删除未处理待办
    public void deleteUnprocessedToDo(int toDoId){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(ToDoTable.TAB_NAME,ToDoTable.COL_todoId+"=?",new String[]{String.valueOf(toDoId)});
    }
    //清空最近待办，即类型为3的待办
    public void clearAllRecent(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(ToDoTable.TAB_NAME,ToDoTable.COL_TYPE+"=?",new String[]{String.valueOf(3)});
    }
}
