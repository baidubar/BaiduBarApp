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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.baidupostbar.Utils.CheckNetUtil;
import com.example.baidupostbar.Utils.HttpUtil;
import com.example.baidupostbar.bean.EmptyRecyclerView;
import com.example.baidupostbar.bean.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.baseadapter.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.imageloader.BGARVOnScrollListener;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class UserPostActivity extends RootBaseActivity implements EasyPermissions.PermissionCallbacks, BGANinePhotoLayout.Delegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener {

    private static final int PRC_PHOTO_PREVIEW = 1;

    private static final int RC_ADD_MOMENT = 1;

    private EmptyRecyclerView mMomentRv;
    private PostAdapter postAdapter;
    private String url;
    private ArrayList<String> picture;
    private String responseData;
    private List<Post> moments;
    private String userId;
    private View mEmptyView;

    private BGANinePhotoLayout mCurrentClickNpl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);
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
        userId = sharedPreferences.getString("user_id", "");

        mMomentRv = findViewById(R.id.recyclerView);
        mEmptyView = findViewById(R.id.empty_iv);
        mMomentRv.setEmptyView(mEmptyView);
        postAdapter = new PostAdapter(mMomentRv,getApplicationContext());
        postAdapter.setOnRVItemClickListener(this);
        postAdapter.setOnRVItemLongClickListener(this);

        mMomentRv.addOnScrollListener(new BGARVOnScrollListener(UserPostActivity.this));
        mMomentRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mMomentRv.setAdapter(postAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        url = "http://139.199.84.147/mytieba.api/posts";
        url = url + "?user="+ userId;
        if (new CheckNetUtil(getApplicationContext()).initNet()) {
            HttpUtil httpUtil = new HttpUtil(UserPostActivity.this,getApplicationContext());
            httpUtil.GetUtilWithCookie(url,1);
            doHandler();
        }
    }

    private void addNetImageTestData(String jsonData) {
        moments = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("post_msg");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String postId = jsonObject1.getString("post_id");
                JSONArray jsonArray1 = jsonObject1.getJSONArray("post_pic");
                String writer_id = jsonObject1.getString("writer_id");
                String writer_name = jsonObject1.getString("writer_name");
                String writer_avatar = jsonObject1.getString("writer_avatar");
                picture = new ArrayList<>();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    String pic = "http://139.199.84.147/" + jsonArray1.get(j);
                    picture.add(pic);
                }
                String post_content = jsonObject1.getString("post_content");
                String comment_number = jsonObject1.getString("comment_number");
                String praise_number = jsonObject1.getString("praise_number");
                String barId = jsonObject1.getString("bar_id");
                String barName = jsonObject1.getString("bar_name");
                String bar_tags = jsonObject1.getString("bar_tags");
                moments.add(new Post(post_content, picture, comment_number, praise_number, writer_avatar, writer_name, bar_tags, barName, barId, postId,writer_id,""));
                postAdapter.setData(moments);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    //帖子的点击事件
    @Override
    public void onRVItemClick(ViewGroup viewGroup, View view, int position) {
        String postId = moments.get(position).postId;
        Intent intent = new Intent(getApplicationContext(), DetailPost.class);
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
                helper.setText(R.id.tv_label,moment.barLabel);
                helper.setText(R.id.tv_bar,moment.barName);
                helper.setText(R.id.tv_author,moment.writterName);
            }
            Glide.with(context).load("http://139.199.84.147"+moment.getHeadImage()).into(helper.getImageView(R.id.iv_author));
            BGANinePhotoLayout ninePhotoLayout = helper.getView(R.id.npl_item_moment_photos);
            ninePhotoLayout.setDelegate(UserPostActivity.this::onClickNinePhotoItem);
            ninePhotoLayout.setData(moment.photos);
        }
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
                        addNetImageTestData(String.valueOf(msg.obj));
                        break;
                    default:
                        break;
                }
            }

        };
    }
}
