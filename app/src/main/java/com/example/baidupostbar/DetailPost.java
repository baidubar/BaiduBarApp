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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.baidupostbar.Adapter.PostDetailAdapter;
import com.example.baidupostbar.Utils.CheckNetUtil;
import com.example.baidupostbar.Utils.HttpUtil;
import com.example.baidupostbar.bean.PostDetail;
import com.example.baidupostbar.fragment.CommentDialogFragment;
import com.example.baidupostbar.fragment.FloorDetailFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

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
    private boolean collection_status;
    private boolean RealPraise;
    private ImageView btn_concerd;
    private ImageView iv_praise;
    private ImageView iv_headImage;
    private FloatingActionButton actionA;
    PullToRefreshLayout pullToRefreshLayout;
    private String url;
    private int page = 1;
    private BaseQuickAdapter postDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        initView();
        initAdapter();

        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.activity_main);
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mDataList.clear();
                //sendRequestWithOkHttp();//请求数据，不用带lastId
                if (new CheckNetUtil(getApplicationContext()).initNet()) {}
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DetailPost.this,"刷新成功",Toast.LENGTH_LONG).show();
                        // 结束刷新
                        pullToRefreshLayout.finishRefresh();
                    }
                }, 1000);
            }

            @Override
            public void loadMore() {
                //sendRequestWithOkHttp(lastId);//加载更多，要带lastId，我已经取好了
                if(page<=totalPage) {
                    page++;
                    String remarkUrl = "http://139.199.84.147/mytieba.api/post/" + postId + "/comment" + "?page=" + page;
                    if (new CheckNetUtil(getApplicationContext()).initNet()) {
                        initRefreshData(remarkUrl);
                    }
                    Log.e("DetailPost","remarkUrl:"+ remarkUrl);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束加载更多
                        Toast.makeText(DetailPost.this,"刷新成功",Toast.LENGTH_LONG).show();
                        pullToRefreshLayout.finishLoadMore();
                    }
                }, 1000);
            }
        });


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

        getData();
//        initData();
        //悬浮按钮的点击事件
        //收藏当前帖子
        actionA = (FloatingActionButton) findViewById(R.id.btn_collect);

        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //收藏按钮收藏状态点击事件
                if(collection_status){
                    HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
                    String url = "http://139.199.84.147/mytieba.api/user/"+ userId +"/collection";
                    FormBody formBody = new FormBody.Builder()
                            .add("post_id",postId)
                            .build();
                    httpUtil.DeleteUtil(url,formBody,6);
                    doHandler();
                    //收藏按钮未收藏状态点击事件
                }else {
                    HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
                    String url = "http://139.199.84.147/mytieba.api/user/"+ userId +"/collection";
                    FormBody formBody = new FormBody.Builder()
                            .add("post_id",postId)
                            .build();
                    httpUtil.PostUtilsWithCookie(url,formBody,5);
                    doHandler();

                }
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


    }

    //onResume重新联网请求
    @Override
    protected void onResume() {
        super.onResume();
        if (new CheckNetUtil(getApplicationContext()).initNet()) {
            initData();
        }

    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //adapter和头部点击事件
    @SuppressWarnings("unchecked")
    private void initAdapter() {
        postDetailAdapter = new PostDetailAdapter(R.layout.item_post_floor, mDataList,DetailPost.this,getApplicationContext());
        postDetailAdapter.openLoadAnimation();
        View top = getLayoutInflater().inflate(R.layout.header_detail_post, (ViewGroup) mRecyclerView.getParent(), false);
        postDetailAdapter.addHeaderView(top);

        btn_concerd = top.findViewById(R.id.btn_concerd);
        iv_praise = top.findViewById(R.id.btn_like);
        iv_headImage = top.findViewById(R.id.iv_author);

        //关注用户按钮
        btn_concerd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //未关注时是follow，已关注时是follow_fill，自己的帖子时是delete
                //以上是图片的名字，放进btn_concerd就可以
                //从mDataList获取楼主id
                PostDetail postDetail = mDataList.get(0);
                String personId = postDetail.getPersonId();
                //用户自己的帖子点击事件(删除帖子并退出)
                if(personId.equals(userId)){
                    String url = "http://139.199.84.147/mytieba.api/posts";
                    HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
                    FormBody formBody = new FormBody.Builder()
                            .add("user_id",userId)
                            .add("post_id",postId)
                            .build();
                    httpUtil.DeleteUtil(url,formBody,7);
                    doHandler();
                    //如果关注了楼主
                }else if(RealConcernPeople){
                    HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
                    String url = "http://139.199.84.147/mytieba.api/user/"+ userId +"/follow";

                    FormBody formBody = new FormBody.Builder()
                            .add("user_id",personId)
                            .build();
                    httpUtil.DeleteUtil(url,formBody,3);
                    doHandler();
                }
                //如果未关注楼主
                else {
                    HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
                    String url = "http://139.199.84.147/mytieba.api/user/"+ userId +"/follow";
                    FormBody formBody = new FormBody.Builder()
                            .add("user_id",personId)
                            .build();
                    httpUtil.PostUtilsWithCookie(url,formBody,4);
                    doHandler();
                }
            }
        });
        //点赞的监听
        iv_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果状态是点赞
                FormBody formBody = new FormBody.Builder()
                        .add("post_id",postId)
                        .add("user_id",userId)
                        .build();
                if(RealPraise){
                    HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
                    String url = "http://139.199.84.147/mytieba.api/praise";
                    httpUtil.DeleteUtil(url,formBody,8);
                }else {
                    HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
                    String url = "http://139.199.84.147/mytieba.api/praise";
                    httpUtil.PostUtilsWithCookie(url,formBody,9);
                }
            }
        });
        //头像点击事件
        iv_headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDetail postDetail = mDataList.get(0);
                if(postDetail.getPersonId().equals(userId)) {
                    Intent intent = new Intent();
                    intent.putExtra("userId",userId);
                    intent.setClass(getApplicationContext(),DetailUserActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("userId",postDetail.getPersonId());
                    intent.setClass(getApplicationContext(),HomepageActivity.class);
                    startActivity(intent);
                }
            }
        });
        postDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostDetail postDetail1 = mDataList.get(position +1);
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
                        PostDetail postDetail1 = mDataList.get(position +1);
                        thisFloor = postDetail1.getFloor();
                        CommentDialogFragment commentDialogFragment = new CommentDialogFragment();
                        commentDialogFragment.show(getSupportFragmentManager(), "CommentDialogFragment");
                        Log.e("DetailPost","postdetailAdapter点击"+ postId);
                        break;
