package com.example.baidupostbar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baidupostbar.model.Moment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

    /**
     * 拖拽排序九宫格控件
     */
    private BGASortableNinePhotoLayout mPhotosSnpl;
    // ==================================== 测试拖拽排序九宫格图片控件 END ====================================

    private EditText mContentEt;
    private ArrayList<String>photo;

    //这里原demo中的用法：创建新帖并添加到recyclerview的顶部
//    public static Moment getMoment(Intent intent) {
//        return intent.getParcelableExtra(EXTRA_MOMENT);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
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
//                Intent intent = new Intent(CreatePostActivity.this, DetailBarActivity.class);
//                startActivity(intent);
//                finish();
                String content = mContentEt.getText().toString();
                Log.e("CreatPost","content:"+ content);
                Log.e("CreatPost","photo:"+ photo);
//                for(int i = 0;i<photo.size();i++){
//                    String a = photo.get(i);
//                    Log.e("CreatPost","a:"+ a);
//                }
                if(!content.equals("0")){

                }else {
                    Toast.makeText(getApplicationContext(),"请填入内容",Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
        return true;
    }

    public void onClick(View v) {
        //tv_moment_add_publish是原demo中的发表方法，我先不删掉，可以借鉴
//        if (v.getId() == R.id.tv_moment_add_choice_photo) {
//            choicePhotoWrapper();
//        } else if (v.getId() == R.id.tv_moment_add_publish) {
//            String content = mContentEt.getText().toString().trim();
//            if (content.length() == 0 && mPhotosSnpl.getItemCount() == 0) {
//                Toast.makeText(this, "必须填写这一刻的想法或选择照片！", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
            Intent intent = new Intent();
            intent.putExtra(EXTRA_MOMENT, new Moment(mContentEt.getText().toString().trim(), mPhotosSnpl.getData()));
            setResult(RESULT_OK, intent);
            finish();
//        }
        mContentEt = v.findViewById(R.id.et_moment_add_content);
        String content = mContentEt.getText().toString();
        Log.e("CreatPost","content:"+ content);
        Log.e("CreatPost","photo:"+ photo);
//       ,
        if(!content.equals("0")){

        }else {
            Toast.makeText(getApplicationContext(),"请填入内容",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
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
            Log.e("CreatePost","takePhotoDir"+ takePhotoDir.getName());
            getSelectedPhotos(photoPickerIntent);
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
            photo = BGAPhotoPickerActivity.getSelectedPhotos(data);
            Log.e("TAGG1", String.valueOf(photo));
        } else if (requestCode == RC_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
//            photo = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data);
//            Log.e("TAGG2", String.valueOf(photo));
        }
    }

//    private void postImage(String filePath) {
//        Log.e("DetailUserActivity",filePath);
////        if (imagePath != null) {
////            //这里可以上服务器;
//        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .retryOnConnectionFailure(true)
//                .connectTimeout(20, TimeUnit.SECONDS)
//                .writeTimeout(20, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS)
//                .build();
//        File file = new File(filePath);
//        //File file = convertBitmapToFile(bitmap);
//        Log.e("PCActivity", "ok1");
//        //RequestBody image = RequestBody.create(MediaType.parse("image/png"), convertBitmapToFile(bitmap));
//        RequestBody image = RequestBody.create(MediaType.parse("image/png"),file );
//        Log.e("DetailFileName",file.getName());
//        Log.e("DetailFilePath",file.getPath());
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                // .addFormDataPart("headImage", imagePath, image)
//                .addFormDataPart("pic", file.getName() , image)
//                .addFormDataPart("type",type)
//                .addFormDataPart("user_id",userId)
//                .build();
//        Log.e("PCActivity", "为啥传不上去"+ image + "type:"+type + "userId:"+ userId);
//        final Request request = new Request.Builder()
//                .url("http://139.199.84.147/mytieba.api/upload/photo")
//                .addHeader("Cookie",cookie)
//                .post(requestBody)
//                .build();
//        Log.e("PCActivity", "ok2");
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            //请求错误回调方法
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("DetailUserActivity", "获取数据失败");
//                Log.e("DetailUserActivity", String.valueOf(e));
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseData = response.body().string();
//                Log.e("DetailresponseData", responseData);
//                if (response.isSuccessful()) {
//                    try {
//                        Log.e("DetailUserActivity", "ok3");
//
//                        Log.e("DetailUserActivity", "ResponseData" + responseData);
//                        JSONObject jsonObject = new JSONObject(responseData);
//                        statu = jsonObject.getBoolean("status");
//                        if(statu) {
//
//                            String pic = "http://139.199.84.147" + jsonObject.getString("pic");
//                            SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            if(type.equals("avatar")){
//                                editor.putString("avater", pic);
//                                editor.apply();
//                            }else {
//                                editor.putString("background", pic);
//                                editor.apply();
//                            }
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getApplicationContext(),"成功",Toast.LENGTH_LONG).show();
//                                }
//                            });
//                        }else {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getApplicationContext(),"发送失败",Toast.LENGTH_LONG).show();
//                                }
//                            });
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Log.e("DetailUserActivity", response.body().string());
//                        Log.e("DetailUserActivity", String.valueOf(e));
//                    }
//
//                }
//            }
//        });
//    }
    public static ArrayList<String> getSelectedPhotos(Intent intent) {
        return intent.getStringArrayListExtra(EXTRA_SELECTED_PHOTOS);
    }

//    public BGAPhotoPickerPreviewActivity.IntentBuilder previewPhotos(ArrayList<String> previewPhotos) {
//        mIntent.putStringArrayListExtra("EXTRA_PREVIEW_PHOTOS", previewPhotos);
//        return this;
//    }



}
