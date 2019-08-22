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
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baidupostbar.Utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;

import static com.example.baidupostbar.ChangePasswordActivity.setEditTextInhibitInputSpace;

public class Register_user extends RootBaseActivity {

    private String email;
    private String name;
    private String password;
    EditText et_account;
    EditText et_password;
    EditText et_confirmPassword;
    Button btn_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_user);
        et_account = findViewById(R.id.et_account);
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        et_password = findViewById(R.id.et_password);
        btn_next = findViewById(R.id.btn_next);
        setEditTextInhibitInputSpace(et_confirmPassword);
        setEditTextInhibitInputSpace(et_account);
        setEditTextInhibitInputSpace(et_password);
        et_account.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
        et_confirmPassword.setFilters(new InputFilter[] {new InputFilter.LengthFilter(16)});
        et_password.setFilters(new InputFilter[] {new InputFilter.LengthFilter(16)});

        SharedPreferences sharedPreferences =getSharedPreferences("theUser", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");

        //setTextWatcher();
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_account.getText().toString();
                password = et_password.getText().toString();
                String rePassword = et_confirmPassword.getText().toString();
                if(!name.equals("")&&!password.equals("")&&!rePassword.equals("")) {

                    if (checkName(name)) {
                        if (password.equals(rePassword)) {
                            HttpUtil httpUtil = new HttpUtil(Register_user.this, getApplicationContext());
                            httpUtil.GetUtil("http://139.199.84.147/mytieba.api/register?name="+email+"&email="+email, 1);
                            doHandler();
                        } else {
                            Toast.makeText(Register_user.this, "两次密码不一致", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(Register_user.this, "用户名不能含有空白字符", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(Register_user.this, "请填入数据", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public static boolean checkName(String nickname) {
        Pattern pattern = Pattern.compile("^[\\S\u4e00-\u9fa5]{1,12}$");
        Matcher matcher = pattern.matcher(nickname);
        return matcher.matches();
    }
    private void setTextWatcher(){
        et_confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_account.getText().toString().equals("")&&!et_confirmPassword.getText().toString().equals("")&&!et_password.getText().toString().equals("")){
                    btn_next.setEnabled(true);
                    btn_next.setBackgroundColor(Color.parseColor("#6495ED"));
                }else {
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
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_account.getText().toString().equals("")&&!et_confirmPassword.getText().toString().equals("")&&!et_password.getText().toString().equals("")){
                    btn_next.setEnabled(true);
                    btn_next.setBackgroundColor(Color.parseColor("#6495ED"));
                }else {
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
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_account.getText().toString().equals("")&&!et_confirmPassword.getText().toString().equals("")&&!et_password.getText().toString().equals("")){
                    btn_next.setEnabled(true);
                    btn_next.setBackgroundColor(Color.parseColor("#6495ED"));
                }else {
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
                    default:
                        break;
                }
            }

        };
    }
    private void prasedWithCodeJsonData(String JsonData){
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            boolean state = jsonObject.getBoolean("status");
            if(state){
                Intent intent = new Intent();
                intent.setClass(Register_user.this, RegisterInfor.class);
                intent.putExtra("name", name);
                intent.putExtra("password", password);
                startActivity(intent);
                finish();
            }else {
                String msg  = jsonObject.getString("msg");
                Toast.makeText(Register_user.this,msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"请求失败",Toast.LENGTH_LONG).show();
        }
    }
}
