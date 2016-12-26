package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.model.BlogEntry;
import com.teamtreehouse.blog.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class BlogDaoImpl implements BlogDao {
    private List<BlogEntry> entries;
    private List<Comment> comments;

    public BlogDaoImpl() {
        entries = new ArrayList<>();
        comments = new ArrayList<>();
    }

    @Override
    public boolean addEntry(BlogEntry blogEntry) {
        return entries.add(blogEntry);
    }

    @Override
    public boolean addComment(Comment blogComment) {
        return comments.add(blogComment);
    }

    @Override
    public List<BlogEntry> findAllEntries() {
        return new ArrayList<>(entries);
    }

    @Override
    public BlogEntry findEntryBySlug(String slug) {
        return null;
    }
}
