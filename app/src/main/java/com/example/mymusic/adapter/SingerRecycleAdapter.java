package com.example.mymusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.R;
import com.example.mymusic.activity.MainActivity;
import com.example.mymusic.activity.SingerDescActivity;
import com.example.mymusic.bean.Singer;
import com.example.mymusic.fragment.SingerFragment;
import com.example.mymusic.fragment.SingerRecycleFragment;
import com.example.mymusic.utils.RecyclerExtras;

import java.util.List;

public class SingerRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "SingerRecycleAdapter";
    private Context context;
    private List<Singer> mDatas;
    //private ItemHolder mHolder;

    public SingerRecycleAdapter(Context context, List<Singer> mDatas){
        this.context = context;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(context).inflate(R.layout.item_singergrid,parent,false);
       return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemHolder mHolder = (ItemHolder) holder;
        mHolder.iv.setImageResource(mDatas.get(position).getImgId());
        mHolder.tv.setText(mDatas.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        LinearLayout item;
        ImageView iv; //子项的图片框
        TextView tv;  //子项的文本框

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.itm_singer);
            iv = itemView.findViewById(R.id.item_grid_iv);
            tv = itemView.findViewById(R.id.item_grid_tv);
        }
    }

    private RecyclerExtras.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(RecyclerExtras.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
