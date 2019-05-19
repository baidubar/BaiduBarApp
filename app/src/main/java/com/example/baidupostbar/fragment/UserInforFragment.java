package com.example.baidupostbar.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baidupostbar.DetailUserActivity;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.Search;

import java.util.ArrayList;

public class UserInforFragment extends Fragment {
    View view;


    private String gender;
    private String birthday;

    private TextView tv_gender;
    private TextView tv_birthday;
    private TextView tv_interest;
    private String interests;
    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        gender = ((DetailUserActivity) activity).getGender();//通过强转成宿主activity，就可以获取到传递过来的数据
        birthday = ((DetailUserActivity) activity).getBirthday();
        interests = ((DetailUserActivity) activity).getLabel();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_infor,container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_birthday = view.findViewById(R.id.tv_birthDay);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_interest = view.findViewById(R.id.tv_interest);

        if(gender.equals("0")){
            tv_gender.setText("男");
        }else if (gender.equals("1")){
            tv_gender.setText("女");
        }else if(gender.equals("2")){
            tv_gender.setText("保密");
        }
        tv_birthday.setText(birthday);
        tv_interest.setText(interests);

    }
}
