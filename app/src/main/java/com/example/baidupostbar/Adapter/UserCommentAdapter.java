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

import com.bumptech.glide.Glide;
import com.example.baidupostbar.R;
import com.example.baidupostbar.bean.UserComment;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserCommentAdapter extends RecyclerView.Adapter<UserCommentAdapter.ViewHolder>{

    private List<UserComment> list;
    private Context context;
    private boolean hasMore = true;
    private boolean isDeleteAble = true;
    private String cookie;
    private String userId;
    private boolean status = true;


    public UserCommentAdapter(List<UserComment> list,Context context,String cookie,String userId){
        this.list = list;
        this.context = context;
        this.cookie = cookie;
        this.userId = userId;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_author;
        TextView tv_author;
        View userCommentView;
        TextView tv_time;
        TextView tv_floorContent;
        TextView tv_postContent;
        Button btn_delete;

        public ViewHolder(View view) {
            super(view);
            userCommentView = view;
            iv_author = (ImageView) view.findViewById(R.id.iv_author);
            tv_author = (TextView) view.findViewById(R.id.tv_author);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_floorContent = view.findViewById(R.id.tv_floorContent);
            tv_postContent = view.findViewById(R.id.tv_postContent);
            btn_delete = view.findViewById(R.id.btnDelete);
        }
    }

    public UserCommentAdapter(List<UserComment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context == null){
            context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_msg_setfloor, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.userCommentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                UserComment userComment = list.get(position);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                UserComment userComment = list.get(position);
                holder.btn_delete.setEnabled(false);
                sendRequestWithOkHttp(position,userComment,view,holder);
//                if (status){
//                        Intent intent = new Intent(view.getContext(), NewsDetail.class);
//                        intent.putExtra("user_id",userName);
//                        intent.putExtra("session",session);
//                        intent.putExtra("newsId",newsId);
//                        view.getContext().startActivity(intent);
                    //changeUi(position);
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,getItemCount() - position);

//                }else
//                {
//                    Toast.makeText(view.getContext(), "操作失败", Toast.LENGTH_LONG).show();
//                    holder.btn_delete.setEnabled(true);
//                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserCommentAdapter.ViewHolder viewHolder, int i) {
        UserComment userComment = list.get(i);
        viewHolder.tv_author.setText(userComment.getWriter_name());
        viewHolder.tv_time.setText(userComment.getTime());
        Glide.with(context).load("http://139.199.84.147"+userComment.getWriter_avatar()).into(viewHolder.iv_author);
        viewHolder.tv_postContent.setText("我:" + userComment.getPost_content());
        viewHolder.tv_floorContent.setText(userComment.getFloor_content());
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
    public void updateList(List<UserComment> newDatas, boolean hasMore) {
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            list.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }
    private void sendRequestWithOkHttp(int position, UserComment userComment, View view, UserCommentAdapter.ViewHolder holder){
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
                            .add("floor_id",String.valueOf(userComment.getId()))
                            .add("type","floor")
                            .build();

                    Request request = new Request.Builder()
                            .url("http://139.199.84.147/mytieba.api/user/"+userId+"/information")
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