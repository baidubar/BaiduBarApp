package com.example.baidupostbar.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Post implements Parcelable {
    public String barId;
    public String content;
    public String comment_number;
    public String praise_number;
    public ArrayList<String> photos;
    public String headImage;
    public String writterName;
    public String barName;
    public String barLabel;
    public String postId;
    public String writer_id;
    private boolean praise_status;


    public String getWriter_id(){
        return writer_id;
    }

    public void setWriter_id(String writer_id){
        this.writer_id = writer_id;
    }

    public String getBarId(){
        return barId;
    }

    public void setBarId(String barId){
        this.barId = barId;
    }

    public String getBarName(){
        return barName;
    }

    public void setBarName(String barName){
        this.barName = barName;
    }

    public String getBarLabel(){
        return barLabel;
    }

    public void setBarLabel(String barLabel){
        this.barLabel = barLabel;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getWritterName() {
        return writterName;
    }

    public void setWritterName(String writterName) {
        this.writterName = writterName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPraise_number() {
        return praise_number;
    }

    public void setPraise_number(String praise_number) {
        this.praise_number = praise_number;
    }

    public String getComment_number() {
        return comment_number;
    }

    public void setComment_number(String comment_number) {
        this.comment_number = comment_number;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeStringList(this.photos);
        dest.writeString(this.comment_number);
        dest.writeString(this.praise_number);
        dest.writeString(this.headImage);
        dest.writeString(this.writterName);
        dest.writeString(this.barLabel);
        dest.writeString(this.barName);
        dest.writeString(this.barId);
        dest.writeString(this.postId);
        dest.writeString(this.writer_id);
    }

    public Post() {
    }

    public Post(String content, ArrayList<String> photos,String comment_number,String praise_number,String headImage,String writterName,String barLabel,String barName,String barId,String postId,String writer_id) {
        this.content = content;
        this.photos = photos;
        this.comment_number = comment_number;
        this.praise_number = praise_number;
        this.writterName = writterName;
        this.headImage = headImage;
        this.barLabel = barLabel;
        this.barName = barName;
        this.barId = barId;
        this.postId = postId;
        this.writer_id = writer_id;
    }

    protected Post(Parcel in) {
        this.content = in.readString();
        this.photos = in.createStringArrayList();
        this.praise_number = in.readString();
        this.comment_number = in.readString();
        this.writterName = in.readString();
        this.headImage = in.readString();
        this.barName = in.readString();
        this.barLabel = in.readString();
        this.barId = in.readString();
        this.postId = in.readString();
        this.writer_id = in.readString();
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
