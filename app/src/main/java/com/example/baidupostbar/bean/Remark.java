package com.example.baidupostbar.bean;

public class Remark {
    private String authorImage;
    private String authorName;
    private String content;
    public String getRemarkAuthorImage(){return  authorImage;}

    public void setRemarkAuthorImage(String authorImage){
        this.authorImage = authorImage;
    }

    public String getRemarkAuthorName() {
        return authorName;
    }

    public void setRemarkAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getRemarkContent(){return content;}

    public void setRemarkContent(String content) {
        this.content = content;
    }
}
