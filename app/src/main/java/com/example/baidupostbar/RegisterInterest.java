package com.example.baidupostbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.donkingliang.labels.LabelsView;
import com.example.baidupostbar.bean.TestBean;

import java.util.ArrayList;

public class RegisterInterest extends AppCompatActivity {

    private LabelsView labelsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_interest);
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
    }
}
