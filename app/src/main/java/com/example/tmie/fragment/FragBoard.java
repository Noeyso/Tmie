package com.example.tmie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.tmie.activity.MakePostActivity;

import com.example.tmie.R;
import com.example.tmie.activity.PostListActivity;

public class FragBoard extends Fragment {
    private View view;
    private int boardArray[]={R.id.board_book,R.id.board_college,R.id.board_freeboard,R.id.board_homework,
            R.id.board_job,R.id.board_notice,R.id.board_shareInfo};

    public FragBoard() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_board,container,false);

        //상단 툴바
        TextView text = view.findViewById(R.id.toolbar_title);
        text.setText("게시판");



        for(int i=0;i<boardArray.length;i++){
            LinearLayout ly = view.findViewById(boardArray[i]);
            ly.setOnClickListener(onClickListener);
        }

        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.board_book:
                    myStartActivity(PostListActivity.class, "책방");
                    break;
                case R.id.board_college:
                    myStartActivity(PostListActivity.class, "학교생활");
                    break;
                case R.id.board_freeboard:
                    myStartActivity(PostListActivity.class, "자유게시판");
                    break;
                case R.id.board_homework:
                    myStartActivity(PostListActivity.class, "과제");
                    break;
                case R.id.board_job:
                    myStartActivity(PostListActivity.class, "취업및진로");
                    break;
                case R.id.board_notice:
                    myStartActivity(PostListActivity.class, "공지");
                    break;
                case R.id.board_shareInfo:
                    myStartActivity(PostListActivity.class, "정보공유");
                    break;
            }
        }
    };
    private void myStartActivity(Class c,String boardName) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra("boardName", boardName);
        //startActivityForResult(intent, 0);
        startActivity(intent);
    }
}
