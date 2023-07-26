package com.example.mymusic.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mymusic.R;
import com.example.mymusic.application.MainApplication;
import com.example.mymusic.bean.User;
import com.example.mymusic.dao.UserDao;

public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    private static final String TAG = "LoginActivity";
    private Button btn_login;    //登录按钮
    private Button btn_register; //注册按钮
    private EditText et_account; //账号输入框
    private EditText et_password;//密码输入框
    private UserDao dao;         //数据库对象
    private int isRemember;      //记住密码
    private CheckBox cb_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    public void initView() {
        //去除标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        dao = MainApplication.getInstance().getUserDataBase().userDao();
        //绑定控件
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        cb_remember = findViewById(R.id.cb_remember);

        et_password.setOnFocusChangeListener(this);

        cb_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == R.id.cb_remember) {
                    if (isChecked) isRemember = 1;//选择了记住密码
                    else isRemember = 0;//没选记住密码
                }
            }
        });

        //匿名内部类方式实现按钮点击事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acc = et_account.getText().toString().trim();
                String pass = et_password.getText().toString().trim();

                User user = dao.find(acc);
                if (user == null) {
                    Toast.makeText(LoginActivity.this, "账号不存在，请重新输入！", Toast.LENGTH_SHORT).show();
                } else {
                    if (user.getPassword().equals(pass)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        //创建意图对象，进行跳转
                        startActivity(intent);

                        //记住密码
                        if (isRemember == 1) {
                            user.setIsRemember(1);
                            dao.update(user);//更新数据库字段 is_remember;
                        }else {
                            user.setIsRemember(0);
                            dao.update(user);
                        }

                        //销毁该活动
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "密码错误，请重新输入！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    //在进行界面跳转的时候，需要获取跳转之后的界面的数据和状态信息，使用onActivityResult可以解决这个问题。
    //注册完后回显数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            //判断结果码是否等于1，等于1则接受返回数据。
            if (requestCode == 1 && resultCode == 1) {
                String name = data.getStringExtra("account");
                String password = data.getStringExtra("password");
                et_account.setText(name);
                et_password.setText(password);
            }
        }
    }

    private boolean needExit = false;

    @Override
    public void onBackPressed() {
        boolean delayedHandler = new Handler(Looper.myLooper()).postDelayed(() -> {
            if (needExit) {
                needExit = false;
            }
        }, 3000);
        if (needExit) {
            finish();
            return;
        }
        needExit = true;
        Toast.makeText(LoginActivity.this, "按错了吗，请再按一次退出哦", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String account = et_account.getText().toString();
        Log.d(TAG, "onFocusChange: " + et_account);
        if (v.getId() == R.id.et_password) {
            //用户已输入手机号，且密码框获得焦点
            User info = dao.find(account);
            if (info != null) {
                if (info.getIsRemember() == 1) {
                    cb_remember.setChecked(true);
                    et_password.setText(info.password);
                }
            }
        }
    }
}