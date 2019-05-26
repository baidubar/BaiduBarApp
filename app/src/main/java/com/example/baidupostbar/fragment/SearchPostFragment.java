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
import com.example.baidupostbar.R;
import com.example.baidupostbar.SearchResultActivity;
import com.example.baidupostbar.Utils.CheckNetUtil;
import com.example.baidupostbar.bean.EmptyRecyclerView;
import com.example.baidupostbar.bean.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

public class SearchPostFragment extends Fragment implements EasyPermissions.PermissionCallbacks, BGANinePhotoLayout.Delegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener {

    View view;

    private static final int PRC_PHOTO_PREVIEW = 1;

    private static final int RC_ADD_MOMENT = 1;

    private String searchText;
    private EmptyRecyclerView mMomentRv;
    private PostAdapter postAdapter;
    private String url;
    private ArrayList<String> picture;
    private String responseData;
    private String cookie;
    private View mEmptyView;
    private ArrayList<Post>moments;

    private BGANinePhotoLayout mCurrentClickNpl;

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        searchText = ((SearchResultActivity) activity).getSearchText();//通过强转成宿主activity，就可以获取到传递过来的数据
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_search_post, container, false);
        mEmptyView = view.findViewById(R.id.empty_iv);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMomentRv = view.findViewById(R.id.search_post_recyclerView);
        postAdapter = new PostAdapter(mMomentRv,getActivity());
        postAdapter.setOnRVItemClickListener(this);
        postAdapter.setOnRVItemLongClickListener(this);

        mMomentRv.addOnScrollListener(new BGARVOnScrollListener(getActivity()));
        mMomentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mMomentRv.setAdapter(postAdapter);
        mMomentRv.setEmptyView(mEmptyView);

        List<Map<String, String>> list_url = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("type", "post");
        map.put("search",searchText);
        list_url.add(map);
        url = getUrl("http://139.199.84.147/mytieba.api/search", list_url);
        Log.e("FirstFragment","url" + url);

        if (new CheckNetUtil(getContext()).initNet()) {
            initData(url);
        }
    }

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
        Intent intent = new Intent(getContext(), DetailPost.class);
        Post post = moments.get(position);
        String postId = post.postId;
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
            this.context=context;
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, Post moment) {
            if (TextUtils.isEmpty(moment.content)) {
                helper.setVisibility(R.id.tv_content, View.GONE);
            } else {
                helper.setVisibility(R.id.tv_content, View.VISIBLE);
                helper.setText(R.id.tv_author,moment.writterName);
                helper.setText(R.id.tv_content, moment.content);
                helper.setText(R.id.tv_commentNum,moment.comment_number);
                helper.setText(R.id.tv_likeNum,moment.praise_number);
                helper.setText(R.id.tv_label,moment.barLabel);
                helper.setText(R.id.tv_bar,moment.barName);
            }

            Glide.with(context).load("http://139.199.84.147"+ moment.getHeadImage()).into(helper.getImageView(R.id.iv_author));
            BGANinePhotoLayout ninePhotoLayout = helper.getView(R.id.npl_item_moment_photos);
            ninePhotoLayout.setDelegate(SearchPostFragment.this::onClickNinePhotoItem);
            ninePhotoLayout.setData(moment.photos);
        }

    }

    private void addNetImageTestData(String jsonData) {

        moments = new ArrayList<>();
        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            String post_number = jsonObject.getString("post_number");
            if(!post_number.equals("0")) {

//                {
//                    "post_number": 2,
//                        "post_msg":[
//                    {"post_id": 2,
//                            "post_writer_id": 2,
//                            "post_writer_name": "cyx",
//                            "post_writer_avatar": "/media/avatar/123.jpg",
//                            "post_content":"12312432453"// 很长的时候app只显示部分
//                        "bar_name": "cxk吧",
//                            "post_photo": "xx",
//                            "comment_number": 20,
//                            "praise_number":20,}
//                    // 略
//
//    ]
//                }
                JSONArray jsonArray = jsonObject.getJSONArray("post_msg");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String post_id = jsonObject1.getString("post_id");
                    String post_writer_id = jsonObject1.getString("post_writer_id");
                    String post_writer_name = jsonObject1.getString("post_writer_name");
                    String post_writer_avatar = jsonObject1.getString("post_writer_avatar");
                    String post_content = jsonObject1.getString("post_content");
                    String bar_name = jsonObject1.getString("bar_name");
                    String post_photo = jsonObject1.getString("post_photo");
                    String comment_number = jsonObject1.getString("comment_number");
                    String praise_number = jsonObject1.getString("praise_number");
//                    JSONArray jsonArray1 = jsonObject1.getJSONArray("post_photo");
//                    picture = new ArrayList<>();
//                    for (int j = 0; j < jsonArray1.length(); j++) {
//                        String pic = "http://139.199.84.147/" + jsonArray1.get(j);
//                        picture.add(pic);
//                    }
                    picture = new ArrayList<>();
                    picture.add("http://139.199.84.147/" + post_photo);
                    moments.add(new Post(post_content, picture, comment_number, praise_number, post_writer_avatar,post_writer_name, "", bar_name,post_id,post_id,post_writer_id,""));

                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        //*************************
        //这里添加内容和图片
        //*************************
        postAdapter.setData(moments);

    }

    private String getUrl(String url, List<Map<String, String>> list_url) {
        for (int i = 0; i < list_url.size(); i++) {
            Map<String, String> params = list_url.get(i);
            if (params != null) {
                Iterator<String> it = params.keySet().iterator();
                StringBuffer sb = null;
                while (it.hasNext()) {
                    String key = it.next();
                    String value = params.get(key);
                    if (sb == null) {
                        sb = new StringBuffer();
                        sb.append("?");
                    } else {
                        sb.append("&");
                    }
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                }
                url += sb.toString();
            }
        }
        Log.d("getUrl", url);
        return url;
    }
    private void initData(String url){
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("theUser", Context.MODE_PRIVATE);
            cookie = sharedPreferences.getString("cookie", "");
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
