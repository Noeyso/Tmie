package com.example.tmie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tmie.Info.PostInfo;
import com.example.tmie.R;
import com.example.tmie.adapter.BoardAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostListActivity extends AppCompatActivity {
    ArrayList<PostInfo> postInfoList ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlists);

        Intent intent = getIntent();
        String boardName = intent.getStringExtra("boardName");
        System.out.println("게시판 이름 : "+boardName);

        try {
            request();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //상단 툴바
        TextView text = (TextView)findViewById(R.id.toolbar_title);
        text.setText(boardName);

        FloatingActionButton btn = findViewById(R.id.btn_edit);
        btn.setOnClickListener(onClickListener);


    }

    public void request() throws JSONException {
        System.out.println("서버 연결");
        String url = "http://172.30.1.10:3000/api/posts";
        //JSON 형식으로 데이터 통신 진행
        JSONObject jsonObject = new JSONObject();

        //데이터 전송
        final RequestQueue requestQueue = Volley.newRequestQueue(PostListActivity.this);
        final JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(getClass().getName(),"수 성공");
                try {
                    Log.d(getClass().getName(),"응답 : "+response.get(0));

                    postInfoList = new ArrayList<PostInfo>();

                    for(int i=0;i<response.length();i++){
                        String author = "author"+i;
                        JSONObject jsonObject = (JSONObject) response.get(i);
                        System.out.println("id : "+jsonObject.getString("_id"));
                        System.out.println("title : "+jsonObject.getString("title"));
                        String body  = jsonObject.getString("body");
                        if(body.length()>40){
                            body = body.substring(0,40)+"...";
                        }
                        postInfoList.add(new PostInfo(jsonObject.getString("title"),body,author));
                    }

                    ListView listView = (ListView)findViewById(R.id.post_listView);
                    BoardAdapter adapter = new BoardAdapter(getApplicationContext(),postInfoList);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView parent, View view, int position, long id) {
                            System.out.println("position : "+position);
                            Intent intent = new Intent(getApplicationContext(),BoardContentActivity.class);
                            PostInfo obj = postInfoList.get(position);
                            intent.putExtra("title",obj.getTitle());
                            intent.putExtra("body",obj.getContent());
                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(),
//                                    adapter.getItem(position).getTitle(),Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_edit:
                    myStartActivity(MakePostActivity.class);
                    break;
            }
        }
    };
    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        //intent.putExtra("Code", meetingCode);
        //startActivityForResult(intent, 0);
        startActivity(intent);
    }

}
