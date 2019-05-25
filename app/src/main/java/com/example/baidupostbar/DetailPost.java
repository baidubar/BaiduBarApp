package com.example.baidupostbar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.baidupostbar.Adapter.PostDetailAdapter;
import com.example.baidupostbar.Utils.HttpUtil;
import com.example.baidupostbar.bean.PostDetail;
import com.example.baidupostbar.fragment.CommentDialogFragment;
import com.example.baidupostbar.fragment.FloorDetailFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.baseadapter.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import okhttp3.FormBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DetailPost extends RootBaseActivity implements EasyPermissions.PermissionCallbacks, BGANinePhotoLayout.Delegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener {

    private ArrayList<PostDetail> mDataList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private String postId;
    private BGANinePhotoLayout mCurrentClickNpl;
    private static final int PRC_PHOTO_PREVIEW = 1;
    private ArrayList<String>picture;
    private PostDetail postDetail;
    private int totalPage;
    private String thisFloor;
    private boolean RealConcernPeople;
    private String userId;
    private String thisPcId;
    private ImageView btn_concerd;
    boolean praise_status;
    private ImageView btn_like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        Intent intent = getIntent();
        postId = intent.getStringExtra("post_id");
        Log.e("DetailPost","postId:"+ postId);

        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", "");

        //悬浮按钮的点击事件
        //收藏当前帖子
        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.btn_collect);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //actionA.setTitle("按钮一被单击");
            }
        });
        //在当前帖子下盖楼
        final FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.btn_comment);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPost.this,SetFloorActivity.class);
                intent.putExtra("postId",postId);
                startActivity(intent);
            }
        });
        initView();
        initAdapter();
        if (praise_status){
            btn_like.setImageResource(R.drawable.like_fill);
        }
        else btn_like.setImageResource(R.drawable.like);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
        BaseQuickAdapter postDetailAdapter = new PostDetailAdapter(R.layout.item_post_floor, mDataList,DetailPost.this,getApplicationContext());
        postDetailAdapter.openLoadAnimation();
        View top = getLayoutInflater().inflate(R.layout.header_detail_post, (ViewGroup) mRecyclerView.getParent(), false);
        postDetailAdapter.addHeaderView(top);
        btn_concerd = top.findViewById(R.id.btn_concerd);
        btn_like = top.findViewById(R.id.btn_like);

        btn_concerd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //未关注时是follow，已关注时是follow_fill，自己的帖子时是delete
                //以上是图片的名字，放进btn_concerd就可以
                Log.e("DetailPost","关注:"+ RealConcernPeople);
                if(RealConcernPeople){
                    HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
                    String url = "http://139.199.84.147/mytieba.api/user/"+ userId +"/follow";
                    PostDetail postDetail = mDataList.get(0);
                    String personId = postDetail.getPersonId();
                    FormBody formBody = new FormBody.Builder()
                            .add("user_id",personId)
                            .build();
                    httpUtil.DeleteUtil(url,formBody,3);
                    doHandler();
                }
                else {
                    HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
                    String url = "http://139.199.84.147/mytieba.api/user/"+ userId +"/follow";
                    PostDetail postDetail = mDataList.get(0);
                    String personId = postDetail.getPersonId();
                    FormBody formBody = new FormBody.Builder()
                            .add("user_id",personId)
                            .build();
                    httpUtil.PostUtilsWithCookie(url,formBody,4);
                    doHandler();
                }
            }
        });
        postDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostDetail postDetail1 = mDataList.get(position+1);
                thisFloor = postDetail1.getFloor();
                FloorDetailFragment dialogFragment = new FloorDetailFragment();
                dialogFragment.show(getSupportFragmentManager(),"dialogFragment");
            }
        });
        postDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){

                    case R.id.btn_comment:
                        CommentDialogFragment commentDialogFragment = new CommentDialogFragment();
                        commentDialogFragment.show(getSupportFragmentManager(), "CommentDialogFragment");
                        Log.e("DetailPost","postdetailAdapter点击"+ postId);
                        break;
                    case R.id.btn_concerd:
                        Log.e("DetailPost","关注:"+ RealConcernPeople);
                        if(RealConcernPeople){
                            HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
                            String url = "http://139.199.84.147/mytieba.api/user/"+ userId +"/follow";
                            PostDetail postDetail = mDataList.get(position);
                            String personId = postDetail.getPersonId();
                            FormBody formBody = new FormBody.Builder()
                                    .add("user_id",personId)
                                    .build();
                            httpUtil.DeleteUtil(url,formBody,3);
                            doHandler();
                        }
                        else {
                            HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
                            String url = "http://139.199.84.147/mytieba.api/user/"+ userId +"/follow";
                            PostDetail postDetail = mDataList.get(position);
                            String personId = postDetail.getPersonId();
                            FormBody formBody = new FormBody.Builder()
                                    .add("user_id",personId)
                                    .build();
                            httpUtil.PostUtilsWithCookie(url,formBody,4);
                            doHandler();
                        }
                        break;
                    default:
                }

            }
        });


        mRecyclerView.setAdapter(postDetailAdapter);
    }

    private void initData() {

        String postUrl = "http://139.199.84.147/mytieba.api/post/"+ postId +"/detail";
        HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
        httpUtil.GetUtilWithCookie(postUrl,1);
        doHandler();
    }
    private void doHandler() {
        viewHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        Toast.makeText(getApplicationContext(),String.valueOf(msg.obj),Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        prasedWithFirstJosnData(String.valueOf(msg.obj));
                        Log.e("ListBArActivity", String.valueOf(msg.obj));
                        break;
                    case 2:
                        prasedWithJsonData(String.valueOf(msg.obj));
                        Log.e("ListBArActivity", String.valueOf(msg.obj));
                        break;
                    case 3:
                        prasedWithType3(String.valueOf(msg.obj));
                        break;
                    case 4:
                        prasedWithType4(String.valueOf(msg.obj));
                        break;
                    default:
                        break;
                }
            }

        };
    }
    private void prasedWithFirstJosnData(String JsonData) {
        mDataList = new ArrayList<>();
        Log.e("DetailPost1",JsonData);
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            Boolean status = jsonObject.getBoolean("status");
            if(status) {
                boolean collection_status = jsonObject.getBoolean("collection_status");
                praise_status = jsonObject.getBoolean("praise_status");
                String use_id = jsonObject.getString("user_id");
//                String floor = jsonObject.getString("floor");
                JSONObject jsonObject1 = jsonObject.getJSONObject("post_msg");
                String person_id = jsonObject1.getString("person_id");
                String person_name = jsonObject1.getString("person_name");
                Log.e("DetailPost","personName:"+ person_name);
                String person_avatar = jsonObject1.getString("person_avatar");
                boolean follow_status = jsonObject1.getBoolean("follow_status");
                RealConcernPeople = follow_status;
                String time = jsonObject1.getString("time");
                JSONArray jsonArray1 = jsonObject1.getJSONArray("pic");
                picture = new ArrayList<>();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    String pic = "http://139.199.84.147/media/" + jsonArray1.get(j);
                    picture.add(pic);
                }
                String content = jsonObject1.getString("content");
                String bar_id = jsonObject1.getString("bar_id");
                String bar = jsonObject1.getString("bar");
                postDetail = new PostDetail();
                postDetail.setContent(content);
                postDetail.setBarName(bar);
                postDetail.setTime(time);
                postDetail.setPersonId(person_id);
                postDetail.setPhoto(picture);
                postDetail.setUserName(person_name);
                postDetail.setCollection_status(follow_status);
                postDetail.setUserImg("http://139.199.84.147" + person_avatar);
                mDataList.add(postDetail);
            }else {
                Toast.makeText(getApplicationContext(),"网络请求失败",Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        initRemark();
    }
    private void prasedWithJsonData(String JsonData){
        Log.e("DetailPost","Pj:"+ JsonData);
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            totalPage = jsonObject.getInt("total_page");
                String number = jsonObject.getString("number");
                if (!number.equals("0")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("floor_info");
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String floor = jsonObject1.getString("floor");
                        String person_id = jsonObject1.getString("person_id");
                        String person_name = jsonObject1.getString("person_name");
                        String person_avatar = jsonObject1.getString("person_avatar");
                        String datetime = jsonObject1.getString("datetime");
                        String content = jsonObject1.getString("content");

                        Log.e("DetailPost",content);

                        postDetail = new PostDetail();
                        postDetail.setContent(content);
                        postDetail.setTime(datetime);
                        postDetail.setUserName(person_name);
                        postDetail.setPersonId(person_id);
                        postDetail.setFloor(floor);
                        Log.e("DetailPost","floor1"+floor);
                        postDetail.setUserImg("http://139.199.84.147" + person_avatar);
                        postDetail.setFloor(floor);
                        mDataList.add(postDetail);
                    }

                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initAdapter();
    }
    @AfterPermissionGranted(PRC_PHOTO_PREVIEW)
    private void photoPreviewWrapper() {
        if (mCurrentClickNpl == null) {
            return;
        }

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getApplicationContext(), perms)) {
            File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");
            BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(getApplicationContext())
                    .saveImgDir(downloadDir); // 保存图片的目录，如果传 null，则没有保存图片功能

            if (mCurrentClickNpl.getItemCount() == 1) {
                // 预览单张图片
                photoPreviewIntentBuilder.previewPhoto(mCurrentClickNpl.getCurrentClickItem());
            } else if (mCurrentClickNpl.getItemCount() > 1) {
                // 预览多张图片
                photoPreviewIntentBuilder.previewPhotos(mCurrentClickNpl.getData())
                        .currentPosition(mCurrentClickNpl.getCurrentClickItemPosition()); // 当前预览图片的索引
            }
            startActivity(photoPreviewIntentBuilder.build());
        } else {
            EasyPermissions.requestPermissions(this, "图片预览需要以下权限:\n\n1.访问设备上的照片", PRC_PHOTO_PREVIEW, perms);
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
        if (requestCode == PRC_PHOTO_PREVIEW) {
            Toast.makeText(getApplicationContext(), "您拒绝了「图片预览」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        mCurrentClickNpl = ninePhotoLayout;
        photoPreviewWrapper();
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

    }

    @Override
    public boolean onRVItemLongClick(ViewGroup parent, View itemView, int position) {
        return false;
    }
    private void initRemark(){
        String remarkUrl = "http://139.199.84.147/mytieba.api/post/"+ postId + "/comment";
        HttpUtil httpUtil2 = new HttpUtil(DetailPost.this,getApplicationContext());
        httpUtil2.GetUtilWithCookie(remarkUrl,2);
        doHandler();
    }

    private void prasedWithType3(String JosnData){
        Log.e("DetailPost","3:"+JosnData);
        try {
            JSONObject jsonObject = new JSONObject(JosnData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                RealConcernPeople = false;
                Toast.makeText(getApplicationContext(),"成功",Toast.LENGTH_LONG).show();
            }else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void prasedWithType4(String JosnData){
        try {
            Log.e("DetailPost","4:"+JosnData);
            JSONObject jsonObject = new JSONObject(JosnData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                RealConcernPeople = true;
                Toast.makeText(getApplicationContext(),"成功",Toast.LENGTH_LONG).show();
            }else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPostId() {
        return postId;
    }
    private String getThisPcId(){
        return thisPcId;
    }
    public String getThisFloor(){
        return thisFloor;
    }
}
