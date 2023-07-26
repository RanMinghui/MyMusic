package com.example.mymusic.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mymusic.bean.User;
import com.example.mymusic.dao.UserDao;

@Database(entities = {User.class},version = 1,exportSchema = false)
public abstract class UserDataBase extends RoomDatabase {
    //获取该数据库中某张表的持久化对象
    public abstract UserDao userDao();
}
