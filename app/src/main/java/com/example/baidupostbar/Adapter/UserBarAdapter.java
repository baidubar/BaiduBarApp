package com.example.baidupostbar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.UserBar;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserBarAdapter extends RecyclerView.Adapter<UserBarAdapter.ViewHolder>{

    private List<UserBar> list;
    private Context context;
    private boolean hasMore = true;
    private String cookie;
    private String userId;
    private boolean status;


    public UserBarAdapter(List<UserBar> list,Context context,String cookie,String userId){
        this.list = list;
        this.context = context;
        this.cookie = cookie;
        this.userId = userId;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_bar;
        TextView tv_bar;
        View userBarView;
        TextView btn;

        public ViewHolder(View view) {
            super(view);
            userBarView = view;
            iv_bar = view.findViewById(R.id.iv_bar);
            tv_bar = view.findViewById(R.id.tv_bar);
            btn = view.findViewById(R.id.btn);
        }
    }

    public UserBarAdapter(List<UserBar> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserBarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context == null){
            context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_user_bar, viewGroup, false);
        final UserBarAdapter.ViewHolder holder = new UserBarAdapter.ViewHolder(view);
        holder.userBarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                UserBar userBar = list.get(position);
            }
        });
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                UserBar userBar = list.get(position);
                Snackbar.make(view,"确定要取消关注吗？",Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.btn.setEnabled(false);
                                sendRequestWithOkHttp(position,userBar,view,holder);
                                //if (status){
                                    Toast.makeText(view.getContext(),"已取消",Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(view.getContext(), NewsDetail.class);
//                        intent.putExtra("user_id",userName);
//                        intent.putExtra("session",session);
//                        intent.putExtra("newsId",newsId);
//                        view.getContext().startActivity(intent);
                                    //changeUi(position);
                                    list.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,getItemCount() - position);

//                                }else
//                                {
//                                    Toast.makeText(view.getContext(), "操作失败", Toast.LENGTH_LONG).show();
//                                    holder.btn.setEnabled(true);
//                                }
//                                list.remove(position);
//                                notifyItemRemoved(position);
//                                notifyItemRangeChanged(position,getItemCount() - position);
                            }
                        })
                        .show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserBarAdapter.ViewHolder viewHolder, int i) {
        UserBar userBar = list.get(i);
        viewHolder.tv_bar.setText(userBar.getBar_name());
        Glide.with(context).load("http://139.199.84.147"+userBar.getBar_icon()).into(viewHolder.iv_bar);
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
    // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
    public void updateList(List<UserBar> newDatas, boolean hasMore) {
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            list.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }
    private void sendRequestWithOkHttp(int position, UserBar userBar, View view, UserBarAdapter.ViewHolder holder){
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
                            .add("bar_id",String.valueOf(userBar.getBar_id()))
                            .build();

                    Request request = new Request.Builder()
                            .url("http://139.199.84.147/mytieba.api/user/"+userId+"/watching")
                            .delete(requestBody)
                            .addHeader("Cookie",cookie)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();
                    Log.d("返回的是啥",responseDate);
                    Log.d("要删的id",String.valueOf(userBar.getBar_id()));
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
