package com.example.baidupostbar.bean;

public class UserComment {
    private int id;
    private String post_content;
    private int post_id;
    private int floor_number;
    private int writer_id;
    private String writer_name;
    private String writer_avatar;
    private boolean read_status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = "我："+ post_content;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getFloor_number() {
        return floor_number;
    }

    public void setFloor_number(int floor_number) {
        this.floor_number = floor_number;
    }

    public int getWriter_id() {
        return writer_id;
    }

    public void setWriter_id(int writer_id) {
        this.writer_id = writer_id;
    }

    public String getWriter_name() {
        return writer_name;
    }

    public void setWriter_name(String writer_name) {
        this.writer_name = writer_name;
    }

    public String getWriter_avatar() {
        return writer_avatar;
    }

    public void setWriter_avatar(String writer_avatar) {
        this.writer_avatar = writer_avatar;
    }

    public boolean isRead_status() {
        return read_status;
    }

    public void setRead_status(boolean read_status) {
        this.read_status = read_status;
    }
}
