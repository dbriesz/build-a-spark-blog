package com.teamtreehouse.blog.model;

import java.util.Date;

public class Comment {
    private String author;
    private String body;
    private Date date;

    public Comment(String author, String body) {
        this.author = author;
        this.body = body;
        this.date = new Date();
    }

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (author != null ? !author.equals(comment.author) : comment.author != null) return false;
        return body != null ? body.equals(comment.body) : comment.body == null;
    }

    @Override
    public int hashCode() {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }
}
