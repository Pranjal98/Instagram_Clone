package com.pranjal98.instagram;

public class searchContents {

    private String dp_url;
    private String UserName;
    private String Key;

    public searchContents() {
    }

    public searchContents(String dp_url, String userName, String key) {
        this.dp_url = dp_url;
        UserName = userName;
        Key = key;
    }

    public String getDp_url() {
        return dp_url;
    }

    public void setDp_url(String dp_url) {
        this.dp_url = dp_url;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
