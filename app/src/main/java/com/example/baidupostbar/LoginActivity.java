package com.example.baidupostbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baidupostbar.Utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;

public class LoginActivity extends AppCompatActivity {
    EditText et_account;
    EditText et_password;
    Button btn_login;
    Button btn_register;
    Button btn_forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        btn_forgetPassword = findViewById(R.id.btn_forgetPassword);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        SetonClickListener();
        SetTextWatcher();

    }
    private void SetonClickListener(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = et_account.getText().toString();
                String password = et_password.getText().toString();
                //post
                HttpUtil httpUtil = new HttpUtil();
                FormBody formBody = new FormBody.Builder()
                        .add("username",account)
                        .add("password",password)
                        .build();
                String responseData = httpUtil.PostUtils("http://139.199.84.147/mytieba.api/login",formBody);
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterEmail.class);
                startActivity(intent);
            }
        });
    }
    private void SetTextWatcher() {
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                judgeStateAndChange();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                judgeStateAndChange();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void judgeStateAndChange(){
        if(!et_password.getText().toString().equals("")&&!et_account.getText().toString().equals("")){
            btn_login.setEnabled(true);
            btn_login.setBackgroundColor(Color.parseColor("#6495ED"));
        }else {
            btn_login.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btn_login.setBackground(getResources().getDrawable(R.drawable.btn_rounded));
            }

        }
    }
    private void praseWithJsonData(String jsonData)  {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                String name = jsonObject.getString("username");
                String user_id = jsonObject.getString("user_id");
                String avatar = jsonObject.getString("avatar");
                String username = jsonObject.getString("username");
                SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username",name);
                editor.putString("user_id",user_id);
                editor.putString("avatar",avatar);
                editor.putString("username",username);
                editor.apply();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
