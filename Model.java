package com.example.testapplication.model;

import android.content.Context;

import com.example.testapplication.model.bean.UserInfo;

import com.example.testapplication.model.dao.TodoTableDao;

import com.example.testapplication.model.dao.UserAccountDao;
import com.example.testapplication.model.db.DBManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Model {
    private Context mContext;
    private DBManager dbManager;
    private  static Model model=new Model();
    private  UserAccountDao userAccountDao;
    private TodoTableDao toDoTableDao;

    private ExecutorService executors = Executors.newCachedThreadPool();

    private Model(){

    }
    public static Model getInstance(){
        return model;
    }

    public void init(Context context) {
        mContext = context;
        userAccountDao = new UserAccountDao(mContext);

        toDoTableDao = new TodoTableDao(mContext);

    }

    //获取全局线程池
    public ExecutorService getGlobalThreadPool(){
        return executors;
    }

    public void loginSuccess(UserInfo account) {
        if(account==null){return ;}

        if(dbManager!=null){
            dbManager.close();
        }
        dbManager = new DBManager(mContext, account.getName());
    }

    public DBManager getDbManager(){
        return dbManager;
    }

    public UserAccountDao getUserAccountDao(){
        return userAccountDao;
    }

    public TodoTableDao getTodoTableDao(){return toDoTableDao;}


}
