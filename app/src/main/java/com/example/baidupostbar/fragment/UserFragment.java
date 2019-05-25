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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.baidupostbar.ChangeInforActivity;
import com.example.baidupostbar.DetailUserActivity;
import com.example.baidupostbar.LoginActivity;
import com.example.baidupostbar.PostCollectActivity;
import com.example.baidupostbar.R;
import com.example.baidupostbar.RegisterInterest;
import com.example.baidupostbar.UserPostActivity;
import com.example.baidupostbar.UsersBarActivity;
import com.example.baidupostbar.UsersFansActivity;
import com.example.baidupostbar.UsersFollowActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

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

    private String userId;
    private String cookie;
    private RelativeLayout follow, fans, bar, post;
    private TextView tv_followNum, tv_fansNum, tv_barNum, tv_postNum;
    int fans_num;
    int post_num;
    int follow_num;
    int bar_num;

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

        avater = "http://139.199.84.147" + sharedPreferences.getString("avater", "");
        userId = sharedPreferences.getString("user_id", "");
        cookie = sharedPreferences.getString("cookie", "");


        collect = view.findViewById(R.id.pc_collect);
        info = view.findViewById(R.id.pc_info);
        interest = view.findViewById(R.id.pc_interest);
        btn_exit = view.findViewById(R.id.btn_exit);
        follow = view.findViewById(R.id.follow);
        fans = view.findViewById(R.id.fans);
        bar = view.findViewById(R.id.bar);
        post = view.findViewById(R.id.post);
        tv_barNum = view.findViewById(R.id.tv_barNum);
        tv_fansNum = view.findViewById(R.id.tv_fansNum);
        tv_followNum = view.findViewById(R.id.tv_followNum);
        tv_postNum = view.findViewById(R.id.tv_postNum);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UsersFollowActivity.class);
                startActivity(intent);

            }
        });
        fans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UsersFansActivity.class);
                startActivity(intent);
            }
        });
        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UsersBarActivity.class);
                startActivity(intent);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserPostActivity.class);
                startActivity(intent);
            }
        });

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
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), PostCollectActivity.class);
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

    private void sendRequestWithOKHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .build();

                    String url = "http://139.199.84.147/mytieba.api/user/" + userId + "/info";

//                    List<Map<String, String>> list_url = new ArrayList<>();
//                    Map<String, String> map = new HashMap<>();
//                    list_url.add(map);

                    //url = getUrl(url, list_url);

                    Request request = new Request.Builder()
                            .url(url)   //网址有待改动
                            .addHeader("Cookie", cookie)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("个人信息", responseData);
                    showResponse(responseData);
                } catch (Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                            if (e instanceof SocketTimeoutException) {
                                Toast.makeText(getContext(), "连接超时", Toast.LENGTH_SHORT).show();
                            }
                            if (e instanceof ConnectException) {
                                Toast.makeText(getContext(), "连接异常", Toast.LENGTH_SHORT).show();
                            }

                            if (e instanceof ProtocolException) {
                                Toast.makeText(getContext(), "未知异常，请稍后再试", Toast.LENGTH_SHORT).show();
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
            fans_num = jsonObject.getInt("follower_number");
            post_num = jsonObject.getInt("posts");
            follow_num = jsonObject.getInt("concern_number");
            bar_num = jsonObject.getInt("watched_bar_number");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){ //设置ui
                tv_barNum.setText(String.valueOf(bar_num));
                tv_fansNum.setText(String.valueOf(fans_num));
                tv_followNum.setText(String.valueOf(follow_num));
                tv_postNum.setText(String.valueOf(post_num));
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
        sendRequestWithOKHttp();
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

    private void exitHttp() {
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("theUser", Context.MODE_PRIVATE);
            String cookie = sharedPreferences.getString("cookie", "");
            String userId = sharedPreferences.getString("user_id", "");
            OkHttpClient client = new OkHttpClient();
            FormBody formBody = new FormBody.Builder()
                    .add("id", userId)
                    .build();
            Request request = new Request.Builder()
                    .url("http://139.199.84.147/mytieba.api/logout")
                    .addHeader("Cookie", cookie)
                    .post(formBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Log.e("onFailure", "获取数据失败");
                    Toast.makeText(getContext(), "网络请求失败", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.e("rsponseData", responseData);
                    if (response.isSuccessful()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "退出成功", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.setClass(getContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "服务器请求失败", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "网络请求失败", Toast.LENGTH_LONG).show();
        }
    }
}