//                    case R.id.btn_concerd:
//                        Log.e("DetailPost","关注:"+ RealConcernPeople);
//                        if(RealConcernPeople){
//                            HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
//                            String url = "http://139.199.84.147/mytieba.api/user/"+ userId +"/follow";
//                            PostDetail postDetail = mDataList.get(position);
//                            String personId = postDetail.getPersonId();
//                            FormBody formBody = new FormBody.Builder()
//                                    .add("user_id",personId)
//                                    .build();
//                            httpUtil.DeleteUtil(url,formBody,3);
//                            doHandler();
//                        }
//                        else {
//                            HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
//                            String url = "http://139.199.84.147/mytieba.api/user/"+ userId +"/follow";
//                            PostDetail postDetail = mDataList.get(position);
//                            String personId = postDetail.getPersonId();
//                            FormBody formBody = new FormBody.Builder()
//                                    .add("user_id",personId)
//                                    .build();
//                            httpUtil.PostUtilsWithCookie(url,formBody,4);
//                            doHandler();
//                        }
//                        break;
                    case R.id.iv_author:
                        PostDetail postDetail = mDataList.get(position + 1);
                        int id = Integer.parseInt(postDetail.getPersonId());
                        String Id = String.valueOf(id);
                        if(Id.equals(userId)){
                            Intent intent = new Intent();
                            intent.putExtra("userId",userId);
                            intent.setClass(getApplicationContext(),DetailUserActivity.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent();
                            intent.putExtra("userId",Id);
                            intent.setClass(getApplicationContext(),HomepageActivity.class);
                            startActivity(intent);
                        }
                        break;
                    default:
                }

            }
        });
        mRecyclerView.setAdapter(postDetailAdapter);
    }

    //获取第一楼及数据
    private void initData() {

        String postUrl = "http://139.199.84.147/mytieba.api/post/"+ postId +"/detail";
        HttpUtil httpUtil = new HttpUtil(DetailPost.this,getApplicationContext());
        httpUtil.GetUtilWithCookie(postUrl,1);
        doHandler();
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
    //获取评论数据
    private void initRemark(){
        String remarkUrl = "http://139.199.84.147/mytieba.api/post/"+ postId + "/comment";
        HttpUtil httpUtil2 = new HttpUtil(DetailPost.this,getApplicationContext());
        httpUtil2.GetUtilWithCookie(remarkUrl,2);
        doHandler();
    }
    private void initRefreshData(String url){
        HttpUtil httpUtil2 = new HttpUtil(DetailPost.this,getApplicationContext());
        httpUtil2.GetUtilWithCookie(url,10);
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
                        prasedWithJosnData1(String.valueOf(msg.obj));
                        Log.e("ListBArActivity", String.valueOf(msg.obj));
                        break;
                    case 2:
                        prasedWithJsonData2(String.valueOf(msg.obj));
                        Log.e("ListBArActivity", String.valueOf(msg.obj));
                        break;
                    case 3:
                        prasedWithJsonData3(String.valueOf(msg.obj));
                        break;
                    case 4:
                        prasedWithJsonData4(String.valueOf(msg.obj));
                        break;
                    case 5:
                        prasedWithJsonData5(String.valueOf(msg.obj));
                        break;
                    case 6:
                        prasedWithJsonData6(String.valueOf(msg.obj));
                        break;
                    case 7:
                        prasedWithJsonData7(String.valueOf(msg.obj));
                        break;
                    case 8:
                        prasedWithJsonData8(String.valueOf(msg.obj));
                        break;
                    case 9:
                        prasedWithJsonData9(String.valueOf(msg.obj));
                        break;
                    case 10:
                        prasedWithJsonData10(String.valueOf(msg.obj));
                        break;
                    default:
                        break;
                }
            }

        };
    }
    //获取其他页面传值及存储数据
    private void getData(){
        Intent intent = getIntent();
        postId = intent.getStringExtra("post_id");
        Log.e("DetailPost","postId:"+ postId);

        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", "");
    }
    //以下为对获取json数据解析
    private void prasedWithJosnData1(String JsonData) {
        mDataList = new ArrayList<>();
        Log.e("DetailPost1",JsonData);
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            Boolean status = jsonObject.getBoolean("status");
            if(status) {
                collection_status = jsonObject.getBoolean("collection_status");
                boolean praise_status = jsonObject.getBoolean("praise_status");
                RealPraise = praise_status;
                String use_id = jsonObject.getString("user_id");
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
                postDetail.setPraise_status(praise_status);
                Log.e("PostDetailAdapter","praise_status"+ praise_status);
                postDetail.setContent(content);
                postDetail.setBarName(bar);
                postDetail.setTime(time);
                postDetail.setPersonId(person_id);
                postDetail.setPhoto(picture);
                postDetail.setUserName(person_name);
                postDetail.setCollection_status(follow_status);
                postDetail.setUserImg("http://139.199.84.147" + person_avatar);
                mDataList.add(postDetail);
                judgeCollectIcon();
            }else {
                Toast.makeText(getApplicationContext(),"网络请求失败",Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        initRemark();
    }
    private void prasedWithJsonData2(String JsonData){
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
        postDetailAdapter.addData(mDataList);
    }
    private void prasedWithJsonData3(String JosnData){
        Log.e("DetailPost","3:"+JosnData);
        try {
            JSONObject jsonObject = new JSONObject(JosnData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                RealConcernPeople = false;
                btn_concerd.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.follow));
                Toast.makeText(getApplicationContext(),"取消关注成功",Toast.LENGTH_LONG).show();
            }else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void prasedWithJsonData4(String JosnData){
        try {
            Log.e("DetailPost","4:"+JosnData);
            JSONObject jsonObject = new JSONObject(JosnData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                RealConcernPeople = true;
                btn_concerd.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.follow_fill));
                Toast.makeText(getApplicationContext(),"关注成功",Toast.LENGTH_LONG).show();
            }else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void prasedWithJsonData5(String JosnData){
        try {
            Log.e("DetailPost","5:"+JosnData);
            JSONObject jsonObject = new JSONObject(JosnData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                collection_status = true;
                actionA.setIcon(R.drawable.collect_fill);
                Toast.makeText(getApplicationContext(),"收藏成功",Toast.LENGTH_LONG).show();
            }else {
                collection_status = false;

                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void prasedWithJsonData6(String JosnData){
        try {
            Log.e("DetailPost","6:"+JosnData);
            JSONObject jsonObject = new JSONObject(JosnData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                collection_status = false;
                actionA.setIcon(R.drawable.collect);
                Toast.makeText(getApplicationContext(),"取消收藏成功",Toast.LENGTH_LONG).show();
            }else {
                collection_status = true;
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void prasedWithJsonData7(String JosnData){
        try {
            Log.e("DetailPost","7:"+JosnData);
            JSONObject jsonObject = new JSONObject(JosnData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                finish();
                Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_LONG).show();
            }else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void prasedWithJsonData8(String JsonData){
        try {
            Log.e("DetailPost","8:"+JsonData);
            JSONObject jsonObject = new JSONObject(JsonData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                RealPraise = false;
                iv_praise.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.like));
                Toast.makeText(getApplicationContext(),"取消点赞成功",Toast.LENGTH_LONG).show();
            }else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void prasedWithJsonData9(String JsonData){
        try {
            Log.e("DetailPost","9:"+JsonData);
            JSONObject jsonObject = new JSONObject(JsonData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                RealPraise = true;
                iv_praise.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.like_fill));
                Toast.makeText(getApplicationContext(),"点赞成功",Toast.LENGTH_LONG).show();
            }else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void prasedWithJsonData10(String JsonData){
        Log.e("DetailPost","Pj:"+ JsonData);
        mDataList = new ArrayList<>();
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
                postDetailAdapter.addData(mDataList);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //暴露接口，便于fragment传值
    public String getPostId() {
        return postId;
    }
    private String getThisPcId(){
        return thisPcId;
    }
    public String getThisFloor(){
        return thisFloor;
    }
    //判断收藏图标状态并修改
    private void judgeCollectIcon(){
        Log.e("DetailPost","collection_status"+collection_status);
        if(collection_status){
            //收藏的图标状态
            actionA.setIcon(R.drawable.collect_fill);
        }else {
            actionA.setIcon(R.drawable.collect);
        }
    }
}
