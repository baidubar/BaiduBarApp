package com.example.baidupostbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.example.baidupostbar.Adapter.ViewPagerAdapter;
import com.example.baidupostbar.fragment.UserBarFragment;
import com.example.baidupostbar.fragment.UserInforFragment;
import com.example.baidupostbar.fragment.UserPostFragment;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGABaseAdapterUtil;
import cn.bingoogolapple.photopicker.imageloader.BGAImage;
import cn.bingoogolapple.photopicker.util.BGAPhotoHelper;
import cn.bingoogolapple.photopicker.util.BGAPhotoPickerUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DetailUserActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{
    private ViewPager viewpager;
    private TextView tv;
    private ImageView iv_parallax,iv_back,iv_head;
    private ButtonBarLayout buttonBarLayout;
    private CollapsingToolbarLayout collapsing_toolbar;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private int mOffset = 0;
    boolean isblack = false;//状态栏字体是否是黑色
    boolean iswhite = true;//状态栏字体是否是亮色
    private TabLayout tabLayout;
    private ArrayList<String> TitleList = new ArrayList<>();  //页卡标题集合
    private ArrayList<Fragment> ViewList = new ArrayList<>();   //页卡视图集合
    private Fragment userBarFragment,userPostFragment,userInforFragment;
    //以下有关修改头像及头像裁剪
    private static final int REQUEST_CODE_PERMISSION_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PERMISSION_TAKE_PHOTO = 2;

    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_TAKE_PHOTO = 2;
    private static final int REQUEST_CODE_CROP = 3;

    private BGAPhotoHelper mPhotoHelper;
    private RelativeLayout rl_allinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        initView();
        initViewPager();
        initListener();
        processLogic(savedInstanceState);
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

                    }
                });
                builder.setPositiveButton("系统相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
                    }
                });
                builder.setPositiveButton("系统相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choosePhoto();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);      //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                dialog.show();
            }
        });
    }

    protected void processLogic(Bundle savedInstanceState) {
        setTitle("系统相册选择图片、裁剪");

        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");
        mPhotoHelper = new BGAPhotoHelper(takePhotoDir);
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

//    public void onClick(View v) {
//        if (v.getId() == R.id.tv_system_gallery_crop_choose) {
//            choosePhoto();
//        } else if (v.getId() == R.id.tv_system_gallery_crop_take_photo) {
//            takePhoto();
//        }
//    }

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
                } catch (Exception e) {
                    mPhotoHelper.deleteCropFile();
                    BGAPhotoPickerUtil.show(R.string.bga_pp_not_support_crop);
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                try {
                    startActivityForResult(mPhotoHelper.getCropIntent(mPhotoHelper.getCameraFilePath(), 200, 200), REQUEST_CODE_CROP);
                } catch (Exception e) {
                    mPhotoHelper.deleteCameraFile();
                    mPhotoHelper.deleteCropFile();
                    BGAPhotoPickerUtil.show(R.string.bga_pp_not_support_crop);
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CODE_CROP) {
                //mPhotoHelper.getCropFilePath()获取当前图片的路径
                BGAImage.display(iv_head, R.mipmap.bga_pp_ic_holder_light, mPhotoHelper.getCropFilePath(), BGABaseAdapterUtil.dp2px(200));
            }
        } else {
            if (requestCode == REQUEST_CODE_CROP) {
                mPhotoHelper.deleteCameraFile();
                mPhotoHelper.deleteCropFile();
            }
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
    }
}