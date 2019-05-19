package com.example.baidupostbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.donkingliang.labels.LabelsView;
import com.example.baidupostbar.Utils.HttpUtil;
import com.example.baidupostbar.bean.TestBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

public class RegisterInterest extends RootBaseActivity {
Button btn_finish;
Button btn_ignore;
    private LabelsView labelsView;
    private FormBody formBody = new FormBody.Builder().build();
    private List<String>labelList;
    private String username;
    private String password;
    private String birthday;
    private String description;
    private String interest;
    private String email;
    private String email_access;
    private String gender;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //初始化所需数据
        InitData();

        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_interest);
        btn_finish = findViewById(R.id.btn_finish);
        btn_ignore = findViewById(R.id.btn_ignore);
        labelsView = (LabelsView) findViewById(R.id.labels);
        ArrayList<TestBean> testList = new ArrayList<>();
        testList.add(new TestBean("体育",1));
        testList.add(new TestBean("娱乐",2));
        testList.add(new TestBean("IT",3));
        testList.add(new TestBean("教育",4));
        testList.add(new TestBean("美容",5));
        testList.add(new TestBean("宠物",6));
        testList.add(new TestBean("美食",7));
        testList.add(new TestBean("购物",8));
        testList.add(new TestBean("文字",9));
        testList.add(new TestBean("ACG",10));
        testList.add(new TestBean("音乐",11));
        testList.add(new TestBean("电影",12));
        labelsView.setLabels(testList, new LabelsView.LabelTextProvider<TestBean>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, TestBean data) {
                return data.getName();
            }
        });
        labelsView.setSelectType(LabelsView.SelectType.MULTI);
        labelsView.setMaxSelect(0);
        //创建labelList暂存标签
        labelList = new ArrayList<>();
        //FormBody.Builder builder = new FormBody.Builder();
        labelsView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                //labelList.add(String.valueOf(data));
                removeDuplicate(String.valueOf(data));
                Log.e("RegisterEMail", String.valueOf(labelList));
            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormBody.Builder builder = new FormBody.Builder();
                for (int i = 0; i < labelList.size(); i++) {
                    builder.add("interest", labelList.get(i));
                }
                Log.e("labelList.size()", String.valueOf(labelList.size()));
                if (type.equals("1")) {
                    formBody = builder
                            .add("email", email)
                            .add("username", username)
                            .add("password", password)
                            .add("email_access", email_access)
                            .add("birthday", birthday)
                            .add("description", description)
                            .add("gender", gender)
                            .build();

                } else {
                    formBody = builder
                            .add("email", email)
                            .add("username", username)
                            .add("password", password)
                            .add("email_access", email_access)
//                            .add("birthday", birthday)
//                            .add("description", description)
//                            .add("gender", gender)
                            .build();
                }
                HttpUtil httpUtil = new HttpUtil(RegisterInterest.this, getApplicationContext());
                httpUtil.PostUtilsWithCookie("http://139.199.84.147/mytieba.api/register", formBody, 1);
                doHandler();
            }
//                prasedWithJsonData(responseData);

        });
        btn_ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("1")){
                    formBody = new FormBody.Builder()
                            .add("email",email)
                            .add("username",username)
                            .add("password",password)
                            .add("email_access",email_access)
                            .add("birthday",birthday)
                            .add("description",description)
                            .add("gender",gender)
                            .build();
                }else {
                    formBody = new FormBody.Builder()
                            .add("email",email)
                            .add("username",username)
                            .add("password",password)
                            .add("email_access",email_access)
                           // .add("description",description)
//                            .add("gender",gender)
                            .build();
                }
                HttpUtil httpUtil = new HttpUtil(RegisterInterest.this,getApplicationContext());
                httpUtil.PostUtilsWithCookie("http://139.199.84.147/mytieba.api/register",formBody,1);
                doHandler();
//                String responseData = httpUtil.PostUtilsWithCookie("http://139.199.84.147/mytieba.api/register",formBody);
//                prasedWithJsonData(responseData);
            }
        });
    }
    //检查标签数组中的有无重复，重复删除，相同补充。
    public void removeDuplicate(String add) {
        int j = labelList.size();
        Log.e("TAG", String.valueOf(j));
        Log.e("TAG", add);
        if(j == 0){
            labelList.add(add);
            Log.e("removeDuplicate1", String.valueOf(labelList));
        }else {
            for (int i = 0; i < labelList.size(); i++) {
                if (labelList.get(i).equals(add)) {
                    labelList.remove(i);
                    break;
                } else {
                    if (i == j-1 && !labelList.get(i).equals(add)) {
                        labelList.add( add);
                        break;
                    }
                }
            }
        }
    }
    //初始化所需数据
    private void InitData(){

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if(type.equals("1")) {
            username = intent.getStringExtra("username");
            Log.e("RegisterInforUsername",username);
            password = intent.getStringExtra("password");
            birthday = intent.getStringExtra("birthday");
            description = intent.getStringExtra("signture");
            gender = intent.getStringExtra("gender");
        }
        if(type.equals("2")){
            username = intent.getStringExtra("username");
            password = intent.getStringExtra("password");
        }

        SharedPreferences sharedPreferences =getSharedPreferences("theUser", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        email_access = sharedPreferences.getString("email_access","");
    }
    private void prasedWithJsonData(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            boolean state = jsonObject.getBoolean("status");
            String msg = jsonObject.getString("msg");
            if(state){
                Intent intent = new Intent(RegisterInterest.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }else {
                //***********************************************************************
                //此时我想到一个严肃的问题，如果此时邮箱已经存在/用户名已经存在就会判断登陆失败
                //一旦失败就得重新来一遍这个似乎有点反人类（考虑下咱们注册复杂程度）
                //当然，如果邮箱email_access不过期，至少会少两步过程，改天问问蔡佬啥时候过期
                //***********************************************************************
                if(msg.equals("用户名已存在")){
                    Intent intent = new Intent(RegisterInterest.this,Register_user.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(RegisterInterest.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
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
                        prasedWithJsonData(String.valueOf(msg.obj));
                        break;
                    default:
                        break;
                }
            }

        };
    }
}
