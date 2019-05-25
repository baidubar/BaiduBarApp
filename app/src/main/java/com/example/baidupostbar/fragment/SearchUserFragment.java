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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.baidupostbar.Adapter.SearchUserAdapter;
import com.example.baidupostbar.HomepageActivity;
import com.example.baidupostbar.R;
import com.example.baidupostbar.SearchResultActivity;
import com.example.baidupostbar.Utils.CheckNetUtil;
import com.example.baidupostbar.bean.EmptyRecyclerView;
import com.example.baidupostbar.bean.SearchUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchUserFragment extends Fragment {


    View view;
    private ViewPager viewPager;
    private String searchText;
    private String url;
    private EmptyRecyclerView mRecyclerView;
    private String responseData;
    private ArrayList<SearchUser>mDataList;
    private View mEmptyView;
    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        searchText = ((SearchResultActivity) activity).getSearchText();//通过强转成宿主activity，就可以获取到传递过来的数据
    }
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_search_user, container, false);
        view = inflater.inflate(R.layout.fragment_search_user,container,false);
        viewPager = view.findViewById(R.id.viewPager);
        mEmptyView = view.findViewById(R.id.empty_iv);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        url = "http://139.199.84.147/mytieba.api/search" + "?type=user"+"&search="+searchText;


        initView();
        if (new CheckNetUtil(getContext()).initNet()) {
            initData(url);
        }
    }
    private void initView() {
        mRecyclerView = view.findViewById(R.id.search_user_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setEmptyView(mEmptyView);
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
            JSONArray jsonArray = jsonObject.getJSONArray("user_msg");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String user_id = jsonObject1.getString("user_id");
                    String user_avatar = jsonObject1.getString("user_avatar");
                    String user_name = jsonObject1.getString("user_name");
                    String user_followers = jsonObject1.getString("user_followers");
                    SearchUser item = new SearchUser();
                    item.setUser_avatar("http://139.199.84.147/" + user_avatar);
                    item.setUser_id(user_id);
                    item.setUser_name(user_name);
                    item.setFollower_number(user_followers);
                    mDataList.add(item);

                }

        }catch(JSONException e) {
            e.printStackTrace();
        }
        initAdapter();
    }
    private void initAdapter() {
        BaseQuickAdapter searchUserAdapter = new SearchUserAdapter(R.layout.item_search_user, mDataList,getContext());
        searchUserAdapter.openLoadAnimation();
        searchUserAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
               SearchUser searchUser = mDataList.get(position);
               String id = searchUser.getUser_id();
                Intent intent = new Intent();
                intent.setClass(getContext(), HomepageActivity.class);
                intent.putExtra("userId",id);
                startActivity(intent);

            }
        });

        mRecyclerView.setAdapter(searchUserAdapter);
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

}
