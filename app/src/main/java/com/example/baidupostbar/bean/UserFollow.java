package com.example.baidupostbar.bean;

public class UserFollow {
    private int user_id;
    private String user_avatar;
    private String user_name;
    private boolean follower_status;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public boolean isFollower_status() {
        return follower_status;
    }

    public void setFollower_status(boolean follower_status) {
        this.follower_status = follower_status;
    }
}
