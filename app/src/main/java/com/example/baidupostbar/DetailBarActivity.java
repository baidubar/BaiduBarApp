package com.example.baidupostbar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.baidupostbar.Utils.HttpUtil;
import com.example.baidupostbar.bean.Post;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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

import static com.example.baidupostbar.R.layout.header_detail_bar;
import static com.example.baidupostbar.R.layout.tab_item;

public class DetailBarActivity extends RootBaseActivity implements EasyPermissions.PermissionCallbacks, BGANinePhotoLayout.Delegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener {
    private ArrayList<Post> mDataList;
    private FloatingActionButton fab;

    private static final int PRC_PHOTO_PREVIEW = 1;

    private static final int RC_ADD_MOMENT = 1;

    private RecyclerView mMomentRv;
    private PostAdapter postAdapter;
    private ArrayList<String>picture;
    private String barLabel;

    private BGANinePhotoLayout mCurrentClickNpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bar);

        Intent intent = getIntent();
        String barId = intent.getStringExtra("barId");
        barLabel = intent.getStringExtra("barLabel");
        Log.e("DetailBarActivity", "barId" + barId);

        HttpUtil httpUtil = new HttpUtil(DetailBarActivity.this,getApplicationContext());
       // httpUtil.GetUtilWithCookie("http://139.199.84.147/mytieba.api/postbar?bar_id="+ barId,1);
        httpUtil.GetUtilWithCookie("http://139.199.84.147/mytieba.api/postbar?bar_id=" + barId,1);
        Log.e("DetailBArActivity","bae_Id" + barId );
        doHandler();

//
//        if (responsedata != null) {
//            addNetImageTestData(responsedata);
//        }

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailBarActivity.this, CreatePostActivity.class);
                startActivity(intent);
                //startActivityForResult(new Intent(DetailBarActivity.this, CreatePostActivity.class),1);
            }
        });
        mMomentRv = findViewById(R.id.recyclerView);
        postAdapter = new PostAdapter(mMomentRv,getApplicationContext());
        postAdapter.setOnRVItemClickListener(this);
        postAdapter.setOnRVItemLongClickListener(this);

        mMomentRv.addOnScrollListener(new BGARVOnScrollListener(DetailBarActivity.this));
        mMomentRv.setLayoutManager(new LinearLayoutManager(this));
        mMomentRv.setAdapter(postAdapter);

        //addBannerHeader();
        // 当有 Header 或者 Footer 时，需要传入 mAdapter.getHeaderAndFooterAdapter()
        mMomentRv.setAdapter(postAdapter.getHeaderAndFooterAdapter());
