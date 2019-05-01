package com.example.baidupostbar.bean;

public class Recommend {
    private String authorImage;
    private String authorName;
    private String content;
    private String commentNum;
    private String likeNum;
    private String barName;
    private String label;
    public Recommend(){}
    public String getAuthorImage(){return  authorImage;}

    public void setAuthorImage(String authorImage){
        this.authorImage = authorImage;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContent(){return content;}

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentNum(){return commentNum;}

    public void setCommentNum(String commentNum) {
        this.commentNum= commentNum;
    }
    public String getLikeNum(){return likeNum;}

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }
    public String getBarName(){return barName;}

    public void setBarName(String barName) {
        this.barName = barName;
    }
    public String getLabel(){return label;}

    public void setLabel(String label) {
        this.label = label;
    }
}
