package com.example.baidupostbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.baidupostbar.Utils.HttpUtil;

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

        SetTextWatcher();

        btn_login.setEnabled(true);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = et_account.getText().toString();
                String password = et_password.getText().toString();
                HttpUtil httpUtil = new HttpUtil(getApplicationContext());
                
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                //post
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
    private void SetTextWatcher(){
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
