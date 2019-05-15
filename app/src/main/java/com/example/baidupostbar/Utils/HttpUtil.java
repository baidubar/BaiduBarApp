package com.example.baidupostbar.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    private boolean Tag = false;
    private String responseData = null;
    private Context context;
    private String cookie;
    public HttpUtil(Context context){
        this.context = context;
    }
    public String PostUtils(String url, FormBody formBody){
        responseData = null;
        Tag = false;
        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .addHeader("Connection", "close")
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Tag  = true;
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    responseData = response.body().string();
                    if (response.isSuccessful()) {
                        Log.e("posr",responseData);

                    }
                    Tag = true;
                }

            });
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("TAGG",Thread.currentThread().getName()+"->" + "2");
        while (!Tag){}
        return responseData;
    }
    public String PostUtilsWithCookie(String url, FormBody formBody){
        Tag = false;
        responseData = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");
        Log.e("post","1");
        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .cookieJar(new CookieJar() {
                        @Override
                        public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
                            Log.e("PostWithCookie","保存cookie");
                            Log.e("PostCookie", String.valueOf(cookies));
                            for (int i = 0;i<cookies.size();i++){
                                Log.e("Cookie"+ i, String.valueOf(cookies.get(i)));
                                cookie = String.valueOf(cookies.get(i));
                            }
                            SharedPreferences sharedPreferences = context.getSharedPreferences("theUser", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("cookie",cookie);
                            editor.apply();
                            Log.e("post","2");

                        }
                        @Override
                        public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                            List<Cookie> cookies = new ArrayList<>();
//                            for (int i = 0;i<cookies.size();i++){
//                                Log.e("Cookie"+ i, String.valueOf(cookies.get(i)));
//                                cookie = String.valueOf(cookies.get(i));
//                            }
                            return cookies != null ? cookies : new ArrayList<Cookie>();
                        }
                    })
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .addHeader("Connection", "close")
                    .addHeader("Cookie",cookie)
                    .build();

            Log.e("post","3");
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Log.e("PostWithCookie","获取数据失败");
                    Log.e("postcookie", String.valueOf(e));
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    responseData = response.body().string();
                    Log.e("PostWithCookieResponse",responseData);
                    if (response.isSuccessful()) {
                    }
                    Tag = true;
                }
            });
            Log.e("post","4");
        }catch (Exception e) {
            e.printStackTrace();
        }
        while (!Tag){
        }
        return responseData;
    }

    public String DeleteUtil(String url, FormBody formBody){
        try {
            Tag = false;
            responseData = null;
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .delete(formBody)
                    .addHeader("Connection", "close")
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    responseData = response.body().string();
                    if (response.isSuccessful()) {
                    }
                    Tag = true;
                }

            });
        }catch (Exception e) {
            e.printStackTrace();
        }
//        synchronized (this){
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        while (!Tag){}
        return responseData;
    }
    public String GetUtil(String url){
        try {
            responseData = null;
            Tag = false;
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    responseData = response.body().string();
                    if (response.isSuccessful()) {
                    }
                    Tag = true;
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
        while (!Tag){}
        return responseData;
    }

}
