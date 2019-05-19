package com.example.baidupostbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class FindPasswordActivity extends RootBaseActivity {

    private String url;
    private String emailText;
    private String userNameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        Button getCode = findViewById(R.id.btn_get_code);
        EditText userName = findViewById(R.id.et_username);
        EditText email = findViewById(R.id.et_email);




        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailText = email.getText().toString();
                userNameText  = userName.getText().toString();
                HttpUtil httpUtil = new HttpUtil(FindPasswordActivity.this, getApplicationContext());

                List<Map<String, String>> list_url = new ArrayList<>();
                Map<String, String> map = new HashMap<>();
//                    map.put("id",headnum.toString());
//                    headnum = headnum + 10;
                map.put("username", userNameText);
                map.put("email", emailText);
                list_url.add(map);
                url = getUrl("http://139.199.84.147/mytieba.api/find-back", list_url);

                httpUtil.GetUtil(url,1);
                doHandler();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!email.getText().toString().equals("")&&!userName.getText().toString().equals("")){
                    getCode.setEnabled(true);
                    getCode.setBackgroundColor(Color.parseColor("#6495ED"));
                }else {
                    getCode.setEnabled(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getCode.setBackground(getResources().getDrawable(R.drawable.btn_rounded));
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!email.getText().toString().equals("")&&!userName.getText().toString().equals("")){
                    getCode.setEnabled(true);
                    getCode.setBackgroundColor(Color.parseColor("#6495ED"));
                }else {
                    getCode.setEnabled(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getCode.setBackground(getResources().getDrawable(R.drawable.btn_rounded));
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
    private void prasedWithJsonData(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            boolean state = jsonObject.getBoolean("status");
            if (state) {
                Toast.makeText(getApplicationContext(),"验证码发送成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),FindNewPasswordActivity.class);
                intent.putExtra("email",emailText);
                intent.putExtra("username",userNameText);
                startActivity(intent);
                finish();
            }else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
