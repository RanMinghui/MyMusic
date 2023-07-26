package com.example.mymusic.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymusic.bean.User;

//数据库中的CRUD操作
@SuppressLint("Range")
@Dao
public interface UserDao {

    //添加用户
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(User user);

    //删除用户
    @Delete
    void deleteUser(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(User user);

    @Query("select * from User where account = :account")
    User find(String account);
}
