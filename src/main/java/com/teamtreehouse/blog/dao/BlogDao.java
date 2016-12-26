package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.model.BlogEntry;
import com.teamtreehouse.blog.model.Comment;

import java.util.List;

public interface BlogDao {
    boolean addEntry(BlogEntry blogEntry);
    boolean addComment(Comment blogComment);
    List<BlogEntry> findAllEntries();
    BlogEntry findEntryBySlug(String slug);
}
