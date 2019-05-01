package com.example.baidupostbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterInfor extends AppCompatActivity {
    private RadioGroup rg_sex;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private DatePicker datapicker;
    private Calendar cal;//显示当前日期
    private EditText et_birthDay;
    private int year;
    private int month;
    private int day;
    private String temp = null;
    private Button btn_ignore;
    Button btn_next;
    EditText et_nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_infor);
        btn_ignore = findViewById(R.id.btn_ignore);
        btn_next = findViewById(R.id.btn_next);
        //et_nickName = findViewById(R.id.et_nickName);
        rg_sex = findViewById(R.id.rg_sex);
        rb_female = findViewById(R.id.rb_female);
        rb_male = findViewById(R.id.rb_male);
        //et_birthDay = findViewById(R.id.et_birthDay);

        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            if(RegisterInfor.this.rb_male.getId()==checkedId){
                temp = "男";
            }
            if(RegisterInfor.this.rb_female.getId()==checkedId){
                temp = "女";
            }
        }
        });

        //datapicker = (DatePicker) findViewById(R.id.id_datePicker1);
        //获取日历的对象
        cal=Calendar.getInstance();
        //获取年月日时分秒信息
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH)+1;//注意点 ，要加一
        day = cal.get(Calendar.DAY_OF_MONTH);
//        et_birthDay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // TODO Auto-generated method stub
                setTitle("当前时间："+year+"年-"+month+"月-"+day+"日 ");
            }
        }, year, cal.get(Calendar.MONTH), day).show(); //后三个参数相当于初始化
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterInfor.this,RegisterInterest.class);
                startActivity(intent);
            }
        });
    }
}
