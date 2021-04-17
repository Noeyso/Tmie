package com.example.tmie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tmie.Info.PostInfo;
import com.example.tmie.R;

import java.util.ArrayList;

public class BoardAdapter extends BaseAdapter {

    Context bContext = null;
    LayoutInflater layoutInflater =null;
    ArrayList<PostInfo> postInfoList;

    public BoardAdapter(Context context, ArrayList<PostInfo> data){
        bContext = context;
        postInfoList = data;
        layoutInflater = LayoutInflater.from(bContext);
    }
    @Override
    public int getCount() {
        return postInfoList.size();
    }

    @Override
    public PostInfo getItem(int position) {
        return postInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.post_item,null);

        TextView title = (TextView)view.findViewById(R.id.post_title);
        TextView content = (TextView)view.findViewById(R.id.post_content);
        TextView author = (TextView)view.findViewById(R.id.post_author);

        title.setText(postInfoList.get(position).getTitle());
        content.setText(postInfoList.get(position).getContent());
        author.setText(postInfoList.get(position).getAuthor());
        return view;
    }
}