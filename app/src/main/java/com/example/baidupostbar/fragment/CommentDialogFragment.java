package com.example.baidupostbar.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.baidupostbar.DetailPost;
import com.example.baidupostbar.Interface.DialogFragmentDataCallback;
import com.example.baidupostbar.R;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by showzeng on 17-8-11.
 * Email: kingstageshow@gmail.com
 * GitHub: https://github.com/showzeng
 */

public class CommentDialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener{

    private EditText commentEditText;
    private ImageView sendButton;
    private InputMethodManager inputMethodManager;
    private DialogFragmentDataCallback dataCallback;
    private String postId;
    private String reply_id = "0";
    private String reply_floor;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(getActivity() instanceof DialogFragmentDataCallback)) {
            //throw new IllegalStateException("DialogFragment 所在的 activity 必须实现 DialogFragmentDataCallback 接口");
        }
        reply_floor = ((DetailPost) activity).getThisFloor();
        postId = ((DetailPost) activity).getPostId();
        Log.e("CDF","postId"+ postId);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog mDialog = new Dialog(getActivity(), R.style.BottomDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_fragment_comment_layout);
        mDialog.setCanceledOnTouchOutside(true);

        Window window = mDialog.getWindow();
        WindowManager.LayoutParams layoutParams;
        if (window != null) {
            layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }

        commentEditText = (EditText) mDialog.findViewById(R.id.edit_comment);
        sendButton = (ImageView) mDialog.findViewById(R.id.image_btn_comment_send);

        setSoftKeyboard();

        commentEditText.addTextChangedListener(mTextWatcher);

        sendButton.setOnClickListener(this);

        return mDialog;
    }


    private void setSoftKeyboard() {
        commentEditText.setFocusable(true);
        commentEditText.setFocusableInTouchMode(true);
        commentEditText.requestFocus();

        //为 commentEditText 设置监听器，在 DialogFragment 绘制完后立即呼出软键盘，呼出成功后即注销
        commentEditText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    if (inputMethodManager.showSoftInput(commentEditText, 0)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            commentEditText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                }
            }
        });
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() > 0) {
                sendButton.setEnabled(true);
                sendButton.setClickable(true);
                sendButton.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorAccent));
            } else {
                sendButton.setEnabled(false);
                sendButton.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.iconCover));
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_btn_comment_send:
                String content = commentEditText.getText().toString();
                content = trim(content);
                if(!content.equals("")){
                    postComment(content);
                }
                else {
                    Toast.makeText(getContext(),"请填入内容",Toast.LENGTH_LONG).show();
                }
                commentEditText.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //dataCallback.setCommentText(commentEditText.getText().toString());
//        Toast.makeText(getActivity(), "onDismiss", Toast.LENGTH_SHORT).show();
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        //dataCallback.setCommentText(commentEditText.getText().toString());
       // Toast.makeText(getActivity(), "onCancel", Toast.LENGTH_SHORT).show();
        super.onCancel(dialog);
    }
    private void postComment(String content){
        String url = "http://139.199.84.147/mytieba.api/post/"+ postId +"/comment";
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("theUser", Context.MODE_PRIVATE);
            String cookie = sharedPreferences.getString("cookie", "");
            String userId = sharedPreferences.getString("user_id","");
            FormBody formBody = new FormBody.Builder()
                    .add("user_id",userId)
                    .add("reply_id",reply_id)
                    .add("reply_floor",reply_floor)
                    .add("content",content)
                    .build();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url)
                    .addHeader("Cookie",cookie)
                    .post(formBody)
                    .build();
            Log.e("FloorDrtail",url);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Log.e("onFailure","获取数据失败");
                    Toast.makeText(getContext(),"网络请求失败",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.e("rsponseData",responseData);
                    if (response.isSuccessful()){
                        prasedWithJsonData(responseData);
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
    public static String trim(String s){
        String result = "";
        if(null!=s && !"".equals(s)){
            result = s.replaceAll("[\\s]", "").replaceAll("[\\s]", "");
        }
        return result;
    }
    private void prasedWithJsonData(String JsonData){
        Log.e("CDF",JsonData);
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            boolean status = jsonObject.getBoolean("status");
            if(status){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"评论成功",Toast.LENGTH_LONG).show();
                    }
                });
            }else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"请求失败",Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dismiss();
    }
}