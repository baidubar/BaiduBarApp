package com.example.baidupostbar.bean;

public class UserFan {
    private int follower_id;
    private String follower_avatar;
    private  String follower_name;
    private boolean concern_status;

    public int getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(int follower_id) {
        this.follower_id = follower_id;
    }

    public String getFollower_avater() {
        return follower_avatar;
    }

    public void setFollower_avater(String follower_avatar) {
        this.follower_avatar = follower_avatar;
    }

    public String getFollower_name() {
        return follower_name;
    }

    public void setFollower_name(String follower_name) {
        this.follower_name = follower_name;
    }

    public boolean isConcern_status() {
        return concern_status;
    }

    public void setConcern_status(boolean concern_status) {
        this.concern_status = concern_status;
    }
}
