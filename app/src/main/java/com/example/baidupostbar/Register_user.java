package com.example.baidupostbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register_user extends AppCompatActivity {

    EditText et_account;
    EditText et_password;
    EditText et_confirmPassword;
    Button btn_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        et_account = findViewById(R.id.et_account);
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        et_password = findViewById(R.id.et_password);
        btn_next = findViewById(R.id.btn_next);
        et_confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_account.getText().toString().equals("")&&!et_confirmPassword.getText().toString().equals("")&&!et_password.getText().toString().equals("")){
                    btn_next.setEnabled(true);
                    btn_next.setBackgroundColor(Color.parseColor("#6495ED"));
                }else {
                    btn_next.setEnabled(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        btn_next.setBackground(getResources().getDrawable(R.drawable.btn_rounded));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_account.getText().toString();
                String password = et_password.getText().toString();
                String rePassword = et_confirmPassword.getText().toString();
                if(checkName(name)){
                    if(password.equals(rePassword)){
                        //post
                        Intent intent = new Intent();
                        intent.setClass(Register_user.this,RegisterInfor.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(Register_user.this,"两次密码不一致",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(Register_user.this,"用户名不能含有空白字符",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    public static boolean checkName(String nickname) {
        Pattern pattern = Pattern.compile("^[\\S\u4e00-\u9fa5]{1,12}$");
        Matcher matcher = pattern.matcher(nickname);
        return matcher.matches();
    }
}
