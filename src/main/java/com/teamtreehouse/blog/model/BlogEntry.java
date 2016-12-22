package com.teamtreehouse.blog.model;

public class BlogEntry {
    private String title;
    private String creator;
    private String blogEntry;

    public BlogEntry(String title, String creator, String blogEntry) {
        this.title = title;
        this.creator = creator;
        this.blogEntry = blogEntry;
    }

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }

    public String getBlogEntry() {
        return blogEntry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogEntry blogEntry1 = (BlogEntry) o;

        if (title != null ? !title.equals(blogEntry1.title) : blogEntry1.title != null) return false;
        if (creator != null ? !creator.equals(blogEntry1.creator) : blogEntry1.creator != null) return false;
        return blogEntry != null ? blogEntry.equals(blogEntry1.blogEntry) : blogEntry1.blogEntry == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        result = 31 * result + (blogEntry != null ? blogEntry.hashCode() : 0);
        return result;
    }

    public boolean addComment(Comment comment) {
        // Store these comments!
        return false;
    }
}
