package com.example.baidupostbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;

import cn.qqtheme.framework.util.ConvertUtils;

import static com.example.baidupostbar.ChangePasswordActivity.setEditTextInhibitInputSpace;

public class RegisterInfor extends AppCompatActivity {
    private RadioGroup rg_sex;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private DatePicker datapicker;
    private Calendar cal;//显示当前日期
    private TextView tv_birthDay;
    private EditText et_signture;
    private int year;
    private int month;
    private int day;
//    private String temp = null;
    private String temp = "2";
    private Button btn_ignore;
    Button btn_next;
    EditText et_nickName;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent intent = getIntent();
        username = intent.getStringExtra("name");
        password = intent.getStringExtra("password");

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
        tv_birthDay = findViewById(R.id.tv_birthDay);
        et_signture = findViewById(R.id.et_signature);
        et_signture.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                return(event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        setEditTextInhibitInputSpace(et_signture);
        et_signture.setFilters(new InputFilter[] {new InputFilter.LengthFilter(16)});

        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            if(RegisterInfor.this.rb_male.getId()==checkedId){
                //temp = "男";
                temp = "0";
            }
            if(RegisterInfor.this.rb_female.getId()==checkedId){
                //temp = "女";
                temp = "1";
            }
        }
        });

        //datapicker = (DatePicker) findViewById(R.id.id_datePicker1);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String birthday = tv_birthDay.getText().toString();
                if (birthday.equals("")){
                    birthday = "2019-5-26";
                }
                String signture = et_signture.getText().toString();
                Log.e("RegisterInfor",signture);
                Log.e("RegisterInfor",birthday);
                Intent intent = new Intent(RegisterInfor.this,RegisterInterest.class);
                intent.putExtra("type","1");
                intent.putExtra("username",username);
                intent.putExtra("password",password);
                intent.putExtra("birthday",birthday);
                intent.putExtra("signture",signture);
                intent.putExtra("gender",temp);
                startActivity(intent);
                finish();
            }
        });
        btn_ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterInfor.this,RegisterInterest.class);
                intent.putExtra("type","2");
                intent.putExtra("username",username);
                Log.e("RegisterInforUsername",username);
                intent.putExtra("password",password);
                startActivity(intent);
                finish();
            }
        });
    }
    public void onYearMonthDayPicker(View view) {
        final cn.qqtheme.framework.picker.DatePicker picker = new cn.qqtheme.framework.picker.DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 10));
        picker.setRangeEnd(2050, 12, 31);
        picker.setRangeStart(1950, 1, 1);
        picker.setSelectedItem(2019, 5, 26);
        picker.setResetWhileWheel(false);
        picker.setTextSize(18);
        picker.setOnDatePickListener(new cn.qqtheme.framework.picker.DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                //showToast(year + "-" + month + "-" + day);
                tv_birthDay.setText(year + "-" + month + "-" + day);
            }
        });
        picker.setOnWheelListener(new cn.qqtheme.framework.picker.DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }
}
