package com.example.mymusic.application;

import android.app.Application;

import androidx.room.Room;

import com.example.mymusic.database.UserDataBase;

public class MainApplication extends Application {
    private static MainApplication mapp;
    private UserDataBase userDataBase;

    public static MainApplication getInstance(){
        return mapp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mapp = this;
        userDataBase = Room.databaseBuilder(mapp,UserDataBase.class,"User")
                .addMigrations()//允许迁移数据库
                .allowMainThreadQueries()//允许主线程操作数据库
                .build();
    }
    public UserDataBase getUserDataBase(){
        return userDataBase;
    }
}
