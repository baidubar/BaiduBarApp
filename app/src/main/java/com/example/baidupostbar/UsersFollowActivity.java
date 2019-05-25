package com.example.baidupostbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.baidupostbar.Adapter.UserFollowAdapter;
import com.example.baidupostbar.bean.EmptyRecyclerView;
import com.example.baidupostbar.bean.UserFollow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UsersFollowActivity extends AppCompatActivity {
    private EmptyRecyclerView recyclerView;
    private String userId;
    private String cookie;
    private List<UserFollow> userFollowList = new ArrayList<>();
    UserFollowAdapter mAdapter;
    private View mEmptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_follow);
        SharedPreferences preferences = getSharedPreferences("theUser",MODE_PRIVATE);
        userId = preferences.getString("user_id","");
        cookie = preferences.getString("cookie", "");
        Log.d("cookie",cookie);
        Log.d("id",userId);
        initView();

        //initListener();
        sendRequestWithOKHttp();
        mAdapter = new UserFollowAdapter(userFollowList,getBaseContext(),cookie,userId);
        recyclerView.setAdapter(mAdapter);
    }

    private void initView(){
        recyclerView = findViewById(R.id.recyclerView);
        mEmptyView = findViewById(R.id.empty_iv);
    }
    private void sendRequestWithOKHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10,TimeUnit.SECONDS)
                            .build();

                    String url = "http://139.199.84.147/mytieba.api/user/"+userId+"/follow";

//                    List<Map<String, String>> list_url = new ArrayList<>();
//                    Map<String, String> map = new HashMap<>();
//                    list_url.add(map);

                    //url = getUrl(url, list_url);

                    Request request = new Request.Builder()
                            .url(url)   //网址有待改动
                            .addHeader("Cookie",cookie)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("建楼消息",responseData);
                    showResponse(responseData);
                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                            if (e instanceof SocketTimeoutException){
                                Toast.makeText(UsersFollowActivity.this,"连接超时",Toast.LENGTH_SHORT).show();
                            }
                            if (e instanceof ConnectException){
                                Toast.makeText(UsersFollowActivity.this,"连接异常",Toast.LENGTH_SHORT).show();
                            }

                            if (e instanceof ProtocolException) {
                                Toast.makeText(UsersFollowActivity.this,"未知异常，请稍后再试",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }
    private String getUrl(String url, List<Map<String, String>> list_url) {
        StringBuilder urlBuilder = new StringBuilder(url);
        for (int i = 0; i < list_url.size(); i++) {
            Map<String, String> params = list_url.get(i);
            if (params != null) {
                Iterator<String> it = params.keySet().iterator();
                StringBuffer sb = null;
                while (it.hasNext()) {
                    String key = it.next();
                    String value = params.get(key);
                    if (sb == null) {
                        sb = new StringBuffer();
                        sb.append("?");
                    } else {
                        sb.append("&");
                    }
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                }
                assert sb != null;
                urlBuilder.append(sb.toString());
            }
        }
        url = urlBuilder.toString();
        return url;
    }
    private void showResponse(final String response){
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String list = jsonObject.getString("concern");
            List<UserFollow> t = new ArrayList<UserFollow>();
            t = gson.fromJson(list, new TypeToken<List<UserFollow>>(){}.getType());

            userFollowList.addAll(t);
            //if (userCommentList != null)
            //{
            Log.d("关注的人列表",userFollowList.toString());
            //}

        } catch (JSONException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable(){
            @Override
            public void run(){ //设置ui
                //recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                LinearLayoutManager manager=new LinearLayoutManager(UsersFollowActivity.this);
                recyclerView.setLayoutManager(manager);
                mAdapter = new UserFollowAdapter(userFollowList,getBaseContext(),cookie,userId);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setEmptyView(mEmptyView);
            }
        });
    }

}
