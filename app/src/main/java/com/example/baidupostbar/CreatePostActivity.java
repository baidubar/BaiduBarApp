package com.example.baidupostbar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baidupostbar.model.Moment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CreatePostActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, BGASortableNinePhotoLayout.Delegate  {

    Toolbar toolbar;
    private static final int PRC_PHOTO_PICKER = 1;

    private static final int RC_CHOOSE_PHOTO = 1;
    private static final int RC_PHOTO_PREVIEW = 2;

    private static final String EXTRA_SELECTED_PHOTOS = "EXTRA_SELECTED_PHOTOS";

    private static final String EXTRA_MOMENT = "EXTRA_MOMENT";
    private MultipartBody.Builder builder;

    /**
     * 拖拽排序九宫格控件
     */
    private BGASortableNinePhotoLayout mPhotosSnpl;
    // ==================================== 测试拖拽排序九宫格图片控件 END ====================================

    private EditText mContentEt;
    private ArrayList<String>photo;
    private String cookie;
    private String userId;
    private String bar_id;
    private String content;
    private File postFile;
    private TextView tv_countNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
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
        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");
        userId = sharedPreferences.getString("user_id","");

        Intent intent = getIntent();
        bar_id = intent.getStringExtra("bar_id");
        mContentEt = findViewById(R.id.et_moment_add_content);
        mPhotosSnpl = findViewById(R.id.snpl_moment_add_photos);
        mPhotosSnpl.setMaxItemCount(9);
        mPhotosSnpl.setEditable(true);
        mPhotosSnpl.setPlusEnable(true);
        mPhotosSnpl.setSortable(true);
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        builder = new MultipartBody.Builder();
        tv_countNum = findViewById(R.id.countNum);
        mContentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = mContentEt.getText().toString();
                int num = content.length();
                tv_countNum.setText(num +"/200");
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_post, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_publish:
                photo = mPhotosSnpl.getData();
                content = mContentEt.getText().toString();
                Log.e("CreatPost","content:"+ content);
                Log.e("CreatPost","photo:"+ photo);
                content = trim(content);
                if(!content.equals("")){
                    MultipartBody.Builder builder = new MultipartBody.Builder();

                    for(int i = 0;i<photo.size();i++){
                        String filePath = photo.get(i);
                        filePath = compressImage(filePath,getApplicationContext());
                        File file = new File(filePath);
                        RequestBody image = RequestBody.create(MediaType.parse("image/png"),file );
                        builder.addFormDataPart("pic",file.getName(),image);
//                        long num = file.length();


                    }
                    RequestBody requestBody = builder
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("user_id",userId)
                            .addFormDataPart("bar_id",bar_id)
                            .addFormDataPart("content",content)
                            .addFormDataPart("title","0")
                            .build();
                    postImage(requestBody);
                }else {
                    Toast.makeText(getApplicationContext(),"请填入内容",Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
        return true;
    }

    public void onClick(View v) {
        String content = mContentEt.getText().toString().trim();
        if (content.length() == 0 && mPhotosSnpl.getItemCount() == 0) {
            Toast.makeText(this, "必须填写这一刻的想法或选择照片！", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_MOMENT, new Moment(mContentEt.getText().toString().trim(), mPhotosSnpl.getData()));
        setResult(RESULT_OK, intent);
        Log.e("TAGGGGGGGGGGGG","获取图片路径了吗？"+ mPhotosSnpl.getData());
        finish();
        Log.e("CreatPost","content:"+ content);
        Log.e("CreatPost","mPhotoSnpl:"+ mPhotosSnpl.getData());
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
        Log.e("TAG", String.valueOf(mContentEt));
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(this)
                .previewPhotos(models) // 当前预览的图片路径集合
                .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(mPhotosSnpl.getMaxItemCount()) // 图片选择张数的最大值
                .currentPosition(position) // 当前预览图片的索引
                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                .build();
        startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW);
        Log.e("CreatePost","models"+ models);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {
        Toast.makeText(this, "排序发生变化", Toast.LENGTH_SHORT).show();
    }

    @AfterPermissionGranted(PRC_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");

            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                    .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                    .maxChooseCount(mPhotosSnpl.getMaxItemCount() - mPhotosSnpl.getItemCount()) // 图片选择张数的最大值
                    .selectedPhotos(null) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                    .build();
            startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", PRC_PHOTO_PICKER, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PRC_PHOTO_PICKER) {
            Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {

            mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));
//            photo = BGAPhotoPickerActivity.getSelectedPhotos(data);
           // File file = new File(photo.get(0));
//            Log.e("TAGG1", String.valueOf(photo));
//            RequestBody image = RequestBody.create(MediaType.parse("image/png"),file );
//            builder.addFormDataPart("pic",file.getName(),image);
        } else if (requestCode == RC_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
//            photo = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data);
//            Log.e("TAGG2", String.valueOf(photo));
        }
    }

    private void postImage(RequestBody requestBody) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url("http://139.199.84.147/mytieba.api/posts")
                .addHeader("Cookie",cookie)
                .post(requestBody)
                .build();
        Log.e("PCActivity", "ok2");
        okHttpClient.newCall(request).enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("CreatePost", "获取数据失败");
                Log.e("CreatePost", String.valueOf(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.e("CreateresponseData", responseData);
                if (response.isSuccessful()) {
                    try {
                        Log.e("CreatePost", "ok3");

                        Log.e("CreatePost", "ResponseData" + responseData);
                        JSONObject jsonObject = new JSONObject(responseData);
                        boolean statu = jsonObject.getBoolean("status");
                        if(statu) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"上传成功",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        }else {
                            Toast.makeText(getApplicationContext(),"上传失败",Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"请求失败",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    public static String trim(String s){
        String result = "";
        if(null!=s && !"".equals(s)){
            result = s.replaceAll("[\\s]", "").replaceAll("[\\s]", "");
        }
        return result;
    }
    public static String compressImage(String mCurrentPhotoPath, Context context) {
        if (mCurrentPhotoPath != null) {
            try {
                File f = new File(mCurrentPhotoPath);
        Bitmap bm = getSmallBitmap(mCurrentPhotoPath);
        //获取文件路径 即：/data/data/***/files目录下的文件
        String path = context.getFilesDir().getPath();
//                Log.e(TAG, "compressImage:path== "+path );
        //获取缓存路径
        File cacheDir = context.getCacheDir();
//                Log.e(TAG, "compressImage:cacheDir== "+cacheDir );
//                File newfile = new File(
//                getAlbumDir(), "small_" + f.getName());
        File newfile = new File( cacheDir, "small_" + f.getName());
        FileOutputStream fos = new FileOutputStream(newfile);
        bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        return newfile.getPath();
            } catch (Exception e) {
                Log.e("TAG", "error", e);
            }
        }
        else {
            Log.e("TAG", "save: 图片路径为空");
        } return mCurrentPhotoPath;
    }
    public static Bitmap getSmallBitmap(String filePath) { final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) { final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) { final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        } return inSampleSize;
    }
}
