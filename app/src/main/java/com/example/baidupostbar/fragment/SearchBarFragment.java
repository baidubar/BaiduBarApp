package com.example.baidupostbar.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.baidupostbar.Adapter.BarListAdapter;
import com.example.baidupostbar.DetailBarActivity;
import com.example.baidupostbar.ListBarActivity;
import com.example.baidupostbar.R;
import com.example.baidupostbar.SearchResultActivity;
import com.example.baidupostbar.Utils.CheckNetUtil;
import com.example.baidupostbar.bean.BarList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchBarFragment extends Fragment {

    View view;
    private ViewPager viewPager;
    private String searchText;

    private ArrayList<BarList> mDataList;
    private RecyclerView mRecyclerView;
    private String url;
    private String BarLabel;
    private String responseData;

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        searchText = ((SearchResultActivity) activity).getSearchText();//通过强转成宿主activity，就可以获取到传递过来的数据
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_bar,container,false);
        viewPager = view.findViewById(R.id.viewPager);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Map<String, String>> list_url = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("type", "bar");
        map.put("search",searchText);
        list_url.add(map);
        url = getUrl("http://139.199.84.147/mytieba.api/search", list_url);
        Log.e("FirstFragment","url" + url);
        initView();

        if (new CheckNetUtil(getContext()).initNet()) {
            initData(url);
        }

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
    private void initView() {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                Intent intent = new Intent(getActivity(), DetailBarActivity.class);
                intent.putExtra("barId",id);
                intent.putExtra("barLabel",BarLabel);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(barListAdapter);
    }
    private void initData(String url){
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("theUser", Context.MODE_PRIVATE);
            String cookie = sharedPreferences.getString("cookie", "");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url)
                    .addHeader("Cookie",cookie)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Log.e("onFailure","获取数据失败");
                    Toast.makeText(getContext(),"网络请求失败",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    responseData = response.body().string();
                    Log.e("rsponseData",responseData);
                    if (response.isSuccessful()){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                prasedWithJosnData(responseData);
                            }
                        });
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"服务器请求失败",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"网络请求失败",Toast.LENGTH_LONG).show();
        }

    }
    private void prasedWithJosnData(String jsonData){
        mDataList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
                String number = jsonObject.getString("bar_number");
                if (!number.equals("0")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("bar_msg");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String bar_id = jsonObject1.getString("bar_id");
                        String bar_title = jsonObject1.getString("bar_title");
                        String bar_icon = jsonObject1.getString("bar_icon");
                        String post_number = jsonObject1.getString("post_number");
                        String watching_number = jsonObject1.getString("watching_number");
                        String bar_description = jsonObject1.getString("bar_description");
                        boolean watching_status = jsonObject1.getBoolean("watching_status");

                        Log.e("SearchBarFragment",bar_title);
                        Log.e("SearchBarFragment", bar_description);
                        BarList item = new BarList();
                        item.setBarImg("http://139.199.84.147/" + bar_icon);
                        item.setAttentionNum(watching_number);
                        item.setBarInfor(bar_description);
                        item.setBarName(bar_title);
                        item.setPostNum(post_number);
                        item.setBarId(bar_id);
                        mDataList.add(item);

                    }
                }
        }catch(JSONException e) {
            e.printStackTrace();
        }
        initAdapter();
    }
}
