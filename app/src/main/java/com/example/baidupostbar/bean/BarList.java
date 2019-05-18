package com.example.baidupostbar.bean;

//这个bean是barFragment点击标签后显示的bar的列表item
public class BarList {
    private String barImg;
    private String barName;
    private String barInfor;
    private String barId;
    private String attentionNum;
    private String postNum;

    public BarList(){}
    public String getBarImg() {
        return barImg;
    }

    public void setBarImg(String barImg) {
        this.barImg = barImg;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public String getBarInfor() {
        return barInfor;
    }

    public void setBarInfor(String barInfor) {
        this.barInfor = barInfor;
    }

    public String getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(String attentionNum) {
        this.attentionNum = attentionNum;
    }

    public String getPostNum() {
        return postNum;
    }

    public void setPostNum(String postNum) {
        this.postNum = postNum;
    }

    public String getBarId() {
        return barId;
    }

    public void setBarId(String barId) {
        this.barId = barId;
    }
}
