package com.example.mymusic.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {
    //无参构造函数
    @Ignore
    public User() {
    }
    //有参构造函数
    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    //用户账号和用户密码
    @PrimaryKey
    @NonNull
    public String account;
    public String password;

    public int isRemember = 0;//默认不记住

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsRemember(int isRemember){this.isRemember = isRemember;}

    public int getIsRemember(){return isRemember;}
}
