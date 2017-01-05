package com.teamtreehouse.blog.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
    private String author;
    private String body;
    private String date;

    public Comment(String author, String body) {
        this.author = author;
        this.body = body;
        this.date = getDate();
    }

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy h:mm aa");
        String dateToStr= format.format(date);
        return dateToStr;
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
