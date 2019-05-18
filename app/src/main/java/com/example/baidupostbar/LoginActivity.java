package com.example.baidupostbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baidupostbar.Utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;

public class LoginActivity extends RootBaseActivity {
    EditText et_account;
    EditText et_password;
    Button btn_login;
    Button btn_register;
    Button btn_forgetPassword;
    CheckBox remember_code;
    boolean remember;

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
        remember_code = findViewById(R.id.remember_password);

        SetTextWatcher();
        initData();

        btn_login.setEnabled(true);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = et_account.getText().toString();
                String password = et_password.getText().toString();
                //创表单
                if(remember) {
                    SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Re_name",account );
                    editor.putString("Re_password", password);
                    editor.apply();
                }
                FormBody formBody = new FormBody.Builder()
                        .add("username",account)
                        .add("password",password)
                        .build();
                //用这个工具类发请求
                //*********************************************
                //*联网检查还有个bug存在
                //*********************************************
                HttpUtil httpUtil = new HttpUtil(LoginActivity.this,getApplicationContext());
                httpUtil.PostUtilsWithCookie("http://139.199.84.147/mytieba.api/login",formBody,1);
                doHandler();
//                if(responseData!=null) {
//                    prasedWithJsonData(responseData);
//                }
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
        remember_code.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    remember = true;
                }else{
                    remember = false;
                }
            }
        });

    }
    //Edittext的监听
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
    private void prasedWithJsonData(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            boolean state = jsonObject.getBoolean("status");
            String msg = jsonObject.getString("msg");
            if (state) {
                String user_id = jsonObject.getString("user_id");
                String avatar = jsonObject.getString("avatar");
                String username = jsonObject.getString("username");
                SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_id",user_id);
                editor.putString("avater",avatar);
                editor.putString("username",username);
                editor.apply();
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            showResponse(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void showResponse(String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_LONG).show();
            }
        });
    }
    private void initData(){
        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        String account = sharedPreferences.getString("Re_name", "");
        String password = sharedPreferences.getString("Re_password","");
        et_account.setText(account);
        et_password.setText(password);

    }
    private void doHandler() {
        viewHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        Toast.makeText(getApplicationContext(),String.valueOf(msg.obj),Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        prasedWithJsonData(String.valueOf(msg.obj));
                        break;
                        default:
                            break;
                }
            }

        };
    }
}
