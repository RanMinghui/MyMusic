package com.example.mymusic.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.mymusic.R;
import com.example.mymusic.activity.SingerDescActivity;
import com.example.mymusic.adapter.SingerGridAdapter;
import com.example.mymusic.adapter.SingerRecycleAdapter;
import com.example.mymusic.bean.Singer;
import com.example.mymusic.utils.RecyclerExtras;
import com.example.mymusic.utils.SingerUtils;

import java.util.List;

public class SingerRecycleFragment extends Fragment implements RecyclerExtras.OnItemClickListener {
    private static final String TAG = "SingerRecycleFragment";
    private View singerView;
    private RecyclerView rv;
    private List<Singer> mDatas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        singerView = inflater.inflate(R.layout.fragment_singer_recycle,null);
        rv = singerView.findViewById(R.id.rv_singer);

        //1、获得数据源，也就是所有对象的列表
        mDatas = SingerUtils.getSingerList();

        //new GridLayoutManager(this,2);
        //StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),2);
        rv.setLayoutManager(manager);
        SingerRecycleAdapter adapter = new SingerRecycleAdapter(getActivity(), mDatas);
        adapter.setOnItemClickListener(this);
        rv.setAdapter(adapter);

        return singerView;
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: "+"position = "+position);
        Singer singer = mDatas.get(position);
        Intent intent = new Intent(getActivity(), SingerDescActivity.class);
        intent.putExtra("singer", singer);
        startActivity(intent);
    }
}