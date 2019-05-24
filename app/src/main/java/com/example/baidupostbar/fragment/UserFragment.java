package com.example.baidupostbar.fragment;

import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.baidupostbar.ChangeInforActivity;
import com.example.baidupostbar.DetailUserActivity;
import com.example.baidupostbar.LoginActivity;
import com.example.baidupostbar.R;
import com.example.baidupostbar.RegisterInterest;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserFragment extends Fragment {
    View view;
    LinearLayout userDetail;

    private TextView seePc;
    private TextView userName;
    private ImageView headImage;
    private String username;
    private String avater;
    private LinearLayout info;
    private LinearLayout interest;
    private LinearLayout collect;
    private Button btn_exit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("theUser", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        avater = "http://139.199.84.147" + sharedPreferences.getString("avater", "");


        collect = view.findViewById(R.id.pc_collect);
        info = view.findViewById(R.id.pc_info);
        interest = view.findViewById(R.id.pc_interest);
        btn_exit = view.findViewById(R.id.btn_exit);


        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ChangeInforActivity.class);
                startActivity(intent);

            }
        });
        interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), RegisterInterest.class);
                intent.putExtra("type", "3");
                startActivity(intent);
            }
        });

        setfindId();

        userName.setText(username);
        Glide.with(getContext()).load(avater).into(headImage);

        seePc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), DetailUserActivity.class);
                startActivity(intent);
            }
        });
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), DetailUserActivity.class);
                startActivity(intent);
                Log.e("PC", "点击了头像");
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitHttp();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("theUser", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        avater = "http://139.199.84.147" + sharedPreferences.getString("avater", "");

        setfindId();

        userName.setText(username);
        Glide.with(getContext()).load(avater).into(headImage);

    }

    private void setfindId() {
        seePc = view.findViewById(R.id.check_personalPage);
        headImage = view.findViewById(R.id.iv_user);
        userName = view.findViewById(R.id.tv_userName);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userDetail = view.findViewById(R.id.userDetail);
        userDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DetailUserActivity.class);
                startActivity(intent);
            }
        });
    }
    private void exitHttp(){
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("theUser", Context.MODE_PRIVATE);
            String cookie = sharedPreferences.getString("cookie", "");
            String userId = sharedPreferences.getString("user_id","");
            OkHttpClient client = new OkHttpClient();
            FormBody formBody = new FormBody.Builder()
                    .add("id",userId)
                    .build();
            Request request = new Request.Builder()
                    .url("http://139.199.84.147/mytieba.api/logout")
                    .addHeader("Cookie",cookie)
                    .post(formBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Log.e("onFailure","获取数据失败");
                    Toast.makeText(getContext(),"网络请求失败",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.e("rsponseData",responseData);
                    if (response.isSuccessful()){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"退出成功",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.setClass(getContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"服务器请求失败",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"网络请求失败",Toast.LENGTH_LONG).show();
        }
    }
}
