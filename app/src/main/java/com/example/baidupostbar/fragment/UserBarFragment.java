package com.example.baidupostbar.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.baidupostbar.R;
import com.example.baidupostbar.Utils.HttpUtil;
import com.example.baidupostbar.bean.BarList;
import com.example.baidupostbar.bean.EmptyRecyclerView;
import com.example.baidupostbar.bean.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserBarFragment extends Fragment {
    View view;
    private ArrayList<BarList> mDataList;
    private EmptyRecyclerView mRecyclerView;
    private String userId;
    private String url;
    private String barId;
    private String barLabel;
    private String responseData;
    private View mEmptyView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_bar,container,false);
        mEmptyView = view.findViewById(R.id.empty_iv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("theUser", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", "");
        url = "http://139.199.84.147/mytieba.api/user/"+ userId +"/watching";
        initView();
        initData(url);
    }
    private void initView() {
        mRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setEmptyView(mEmptyView);
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
        BaseQuickAdapter barListAdapter = new BarListAdapter(R.layout.item_bar, mDataList);
        barListAdapter.openLoadAnimation();
        barListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), DetailBarActivity.class);
                intent.putExtra("barId",barId);
                intent.putExtra("barLabel",barLabel);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(barListAdapter);
    }

    private void initData(String url) {
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
                    Log.e("UserBarFragment",responseData);
                    if (response.isSuccessful()){
                        prasedWithJsonData(responseData);
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
    private void prasedWithJsonData(String jsonData){
        mDataList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("bar_msg");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                barId = jsonObject1.getString("bar_id");
                String bar_name = jsonObject1.getString("bar_name");
                String bar_tags = jsonObject1.getString("bar_tags");
                String bar_icon = jsonObject1.getString("bar_icon");
                BarList item = new BarList();
                item.setBarImg("http://139.199.84.147/" + bar_icon);
                item.setBarName(bar_name);
                item.setBarId(barId);
                Log.e("UserBarFragment+ bar_id",barId);
                mDataList.add(item);

            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initAdapter();
                }
            });

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

}
