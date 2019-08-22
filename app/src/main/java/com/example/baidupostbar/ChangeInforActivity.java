package com.example.baidupostbar;

import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baidupostbar.Utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cn.qqtheme.framework.util.ConvertUtils;
import okhttp3.FormBody;

public class ChangeInforActivity extends RootBaseActivity {

    private RadioGroup rg_sex;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private DatePicker datapicker;
    private Calendar cal;//显示当前日期
    private TextView tv_birthDay;
    private EditText et_signture;
    private String temp = null;
    private Button btn_finish;
    private String userId;
    private String userName;
    private TextView wordsNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_infor);
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
        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("username", "");
        userId = sharedPreferences.getString("user_id", "");




        btn_finish = findViewById(R.id.btn_finish);
        rg_sex = findViewById(R.id.rg_sex);
        rb_female = findViewById(R.id.rb_female);
        rb_male = findViewById(R.id.rb_male);
        tv_birthDay = findViewById(R.id.tv_birthDay);
        et_signture = findViewById(R.id.et_signature);
        wordsNum = findViewById(R.id.tv_words_number);

        et_signture.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int num = et_signture.getText().length();
                wordsNum.setText(num + "/20");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (ChangeInforActivity.this.rb_male.getId() == checkedId) {
                    //temp = "男";
                    temp = "0";
                }
                if (ChangeInforActivity.this.rb_female.getId() == checkedId) {
                    //temp = "女";
                    temp = "1";
                }
            }

        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://139.199.84.147/mytieba.api/user/"+ userId+"/info";
                int i = 0;
                String signiture = et_signture.getText().toString();
                String gender = temp;
                String birthday = tv_birthDay.getText().toString();
                FormBody.Builder builder = new FormBody.Builder();
                if(signiture!=null&&!signiture.equals("")){
                    builder.add("description",signiture);
                    i++;
                }
                if(gender!=null&&!gender.equals("")){
                    builder.add("gender",gender);
                    i++;
                }
                if(birthday!=null&&!birthday.equals("")){
                    builder.add("birthday",birthday);
                    i++;
                }
                if(i!=0){
                    FormBody formBody = builder
                            .add("username",userName)
                            .build();
                    HttpUtil httpUtil = new HttpUtil(ChangeInforActivity.this,getApplicationContext());
                    httpUtil.PostUtilsWithCookie(url,formBody,1);
                    doHandler();
                }else {
                    Toast.makeText(getApplicationContext(),"没有信息可以修改",Toast.LENGTH_LONG).show();
                }

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
//        // 成功
//        {
//            'status': True,
//                'user_id': 1，
//        }
//
//    }
//    // 登录失败
//    {
//        'status':False,
//            'error_code':3,
//            'msg':"修改的用户名存在"
//    }
        Log.e("TAG",JsonData);
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_LONG).show();
                finish();
            }else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"请求失败",Toast.LENGTH_LONG).show();
        }
    }
}
