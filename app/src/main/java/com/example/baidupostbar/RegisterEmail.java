package com.example.baidupostbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baidupostbar.Utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;

public class RegisterEmail extends RootBaseActivity {

    EditText et_email;
    EditText et_code;
    Button btn_getCode;
    CountDownTime timer;
    Button btn_next;
    private String email;
    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_email);
        timer = new CountDownTime(60000, 1000);
        //监听事件
        et_code = findViewById(R.id.et_code);
        et_email = findViewById(R.id.et_email);
        btn_next = findViewById(R.id.btn_next);
        et_email.setFilters(new InputFilter[] {new InputFilter.LengthFilter(30)});
        et_code.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
        et_email.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                return(event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        et_code.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                return(event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        btn_getCode = findViewById(R.id.btn_getCode);
        btn_getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_email.getText().toString();
                HttpUtil httpUtil = new HttpUtil(RegisterEmail.this,getApplicationContext());
                FormBody formBody = new FormBody.Builder()
                        .add("email",email)
                        .build();
                httpUtil.PostUtilsWithCookie("http://139.199.84.147/mytieba.api/email",formBody,1);
                doHandler();
            }
        });
        //EditText监听
//        et_code.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(!et_code.getText().toString().equals("")&&!et_email.getText().toString().equals("")){
//                    btn_next.setEnabled(true);
//                    btn_next.setBackgroundColor(Color.parseColor("#6495ED"));
//                }
//                if(et_code.getText().toString().equals("")||et_email.getText().toString().equals("")){
//                    btn_next.setEnabled(false);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        btn_next.setBackground(getResources().getDrawable(R.drawable.btn_rounded));
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        //下一步
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_email.getText().toString();
                code = et_code.getText().toString();
                if(checkEmail(email)){
                    HttpUtil httpUtil = new HttpUtil(RegisterEmail.this,getApplicationContext());
                    FormBody formBody = new FormBody.Builder()
                            .add("code",code)
                            .build();
                    httpUtil.PostUtilsWithCookie("http://139.199.84.147/mytieba.api/email/vertify",formBody,2);
                    doHandler();
//                    }
                }else {
                    Toast.makeText(RegisterEmail.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 第一种方法 使用android封装好的 CountDownTimer
     * 创建一个类继承 CountDownTimer
     */
    class CountDownTime extends CountDownTimer{

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
    public static boolean checkEmail(String email){
        Pattern pattern=Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher matcher=pattern.matcher(email);
        return matcher.matches();
    }
    private void prasedWithCodeJsonData(String jsondata){
        try {
            JSONObject jsonObject = new JSONObject(jsondata);
            boolean state = jsonObject.getBoolean("status");
            String msg = jsonObject.getString("msg");
            if (state) {
                timer.start();
            }
            showResponse(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void prasedWithJsonData(String jsondata){
        try {
            Log.e("RegisterEmail","2");
            JSONObject jsonObject = new JSONObject(jsondata);
            boolean state = jsonObject.getBoolean("status");
            Log.e("RegisterEmail","3");
            Log.e("RegisterEmail","boolen" + state);
            if (state) {
                String email_access = jsonObject.getString("email_access");
                SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email",email);
                editor.putString("email_access",email_access);
                editor.apply();
                Log.e("RegisterEmail","4");

                Intent intent = new Intent();
                intent.setClass(RegisterEmail.this,Register_user.class);
                startActivity(intent);
                finish();
                }
            else {
                String msg = jsonObject.getString("msg");
                Log.e("RegisterEmail","5");
                showResponse(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showResponse(String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterEmail.this,msg,Toast.LENGTH_LONG).show();
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
}
