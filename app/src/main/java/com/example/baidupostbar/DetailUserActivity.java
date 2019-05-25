package com.example.baidupostbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import com.example.baidupostbar.Utils.CheckNetUtil;
import com.example.baidupostbar.Utils.HttpUtil;
import com.example.baidupostbar.fragment.UserBarFragment;
import com.example.baidupostbar.fragment.UserInforFragment;
import com.example.baidupostbar.fragment.UserPostFragment;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.baseadapter.BGABaseAdapterUtil;
import cn.bingoogolapple.photopicker.imageloader.BGAImage;
import cn.bingoogolapple.photopicker.util.BGAPhotoHelper;
import cn.bingoogolapple.photopicker.util.BGAPhotoPickerUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DetailUserActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private ViewPager viewpager;
    private TextView tv;
    private ImageView iv_parallax, iv_back, iv_head;
    private ButtonBarLayout buttonBarLayout;
    private CollapsingToolbarLayout collapsing_toolbar;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private TextView nickname;
    private TextView attention;
    private TextView info;
    private int mOffset = 0;
    boolean isblack = false;//状态栏字体是否是黑色
    boolean iswhite = true;//状态栏字体是否是亮色
    private TabLayout tabLayout;
    private ArrayList<String> TitleList = new ArrayList<>();  //页卡标题集合
    private ArrayList<Fragment> ViewList = new ArrayList<>();   //页卡视图集合
    private Fragment userBarFragment, userPostFragment, userInforFragment;
    private ArrayList<String>label;
    //以下有关修改头像及头像裁剪
    private static final int REQUEST_CODE_PERMISSION_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PERMISSION_TAKE_PHOTO = 2;

    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_TAKE_PHOTO = 2;
    private static final int REQUEST_CODE_CROP = 3;


    private File outputImage;
    private File postFile;
    private boolean statu;
    private String cookie;
    private String type;
    private String userId;
    private String GetUrl;
    private String birthday;
    private String interests = "";

    private BGAPhotoHelper mPhotoHelper;
    private RelativeLayout rl_allinfo;


    private String avatar;
    private String username ;
    private String gender ;
    private String description ;
    private String follower_number ;
    private String collection_number ;
    private String concern_number ;
    private String watched_bar_number ;
    private String background_pic ;
    private String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        initView();
        initViewPager();
        initListener();
        processLogic(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");
        userId = sharedPreferences.getString("user_id","");
        GetUrl = "http://139.199.84.147/mytieba.api/user/"+ userId + "/info";
        GetUserInformation();

    }

    private void initView() {
        tv = findViewById(R.id.tv_title);
        tv.setText("我的主页");
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        viewpager = (ViewPager) findViewById(R.id.view_pager);
        iv_parallax = (ImageView) findViewById(R.id.iv_parallax);
        buttonBarLayout = (ButtonBarLayout) findViewById(R.id.buttonBarLayout);
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        rl_allinfo = findViewById(R.id.rl_allinfo);
        info = findViewById(R.id.tv_info);
        attention = findViewById(R.id.tv_attention);
        nickname = findViewById(R.id.tv_name);
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
     *
     * @param
     */
    private void initViewPager() {
        viewpager = (ViewPager) findViewById(R.id.view_pager);
        userBarFragment = new UserBarFragment();
        userInforFragment = new UserInforFragment();
        userPostFragment = new UserPostFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(userPostFragment, "动态");
        adapter.addFragment(userBarFragment, "关注的吧");
        adapter.addFragment(userInforFragment, "个人资料");
        viewpager.setAdapter(adapter);
        //设置tablayout，viewpager上的标题
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        //viewpager.setCurrentItem(1);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                //setIndicator(tabLayout,30,30);
            }
        });
    }

    private void initListener() {

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                iv_parallax.setTranslationY(verticalOffset);
                //200是appbar的高度
                if (Math.abs(verticalOffset) == DensityUtil.dp2px(280) - toolbar.getHeight()) {//关闭
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
//                    toolbar.setVisibility(View.VISIBLE);
                } else {  //展开
                    if (isblack) { //变白
                        mImmersionBar.statusBarDarkFont(false).init();
                        isblack = false;
                        iswhite = true;
                    }
                    buttonBarLayout.setVisibility(View.INVISIBLE);
                    collapsing_toolbar.setContentScrimResource(R.color.transparent);
                    iv_back.setBackgroundResource(R.drawable.back_white);
                    //toolbar.setVisibility(View.INVISIBLE);
                }
            }
        });
        rl_allinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出一个选择框
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DetailUserActivity.this);
                builder.setTitle("请选择背景图片来源");
                builder.setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takePhoto();
                        type = "background";
                    }
                });
                builder.setPositiveButton("系统相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choosePhoto();
                        type = "background";
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);      //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                dialog.show();
            }
        });
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出一个选择框
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DetailUserActivity.this);
                builder.setTitle("请选择头像图片来源");
                builder.setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.cancel();该方法是隐藏dialog
                        takePhoto();
                        type = "avatar";
                    }
                });
                builder.setPositiveButton("系统相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choosePhoto();
                        type = "avatar";
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);      //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                dialog.show();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void processLogic(Bundle savedInstanceState) {
        setTitle("系统相册选择图片、裁剪");

        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        //File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");
        postFile = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");
        mPhotoHelper = new BGAPhotoHelper(postFile);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BGAPhotoHelper.onSaveInstanceState(mPhotoHelper, outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        BGAPhotoHelper.onRestoreInstanceState(mPhotoHelper, savedInstanceState);
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_CHOOSE_PHOTO)
    public void choosePhoto() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            startActivityForResult(mPhotoHelper.getChooseSystemGalleryIntent(), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "请开启存储空间权限，以正常使用该功能", REQUEST_CODE_PERMISSION_CHOOSE_PHOTO, perms);
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_TAKE_PHOTO)
    public void takePhoto() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            try {
                startActivityForResult(mPhotoHelper.getTakePhotoIntent(), REQUEST_CODE_TAKE_PHOTO);
            } catch (Exception e) {
                BGAPhotoPickerUtil.show(R.string.bga_pp_not_support_take_photo);
            }
        } else {
            EasyPermissions.requestPermissions(this, "请开启存储空间和相机权限，以正常使用该功能", REQUEST_CODE_PERMISSION_TAKE_PHOTO, perms);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
                try {
                    startActivityForResult(mPhotoHelper.getCropIntent(mPhotoHelper.getFilePathFromUri(data.getData()), 200, 200), REQUEST_CODE_CROP);
//                    CheckNetUtil checkNetUtil = new CheckNetUtil(getApplicationContext());
//                    if (checkNetUtil.initNet()) {
//                        postImage();
//                    }
                } catch (Exception e) {
                    mPhotoHelper.deleteCropFile();
                    BGAPhotoPickerUtil.show(R.string.bga_pp_not_support_crop);
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                try {
                    startActivityForResult(mPhotoHelper.getCropIntent(mPhotoHelper.getCameraFilePath(), 200, 200), REQUEST_CODE_CROP);
//                    CheckNetUtil checkNetUtil = new CheckNetUtil(getApplicationContext());
//                    if (checkNetUtil.initNet()) {
//                        postImage();
//                    }


                } catch (Exception e) {
                    mPhotoHelper.deleteCameraFile();
                    mPhotoHelper.deleteCropFile();
                    BGAPhotoPickerUtil.show(R.string.bga_pp_not_support_crop);
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CODE_CROP) {
                //mPhotoHelper.getCropFilePath()获取当前图片的路径
                if(type.equals("avatar")) {
                    BGAImage.display(iv_head, R.mipmap.bga_pp_ic_holder_light, mPhotoHelper.getCropFilePath(), BGABaseAdapterUtil.dp2px(200));
                }else if (type.equals("background")){
                    BGAImage.display(iv_parallax, R.mipmap.bga_pp_ic_holder_light, mPhotoHelper.getCropFilePath(), BGABaseAdapterUtil.dp2px(200));
                }
                CheckNetUtil checkNetUtil = new CheckNetUtil(getApplicationContext());
                if (checkNetUtil.initNet()) {
                    postImage(mPhotoHelper.getCropFilePath());
                }
            }
        } else {
            if (requestCode == REQUEST_CODE_CROP) {
                mPhotoHelper.deleteCameraFile();
                mPhotoHelper.deleteCropFile();
            }
        }
    }

        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }

        @Override
        public void onPermissionsGranted ( int requestCode, List<String > perms){
        }

        @Override
        public void onPermissionsDenied ( int requestCode, List<String > perms){
        }

