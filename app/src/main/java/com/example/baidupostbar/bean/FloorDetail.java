package com.example.baidupostbar.bean;

public class FloorDetail {
    private String headImag;
    private String authorName;
    private String content;
    private String time;
    private boolean collection_status;
    private String reply_person_id;

    private String floorNum;
    public FloorDetail(){}
    public boolean isCollection_status() {
        return collection_status;
    }

    public String getReply_person_id() {
        return reply_person_id;
    }

    public void setReply_person_id(String reply_person_id) {
        this.reply_person_id = reply_person_id;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }
    public String getFloorNum(){
        return floorNum;
    }

    public void setHeadImag(String headImag) {
        this.headImag = headImag;
    }
    public String getHeadImag(){
        return headImag;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public String getAuthorName(){
        return authorName;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent(){
        return content;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTime(){
        return time;
    }
    public void setCollection_status(boolean collection_status) {
        this.collection_status = collection_status;
    }
    public boolean getCollection_status(){
        return collection_status;
    }
}