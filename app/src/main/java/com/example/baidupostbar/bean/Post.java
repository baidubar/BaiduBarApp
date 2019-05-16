package com.example.baidupostbar.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Post implements Parcelable {
    public String content;
    public ArrayList<String> photos;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
    }

    public Post() {
    }

    public Post(String content, ArrayList<String> photos) {
        this.content = content;
        this.photos = photos;
    }

    protected Post(Parcel in) {
        this.content = in.readString();
        this.photos = in.createStringArrayList();
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