//        initData();
//        initAdapter();
    }

    private void addNetImageTestData(String JsonData) {
        Log.e("DetailBarActivity",JsonData);
        List<Post> moments = new ArrayList<>();
        mDataList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                String bar_id =jsonObject.getString("bar_id");
                String name = jsonObject.getString("name");
                String icon = jsonObject.getString("icon");
                String post_number = jsonObject.getString("post_number");
                String watcher_number = jsonObject.getString("watcher_number");
                String description = jsonObject.getString("description");
               // postAdapter.addHeaderView();
                addBannerHeader("http://139.199.84.147/" + icon,name,watcher_number,post_number  );
                boolean watching_status = jsonObject.getBoolean("watching_status");
                Log.e("watching_status", String.valueOf(watching_status));
                    JSONArray jsonArray = jsonObject.getJSONArray("post_info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String writer_id = jsonObject1.getString("writer_id");
                        String writer_avatar = jsonObject1.getString("writer_avatar");
                        String writer_name = jsonObject1.getString("writer_name");
                        String post_content = jsonObject1.getString("post_content");
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("post_pic");
                        picture = new ArrayList<>();
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            String Picture = "http://139.199.84.147/" + jsonArray1.get(j);
                            picture.add(Picture);
                            Log.e("DeatailBarActivity","Picture + " + Picture);
                        }
                        String comment_number = jsonObject1.getString("comment_number");
                        String praise_number = jsonObject1.getString("praise_number");
                        String time = jsonObject1.getString("time");
                        moments.add(new Post(post_content, picture,comment_number,praise_number,writer_avatar,writer_name,barLabel,name ));
                        postAdapter.setData(moments);

                }


            }else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void addBannerHeader(String barImage,String barName,String concernNum,String postNum) {
        // 初始化HeaderView
        View headerView = View.inflate(DetailBarActivity.this, header_detail_bar, null);
//        mBanner = headerView.findViewById(R.id.banner);
//        mBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
//            @Override
//            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
//                Glide.with(banner.getContext()).load(model).apply(new RequestOptions().placeholder(R.drawable.holder_banner).error(R.drawable.holder_banner).dontAnimate()).thumbnail(0.1f).into(itemView);
//            }
//        });
//        mBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
//            @Override
//            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
//                ToastUtil.show("点击了第" + (position + 1) + "页");
//            }
//        });
        ImageView BarImage = headerView.findViewById(R.id.iv_bar);
        TextView BarName  = headerView.findViewById(R.id.tv_bar);
        TextView ConcernNum = headerView.findViewById(R.id.tv_concern_num);
        TextView PostNum = headerView.findViewById(R.id.tv_post_num);
        Glide.with(getApplicationContext()).load(barImage).into(BarImage);
        BarName.setText(barName);
        ConcernNum.setText("关注 "+concernNum);
        PostNum.setText("帖子" + postNum);
        postAdapter.addHeaderView(headerView);
    }

    /**
     * 图片预览，兼容6.0动态权限
     */
    @AfterPermissionGranted(PRC_PHOTO_PREVIEW)
    private void photoPreviewWrapper() {
        if (mCurrentClickNpl == null) {
            return;
        }

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");
            BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(this)
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
            Toast.makeText(this, "您拒绝了「图片预览」所需要的相关权限!", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), DetailPost.class);
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
        public PostAdapter(RecyclerView recyclerView, Context context) {
            super(recyclerView, R.layout.item_post);
            this.context  = context;
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, Post moment) {
            if (TextUtils.isEmpty(moment.content)) {
                helper.setVisibility(R.id.tv_content, View.GONE);
            } else {
                helper.setVisibility(R.id.tv_content, View.VISIBLE);
                helper.setText(R.id.tv_content, moment.content);
                helper.setText(R.id.tv_author,moment.writterName);
                helper.setText(R.id.tv_commentNum,moment.comment_number);
                helper.setText(R.id.tv_likeNum,moment.praise_number);
                helper.setText(R.id.tv_label,moment.barLabel);
                helper.setText(R.id.tv_bar,moment.barName);
            }
            Glide.with(context).load("http://139.199.84.147"+moment.getHeadImage()).into(helper.getImageView(R.id.iv_author));
            Log.e("DetailBArActivity",moment.getHeadImage());
            BGANinePhotoLayout ninePhotoLayout = helper.getView(R.id.npl_item_moment_photos);
            ninePhotoLayout.setDelegate(DetailBarActivity.this);
            ninePhotoLayout.setData(moment.photos);
        }
    }

    //    @SuppressWarnings("unchecked")
//    private void initAdapter() {
//        BaseQuickAdapter postAdapter = new PostAdapter(R.layout.item_post, mDataList);
//        postAdapter.openLoadAnimation();
//        View top = getLayoutInflater().inflate(R.layout.header_detail_bar, (ViewGroup) mRecyclerView.getParent(), false);
//        postAdapter.addHeaderView(top);
//        postAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(DetailBarActivity.this,DetailPost.class);
//                startActivity(intent);
//            }
//        });
//
//        mRecyclerView.setAdapter(postAdapter);
//    }
//
//    private void initData() {
//        mDataList = new ArrayList<>();
//        for (int i = 0; i < 8; i++) {
//            Post item = new Post();
//            mDataList.add(item);
//        }
//    }
    private void parseWithJsonData(String JsonData) {
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                String user_id = jsonObject.getString("user_id");
                //关注状态
                boolean collection_status = jsonObject.getBoolean("collection_status");
                //点赞状态
                boolean praise_status = jsonObject.getBoolean("praise_status");
                JSONArray jsonArray = jsonObject.getJSONArray("post_msg");
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String person_id = jsonObject1.getString("String person_id");
                    String person_avatar = jsonObject1.getString("person_avatar");
                    String follow_status = jsonObject1.getString("follow_status");
                    String time = jsonObject1.getString("time");
                    String pic = jsonObject1.getString("pic");
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("pic");
                    picture = new ArrayList<>();
                    for (int j = 0;j < jsonArray1.length();j++){
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                        String Picture = "http://139.199.84.147/" + jsonObject2.getString("");
                        picture.add(Picture);
                    }
                    String content = jsonObject1.getString("content");
                    String bar_id = jsonObject1.getString("bar_id");
                    String barName = jsonObject1.getString("bar");

                }


            }else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
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
