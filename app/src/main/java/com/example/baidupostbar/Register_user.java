package com.example.baidupostbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Register_user extends AppCompatActivity {

    EditText et_account;
    EditText et_password;
    EditText et_confirmPassword;
    Button btn_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        et_account = findViewById(R.id.et_account);
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        et_password = findViewById(R.id.et_password);
        btn_next = findViewById(R.id.btn_next);
    }
}
