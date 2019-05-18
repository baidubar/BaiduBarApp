package com.example.baidupostbar;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.hanks.htextview.base.HTextView;


public class SplashActivity extends BaseActivity {

    private HTextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        textView = (HTextView) findViewById(R.id.textview3);
//        textView.setOnClickListener(new ClickListener());
//        textView.setAnimationListener(new SimpleAnimationListener(this));


    }
}
