package com.example.baidupostbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.baidupostbar.Adapter.BarListAdapter;
import com.example.baidupostbar.Utils.HttpUtil;
import com.example.baidupostbar.bean.BarList;
import com.example.baidupostbar.bean.EmptyRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListBarActivity extends RootBaseActivity {

    private ArrayList<BarList> mDataList;
    private EmptyRecyclerView mRecyclerView;
    private String url;
    private String BarLabel;
    private View mEmptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bar);
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
        Intent intent = getIntent();
        BarLabel = intent.getStringExtra("BarLabel");

        List<Map<String, String>> list_url = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("bar_tag",BarLabel);
        list_url.add(map);

                url = getUrl("http://139.199.84.147/mytieba.api/postbar", list_url);
                Log.e("ListBarActivity","url" + url);
                HttpUtil httpUtil = new HttpUtil(ListBarActivity.this,getApplicationContext());
                httpUtil.GetUtil(url,1);
                doHandler();
//                Log.e("ListBarActivity","1");
//                String responseData = httpUtil.GetUtil(url);
//                Log.e("ListBarActivity","2");
//                if(responseData!=null){
//                    Log.e("ListBarActivity","responseData" + responseData);
//                    prasedWithJosnData(responseData);
//                }else {
//                    Log.e("ListBarActivity","responseData=null");
//                }

        initView();

    }
    private void initView() {
        mRecyclerView = (EmptyRecyclerView) findViewById(R.id.recyclerView);
        mEmptyView = findViewById(R.id.empty_iv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
        BaseQuickAdapter barListAdapter = new BarListAdapter(R.layout.item_bar, mDataList);
        barListAdapter.openLoadAnimation();
        barListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BarList barList = mDataList.get(position);
                String id = barList.getBarId();
                Intent intent = new Intent(ListBarActivity.this,DetailBarActivity.class);
                intent.putExtra("barId",id);
                intent.putExtra("barLabel",BarLabel);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(barListAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
    }
    private String getUrl(String url, List<Map<String, String>> list_url) {
        for (int i = 0; i < list_url.size(); i++) {
            Map<String, String> params = list_url.get(i);
            if (params != null) {
                Iterator<String> it = params.keySet().iterator();
                StringBuffer sb = null;
                while (it.hasNext()) {
                    String key = it.next();
                    String value = params.get(key);
                    if (sb == null) {
                        sb = new StringBuffer();
                        sb.append("?");
                    } else {
                        sb.append("&");
                    }
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                }
                url += sb.toString();
            }
        }
        Log.d("getUrl", url);
        return url;
    }
//    {
//        "status":true,
//            "search_tag":"ACG",
//            "number": 2,
//            "bar_info":[
//        {
//            "id": 1,
//                "name": "动漫吧",
//                "icon": xxx, // 图标
//                "post_number":5,
//                "watcher_number":0,
//                "description": "无",
//                "watching_status": false,
//        },
//        // 略一个
//    ]
//    }
    private void prasedWithJosnData(String jsonData){
        mDataList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            boolean status = jsonObject.getBoolean("status");
            if (status) {
                String number = jsonObject.getString("number");
                if (!number.equals("0")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("bar_info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("bar_id");
                        String name = jsonObject1.getString("name");
                        String icon = jsonObject1.getString("icon");
                        String post_number = jsonObject1.getString("post_number");
                        String watcher_number = jsonObject1.getString("watcher_number");
                        String description = jsonObject1.getString("description");
                        boolean watching_status = jsonObject1.getBoolean("watching_status");

                        Log.e("ListBarActivity",name);
                        Log.e("ListBarActivity", String.valueOf(status));
                        Log.e("ListBarActivity", description);
                        BarList item = new BarList();
                        item.setBarImg("http://139.199.84.147/" + icon);
                        item.setAttentionNum(watcher_number);
                        item.setBarInfor(description);
                        item.setBarName(name);
                        item.setPostNum(post_number);
                        item.setBarId(id);
                        mDataList.add(item);

                    }
                }
            }
        }catch(JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"请求失败",Toast.LENGTH_LONG).show();
        }
        initAdapter();
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
                        prasedWithJosnData(String.valueOf(msg.obj));
                        Log.e("ListBArActivity", String.valueOf(msg.obj));
                        break;
                    default:
                        break;
                }
            }

        };
    }
}
