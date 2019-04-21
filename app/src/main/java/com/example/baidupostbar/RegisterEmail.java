package com.example.baidupostbar;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterEmail extends AppCompatActivity {

    EditText et_email;
    EditText et_code;
    Button btn_getCode;
    CountDownTime timer;
    Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}
