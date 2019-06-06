package com.pranjal98.instagram;

public class Post {

    private String UserID;
    private String PostID;
    private String dp_url;
    private String userName;
    private String descUri;
    private String desc;
    private String likeCount;
    private String  uploadHour, uploadMin, uploadSec, uploadDate;

    public Post(){

    }

    public Post(String userID, String postID, String dp_url, String userName, String descUri, String desc, String likeCount, String uploadHour, String uploadMin, String uploadSec, String uploadDate) {
        this.UserID = userID;
        this.PostID = postID;
        this.dp_url = dp_url;
        this.userName = userName;
        this.descUri = descUri;
        this.desc = desc;
        this.likeCount = likeCount;
        this.uploadHour = uploadHour;
        this.uploadMin = uploadMin;
        this.uploadSec = uploadSec;
        this.uploadDate = uploadDate;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }

    public String getDp_url() {
        return dp_url;
    }

    public void setDp_url(String dp_url) {
        this.dp_url = dp_url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescUri() {
        return descUri;
    }

    public void setDescUri(String descUri) {
        this.descUri = descUri;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getUploadHour() {
        return uploadHour;
    }

    public void setUploadHour(String uploadHour) {
        this.uploadHour = uploadHour;
    }

    public String getUploadMin() {
        return uploadMin;
    }

    public void setUploadMin(String uploadMin) {
        this.uploadMin = uploadMin;
    }

    public String getUploadSec() {
        return uploadSec;
    }

    public void setUploadSec(String uploadSec) {
        this.uploadSec = uploadSec;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}
