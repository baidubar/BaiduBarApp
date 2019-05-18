package com.example.baidupostbar.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;


public class Search extends LitePalSupport {

    @Column(unique = true, defaultValue = "unknown")

    private String userName;
    private String searchData;

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getSearchData(){
        return searchData;
    }
    public void setSearchData(String searchData){
        this.searchData = searchData;
    }
}
