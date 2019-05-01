package com.example.baidupostbar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.donkingliang.labels.LabelsView;
import com.example.baidupostbar.bean.TestBean;

import java.util.ArrayList;

public class RegisterInterest extends AppCompatActivity {
Button btn_finish;
Button btn_ignore;
    private LabelsView labelsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);

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
        testList.add(new TestBean("摄影",10));
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
        labelsView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                Toast.makeText(RegisterInterest.this, position + " : " + data,
                        Toast.LENGTH_LONG).show();
            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterInterest.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btn_ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterInterest.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
