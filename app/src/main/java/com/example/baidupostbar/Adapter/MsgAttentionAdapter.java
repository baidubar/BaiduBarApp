package com.example.baidupostbar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.MsgAttention;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MsgAttentionAdapter extends RecyclerView.Adapter<MsgAttentionAdapter.ViewHolder>{

    private List<MsgAttention> list;
    private Context context;
    private boolean hasMore = true;
    private String cookie;
    private String userId;
    private boolean status;


    public MsgAttentionAdapter(List<MsgAttention> list,Context context,String cookie,String userId){
        this.cookie = cookie;
        this.userId = userId;
        this.list = list;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_user;
        TextView tv_userName;
        View msgAttentionView;
        Button btn_delete;

        public ViewHolder(View view) {
            super(view);
            msgAttentionView = view;
            iv_user = view.findViewById(R.id.iv_user);
            tv_userName = view.findViewById(R.id.tv_userName);
            btn_delete = view.findViewById(R.id.btnDelete);
        }
    }

    public MsgAttentionAdapter(List<MsgAttention> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MsgAttentionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context == null){
            context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_msg_attention, viewGroup, false);
        final MsgAttentionAdapter.ViewHolder holder = new MsgAttentionAdapter.ViewHolder(view);
        holder.msgAttentionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                MsgAttention msgAttention = list.get(position);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                MsgAttention msgAttention = list.get(position);
                holder.btn_delete.setEnabled(false);
                sendRequestWithOkHttp(position,msgAttention,view,holder);
                if (status){
//                        Intent intent = new Intent(view.getContext(), NewsDetail.class);
//                        intent.putExtra("user_id",userName);
//                        intent.putExtra("session",session);
//                        intent.putExtra("newsId",newsId);
//                        view.getContext().startActivity(intent);
                    //changeUi(position);
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,getItemCount() - position);

                }else
                {
                    Toast.makeText(view.getContext(), "操作失败", Toast.LENGTH_LONG).show();
                    holder.btn_delete.setEnabled(true);
                }
//                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MsgAttentionAdapter.ViewHolder viewHolder, int i) {
        MsgAttention msgAttention = list.get(i);
        viewHolder.tv_userName.setText(msgAttention.getUsername());
        Glide.with(context).load("http://139.199.84.147"+msgAttention.getAvatar()).into(viewHolder.iv_user);
    }

    @Override
    public int getItemCount() {
        if(null==list) return 0;
        else return list.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public interface onSwipeListener {
        void onDel(int pos);

    }
    // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
    public void updateList(List<MsgAttention> newDatas, boolean hasMore) {
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            list.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }
    private void sendRequestWithOkHttp(int position, MsgAttention msgAttention, View view, MsgAttentionAdapter.ViewHolder holder){
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
                            .add("following_ids",String.valueOf(msgAttention.getId()))
                            .add("type","following")
                            .add("watching_ids","")
                            .build();

                    Request request = new Request.Builder()
                            .url("http://139.199.84.147/mytieba.api/user/"+userId+"/msg")
                            .delete(requestBody)
                            .addHeader("Cookie",cookie)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();
                    Log.d("返回的是啥",responseDate);
                    //Log.d("要删的id",String.valueOf(userFollow.getUser_id()));
                    JSONTokener(responseDate);
                    JSONObject jsonObject = new JSONObject(responseDate);
                    status = jsonObject.getBoolean("status");
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
                    e.printStackTrace();
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
}