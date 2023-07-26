package com.example.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymusic.R;
import com.example.mymusic.bean.Singer;

import java.util.List;

public class SingerGridAdapter extends BaseAdapter {
    private Context context;
    private List<Singer> mDatas;

    public SingerGridAdapter(Context context, List<Singer> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView是一个作为缓存的view，通过使用这个缓存可以替换掉用Inflater加载组件这一步。
        //用ViewHolder就是替换掉findViewById。
        ViewHodler viewHodler = null;
        if (convertView == null) {
            //将item布局转换成view视图
            convertView = LayoutInflater.from(context).inflate(R.layout.item_singergrid,null);
            viewHodler = new ViewHodler(convertView);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        // 获取指定位置的数据
        Singer singer = mDatas.get(position);
        viewHodler.iv.setImageResource(singer.getImgId());
        viewHodler.tv.setText(singer.getName());
        return convertView;
    }

    class ViewHodler {
        ImageView iv; //子项的图片框
        TextView tv;  //子项的文本框
        public ViewHodler(View view) {
            iv = view.findViewById(R.id.item_grid_iv);
            tv = view.findViewById(R.id.item_grid_tv);
        }
    }
}
