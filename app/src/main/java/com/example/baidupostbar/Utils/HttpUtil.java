package com.example.baidupostbar.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.baidupostbar.RootBaseActivity;

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

    private String responseData = null;
    private Context context;
    private String cookie;
    private RootBaseActivity ctx;
    private static HttpUtil mInstance;
    private OkHttpClient mOkHttpClient;

    public HttpUtil(RootBaseActivity rba,Context context){
        this.context = context;
        this.ctx = rba;
    }
    public static HttpUtil getInstance() {
        if (mInstance == null){
            synchronized (HttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtil();
                }
            }
        }
        return mInstance;
    }

    private HttpUtil() {
        mOkHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public void PostUtils(String url, FormBody formBody,int type ){
        responseData = null;
        if (initNet()) {
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .addHeader("Connection", "close")
                        .build();

                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        ctx.viewHandler.obtainMessage(0, "网络请求失败！").sendToTarget();
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        responseData = response.body().string();
                        if (response.isSuccessful()) {
                            Log.e("posr", responseData);
                            ctx.viewHandler.obtainMessage(type, "网络请求失败！").sendToTarget();
                        }
                        ctx.viewHandler.obtainMessage(0, "服务器请求失败！").sendToTarget();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                ctx.viewHandler.obtainMessage(0, "网络请求失败！").sendToTarget();
            }
        }else {
            ctx.viewHandler.obtainMessage(0, "网络未连接").sendToTarget();
        }
    }
    public void PostUtilsWithCookie(String url, FormBody formBody,int type){

        responseData = null;
        if(initNet()){
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
                        ctx.viewHandler.obtainMessage(0, "网络请求失败！").sendToTarget();
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        responseData = response.body().string();
                        Log.e("PostWithCookieResponse",responseData);
                        if (response.isSuccessful()) {
                            ctx.viewHandler.obtainMessage(type, responseData).sendToTarget();
                        }else {
                            ctx.viewHandler.obtainMessage(0, "服务器请求失败！").sendToTarget();
                        }
                    }
                });
                Log.e("post","4");
            }catch (Exception e) {
                e.printStackTrace();
                ctx.viewHandler.obtainMessage(0, "网络请求失败！").sendToTarget();
            }
        }else {
            ctx.viewHandler.obtainMessage(0, "网络未连接！").sendToTarget();
        }
    }

    public void DeleteUtil(String url, FormBody formBody,int type){
        if (initNet()) {
            try {
                responseData = null;
                Request request = new Request.Builder()
                        .url(url)
                        .delete(formBody)
                        .addHeader("Connection", "close")
                        .addHeader("Cookie",cookie)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        Log.e("onFailure", "获取数据失败");
                        ctx.viewHandler.obtainMessage(0, "网络请求失败！").sendToTarget();
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        responseData = response.body().string();
                        if (response.isSuccessful()) {
                            ctx.viewHandler.obtainMessage(type, responseData).sendToTarget();
                        } else {
                            ctx.viewHandler.obtainMessage(0, "服务器请求失败！").sendToTarget();
                        }
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                ctx.viewHandler.obtainMessage(0, "网络请求失败！").sendToTarget();
            }
        }else {
            ctx.viewHandler.obtainMessage(0, "网络未连接！").sendToTarget();
        }
    }
    public void GetUtil(String url,int type){
        if (initNet()) {
            try {
                mOkHttpClient = new OkHttpClient();
                SharedPreferences sharedPreferences = context.getSharedPreferences("theUser", Context.MODE_PRIVATE);
                cookie = sharedPreferences.getString("cookie", "");
                responseData = null;
                Request request = new Request.Builder()
                        .addHeader("Connection", "close")
                        .addHeader("Cookie",cookie)
                        .url(url)
                        .build();

                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        Log.e("onFailure","获取数据失败");
                        ctx.viewHandler.obtainMessage(0, "网络请求失败！").sendToTarget();
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        responseData = response.body().string();
                        if (response.isSuccessful()){
                            ctx.viewHandler.obtainMessage(type, responseData).sendToTarget();
                            Log.e("GetUtils",responseData);
                        }else {
                            ctx.viewHandler.obtainMessage(0, "服务器请求失败！").sendToTarget();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("GetUtils", String.valueOf(e));
            }
        }else {
            ctx.viewHandler.obtainMessage(0, "网络未连接！").sendToTarget();
        }
    }
    public void GetUtilWithCookie(String url,int type){
        Log.e("GetWithCookie", "url"+ url);
        if (initNet()) {
            try {
                SharedPreferences sharedPreferences = context.getSharedPreferences("theUser", Context.MODE_PRIVATE);
                cookie = sharedPreferences.getString("cookie", "");
                responseData = null;
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
//                        .cookieJar(new CookieJar() {
//                            @Override
//                            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
//                                Log.e("PostWithCookie","保存cookie");
//                                Log.e("PostCookie", String.valueOf(cookies));
//                                for (int i = 0;i<cookies.size();i++){
//                                    Log.e("Cookie"+ i, String.valueOf(cookies.get(i)));
//                                    cookie = String.valueOf(cookies.get(i));
//                                }
//                                SharedPreferences sharedPreferences = context.getSharedPreferences("theUser", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.putString("cookie",cookie);
//                                editor.apply();
//                                Log.e("post","2");
//
//                            }
//                            @Override
//                            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
//                                List<Cookie> cookies = new ArrayList<>();
////                            for (int i = 0;i<cookies.size();i++){
////                                Log.e("Cookie"+ i, String.valueOf(cookies.get(i)));
////                                cookie = String.valueOf(cookies.get(i));
////                            }
//                                return cookies != null ? cookies : new ArrayList<Cookie>();
//                            }
//                        })
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Connection", "close")
                        .addHeader("Cookie",cookie)
                        .build();
                Log.e("GetUtilsWithCookie",cookie);
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        Log.e("onFailure","获取数据失败");
                        ctx.viewHandler.obtainMessage(0, "网络请求失败！").sendToTarget();
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        responseData = response.body().string();
                        if (response.isSuccessful()) {
                            ctx.viewHandler.obtainMessage(type, responseData).sendToTarget();
                        }else {
                            ctx.viewHandler.obtainMessage(0, "服务器请求失败！").sendToTarget();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                ctx.viewHandler.obtainMessage(0, "网络请求失败！").sendToTarget();
            }

        }
    }
    public boolean initNet() {
        if (isNet())
            return true;
        else {
            Toast.makeText(context, "没有网络哦！", Toast.LENGTH_SHORT).show();
//            AlertDialog dialog = new AlertDialog.Builder(context)
//                    .setTitle("设置网络")
//                    .setMessage("是否进行网络设置")
//                    .setNegativeButton("取消", null)
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            context.startActivity(new Intent(
//                                    Settings.ACTION_WIRELESS_SETTINGS));
//                        }
//                    })
//                    .create();
//            dialog.show();
            return false;
        }
    }
    private boolean isNet() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }















}
