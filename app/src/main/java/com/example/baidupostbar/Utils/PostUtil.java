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

public class PostUtil {
    private String url;
    private FormBody formBody;
    private String reponsedata;
    public String PostUtils(String url, FormBody formBody){
        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
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
                    String responseData = response.body().string();

                    if (response.isSuccessful()) {

                    }

                }

            });
        }catch (Exception e) {
            e.printStackTrace();
        }
        return reponsedata;
    }

}
