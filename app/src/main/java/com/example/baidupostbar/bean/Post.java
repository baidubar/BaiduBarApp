package com.example.baidupostbar.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Post implements Parcelable {
    public String content;
    public String comment_number;
    public String praise_number;
    public ArrayList<String> photos;

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
    }

    public Post() {
    }

    public Post(String content, ArrayList<String> photos,String comment_number,String praise_number) {
        this.content = content;
        this.photos = photos;
        this.comment_number = comment_number;
        this.praise_number = comment_number;
    }

    protected Post(Parcel in) {
        this.content = in.readString();
        this.photos = in.createStringArrayList();
        this.praise_number = in.readString();
        this.comment_number = in.readString();
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
