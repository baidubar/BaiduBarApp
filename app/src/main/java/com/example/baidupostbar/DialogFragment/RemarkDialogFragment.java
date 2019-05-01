package com.example.baidupostbar.DialogFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.baidupostbar.R;
import com.example.baidupostbar.Adapter.RemarkAdapter;
import com.example.baidupostbar.bean.Remark;

import java.util.ArrayList;
import java.util.List;


public class RemarkDialogFragment extends DialogFragment {
    private String responseData;
    private RecyclerView recyclerView;
    private List<Remark> list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(RemarkDialogFragment.STYLE_NORMAL,R.style.dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_remark,container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.df_remark_recyclerView);
        list = new ArrayList<>();
        for(int i = 0;i<15;i++){
            Remark remark = new Remark();
            remark.setRemarkContent("第"+ i + "条内容");
            list.add(remark);
        }
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        RemarkAdapter remarkAdapter = new RemarkAdapter(R.layout.item_remark,list);
        recyclerView.setAdapter(remarkAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();


        Window window = getDialog().getWindow();
        window.setContentView(R.layout.dialogfragment_remark);
        WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = Gravity.BOTTOM;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
       // params.height = ViewGroup.LayoutParams.WRAP_CONTENT; // 底部弹出的DialogFragment的高度，如果是MATCH_PARENT则铺满整个窗口
        params.height = 2000;
        window.setAttributes(params);

//        getDialog().setCancelable(false);
//        getDialog().setCanceledOnTouchOutside(false);
//        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                dialog.dismiss();
//                return false;
//            }
//        });


//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.dialogfragment_remark, null);
//
//        // 底部弹出的DialogFragment装载的View
//        getDialog().setContentView(view);
//
//        getDialog().setCancelable(true);
//        getDialog().setCanceledOnTouchOutside(true);
//
//        // 设置底部弹出显示的DialogFragment窗口属性。
//        Window window = getDialog().getWindow();
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.gravity = Gravity.BOTTOM;
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = 1000; // 底部弹出的DialogFragment的高度，如果是MATCH_PARENT则铺满整个窗口
//        window.setAttributes(params);
//
//        //***********************************
//        // window.getDecorView().setBackgroundColor(context.getResources().getColor(R.color
//        //            .transparency_0));
//        //            设置背景色
//        //***********************************
//
//        //背景图
//        //window.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.boy));
//        //动画效果
//        //  window.setWindowAnimations(R.style.BottomDialog_Animation);
//        //设置边距
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout((int) (1000 ), ViewGroup.LayoutParams.WRAP_CONTENT);
    }



    @Override
    public void onResume() {
        super.onResume();
    }
}
