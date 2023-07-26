package com.example.mymusic.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mymusic.R;
import com.example.mymusic.activity.MusicActivity;
import com.example.mymusic.service.MusicService;

public class SongFragement extends Fragment {
    //声明视图变量view
    private View view;
    //在这里添加歌曲名
    public String[] name={"邓紫棋——光年之外","蔡健雅——红色高跟鞋","Taylor Swift——Love Story",
            "朴树——平凡之路","田馥甄——小幸运","周杰伦——七里香","林俊杰——江南"};
    //在这里添加歌曲图片
    public static int[] icons={R.drawable.music0,R.drawable.music1,R.drawable.music2,
            R.drawable.music3,R.drawable.music4,R.drawable.music5,R.drawable.music6};
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view=inflater.inflate(R.layout.music_list,null);
        //1、创建并绑定列表
        ListView listView = view.findViewById(R.id.lv);
        //2、创建适配器对象
        MyBaseAdapter adapter = new MyBaseAdapter();
        //3、给列表设置适配器
        listView.setAdapter(adapter);
        //设置列表条目监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //创建Intent对象，启动音乐播放界面
                Intent intent1 = new Intent(SongFragement.this.getContext(), MusicActivity.class);
                //将数据存入Intent对象，利用键值对
                intent1.putExtra("name", name[position]);
                intent1.putExtra("position", String.valueOf(position));
                //开启意图，进行跳转
                startActivity(intent1);

                /*Intent intent2 = new Intent(SongFragement.this.getContext(), MusicService.class);
                //将数据存入Intent对象，利用键值对
                Bundle bundle = new Bundle();
                bundle.putString("position", String.valueOf(position));
                intent2.putExtra("bundle",bundle);
                //开启意图，进行跳转
                startActivity(intent2);*/
            }
        });
        return view;
    }
    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount(){return  name.length;}
        @Override
        public Object getItem(int i){return name[i];}
        @Override
        public long getItemId(int i){return i;}

        @Override
        public View getView(int i , View convertView, ViewGroup parent) {
            //绑定视图，并且显示歌曲名和歌曲图片
            //LayoutInflater是用来将layout.xml布局文件添加到指定View中，或者是将layout.xml布局文件转化为对应的View对象
            //LayoutInflater.from(context);
            View view = View.inflate(SongFragement.this.getContext(), R.layout.item_music, null);
            TextView songName = view.findViewById(R.id.song_name);
            ImageView songPic = view.findViewById(R.id.song_pic);
            songName.setText(name[i]);
            songPic.setImageResource(icons[i]);
            return view;
        }
    }
}