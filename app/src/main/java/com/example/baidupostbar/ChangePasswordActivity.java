package com.example.baidupostbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText et_oldPassword;
    private EditText et_newPassword;
    private EditText et_confirmPassword;
    private String userId;
    private String cookie;
    private Button btn_finish;
    boolean status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        SharedPreferences preferences = getSharedPreferences("theUser",MODE_PRIVATE);
        userId = preferences.getString("user_id","");
        cookie = preferences.getString("cookie", "");
        initView();
        initListener();
        setEditTextInhibitInputSpace(et_confirmPassword);
        setEditTextInhibitInputSpace(et_newPassword);
        setEditTextInhibitInputSpace(et_oldPassword);
        et_confirmPassword.setFilters( new InputFilter[]{new InputFilter.LengthFilter(16)});
        et_newPassword.setFilters( new InputFilter[]{new InputFilter.LengthFilter(16)});
        et_oldPassword.setFilters( new InputFilter[]{new InputFilter.LengthFilter(16)});
    }
    public static void setEditTextInhibitInputSpace(EditText editText){
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals(" "))
                    return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }
    private void initView(){
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        et_newPassword = findViewById(R.id.et_newPassword);
        et_oldPassword = findViewById(R.id.et_oldPassword);
        btn_finish = findViewById(R.id.btn_finish);
    }
    private void initListener(){
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_confirmPassword.getText().toString() != null && et_newPassword.getText().toString()!=null&&et_oldPassword.getText().toString()!=null){
                    if (et_confirmPassword.getText().toString().equals(et_newPassword.getText().toString())){
                        sendRequestWithOKHttp();
                    }
                    else Toast.makeText(ChangePasswordActivity.this,"两次输入的密码必须一致",Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(ChangePasswordActivity.this,"以上内容不能为空",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void sendRequestWithOKHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10,TimeUnit.SECONDS)
                            .build();

                    String url = "http://139.199.84.147/mytieba.api/user/"+userId+"/reset-pwd";

//                    List<Map<String, String>> list_url = new ArrayList<>();
//                    Map<String, String> map = new HashMap<>();
//                    list_url.add(map);

                    //url = getUrl(url, list_url);
                    RequestBody requestBody = new FormBody.Builder()
                            .add("old_pwd",et_oldPassword.getText().toString())
                            .add("new_pwd",et_newPassword.getText().toString())
                            .build();
                    Request request = new Request.Builder()
                            .url(url)   //网址有待改动
                            .addHeader("Cookie",cookie)
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("建楼消息",responseData);
                    showResponse(responseData);
                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                            if (e instanceof SocketTimeoutException){
                                Toast.makeText(ChangePasswordActivity.this,"连接超时",Toast.LENGTH_SHORT).show();
                            }
                            if (e instanceof ConnectException){
                                Toast.makeText(ChangePasswordActivity.this,"连接异常",Toast.LENGTH_SHORT).show();
                            }

                            if (e instanceof ProtocolException) {
                                Toast.makeText(ChangePasswordActivity.this,"未知异常，请稍后再试",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }
    private void showResponse(final String response){
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getBoolean("status");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable(){
            @Override
            public void run(){ //设置ui
                if (status){
                    Toast.makeText(ChangePasswordActivity.this,"修改成功，请重新登录",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ChangePasswordActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else Toast.makeText(ChangePasswordActivity.this,"原密码错误",Toast.LENGTH_LONG).show();
            }
        });
    }
}