//    private File convertBitmapToFile(Bitmap bitmap) {
//        try {
//            // create a file to write bitmap data
//            postFile = new File(this.getCacheDir(), "output_image.jpg");
//            postFile.createNewFile();
//            // convert bitmap to byte array
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            int num = getBitmapSize(bitmap);
//            if (num >= 20000000 && num <= 40000000) {
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
//            } else if (num < 20000000) {
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            } else if (num > 40000000) {
//                bitmap.compress(Bitmap.CompressFormat.JPEG,50 , bos);
//            }
//            byte[] bitmapdata = bos.toByteArray();
//            // write the bytes in file
//            FileOutputStream fos = new FileOutputStream(postFile);
//            fos.write(bitmapdata);
//            fos.flush();
//            fos.close();
//        } catch (Exception e) {
//
//        }
//        return postFile;
//    }
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }
    private void postImage(String filePath) {
        Log.e("DetailUserActivity",filePath);
//        if (imagePath != null) {
//            //这里可以上服务器;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
            File file = new File(filePath);
            //File file = convertBitmapToFile(bitmap);
            Log.e("PCActivity", "ok1");
            //RequestBody image = RequestBody.create(MediaType.parse("image/png"), convertBitmapToFile(bitmap));
            RequestBody image = RequestBody.create(MediaType.parse("image/png"),file );
            Log.e("DetailFileName",file.getName());
            Log.e("DetailFilePath",file.getPath());
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    // .addFormDataPart("headImage", imagePath, image)
                    .addFormDataPart("pic", file.getName() , image)
                    .addFormDataPart("type",type)
                    .addFormDataPart("user_id",userId)
                    .build();
            Log.e("PCActivity", "为啥传不上去"+ image + "type:"+type + "userId:"+ userId);
            final Request request = new Request.Builder()
                    .url("http://139.199.84.147/mytieba.api/upload/photo")
                    .addHeader("Cookie",cookie)
                    .post(requestBody)
                    .build();
            Log.e("PCActivity", "ok2");
            okHttpClient.newCall(request).enqueue(new Callback() {
                //请求错误回调方法
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("DetailUserActivity", "获取数据失败");
                    Log.e("DetailUserActivity", String.valueOf(e));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.e("DetailresponseData", responseData);
                    if (response.isSuccessful()) {
                        try {
                            Log.e("DetailUserActivity", "ok3");

                            Log.e("DetailUserActivity", "ResponseData" + responseData);
                            JSONObject jsonObject = new JSONObject(responseData);
                            statu = jsonObject.getBoolean("status");
                            if(statu) {

                                String pic = "http://139.199.84.147" + jsonObject.getString("pic");
                                SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                if(type.equals("avatar")){
                                    editor.putString("avater", pic);
                                    editor.apply();
                                }else {
                                    editor.putString("background", pic);
                                    editor.apply();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"成功",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"发送失败",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("DetailUserActivity", response.body().string());
                            Log.e("DetailUserActivity", String.valueOf(e));
                        }

                    }
                }
            });
    }

    private void GetUserInformation(){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(GetUrl)
                .addHeader("Cookie",cookie)
                .build();
        Log.e("PCActivity", "ok2");
        okHttpClient.newCall(request).enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("DetailUserActivity", "获取数据失败");
                Log.e("DetailUserActivity", String.valueOf(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.e("DetailresponseData", responseData);
                if (response.isSuccessful()) {
                    prasedWithjsonData(responseData);
                }
            }
        });

    }
    private void prasedWithjsonData(String JsonData){
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
//            boolean state = jsonObject.getBoolean("status");
//            if (state) {
            String user_id = jsonObject.getString("user_id");
            username = jsonObject.getString("username");
            gender = jsonObject.getString("gender");
            Log.e("DetailUser + gender1",gender);
            description = jsonObject.getString("description");
            birthday = jsonObject.getString("birthday");
            avatar = jsonObject.getString("avatar");
            follower_number = jsonObject.getString("follower_number");
            collection_number = jsonObject.getString("collection_number");
            concern_number = jsonObject.getString("concern_number");
            watched_bar_number = jsonObject.getString("watched_bar_number");
            background_pic = jsonObject.getString("background_pic");
            JSONArray jsonArray = jsonObject.getJSONArray("interests");
            label = new ArrayList<>();
            for (int j = 0; j < jsonArray.length(); j++) {
                String Picture = String.valueOf(jsonArray.get(j));
                label.add(Picture);
                interests = interests + Picture + "  ";
            }
            SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_id", user_id);
            editor.putString("avater", avatar);
            editor.putString("username", username);
            editor.apply();
        //}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }
    private void initData(){
        Glide.with(getApplicationContext()).load("http://139.199.84.147"+ avatar).into(iv_head);
        Glide.with(getApplicationContext()).load("http://139.199.84.147"+ background_pic).into(iv_parallax);
        nickname.setText(username);
        String atten = "关注 "+ concern_number + " | " + "粉丝 " + follower_number;
        attention.setText(atten);
        info.setText("简介："+description);
    }

    public String getGender(){
        Log.e("DetailUser + gender2",gender);
        return gender;
    }
    public String getBirthday(){
        return  birthday;
    }
    public String getLabel(){
        return interests;
    }
    public String getUserId(){return userId;}
    }
