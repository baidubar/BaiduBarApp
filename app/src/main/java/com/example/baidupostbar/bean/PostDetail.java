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
    private String floor;
    private boolean collection_status;
    private String personId;
    private boolean praise_status;





    public void setPraise_status(boolean praise_status){this.praise_status = praise_status;}

    public boolean getPraise_status(){
        return praise_status;
    }

    public void setPersonId(String personId){this.personId = personId;}

    public String getPersonId(){return personId;}


    public void setFloor(String floor){this.floor = floor;}

    public String getFloor(){return floor;}

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

    public void setCollection_status(boolean collection_status) {
        this.collection_status = collection_status;
    }
    public boolean getCollection_status(){
        return collection_status;
    }
}
