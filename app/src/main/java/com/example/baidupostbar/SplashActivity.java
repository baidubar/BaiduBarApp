package com.example.baidupostbar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        HTextView hTextView = (HTextView) findViewById(R.id.text);
//        hTextView.setAnimateType(HTextViewType.LINE);
//        hTextView.animateText("new simple string"); // animate

        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
