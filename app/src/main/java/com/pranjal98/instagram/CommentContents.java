package com.pranjal98.instagram;

public class CommentContents {

    String PosterUserID;
    String PostID;
    String CommenterUserID;
    String CommentID;

    public CommentContents() {
    }

    public CommentContents(String posterUserID, String postID, String commenterUserID, String commentID) {
        PosterUserID = posterUserID;
        PostID = postID;
        CommenterUserID = commenterUserID;
        CommentID = commentID;
    }

    public String getPosterUserID() {
        return PosterUserID;
    }

    public void setPosterUserID(String posterUserID) {
        PosterUserID = posterUserID;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }

    public String getCommenterUserID() {
        return CommenterUserID;
    }

    public void setCommenterUserID(String commenterUserID) {
        CommenterUserID = commenterUserID;
    }

    public String getCommentID() {
        return CommentID;
    }

    public void setCommentID(String commentID) {
        CommentID = commentID;
    }
}
