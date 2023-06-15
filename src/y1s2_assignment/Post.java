/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.time.LocalDateTime;

/**
 *
 * @author Asus
 */
public class Post {

    private LocalDateTime timeStamp;
    private int postID;
    private String accountID;
    private String content;
    private String mediaPath;
    private int likes;
    private int comments;
    private Status status;

    enum Status {
        PUBLIC,
        PRIVATE,
    }

    public Post(PostBuilder builder) {
        this.timeStamp = builder.timeStamp;
        this.postID = builder.postID;
        this.accountID = builder.accountID;
        this.content = builder.content;
        this.mediaPath = builder.mediaPath;
        this.status = builder.status;
        this.likes = builder.likes;
        this.comments = builder.comments;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getStatusAsString() {
        return status.name();
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public static class PostBuilder {

        private LocalDateTime timeStamp;
        private int postID;
        private String accountID;
        private String content;
        private String mediaPath;
        private int likes;
        private int comments;
        private Status status;

        public PostBuilder() {
        }

        public PostBuilder(LocalDateTime timeStamp, int postID, String accountID, String content, Status status) {
            this.timeStamp = timeStamp;
            this.postID = postID;
            this.accountID = accountID;
            this.content = content;
            this.status = status;
        }

        public PostBuilder(LocalDateTime timeStamp, int postID, String accountID, String content, String mediaPath, Status status) {
            this.timeStamp = timeStamp;
            this.postID = postID;
            this.accountID = accountID;
            this.content = content;
            this.mediaPath = mediaPath;
            this.status = status;
        }

        public PostBuilder setTimeStamp(LocalDateTime timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public PostBuilder setpostID(int postID) {
            this.postID = postID;
            return this;
        }

        public PostBuilder setAccountID(String accountID) {
            this.accountID = accountID;
            return this;
        }

        public PostBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public PostBuilder setMediaPath(String mediaPath) {
            this.mediaPath = mediaPath;
            return this;
        }

        public PostBuilder setLikes(int likes) {
            this.likes = likes;
            return this;
        }

        public PostBuilder setComments(int comments) {
            this.comments = comments;
            return this;
        }

        public PostBuilder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public Post build() {
            return new Post(this);
        }
    }
}
