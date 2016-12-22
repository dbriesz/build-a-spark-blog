package com.teamtreehouse.blog.model;

public class Comment {
    private String commenter;
    private String blogComment;

    public Comment(String commenter, String blogComment) {
        this.commenter = commenter;
        this.blogComment = blogComment;
    }

    public String getCommenter() {
        return commenter;
    }

    public String getBlogComment() {
        return blogComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (commenter != null ? !commenter.equals(comment.commenter) : comment.commenter != null) return false;
        return blogComment != null ? blogComment.equals(comment.blogComment) : comment.blogComment == null;
    }

    @Override
    public int hashCode() {
        int result = commenter != null ? commenter.hashCode() : 0;
        result = 31 * result + (blogComment != null ? blogComment.hashCode() : 0);
        return result;
    }
}
