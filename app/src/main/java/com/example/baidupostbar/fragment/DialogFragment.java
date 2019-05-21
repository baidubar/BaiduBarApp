package com.example.baidupostbar.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.baidupostbar.R;

public class DialogFragment extends android.support.v4.app.DialogFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //initView(dialog);


        if (getDialog() != null) {
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface anInterface, int keyCode, KeyEvent event) {
                    if(keyCode==KeyEvent.KEYCODE_ENTER&&event.getAction()==KeyEvent.ACTION_DOWN){
//                        if(!TextUtils.isEmpty(gridPasswordView.getText().toString().trim())){
//                            if("123456".equals(gridPasswordView.getText().toString().trim())){
//                                //TODO 跳转支付宝支付
//                            }
//                        }
                    }else{
                        Toast.makeText(getContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
        }



        return dialog;
    }
}
