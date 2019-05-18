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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.example.baidupostbar.DetailUserActivity;
import com.example.baidupostbar.R;

public class UserFragment extends Fragment {
    View view;
    LinearLayout userDetail;

    private TextView seePc;
    private ImageView headImage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setfindId();
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

    private void setfindId() {
        seePc = view.findViewById(R.id.check_personalPage);
        headImage = view.findViewById(R.id.iv_user);
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
