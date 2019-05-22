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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.baidupostbar.ChangeInforActivity;
import com.example.baidupostbar.DetailUserActivity;
import com.example.baidupostbar.R;

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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("theUser", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        avater = "http://139.199.84.147" + sharedPreferences.getString("avater","");


        collect = view.findViewById(R.id.pc_collect);
        info = view.findViewById(R.id.pc_info);
        interest = view.findViewById(R.id.pc_interest);


        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ChangeInforActivity.class);
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
                Log.e("PC","点击了头像");
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("theUser", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        avater = "http://139.199.84.147" + sharedPreferences.getString("avater","");

        setfindId();

        userName.setText(username);
        Glide.with(getContext()).load(avater).into(headImage);

    }

    private void setfindId() {
        seePc = view.findViewById(R.id.check_personalPage);
        headImage = view.findViewById(R.id.iv_user);
        userName = view.findViewById(R.id.tv_userName);
    }
        public void onActivityCreated (@Nullable Bundle savedInstanceState){
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

}
