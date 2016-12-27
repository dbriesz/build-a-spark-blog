package com.teamtreehouse.blog.model;

import com.github.slugify.Slugify;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BlogEntry {
    private String slug;
    private String title;
    private String creator;
    private String blogPost;
    private Date date;
    private Set<String> commenters;

    public BlogEntry(String title, String creator, String blogPost) {
        commenters = new HashSet<>();
        this.title = title;
        this.date = new Date();
        this.creator = creator;
        this.blogPost = blogPost;
        Slugify slugify = new Slugify();
        slug = slugify.slugify(title);
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getCreator() {
        return creator;
    }

    public String getBlogPost() {
        return blogPost;
    }

    public String getSlug() {
        return slug;
    }

    public boolean addCommenter(String commenterUserName) {
        return commenters.add(commenterUserName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogEntry blogEntry1 = (BlogEntry) o;

        if (title != null ? !title.equals(blogEntry1.title) : blogEntry1.title != null) return false;
        if (creator != null ? !creator.equals(blogEntry1.creator) : blogEntry1.creator != null) return false;
        return blogPost != null ? blogPost.equals(blogEntry1.blogPost) : blogEntry1.blogPost == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        result = 31 * result + (blogPost != null ? blogPost.hashCode() : 0);
        return result;
    }

    public boolean addComment(Comment comment) {
        // Store these comments!
        return false;
    }
}
