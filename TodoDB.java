package com.example.testapplication.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.testapplication.model.dao.ToDoTable;

import androidx.annotation.Nullable;

public class TodoDB extends SQLiteOpenHelper {
    public  TodoDB(Context context){
        super(context, "toDo6.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ToDoTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
