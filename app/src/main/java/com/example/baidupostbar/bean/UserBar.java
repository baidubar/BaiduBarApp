package com.example.baidupostbar.bean;

import java.util.ArrayList;

public class UserBar {
    private int bar_id;
    private String bar_name;
    private ArrayList<String> bar_tags;
    private String bar_icon;

    public int getBar_id() {
        return bar_id;
    }

    public void setBar_id(int bar_id) {
        this.bar_id = bar_id;
    }

    public String getBar_name() {
        return bar_name;
    }

    public void setBar_name(String bar_name) {
        this.bar_name = bar_name;
    }

    public ArrayList<String> getBar_tags() {
        return bar_tags;
    }

    public void setBar_tags(ArrayList<String> bar_tags) {
        this.bar_tags = bar_tags;
    }

    public String getBar_icon() {
        return bar_icon;
    }

    public void setBar_icon(String bar_icon) {
        this.bar_icon = bar_icon;
    }
}
