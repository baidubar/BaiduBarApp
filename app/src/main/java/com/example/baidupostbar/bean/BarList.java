package com.example.baidupostbar.bean;

//这个bean是barFragment点击标签后显示的bar的列表item
public class BarList {
    private String barImg;
    private String barName;
    private String barInfor;
    private int attentionNum;
    private int postNum;

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

    public int getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(int attentionNum) {
        this.attentionNum = attentionNum;
    }

    public int getPostNum() {
        return postNum;
    }

    public void setPostNum(int postNum) {
        this.postNum = postNum;
    }
}
