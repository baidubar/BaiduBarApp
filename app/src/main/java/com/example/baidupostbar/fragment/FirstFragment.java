package com.example.baidupostbar.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.baidupostbar.DetailPost;
import com.example.baidupostbar.DetailUserActivity;
import com.example.baidupostbar.HomepageActivity;
import com.example.baidupostbar.R;
import com.example.baidupostbar.Utils.CheckNetUtil;
import com.example.baidupostbar.bean.BooleanPraise;
import com.example.baidupostbar.bean.Post;
import com.example.baidupostbar.bean.PostDetail;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.baseadapter.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.imageloader.BGARVOnScrollListener;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class FirstFragment extends Fragment implements EasyPermissions.PermissionCallbacks, BGANinePhotoLayout.Delegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener {
    View view;
    private static final int PRC_PHOTO_PREVIEW = 1;

    private static final int RC_ADD_MOMENT = 1;

    private RecyclerView mMomentRv;
    private PostAdapter postAdapter;
    private String url;
    private ArrayList<String>picture;
    private String responseData;
    private List<Post> moments = new ArrayList<>();
    private int lastId=0;
    private String userId;
    private boolean Realpraise;
    private ArrayList<BooleanPraise>mDataList = new ArrayList<>();
    private boolean praisePrasie;

    private BGANinePhotoLayout mCurrentClickNpl;
    PullToRefreshLayout pullToRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("theUser", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", "");

        mMomentRv = view.findViewById(R.id.first_recyclerView);
        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.activity_main);
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                moments.clear();
                //sendRequestWithOkHttp();//请求数据，不用带lastId
                url = "http://139.199.84.147/mytieba.api/posts";
                if (new CheckNetUtil(getContext()).initNet()) {
                    initData(url);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_LONG).show();
                        // 结束刷新
                        mDataList = new ArrayList<>();
                        pullToRefreshLayout.finishRefresh();
                    }
                }, 1000);
            }
            @Override
            public void loadMore() {
                //sendRequestWithOkHttp(lastId);//加载更多，要带lastId，我已经取好了
                url =  "http://139.199.84.147/mytieba.api/posts?lastId=" + lastId;
                if (new CheckNetUtil(getContext()).initNet()) {
                    initData(url);
                }
                Log.e("FistFragment","urlloadMore"+url);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束加载更多
                        Toast.makeText(getContext(),"加载完成",Toast.LENGTH_LONG).show();
                        pullToRefreshLayout.finishLoadMore();
                    }
                }, 1000);
            }
        });


        postAdapter = new PostAdapter(mMomentRv,getContext());
        postAdapter.setOnRVItemClickListener(this);
        postAdapter.setOnRVItemLongClickListener(this);

        mMomentRv.addOnScrollListener(new BGARVOnScrollListener(getActivity()));
        mMomentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mMomentRv.setAdapter(postAdapter);

        url = "http://139.199.84.147/mytieba.api/posts";

        if (new CheckNetUtil(getContext()).initNet()) {
            moments = new ArrayList<>();
            initData(url);
        }

    }

    /**
     * 添加网络图片测试数据
     */
    private void addNetImageTestData(String jsonData) {
//        moments = new ArrayList<>();
        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("post_msg");
            lastId = jsonObject.getInt("lastId");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String postId = jsonObject1.getString("post_id");
                JSONArray jsonArray1 = jsonObject1.getJSONArray("post_pic");
                String writer_id = jsonObject1.getString("writer_id");
                String writer_name = jsonObject1.getString("writer_name");
                String writer_avatar = jsonObject1.getString("writer_avatar");
                boolean praise_status = jsonObject1.getBoolean("praise_status");

                BooleanPraise booleanPraise = new BooleanPraise();
                booleanPraise.setPraise_status(praise_status);
                mDataList.add(booleanPraise);

                picture = new ArrayList<>();
                for (int j = 0;j < jsonArray1.length();j++){
                    String pic = "http://139.199.84.147/" + jsonArray1.get(j);
                    picture.add(pic);
                }

                String praise = String.valueOf(praise_status);
                String post_content = jsonObject1.getString("post_content");
                String comment_number = jsonObject1.getString("comment_number");
                String praise_number = jsonObject1.getString("praise_number");
                String barId = jsonObject1.getString("bar_id");
                String barName = jsonObject1.getString("bar_name");
                String bar_tags = jsonObject1.getString("bar_tags");
                moments.add(new Post(post_content,picture,comment_number,praise_number,writer_avatar,writer_name,bar_tags,barName,barId,postId,writer_id,praise));

            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        //*************************
        //这里添加内容和图片
        //*************************
        postAdapter.setData(moments);

    }

