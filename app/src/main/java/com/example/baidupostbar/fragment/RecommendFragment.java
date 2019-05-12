//package com.example.baidupostbar.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.example.baidupostbar.DetailPostActivity;
//import com.example.baidupostbar.R;
//import com.example.baidupostbar.Utils.CheckNetUtil;
//import com.example.baidupostbar.Utils.HttpUtil;
//import com.example.baidupostbar.Adapter.RecommendAdapter;
//import com.example.baidupostbar.bean.Recommend;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RecommendFragment extends Fragment {
//    private RecyclerView recyclerView;
//    private SwipeRefreshLayout swipeRefreshLayout;
//   // private List<Map<String, Object>> list;
//   private List<Recommend> list;
//    private String url;
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_recommend,container, false);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        swipeRefreshLayout = view.findViewById(R.id.fragment_recommend_swipe);
//        recyclerView = view.findViewById(R.id.fragment_recommend_recyclerView);
//        HttpUtil httpUtil = new HttpUtil();
//        list = new ArrayList<>();
//        Recommend recommend;
//        for (int i = 0; i < 15; i++) {
//            recommend = new Recommend();
//            recommend.setAuthorName(String.valueOf(i));
//            recommend.setBarName("barName");
//            recommend.setCommentNum("11");
//            recommend.setLabel("iii");
//            recommend.setContent("第" + i + "条内容");
//            list.add(recommend);
//        }
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(manager);
//        RecommendAdapter adapter = new RecommendAdapter(R.layout.item_post,list);
//        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(getActivity(),DetailPostActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (new CheckNetUtil(getContext()).initNet()) {
//            //String responseData = httpUtil.GetUtil()
//            //showResponse();
//        }
//    }
//
//    private void showResponse() {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                LinearLayoutManager manager = new LinearLayoutManager(getContext());
//                recyclerView.setLayoutManager(manager);
//                RecommendAdapter adapter = new RecommendAdapter(R.layout.item_post,list);
//                recyclerView.setAdapter(adapter);
//            }
//        });
//    }
//}
