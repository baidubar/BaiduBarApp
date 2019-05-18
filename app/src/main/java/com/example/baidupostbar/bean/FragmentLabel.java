package com.example.baidupostbar.bean;

public class FragmentLabel {
    private int imageId;
    private String BarLabel;

    public String getBarLabel() {
        return BarLabel;
    }
    public void setBarLabel(String BarLabel) {
        this.BarLabel = BarLabel;
    }

    public FragmentLabel(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
