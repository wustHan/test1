package com.example.testapplication.model.dao;


public class ToDoTable {
    public static final String TAB_NAME = "tab_todo";

    public static final String COL_todoId = "todo_id";
    public static final String COL_toDo_title="todo_title";
    public static final String COL_toDo_place="todo_place";
    public static final String COL_toDo_content = "todo_content";
    public static final String COL_starTime = "start_time";
    public static final String COL_endTime = "end_time";
    public static final String COL_color = "color";
    public static final String COL_toDo_sender = "todo_sender";
    public static final String COL_toDo_receiverGroup = "todo_receiver_group";
    public static final String COL_toDO_somethingElse = "toDO_something_else";
    public static final String COL_toDo_ifTop = "todo_if_top";
    public static final String COL_Model_type = "model_type";
    public static final String COL_IfDone = "ifDone";
    public static final String COL_UID = "uid";
    public static final String COL_TYPE = "type";



    public static final  String CREATE_TAB = "create table "
            + TAB_NAME + " ("
            + COL_todoId + " integer primary key autoincrement,"
            + COL_toDo_title + " text,"
            + COL_toDo_place + " text,"
            + COL_toDo_content + " text,"
            + COL_starTime + " text,"
            + COL_endTime + " text,"
            + COL_UID + " text,"
            + COL_color + " integer,"
            + COL_toDo_sender + " text,"
            + COL_toDo_receiverGroup + " text,"
            + COL_toDO_somethingElse + " text,"
            + COL_toDo_ifTop + " integer,"
            + COL_Model_type + " integer,"
            + COL_IfDone + " integer,"
            + COL_TYPE +" integer,"
            + " CONSTRAINT `FK_ID_ST` FOREIGN KEY (" + COL_UID + " ) REFERENCES tab_account(hxid));";
}