//    public void onClick(View v) {
//        if (v.getId() == R.id.tv_moment_list_add) {
//            startActivityForResult(new Intent(this, MomentAddActivity.class), RC_ADD_MOMENT);
//        } else if (v.getId() == R.id.tv_moment_list_system) {
//            startActivity(new Intent(this, SystemGalleryActivity.class));
//        }
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == RC_ADD_MOMENT) {
//            postAdapter.addFirstItem(getActivity().getMoment(data));
//            mMomentRv.smoothScrollToPosition(0);
//        }
//    }

    /**
     * 图片预览，兼容6.0动态权限
     */
    @AfterPermissionGranted(PRC_PHOTO_PREVIEW)
    private void photoPreviewWrapper() {
        if (mCurrentClickNpl == null) {
            return;
        }

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");
            BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(getContext())
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
            Toast.makeText(getContext(), "您拒绝了「图片预览」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        mCurrentClickNpl = ninePhotoLayout;
        photoPreviewWrapper();
    }

    //帖子的点击事件
    @Override
    public void onRVItemClick(ViewGroup viewGroup, View view, int position) {

        String postId = moments.get(position).postId;
        Intent intent = new Intent(getContext(), DetailPost.class);
        intent.putExtra("post_id",postId);
        startActivity(intent);
    }

    //帖子的长按事件
    @Override
    public boolean onRVItemLongClick(ViewGroup viewGroup, View view, int position) {
        //Toast.makeText(getContext(), "长按了item " + position, Toast.LENGTH_SHORT).show();
        return true;
    }

    private class PostAdapter extends BGARecyclerViewAdapter<Post> {

        private Context context;
        public PostAdapter(RecyclerView recyclerView,Context context) {
            super(recyclerView, R.layout.item_post);
            this.context = context;
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, Post moment) {
            if (TextUtils.isEmpty(moment.content)) {
                helper.setVisibility(R.id.tv_content, View.GONE);
            } else {
                helper.setVisibility(R.id.tv_content, View.VISIBLE);
                helper.setText(R.id.tv_content, moment.content);
                helper.setText(R.id.tv_commentNum,moment.comment_number);
                helper.setText(R.id.tv_likeNum,moment.praise_number);
                helper.setText(R.id.tv_label,"#"+moment.barLabel.substring(2,moment.barLabel.length()-2));
                helper.setText(R.id.tv_bar,moment.barName);
                helper.setText(R.id.tv_author,moment.writterName);
            }
            if(moment.praise_status.equals("true")){
                helper.setImageResource(R.id.btn_like,R.drawable.like_fill);
            }else {
                helper.setImageResource(R.id.btn_like,R.drawable.like);
            }
            helper.getView(R.id.iv_author).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(moment.writer_id.equals(userId)){
                        Intent intent = new Intent();
                        intent.setClass(getContext(), DetailUserActivity.class);
                        intent.putExtra("userId",userId);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent();
                        intent.setClass(getContext(), HomepageActivity.class);
                        intent.putExtra("userId",moment.writer_id);
                        startActivity(intent);
                    }
                }
            });
            helper.getView(R.id.btn_like).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(moment.praise_status.equals("true")){
                        if (new CheckNetUtil(getContext()).initNet()) {
                            //如果是已点赞状态
                            //********************************
                            //此处有个bug
                            //********************************
                            deletePraise(moment.postId,moment);
                            helper.setImageResource(R.id.btn_like,R.drawable.like);
                            int likeNum = Integer.parseInt(moment.praise_number);
                            String lN = String.valueOf(likeNum);
                            helper.setText(R.id.tv_likeNum,lN);
                        }
                    }else {
                        if (new CheckNetUtil(getContext()).initNet()) {
                            //如果是已点赞状态
                            postPraise(moment.postId,moment);
                            helper.setImageResource(R.id.btn_like,R.drawable.like_fill);
                            int likeNum = Integer.parseInt(moment.praise_number) + 1;
                            String lN = String.valueOf(likeNum);
                            helper.setText(R.id.tv_likeNum,lN);
                        }
                    }
                }
            });
            Glide.with(context).load("http://139.199.84.147"+moment.getHeadImage()).into(helper.getImageView(R.id.iv_author));
            BGANinePhotoLayout ninePhotoLayout = helper.getView(R.id.npl_item_moment_photos);
            ninePhotoLayout.setDelegate(FirstFragment.this::onClickNinePhotoItem);
            ninePhotoLayout.setData(moment.photos);
        }

    }
    private void initData(String url){
                try {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("theUser", Context.MODE_PRIVATE);
                    String cookie = sharedPreferences.getString("cookie", "");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url)
                            .addHeader("Cookie",cookie)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {
                            Log.e("onFailure","获取数据失败");
                            Toast.makeText(getContext(),"网络请求失败",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onResponse(okhttp3.Call call, Response response) throws IOException {
                            responseData = response.body().string();
                            Log.e("rsponseData",responseData);
                            if (response.isSuccessful()){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        addNetImageTestData(responseData);
                                    }
                                });
                            }else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"服务器请求失败",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"网络请求失败",Toast.LENGTH_LONG).show();
                }
    }
    private void deletePraise(String postId,Post moment){
        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("theUser", Context.MODE_PRIVATE);
            String cookie = sharedPreferences.getString("cookie", "");
            FormBody formBody = new FormBody.Builder()
                    .add("post_id",postId)
                    .add("user_id",userId)
                    .build();
            Log.e("DeletePost_id",postId+"|"+userId);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url("http://139.199.84.147/mytieba.api/praise")
                    .delete(formBody)
                    .addHeader("Cookie",cookie)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Log.e("onFailure", "获取数据失败");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getContext(),"网络请求失败",Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    responseData = response.body().string();
                    Log.e("HttpUtilsDelete",responseData);
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            boolean status = jsonObject.getBoolean("status");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(status) {
                                            moment.setPraise_status("false");
                                            Log.e("DeletePraise",responseData);
                                            Toast.makeText(getContext(), "取消点赞成功", Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(getContext(), "请求失败", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"服务器请求失败",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"请求失败",Toast.LENGTH_LONG).show();
        }
    }
    private void postPraise(String postId,Post moment){
        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("theUser", Context.MODE_PRIVATE);
            String cookie = sharedPreferences.getString("cookie", "");
            FormBody formBody = new FormBody.Builder()
                    .add("post_id",postId)
                    .add("user_id",userId)
                    .build();
            Log.e("PostPost_id",postId+"|"+ userId);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url("http://139.199.84.147/mytieba.api/praise")
                    .delete(formBody)
                    .addHeader("Cookie",cookie)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Log.e("onFailure", "获取数据失败");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"网络请求失败",Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    responseData = response.body().string();
                    Log.e("PostPraise",responseData);
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            boolean status = jsonObject.getBoolean("status");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(status) {
                                        moment.setPraise_status("false");
                                        Toast.makeText(getContext(), "点赞成功", Toast.LENGTH_LONG).show();
                                        Log.e("PostPraise",responseData);
                                    }else {
                                        Toast.makeText(getContext(), "请求失败", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"服务器请求失败",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"请求失败",Toast.LENGTH_LONG).show();
        }
    }
}
