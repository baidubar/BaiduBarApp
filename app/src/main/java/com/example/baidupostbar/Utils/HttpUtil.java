package com.example.baidupostbar.Utils;

import android.telecom.Call;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    boolean Tag = false;
    private String responseData;
    public String PostUtils(String url, FormBody formBody){
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

                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    responseData = response.body().string();
                    if (response.isSuccessful()) {
                        Tag = true;
                    }

                }

            });
        }catch (Exception e) {
            e.printStackTrace();
        }
        if (Tag) {
            return responseData;
        }else {
            return null;
        }
    }
    public String DeleteUtil(String url, FormBody formBody){
        try {
            Tag = false;
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
                        Tag = true;
                    }

                }

            });
        }catch (Exception e) {
            e.printStackTrace();
        }
        if (Tag) {
            return responseData;
        }else {
            return null;
        }
    }
    public String GetUtil(String url){
        try {
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
                        Tag = true;
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
        if (Tag) {
            return responseData;
        }else {
            return null;
        }
    }

}
