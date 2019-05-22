package com.example.baidupostbar.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baidupostbar.ListUserAttention;
import com.example.baidupostbar.ListUserCommentActivity;
import com.example.baidupostbar.ListUserLikeActivity;
import com.example.baidupostbar.ListUserReplyActivity;
import com.example.baidupostbar.R;
import com.example.baidupostbar.SearchActivity;
import com.example.baidupostbar.bean.UserComment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class ThirdFragment extends Fragment {
    View view;
    CircleImageView cv0;
    CircleImageView cv1;
    CircleImageView cv2;
    CircleImageView cv3;
    TextView tv_search;
    LinearLayout setFloor;
    LinearLayout reply;
    LinearLayout like;
    LinearLayout attention;
    private String userId;
    private String cookie;
    private List<UserComment> userCommentList = new ArrayList<>();
    private int sfNum,likeNum,replyNum,attentionNum;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_third,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences preferences = getContext().getSharedPreferences("theUser",MODE_PRIVATE);
        userId = preferences.getString("user_id","");
        cookie = preferences.getString("cookie", "");
        Log.d("艾迪",userId);
        initView();
        initListener();
        sendRequestWithOKHttp();
    }
    public void initView(){
        cv0 = view.findViewById(R.id.iv_redCircle0);
        cv1 = view.findViewById(R.id.iv_redCircle1);
        cv2 = view.findViewById(R.id.iv_redCircle2);
        cv3 = view.findViewById(R.id.iv_redCircle3);
        cv0.setVisibility(View.INVISIBLE);
        cv1.setVisibility(View.INVISIBLE);
        cv2.setVisibility(View.INVISIBLE);
        cv3.setVisibility(View.INVISIBLE);
        tv_search = view.findViewById(R.id.tv_search);
        setFloor = view.findViewById(R.id.setFloor);
        reply = view.findViewById(R.id.reply);
        like = view.findViewById(R.id.like);
        attention = view.findViewById(R.id.attention);
    }
    public void initListener(){
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        setFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListUserCommentActivity.class);
                startActivity(intent);
            }
        });
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListUserReplyActivity.class);
                startActivity(intent);
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListUserLikeActivity.class);
                startActivity(intent);
            }
        });
        attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListUserAttention.class);
                startActivity(intent);
            }
        });
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

                    String url = "http://139.199.84.147/mytieba.api/user/" + userId + "/info";

                    List<Map<String, String>> list_url = new ArrayList<>();
                    Map<String, String> map = new HashMap<>();
                    map.put("type", "message");
                    list_url.add(map);

                    url = getUrl(url, list_url);

                    Request request = new Request.Builder()
                            .url(url)   //网址有待改动
                            .addHeader("Cookie",cookie)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("消息页显示红点",responseData);
                    showResponse(responseData);
                }catch (Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                            if (e instanceof SocketTimeoutException){
                                Toast.makeText(getContext(),"连接超时",Toast.LENGTH_SHORT).show();
                            }
                            if (e instanceof ConnectException){
                                Toast.makeText(getContext(),"连接异常",Toast.LENGTH_SHORT).show();
                            }

                            if (e instanceof ProtocolException) {
                                Toast.makeText(getContext(),"未知异常，请稍后再试",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }
    private void showResponse(final String response){
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(response);
            sfNum = jsonObject.getInt("floor_message");
            replyNum = jsonObject.getInt("reply_message");
            likeNum = jsonObject.getInt("praise_message");
            attentionNum = jsonObject.getInt("follower_message");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                //设置ui
                if (sfNum != 0)
                    cv0.setVisibility(View.VISIBLE);
                if (replyNum != 0)
                    cv1.setVisibility(View.VISIBLE);
                if (likeNum != 0)
                    cv2.setVisibility(View.VISIBLE);
                if (attentionNum != 0)
                    cv3.setVisibility(View.VISIBLE);
            }
        });
    }
    private String getUrl(String url, List<Map<String, String>> list_url) {
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
                url += sb.toString();
            }
        }
        return url;
    }
}
