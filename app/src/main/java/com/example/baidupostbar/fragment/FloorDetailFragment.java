package com.example.baidupostbar.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.baidupostbar.Adapter.FloorDetailAdapter;
import com.example.baidupostbar.DetailPost;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.FloorDetail;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FloorDetailFragment extends DialogFragment {
    private ImageView btn_close;
    View view;
    private RecyclerView recyclerView;
    private ArrayList<FloorDetail> mDataList;
    private Dialog dialog;
    private String postId;
    private String floor;
    String cookie;
    String userId;
    boolean status;
    BaseQuickAdapter floorDetailAdapter;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        floor = ((DetailPost) context).getThisFloor();
        postId = ((DetailPost) context).getPostId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dialog,container,false);
               initView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initView(dialog);
//        initData();
//        initAdapter();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");
        userId = sharedPreferences.getString("user_id", "");

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

    @Override
    public void onResume() {
        super.onResume();
        postHttp(floor);
    }
    private void initAdapter(){
        floorDetailAdapter = new FloorDetailAdapter(R.layout.item_dialog_comment, mDataList,getContext(),cookie,userId);
        floorDetailAdapter.openLoadAnimation();
        View top = getLayoutInflater().inflate(R.layout.item_dialog_floor, (ViewGroup) recyclerView.getParent(), false);
        floorDetailAdapter.addHeaderView(top);
        floorDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //设置点击事件
                //应该是做成点击此条评论，弹出评论框
                FloorDetail floorDetail = mDataList.get(position + 1);
                CommentDialogFragment commentDialogFragment = new CommentDialogFragment(floorDetail.getAuthorName());
                commentDialogFragment.show(getFragmentManager(), "CommentDialogFragment");
                dismiss();
            }
        });
        floorDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                FloorDetail floorDetail = mDataList.get(position + 1);
                sendRequestWithOkHttp(position + 1,view,floorDetail);
                Log.d("评论id2",String.valueOf(floorDetail.getCommentId()));
            }
        });
        recyclerView.setAdapter(floorDetailAdapter);
    }
    private void postHttp(String floor){
        try {
            OkHttpClient client = new OkHttpClient();

            String url = "http://139.199.84.147/mytieba.api/post/"+postId+"/comment"+ "?floor=" + floor;

            Log.e("FloorDetailFragment",url);
            Request request = new Request.Builder().url(url)
                    .addHeader("Cookie",cookie)
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
    private void prasedWithJsonData(String JsonData){
        Log.e("FloorDetail",JsonData);
        mDataList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            boolean status = jsonObject.getBoolean("status");
            if(status) {
                String floor_number = jsonObject.getString("floor_number");
                String floor_content = jsonObject.getString("floor_content");
                String floor_writer_name = jsonObject.getString("floor_writer_name");
                String floor_writer_avatar = jsonObject.getString("floor_writer_avatar");
                FloorDetail floorDetail1 = new FloorDetail();
                floorDetail1.setFloorNum(floor_number);
                floorDetail1.setAuthorName(floor_writer_name);
                floorDetail1.setHeadImag("http://139.199.84.147/" + floor_writer_avatar);
                floorDetail1.setContent(floor_content);
                floorDetail1.setTime("");
                mDataList.add(floorDetail1);
                JSONArray jsonArray = jsonObject.getJSONArray("comment_msg");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    int comment_id = jsonObject1.getInt("comment_id");
                    boolean reply_status = jsonObject1.getBoolean("reply_status");
                   int person_id = jsonObject1.getInt("person_id");
                    String person_avatar = jsonObject1.getString("person_avatar");
                    String person_name = jsonObject1.getString("person_name");
                    String datetime = jsonObject1.getString("datetime");
                    String content = jsonObject1.getString("content");
                    if (reply_status)
                    {
                        String reply_person_id = jsonObject1.getString("reply_person_id");
                        String reply_person_name = jsonObject1.getString("reply_person_name");
                    }
                    int num = i+1;
                    FloorDetail floorDetail = new FloorDetail();
                    floorDetail.setAuthorName(person_name);
                    floorDetail.setHeadImag("http://139.199.84.147/" + person_avatar);
                    floorDetail.setContent(content);
                    floorDetail.setFloorNum("第"+ num + "楼");
                    floorDetail.setTime(datetime);
                    floorDetail.setCommentId(comment_id);
                    floorDetail.setReply_person_id(person_id);
                    Log.d("评论者id",String.valueOf(person_id));
                    Log.d("评论id",String.valueOf(comment_id));
                    mDataList.add(floorDetail);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initAdapter();
                    }
                });
            }else {
                String msg = jsonObject.getString("msg");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendRequestWithOkHttp(int position, View view,FloorDetail floorDetail){
        //开启现线程发起网络请求
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    OkHttpClient client = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)  //网查解决end of the stream问题
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(20,TimeUnit.SECONDS)
                            .build();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("comment_id",String.valueOf(floorDetail.getCommentId()))
                            .build();

                    Request request = new Request.Builder()
                            .url("http://139.199.84.147/mytieba.api/user/"+userId+"/comment")
                            .delete(requestBody)
                            .addHeader("Cookie",cookie)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();
                    Log.d("返回的是啥",responseDate);
                    //Log.d("要删的id",String.valueOf(userFollow.getUser_id()));
                    JSONTokener(responseDate);
                    showResponse(responseDate,position);
//                    Looper.prepare();
//                    if (status){
//                        Toast.makeText(view.getContext(),"已取消",Toast.LENGTH_LONG).show();
////                        Intent intent = new Intent(view.getContext(), NewsDetail.class);
////                        intent.putExtra("user_id",userName);
////                        intent.putExtra("session",session);
////                        intent.putExtra("newsId",newsId);
////                        view.getContext().startActivity(intent);
//                        //changeUi(position);
//
//
//                    }else
//                    {
//                        Toast.makeText(view.getContext(), "操作失败", Toast.LENGTH_LONG).show();
//                        holder.btn.setEnabled(true);
//                    }
//
//                    Looper.loop();

                }catch (Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                            if (e instanceof SocketTimeoutException){
                                Toast.makeText(getContext(),"连接超时",Toast.LENGTH_SHORT).show();
                            }
                            if (e instanceof ConnectException){
                                Toast.makeText(getContext(),"连接异常",Toast.LENGTH_SHORT).show();
                            }

                            if (e instanceof ProtocolException) {
                                Toast.makeText(getContext(),"未知异常，请稍后再试",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }
    private static String JSONTokener(String in) {
        // consume an optional byte order mark (BOM) if it exists
        if (in != null && in.startsWith("\ufeff")) {
            in = in.substring(1);
        }
        return in;
    }
    private void showResponse(final String response,int position){
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getBoolean("status");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){ //设置ui
                if (status){

                    mDataList.remove(position);
                    floorDetailAdapter.notifyItemRemoved(position);
                    floorDetailAdapter.notifyItemRangeChanged(position,mDataList.size() - position);
                    Toast.makeText(getContext(),"已删除",Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(getContext(),"请重试",Toast.LENGTH_LONG).show();
            }
        });
    }
}
