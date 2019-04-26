package com.example.baidupostbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterEmail extends AppCompatActivity {

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
        btn_getCode = findViewById(R.id.btn_getCode);
        btn_getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.start();
            }
        });
        //EditText监听
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_code.getText().toString().equals("")&&!et_email.getText().toString().equals("")){
                    btn_next.setEnabled(true);
                    btn_next.setBackgroundColor(Color.parseColor("#6495ED"));
                }
                if(et_code.getText().toString().equals("")||et_email.getText().toString().equals("")){
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
        //下一步
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_email.getText().toString();
                if(checkEmail(email)){
                    //请求验证码
                    Intent intent = new Intent();
                    intent.setClass(RegisterEmail.this,Register_user.class);
                    startActivity(intent);
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

}
