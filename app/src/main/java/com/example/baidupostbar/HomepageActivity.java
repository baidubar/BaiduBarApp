package com.example.baidupostbar;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.example.baidupostbar.Adapter.ViewPagerAdapter;
import com.example.baidupostbar.fragment.UserBarFragment;
import com.example.baidupostbar.fragment.UserInforFragment;
import com.example.baidupostbar.fragment.UserPostFragment;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;

public class HomepageActivity extends BaseActivity {
    private ViewPager viewpager;
    private TextView btn_attention;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initView();
        initViewPager();
        initListener();
    }
    private void initView() {
        btn_attention = findViewById(R.id.btn_attention);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        viewpager = (ViewPager) findViewById(R.id.view_pager);
        iv_parallax = (ImageView) findViewById(R.id.iv_parallax);
        buttonBarLayout = (ButtonBarLayout) findViewById(R.id.buttonBarLayout);
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_head = (ImageView) findViewById(R.id.iv_head);
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
                    btn_attention.setTextColor(getResources().getColor(R.color.black));
                } else {  //展开
                    if (isblack) { //变白
                        mImmersionBar.statusBarDarkFont(false).init();
                        isblack = false;
                        iswhite = true;
                    }
                    buttonBarLayout.setVisibility(View.INVISIBLE);
                    collapsing_toolbar.setContentScrimResource(R.color.transparent);
                    iv_back.setBackgroundResource(R.drawable.back_white);
                    btn_attention.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });
    }
}
