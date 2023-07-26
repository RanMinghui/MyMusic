package com.example.mymusic.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusic.R;
import com.example.mymusic.bean.Singer;

public class SingerDescActivity extends AppCompatActivity {
    private TextView descTvName, descTvSex, descTvWork, descTvSuccess;
    private ImageView descIvBack, descIvPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer_desc);

        initView();
        //接受上一级页面传来的数据
        Intent intent=  getIntent();
        Singer singer = (Singer) intent.getSerializableExtra("singer");
        //设置显示控件
        descTvName.setText(singer.getName());
        descTvSex.setText(singer.getSex());
        descTvWork.setText(singer.getWork());
        descTvSuccess.setText(singer.getSuccess());
        descIvPic.setImageResource(singer.getImgId());
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        descTvName = findViewById(R.id.desc_tv_name);
        descTvSex = findViewById(R.id.desc_tv_sex);
        descTvWork = findViewById(R.id.desc_tv_work);
        descTvSuccess = findViewById(R.id.desc_tv_success);
        descIvBack = findViewById(R.id.desc_iv_back);
        descIvPic = findViewById(R.id.desc_iv_pic);
        descIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();   //销毁当前的activity
            }
        });
    }

    private boolean needExit = false;
    @Override
    public void onBackPressed() {
        boolean delayedHandler = new Handler(Looper.myLooper()).postDelayed(()->{
            if (needExit){
                needExit = false;
            }
        },3000);
        if (needExit){
            finish();
            return;
        }
        needExit = true;
        Toast.makeText(SingerDescActivity.this,"按错了吗，请再按一次退出哦",Toast.LENGTH_SHORT).show();
    }
}