package com.example.baidupostbar.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.baidupostbar.Adapter.FloorDetailAdapter;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.FloorDetail;

import java.util.ArrayList;

public class FloorDetailFragment extends DialogFragment {
    private ImageView btn_close;
    View view;
    private RecyclerView recyclerView;
    private ArrayList<FloorDetail> mDataList;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dialog,container,false);
               initView(view);
        initData();
        initAdapter();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initView(dialog);
//        initData();
//        initAdapter();

    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.fragment_dialog);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 13 / 14;
        window.setAttributes(lp);

//        initView(dialog);
//        initData();
//        initAdapter();
//        if (getDialog() != null) {
//            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface anInterface, int keyCode, KeyEvent event) {
//                    if(keyCode==KeyEvent.KEYCODE_ENTER&&event.getAction()==KeyEvent.ACTION_DOWN){
////                        if(!TextUtils.isEmpty(gridPasswordView.getText().toString().trim())){
////                            if("123456".equals(gridPasswordView.getText().toString().trim())){
////                                //TODO 跳转支付宝支付
////                            }
////                        }
//                    }else{
//                        Toast.makeText(getContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
//                    }
//                    return false;
//                }
//            });
//        }
        this.dialog = dialog;
        return dialog;
    }
    private void initView(View dialog){
        btn_close = (ImageView) dialog.findViewById(R.id.btn_close);
        recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btn_close.setOnClickListener(listener);
    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
    private void initData() {
        mDataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FloorDetail item = new FloorDetail();

            mDataList.add(item);
        }
    }
    private void initAdapter(){
        BaseQuickAdapter floorDetailAdapter = new FloorDetailAdapter(R.layout.item_dialog_comment, mDataList);
        floorDetailAdapter.openLoadAnimation();
        View top = getLayoutInflater().inflate(R.layout.item_dialog_floor, (ViewGroup) recyclerView.getParent(), false);
        floorDetailAdapter.addHeaderView(top);
        floorDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //设置点击事件
                //应该是做成点击此条评论，弹出评论框
            }
        });

        recyclerView.setAdapter(floorDetailAdapter);
    }
}
