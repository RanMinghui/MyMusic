package com.example.mymusic.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusic.fragment.SingerFragment;
import com.example.mymusic.fragment.SingerRecycleFragment;
import com.example.mymusic.fragment.SongFragement;
import com.example.mymusic.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView menu1,menu2;
    private FragmentManager fm;
    private FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //必须重写的onCreate方法
        super.onCreate(savedInstanceState);
        //设定对应的布局文件
        setContentView(R.layout.activity_main);
        //去除标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //绑定控件
        menu1 = findViewById(R.id.menu1);
        menu2 = findViewById(R.id.menu2);
        //设置监听器
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        //获得布局管理器
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        //默认情况下显示frag1（音乐列表界面）
        ft.replace(R.id.content, new SongFragement());
        ft.commit();
    }
    @Override
    //点击事件
    public void onClick(View v){
        ft = fm.beginTransaction();
        //根据控件id来切换页面
        switch (v.getId()){
            case R.id.menu1:
                //如果是menu1，则音乐列表界面frag1替换content
                ft.replace(R.id.content, new SongFragement());
                break;
            case R.id.menu2:
                //如果是menu2，则歌手界面frag2替换content
                ft.replace(R.id.content, new SingerFragment());//SingerFragment  SingerRecycleFragment
                break;
            default:
                break;
        }
        ft.commit();
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
        Toast.makeText(MainActivity.this,"按错了吗，请再按一次退出哦",Toast.LENGTH_SHORT).show();
    }
}