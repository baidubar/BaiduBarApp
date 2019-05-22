package com.example.baidupostbar.bean;

public class MsgReply {
    private int id;// 删除用
    private int comment_user_id;
    private String comment_user_name;
    private String comment_user_avatar;
    private String content;
    private String time;
    private int comment_floor;
    private int post_id; // 拿着这个和楼层去请求“获取帖子评论的接口”
    private String replied_content;
    private boolean floor_reply_status;
    private boolean read_status;

    public String getReplied_content() {
        return replied_content;
    }

    public void setReplied_content(String replied_content) {
        this.replied_content = replied_content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComment_user_id() {
        return comment_user_id;
    }

    public void setComment_user_id(int comment_user_id) {
        this.comment_user_id = comment_user_id;
    }

    public String getComment_user_name() {
        return comment_user_name;
    }

    public void setComment_user_name(String comment_user_name) {
        this.comment_user_name = comment_user_name;
    }

    public String getComment_user_avatar() {
        return comment_user_avatar;
    }

    public void setComment_user_avatar(String comment_user_avatar) {
        this.comment_user_avatar = comment_user_avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getComment_floor() {
        return comment_floor;
    }

    public void setComment_floor(int comment_floor) {
        this.comment_floor = comment_floor;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public boolean isFloor_reply_status() {
        return floor_reply_status;
    }

    public void setFloor_reply_status(boolean floor_reply_status) {
        this.floor_reply_status = floor_reply_status;
    }

    public boolean isRead_status() {
        return read_status;
    }

    public void setRead_status(boolean read_status) {
        this.read_status = read_status;
    }
}
