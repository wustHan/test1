package com.example.testapplication.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.testapplication.model.dao.UserAccountTable;

import androidx.annotation.Nullable;

public class UserAccountDB extends SQLiteOpenHelper {
    public UserAccountDB(@Nullable Context context) {
        super(context,"account.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserAccountTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
