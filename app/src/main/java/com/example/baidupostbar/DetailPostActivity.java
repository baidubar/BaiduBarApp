package com.example.baidupostbar;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.baidupostbar.DialogFragment.RemarkDialogFragment;

public class DetailPostActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_post);
        TextView textView = findViewById(R.id.text_out);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemarkDialogFragment remarkDialogFragment = new RemarkDialogFragment();
                remarkDialogFragment.show(getSupportFragmentManager(),null);
            }
        });
    }

}
