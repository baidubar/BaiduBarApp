package com.example.baidupostbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.baidupostbar.Adapter.ViewPagerAdapter;
import com.example.baidupostbar.fragment.UserBarFragment;
import com.example.baidupostbar.fragment.UserInforFragment;
import com.example.baidupostbar.fragment.UserPostFragment;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomepageActivity extends BaseActivity {
    private ViewPager viewpager;
    private TextView tv_attention,tv_nickname,tv_attentionNum,info;
    private ImageView iv_parallax,iv_back,iv_head;
    private ButtonBarLayout buttonBarLayout;
    private CollapsingToolbarLayout collapsing_toolbar;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private int mOffset = 0;
    boolean isblack = false;//状态栏字体是否是黑色
    boolean iswhite = true;//状态栏字体是否是亮色
    private TabLayout tabLayout;
    private RelativeLayout allinfo;
    private ArrayList<String> TitleList = new ArrayList<>();  //页卡标题集合
    private ArrayList<Fragment> ViewList = new ArrayList<>();   //页卡视图集合
    private Fragment userBarFragment,userPostFragment,userInforFragment;
    private String userId;
    private String username;
    private String gender;
    private String description;
    private String birthday;
    private String avatar;
    private String follower_number;
    private String collection_number;
    private String concern_number;
    private String background_pic;
    private String interests;
    private TextView tv_gender;
    private TextView tv_birthday;
    private TextView tv_interest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        initView();
        initViewPager();
        initListener();
        initData();

    }
    private void initView() {
        tv_attention = findViewById(R.id.tv_attention);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        viewpager = (ViewPager) findViewById(R.id.view_pager);
        iv_parallax = (ImageView) findViewById(R.id.iv_parallax);
        buttonBarLayout = (ButtonBarLayout) findViewById(R.id.buttonBarLayout);
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        allinfo  = findViewById(R.id.rl_allinfo);
        tv_attention = findViewById(R.id.tv_attention);
        tv_attentionNum = findViewById(R.id.tv_attentionNum);
        tv_nickname = findViewById(R.id.nickname);
        info = findViewById(R.id.info);
        tv_gender = findViewById(R.id.tv_gender);
        tv_birthday = findViewById(R.id.tv_birthDay);
        tv_interest = findViewById(R.id.tv_interest);

        //初始化沉浸式

        mImmersionBar.titleBar(toolbar).init();
        RequestOptions options = new RequestOptions();
        //这里是原demo中，设置头像图片的方法，可以参考，原demo直接用的imageView，所以用了circleCrop，我们的用的就是circleImageView
//        Glide.with(this).load(R.mipmap.head)
//                .apply(options.circleCrop())
//                .into(iv_head);

    }

    /**
     * 设置item
     * @param
     */
    private void initViewPager() {
        //viewpager = (ViewPager) findViewById(R.id.view_pager);
        userBarFragment = new UserBarFragment();
        userInforFragment = new UserInforFragment();
        userPostFragment = new UserPostFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(userPostFragment, "动态");
        adapter.addFragment(userBarFragment, "关注的吧");
        adapter.addFragment(userInforFragment, "个人资料");
//        viewpager.setAdapter(adapter);
//        //设置tablayout，viewpager上的标题
//        tabLayout.setupWithViewPager(viewpager);
//        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
//        //viewpager.setCurrentItem(1);
//        tabLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                //setIndicator(tabLayout,30,30);
//            }
//        });
    }

    private void initListener() {

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                iv_parallax.setTranslationY(verticalOffset);
                //200是appbar的高度
                if (Math.abs(verticalOffset) == DensityUtil.dp2px(300) - toolbar.getHeight()) {//关闭
                    if (iswhite) {//变黑
                        if (ImmersionBar.isSupportStatusBarDarkFont()) {
                            mImmersionBar.statusBarDarkFont(true).init();
                            isblack = true;
                            iswhite = false;
                        }
                    }
                    buttonBarLayout.setVisibility(View.VISIBLE);
                    collapsing_toolbar.setContentScrimResource(R.color.white);
                    iv_back.setBackgroundResource(R.drawable.back_black);
                    tv_attention.setTextColor(getResources().getColor(R.color.black));
                } else {  //展开
                    if (isblack) { //变白
                        mImmersionBar.statusBarDarkFont(false).init();
                        isblack = false;
                        iswhite = true;
                    }
                    buttonBarLayout.setVisibility(View.INVISIBLE);
                    collapsing_toolbar.setContentScrimResource(R.color.transparent);
                    iv_back.setBackgroundResource(R.drawable.back_white);
                    tv_attention.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initData(){
        String url = "http://139.199.84.147/mytieba.api/user/"+ userId +"/info";
        Log.e("Homepage","url" + url);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        String cookie = sharedPreferences.getString("cookie", "");
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie",cookie)
                .build();
        Log.e("PCActivity", "ok2");
        okHttpClient.newCall(request).enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("DetailUserActivity", "获取数据失败");
                Log.e("DetailUserActivity", String.valueOf(e));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"获取数据失败",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.e("DetailresponseData", responseData);
                if (response.isSuccessful()) {
                    prasedWithjsonData(responseData);
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"获取数据失败",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
    private void prasedWithjsonData(String JsonData){
        Log.e("Homepage",JsonData);
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            userId = jsonObject.getString("user_id");
            username = jsonObject.getString("username");
            description = jsonObject.getString("description");
            gender = jsonObject.getString("gender");
            birthday = jsonObject.getString("birthday");
            avatar = "http://139.199.84.147/" + jsonObject.getString("avatar");
            follower_number = jsonObject.getString("follower_number");
            concern_number = jsonObject.getString("concern_number");
            background_pic = "http://139.199.84.147/" + jsonObject.getString("background_pic");
            JSONArray jsonArray = jsonObject.getJSONArray("interests");
            ArrayList<String>label = new ArrayList<>();
            for (int j = 0; j < jsonArray.length(); j++) {
                String Picture = String.valueOf(jsonArray.get(j));
                label.add(Picture);
                if(j==0){
                    interests = Picture + "  ";
                }else {
                    interests = interests + Picture + "  ";
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_nickname.setText(username);
                String att = "关注 " +follower_number+"  |  "+"粉丝 "+ concern_number;
                tv_attentionNum.setText(att);
                Log.e("Homepage",description);
                info.setText("简介："+description);
                Glide.with(getApplicationContext()).load( avatar).into(iv_head);
                Glide.with(getApplicationContext()).load( background_pic).into(iv_parallax);
                tv_birthday.setText(birthday);
                if(gender.equals("0")){
                    tv_gender.setText("男");
                }else if(gender.equals("1")){
                    tv_gender.setText("女");
                }else if (gender.equals("2")){
                    tv_gender.setText("保密");
                }
                tv_interest.setText(interests);
            }
        });

    }
}
