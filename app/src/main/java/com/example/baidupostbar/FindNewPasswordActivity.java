package com.example.baidupostbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baidupostbar.Utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;

public class FindNewPasswordActivity extends RootBaseActivity {

    private Button btn_getCode;
    private String email;
    private String userName;
    private String url;
    private EditText et_code;
    private EditText et_password;
    private EditText et_rePassword;
    private Button btn_send;


    CountDownTime timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        userName = intent.getStringExtra("username");
        setContentView(R.layout.activity_find_new_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_getCode = findViewById(R.id.btn_getCode);
        et_code = findViewById(R.id.et_code);
        et_password = findViewById(R.id.et_newPassword);
        et_rePassword = findViewById(R.id.et_confirmPassword);
        btn_send = findViewById(R.id.btn_login);

        addTextWatcher();

        timer = new CountDownTime(60000,60000);
        timer.start();
        btn_getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil httpUtil = new HttpUtil(FindNewPasswordActivity.this, getApplicationContext());

                List<Map<String, String>> list_url = new ArrayList<>();
                Map<String, String> map = new HashMap<>();
//                    map.put("id",headnum.toString());
//                    headnum = headnum + 10;
                map.put("username", userName);
                map.put("email", email);
                list_url.add(map);
                url = getUrl("http://139.199.84.147/mytieba.api/find-back", list_url);
                httpUtil.GetUtil(url,1);
                doHandler();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Code = et_code.getText().toString();
                String Password = et_password.getText().toString();
                String Repassword = et_rePassword.getText().toString();
                if(Password.equals(Repassword)) {
                    FormBody formBody = new FormBody.Builder()
                            .add("code",Code)
                            .add("new_pwd",Password)
                            .build();
                    HttpUtil httpUtil = new HttpUtil(FindNewPasswordActivity.this, getApplicationContext());
                    httpUtil.PostUtilsWithCookie("http://139.199.84.147/mytieba.api/find-back",formBody,2 );
                    doHandler();
                }else {
                    Toast.makeText(getApplicationContext(),"两次密码不一致",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class CountDownTime extends CountDownTimer {

        //构造函数  第一个参数代表总的计时时长  第二个参数代表计时间隔  单位都是毫秒
        public CountDownTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) { //每计时一次回调一次该方法
            btn_getCode.setClickable(false);
            btn_getCode.setText("已发送（"+l/1000+"秒)");
        }

        @Override
        public void onFinish() { //计时结束回调该方法
            btn_getCode.setClickable(true);
            btn_getCode.setText("重新发送");
        }
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
                        prasedWithCodeJsonData(String.valueOf(msg.obj));
                        break;
                    case 2:
                        prasedWithJsonData(String.valueOf(msg.obj));
                        break;
                    default:
                        break;
                }
            }

        };
    }
    private String getUrl(String url, List<Map<String, String>> list_url) {
        for (int i = 0; i < list_url.size(); i++) {
            Map<String, String> params = list_url.get(i);
            if (params != null) {
                Iterator<String> it = params.keySet().iterator();
                StringBuffer sb = null;
                while (it.hasNext()) {
                    String key = it.next();
                    String value = params.get(key);
                    if (sb == null) {
                        sb = new StringBuffer();
                        sb.append("?");
                    } else {
                        sb.append("&");
                    }
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                }
                url += sb.toString();
            }
        }
        Log.d("getUrl", url);
        return url;
    }
    private void prasedWithCodeJsonData(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            boolean state = jsonObject.getBoolean("status");
            if (state) {
                Toast.makeText(getApplicationContext(),"验证码发送成功",Toast.LENGTH_LONG).show();
                timer.start();
            }else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void prasedWithJsonData(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            boolean state = jsonObject.getBoolean("status");
            if (state) {
                Toast.makeText(getApplicationContext(),"成功",Toast.LENGTH_LONG).show();
                finish();
            }else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void addTextWatcher(){
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                judgestate();
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
                judgestate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_rePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                judgestate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void judgestate(){
        if(!et_code.getText().toString().equals("")&&!et_password.getText().toString().equals("")&&!et_rePassword.getText().toString().equals("")){
            btn_send.setEnabled(true);
            btn_send.setBackgroundColor(Color.parseColor("#6495ED"));
        }else {
            btn_send.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btn_send.setBackground(getResources().getDrawable(R.drawable.btn_rounded));
            }

        }
    }
}
