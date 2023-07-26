package com.example.mymusic.activity;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusic.R;
import com.example.mymusic.fragment.SingerFragment;
import com.example.mymusic.fragment.SongFragement;
import com.example.mymusic.service.MusicService;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MusicActivity";
    //定义歌曲名称的数组
    public String[] musicName = {"邓紫棋——光年之外", "蔡健雅——红色高跟鞋", "Taylor Swift——Love Story",
            "朴树——平凡之路", "田馥甄——小幸运", "周杰伦——七里香", "林俊杰——江南"};
    private static SeekBar sb;//定义进度条
    private static TextView tv_progress, tv_total, name_song;//定义开始和总时长,歌曲名控件
    private ObjectAnimator animator;//定义旋转的动画
    private MusicService.MusicControl musicControl;//音乐控制类
    private Button play;        //播放按钮
    private Button pause;       //暂停按钮
    private Button con;         //继续播放按钮
    private Button pre;         //上一首按钮
    private Button next;        //下一首按钮
    private ImageView exit;        //退出按钮
    private ImageView iv_music; //歌手图片框
    Intent intent1, intent2;    //定义两个意图
    MyServiceConn conn;         //服务连接
    private boolean isUnbind = false;//记录服务是否被解绑
    public int change = 0;      //记录下标的变化值
    boolean over = false;       //下一首的控制

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        //去除标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //获得意图
        intent1 = getIntent();
        //初始化
        initView();

        //todo 模式选择 单曲循环 列表循环 随机
        //todo 前台服务 播放
        //todo 从本地中读取播放

        start();//进入界面播放
    }

    /** 异步onServiceConnected没调用使musicControl = null 报错空指针*/
    //绑定服务时异步回调  这里开线程等待onServiceConnected绑定 返回musicControl的结果
    //可以考虑用监视者模式查看回调
    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        Thread.sleep(20);
                        if (musicControl != null){
                            int position = parseInt(intent1.getStringExtra("position"));
                            Log.d(TAG, "onCreate: "+position);
                            if (musicControl == null){
                                Log.d(TAG, "musicControl = null ");
                                Toast.makeText(MusicActivity.this,"musicControl = null",Toast.LENGTH_SHORT).show();
                            }else {
                                play.setVisibility(View.INVISIBLE);
                                musicControl.play(position);
                                runOnUiThread(()->animator.start());
                            }
                            return;
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    //初始化
    private void initView() {
        //依次绑定控件
        tv_progress = findViewById(R.id.tv_progress);
        tv_total = findViewById(R.id.tv_total);
        sb = findViewById(R.id.sb);
        name_song = findViewById(R.id.song_name);
        iv_music = findViewById(R.id.iv_music);

        play = findViewById(R.id.btn_play);
        pause = findViewById(R.id.btn_pause);
        con = findViewById(R.id.btn_continue_play);
        pre = findViewById(R.id.btn_pre);
        next = findViewById(R.id.btn_next);
        exit = findViewById(R.id.btn_exit);

        //依次设置监听器
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        con.setOnClickListener(this);
        pre.setOnClickListener(this);
        next.setOnClickListener(this);
        exit.setOnClickListener(this);

        intent2 = new Intent(this, MusicService.class);
        conn = new MyServiceConn();//创建服务连接对象
        Log.d(TAG, "initView: conn");

        bindService(intent2, conn, BIND_AUTO_CREATE);//绑定服务

        //从歌曲列表传过来的歌曲名
        String name = intent1.getStringExtra("name");
        //设置歌曲名显示
        name_song.setText(name);
        //定义歌曲列表传过来的下标position
        String position = intent1.getStringExtra("position");
        //将字符串转化为整型i
        int i = parseInt(position);
        //图像框设置为frag1里面的图标数组，下标为i
        iv_music.setImageResource(SongFragement.icons[i]);
        //为滑动条添加事件监听
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //当滑动条到末端时，将message对象发送出去
                /**坑，当结束时会触发2次！！!*/
                //Log.d(TAG, "onProgressChanged: seekBar.getMax():"+sb.getMax());
                //Log.d(TAG, "onProgressChanged: progress:"+progress);
                if (progress == sb.getMax() && !over){
                    over=true;
                    next.callOnClick();
                }else {
                    over=false;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//滑动条开始滑动时调用
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//滑动条停止滑动时调用
                //根据拖动的进度改变音乐播放进度
                int progress = seekBar.getProgress();//获取seekBar的进度
                musicControl.seekTo(progress);//改变播放进度
            }
        });

        //围绕中心点旋转的属性动画
        animator = ObjectAnimator.ofFloat(iv_music, "rotation", 0f, 360.0f);
        animator.setDuration(10000);//动画旋转一周的时间为10秒
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(-1);//-1表示设置动画无限循环

    }

    //歌曲进度条的消息机制
    public static Handler handler = new Handler(Looper.myLooper()) {//创建消息处理器对象
        //在主线程中处理从子线程发送过来的消息
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();//获取从子线程发送过来的音乐播放进度
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");


            //Log.d(TAG, "handleMessage: sb.getMax():"+sb.getMax());
            sb.setMax(duration);

            //Log.d(TAG, "handleMessage: sb.getProgress():"+sb.getProgress());
            sb.setProgress(currentPosition);

            //歌曲总时长，单位为毫秒
            int minute = duration / 1000 / 60;
            int second = duration / 1000 % 60;
            String strMinute = null;
            String strSecond = null;
            if (minute < 10) {//如果歌曲的时间中的分钟小于10
                strMinute = "0" + minute;//在分钟的前面加一个0
            } else {
                strMinute = minute + "";
            }
            if (second < 10) {//如果歌曲中的秒钟小于10
                strSecond = "0" + second;//在秒钟前面加一个0
            } else {
                strSecond = second + "";
            }
            tv_total.setText(strMinute + ":" + strSecond);
            //歌曲当前播放时长
            minute = currentPosition / 1000 / 60;
            second = currentPosition / 1000 % 60;
            if (minute < 10) {//如果歌曲的时间中的分钟小于10
                strMinute = "0" + minute;//在分钟的前面加一个0
            } else {
                strMinute = minute + " ";
            }
            if (second < 10) {//如果歌曲中的秒钟小于10
                strSecond = "0" + second;//在秒钟前面加一个0
            } else {
                strSecond = second + " ";
            }
            tv_progress.setText(strMinute + ":" + strSecond);
        }
    };

    //用于实现连接服务
    class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: MusicService.MusicControl created");
            /**可以用CountDownLatch转换成同步操作**/
            musicControl = (MusicService.MusicControl) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    //未解绑则解绑
    private void unbind(boolean isUnbind) {
        if (!isUnbind) {//判断服务是否被解绑
            musicControl.pausePlay();//暂停播放音乐
            unbindService(conn);//解绑服务
        }
    }

    @Override
    public void onClick(View v) {
        //获取歌曲名的下标字符串
        String index = intent1.getStringExtra("position");
        //将字符串转为整数
        int i = parseInt(index);

        switch (v.getId()) {
            case R.id.btn_play://播放按钮点击事件
                Log.d(TAG, "onClick: play");
                play.setVisibility(View.INVISIBLE);
                musicControl.play(i);
                animator.start();
                break;

            //这里musicName.length-1表示的最后一首歌的下标，即歌曲总数-1
            case R.id.btn_pre://播放上一首
                if ((i + change) < 1) {
                    change = musicName.length - 1 - i;
                    iv_music.setImageResource(SongFragement.icons[i + change]);
                    name_song.setText(musicName[i + change]);
                    musicControl.play(i + change);
                    pause.setVisibility(View.VISIBLE);
                    animator.start();
                    break;
                } else {
                    change--;
                    iv_music.setImageResource(SongFragement.icons[i + change]);
                    name_song.setText(musicName[i + change]);
                    musicControl.play(i + change);
                    pause.setVisibility(View.VISIBLE);
                    animator.start();
                    break;
                }

            case R.id.btn_next://播放下一首
                if ((i + change) == musicName.length - 1) {
                    change = -i;
                    iv_music.setImageResource(SongFragement.icons[i + change]);
                    name_song.setText(musicName[i + change]);
                    musicControl.play(i + change);
                    pause.setVisibility(View.VISIBLE);
                    animator.start();
                    break;
                } else {
                    change++;
                    iv_music.setImageResource(SongFragement.icons[i + change]);
                    name_song.setText(musicName[i + change]);
                    musicControl.play(i + change);
                    pause.setVisibility(View.VISIBLE);
                    animator.start();
                    break;
                }

            case R.id.btn_pause://暂停按钮点击事件
                pause.setVisibility(View.INVISIBLE);
                con.setVisibility(View.VISIBLE);
                musicControl.pausePlay();
                animator.pause();
                break;

            case R.id.btn_continue_play://继续播放按钮点击事件
                con.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                musicControl.continuePlay();
                animator.start();
                break;

            case R.id.btn_exit://退出按钮点击事件
                unbind(isUnbind);
                isUnbind = true;
                finish();
                break;
            default:break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind(isUnbind);//解绑服务
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
        Toast.makeText(MusicActivity.this,"按错了吗，请再按一次退出哦",Toast.LENGTH_SHORT).show();
    }
}