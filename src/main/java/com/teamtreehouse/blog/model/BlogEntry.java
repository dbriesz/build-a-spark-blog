package com.teamtreehouse.blog.model;

import com.github.slugify.Slugify;

import java.util.*;

public class BlogEntry {
    private String slug;
    private String title;
    private String creator;
    private String blogPost;
    private Date date;
    private List<Comment> comments;
    private Set<Tag> tags;

    public BlogEntry(String title, String creator, String blogPost) {
        comments = new ArrayList<>();
        tags = new HashSet<>();

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

    public void editEntry(String title, String creator, String blogPost) {
        date = new Date();
        this.title = title;
        this.creator = creator;
        this.blogPost = blogPost;
    }

    public ArrayList getComments() {
        return new ArrayList<>(comments);
    }

    public Set getTags() {
        return new HashSet<>(tags);
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
        date = new Date();
        return comments.add(comment);
    }

    public boolean addTag(Tag tag) {
        return tags.add(tag);
    }

}
