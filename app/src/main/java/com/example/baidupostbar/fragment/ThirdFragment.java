package com.example.baidupostbar.fragment;

import android.content.Intent;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    private List<UserComment> userCommentList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_third,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://139.199.84.147/mytieba.api/user/i/floor_comment-info")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
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
            String list = jsonObject.getString("floor_comment_info");
            String num = jsonObject.getString("floor_comment_number");
            List<UserComment> t = new ArrayList<UserComment>();
            t = gson.fromJson(list, new TypeToken<List<UserComment>>(){}.getType());

            userCommentList.addAll(t);
            if (userCommentList != null)
            {
                Log.d("listhhh",userCommentList.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                //设置ui

            }
        });
    }
}
