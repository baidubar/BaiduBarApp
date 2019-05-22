package com.example.baidupostbar.bean;

import java.util.ArrayList;

public class PostDetail {
    private Class<?> activity;
    private String userImg;
    private String userName;
    private String time;
    private String content;
    private String likeNum;
    private String barName;
    private String commentNum;
    private String label;
    private ArrayList<String>photo;




    public ArrayList<String> getPhoto() {return photo; }

    public void setPhoto(ArrayList<String> photo) {
        this.photo = photo;
    }

    public String getLabel() {return label; }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCommentNum() {return commentNum; }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getBarName() {return barName; }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public String getLikeNum() {return likeNum; }

    public void setLikeNum(String userImg) {
        this.likeNum = likeNum;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Class<?> getActivity() {
        return activity;
    }

    public void setActivity(Class<?> activity) {
        this.activity = activity;
    }
}
