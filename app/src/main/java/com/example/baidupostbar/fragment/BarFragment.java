package com.example.baidupostbar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.baidupostbar.ListBarActivity;
import com.example.baidupostbar.R;
import com.example.baidupostbar.SearchActivity;
import com.example.baidupostbar.bean.FragmentLabel;

import java.util.ArrayList;
import java.util.List;

import km.lmy.searchview.SearchView;

public class BarFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private List<FragmentLabel> data = new ArrayList<>();
    Toolbar toolbar;
    private SearchView searchView;
    private RelativeLayout label_pets,label_music,label_acg,label_sports,label_beauty,label_it,label_book,label_teach,label_photo,label_food,label_fun,label_movie,label_shopping;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bar,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("进吧");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        initView();
        initListener();
    }
    public void initView(){
        label_acg = view.findViewById(R.id.label_acg);
        label_beauty = view.findViewById(R.id.label_beauty);
        label_book = view.findViewById(R.id.label_book);
        label_food = view.findViewById(R.id.label_food);
        label_fun = view.findViewById(R.id.label_fun);
        label_it = view.findViewById(R.id.label_it);
        label_movie = view.findViewById(R.id.label_movie);
        label_music = view.findViewById(R.id.label_music);
        label_pets = view.findViewById(R.id.label_pets);
        label_photo = view.findViewById(R.id.label_photo);
        label_shopping = view.findViewById(R.id.label_shopping);
        label_sports = view.findViewById(R.id.label_sports);
        label_teach = view.findViewById(R.id.label_teach);
    }
    public  void initListener(){
        label_acg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListBarActivity.class);
                intent.putExtra("BarLabel","ACG");
                startActivity(intent);
            }
        });
        label_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListBarActivity.class);
                intent.putExtra("BarLabel","文字");
                startActivity(intent);
            }
        });
        label_beauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListBarActivity.class);
                intent.putExtra("BarLabel","美容");
                startActivity(intent);
            }
        });
        label_fun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListBarActivity.class);
                intent.putExtra("BarLabel","娱乐");
                startActivity(intent);
            }
        });
        label_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListBarActivity.class);
                intent.putExtra("BarLabel","美食");
                startActivity(intent);
            }
        });
        label_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListBarActivity.class);
                intent.putExtra("BarLabel","IT");
                startActivity(intent);
            }
        });
        label_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListBarActivity.class);
                intent.putExtra("BarLabel","音乐");
                startActivity(intent);
            }
        });
        label_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListBarActivity.class);
                intent.putExtra("BarLabel","电影");
                startActivity(intent);
            }
        });
        label_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListBarActivity.class);
                intent.putExtra("BarLabel","摄影");
                startActivity(intent);
            }
        });
        label_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListBarActivity.class);
                intent.putExtra("BarLabel","宠物");
                startActivity(intent);
            }
        });
        label_sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListBarActivity.class);
                intent.putExtra("BarLabel","体育");
                startActivity(intent);
            }
        });
        label_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListBarActivity.class);
                intent.putExtra("BarLabel","购物");
                startActivity(intent);
            }
        });
        label_teach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListBarActivity.class);
                intent.putExtra("BarLabel","教育");
                startActivity(intent);
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //自动打开关闭SearchView
                //searchView.autoOpenOrClose();
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
