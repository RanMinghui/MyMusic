package com.example.mymusic.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.mymusic.activity.SingerDescActivity;
import com.example.mymusic.adapter.SingerGridAdapter;
import com.example.mymusic.bean.Singer;
import com.example.mymusic.R;
import com.example.mymusic.utils.SingerUtils;

import java.util.List;

public class SingerFragment extends Fragment {
    private View singerView;
    private GridView gv;
    private List<Singer> mDatas;
    private SingerGridAdapter adapter;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        singerView = inflater.inflate(R.layout.fragment_singer, null);
        gv = singerView.findViewById(R.id.singer_gv);
        //1、获得数据源，也就是所有对象的列表
        mDatas = SingerUtils.getSingerList();
        //2、适配器加载数据源
        adapter = new SingerGridAdapter(getContext(), mDatas);
        //3、为布局设置适配器
        gv.setAdapter(adapter);
        setListener();
        return singerView;
    }
    public void setListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Singer singer = mDatas.get(position);
                Intent intent = new Intent(getActivity(), SingerDescActivity.class);
                intent.putExtra("singer", singer);
                startActivity(intent);
            }
        });
    }
}