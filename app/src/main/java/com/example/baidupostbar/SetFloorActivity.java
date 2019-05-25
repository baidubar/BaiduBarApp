package com.example.baidupostbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baidupostbar.Utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;

public class SetFloorActivity extends RootBaseActivity {
    private TextView wordNum;
    private EditText post;
    private Button send;
    private String userId;
    private String url;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_floor);

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
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");

        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", "");


        url = "http://139.199.84.147/mytieba.api/post/"+ postId +"/comment";


        send = findViewById(R.id.btn_publish);
        post = findViewById(R.id.ed_content);
        wordNum = findViewById(R.id.tv_words_number);
        post.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int num = post.getText().length();
                wordNum.setText(String.valueOf(num)+ "/140");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = post.getText().toString();
                String con = trim(content);
                if(con!=null&&!con.equals("")){
                    HttpUtil httpUtil = new HttpUtil(SetFloorActivity.this,getApplicationContext());
                    FormBody formBody = new FormBody.Builder()
                            .add("user_id",userId)
                            .add("reply_id","0")
                            .add("reply_floor","1")
                            .add("content",con)
                            .build();

                    httpUtil.PostUtilsWithCookie(url,formBody,1);
                    doHandler();

                    Log.e("SetFloor","con" + con );
                }
            }
        });
    }
    //去除首尾空白字符
    public static String trim(String s){
        String result = "";
        if(null!=s && !"".equals(s)){
            result = s.replaceAll("[\\s]", "").replaceAll("[\\s]", "");
        }
        return result;
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
    private void prasedWithJsonData(String JsonData){
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                Toast.makeText(getApplicationContext(),"发表成功",Toast.LENGTH_LONG).show();
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
