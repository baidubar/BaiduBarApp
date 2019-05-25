package com.example.baidupostbar.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.baidupostbar.bean.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.baseadapter.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.imageloader.BGARVOnScrollListener;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class UserPostFragment extends Fragment implements EasyPermissions.PermissionCallbacks, BGANinePhotoLayout.Delegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener {
    View view;
    private static final int PRC_PHOTO_PREVIEW = 1;

    private static final int RC_ADD_MOMENT = 1;

    private RecyclerView mMomentRv;
    private PostAdapter postAdapter;
    private String url;
    private ArrayList<String>picture;
    private String responseData;
    private List<Post> moments;

    private BGANinePhotoLayout mCurrentClickNpl;


    private String userId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_post,container,false);

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMomentRv = view.findViewById(R.id.recyclerView);
        postAdapter = new PostAdapter(mMomentRv,getContext());
        postAdapter.setOnRVItemClickListener(this);
        postAdapter.setOnRVItemLongClickListener(this);

        mMomentRv.addOnScrollListener(new BGARVOnScrollListener(getActivity()));
        mMomentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mMomentRv.setAdapter(postAdapter);



    }

    @Override
    public void onResume() {
        super.onResume();
        url = "http://139.199.84.147/mytieba.api/posts";

        if (new CheckNetUtil(getContext()).initNet()) {
            initData(url);
        }
    }

    /**
     * 添加网络图片测试数据
     */
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
                for (int j = 0;j < jsonArray1.length();j++){
                    String pic = "http://139.199.84.147/" + jsonArray1.get(j);
                    picture.add(pic);
                }
                String post_content = jsonObject1.getString("post_content");
                String comment_number = jsonObject1.getString("comment_number");
                String praise_number = jsonObject1.getString("praise_number");
                String barId = jsonObject1.getString("bar_id");
                String barName = jsonObject1.getString("bar_name");
                String bar_tags = jsonObject1.getString("bar_tags");
                moments.add(new Post(post_content,picture,comment_number,praise_number,writer_avatar,writer_name,bar_tags,barName,barId,postId,writer_id));

            }
        } catch (JSONException e){
            e.printStackTrace();
        }


//        moments.add(new Post("1张网络图片", new ArrayList<>(Arrays.asList("http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg"))));
//        moments.add(new Post("2张网络图片", new ArrayList<>(Arrays.asList("http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg", "http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg"))));
//        moments.add(new Post("9张网络图片", new ArrayList<>(Arrays.asList("http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg", "http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg", "http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg", "http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered15.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered16.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered17.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered18.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered19.png"))));
//        moments.add(new Post("5张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered11.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered12.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered13.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered14.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered15.png"))));
//        moments.add(new Post("3张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered4.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered5.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered6.png"))));
//        moments.add(new Post("8张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered11.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered12.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered13.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered14.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered15.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered16.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered17.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered18.png"))));
//        moments.add(new Post("4张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered7.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered8.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered9.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered10.png"))));
//        moments.add(new Post("2张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered2.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered3.png"))));
//        moments.add(new Post("3张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered4.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered5.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered6.png"))));
//        moments.add(new Post("4张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered7.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered8.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered9.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered10.png"))));
//        moments.add(new Post("9张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered11.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered12.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered13.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered14.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered15.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered16.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered17.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered18.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered19.png"))));
//        moments.add(new Post("1张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered1.png"))));
//        moments.add(new Post("5张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered11.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered12.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered13.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered14.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered15.png"))));
//        moments.add(new Post("6张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered11.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered12.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered13.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered14.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered15.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered16.png"))));
//        moments.add(new Post("7张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered11.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered12.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered13.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered14.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered15.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered16.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered17.png"))));
//        moments.add(new Post("8张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered11.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered12.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered13.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered14.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered15.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered16.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered17.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered18.png"))));

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
                helper.setText(R.id.tv_label,moment.barLabel);
                helper.setText(R.id.tv_bar,moment.barName);
                helper.setText(R.id.tv_author,moment.writterName);
            }
            Glide.with(context).load("http://139.199.84.147"+moment.getHeadImage()).into(helper.getImageView(R.id.iv_author));
            BGANinePhotoLayout ninePhotoLayout = helper.getView(R.id.npl_item_moment_photos);
            ninePhotoLayout.setDelegate(UserPostFragment.this::onClickNinePhotoItem);
            ninePhotoLayout.setData(moment.photos);
        }
    }
    private void initData(String url){
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("theUser", Context.MODE_PRIVATE);
            String cookie = sharedPreferences.getString("cookie", "");
            String userId = sharedPreferences.getString("user_id", "");
            url = url + "?user="+ userId;
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
}
