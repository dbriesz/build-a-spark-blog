package com.teamtreehouse.blog;

import com.teamtreehouse.blog.dao.BlogDAO;
import com.teamtreehouse.blog.dao.BlogDAOImpl;
import com.teamtreehouse.blog.model.BlogEntry;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

public class Main {
    public static void main(String[] args) {
        staticFileLocation("/public");
        BlogDAO dao = new BlogDAOImpl();

        get ("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("entries", dao.findAllEntries());
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post ("/new", (req, res) -> {
            String title = req.queryParams("title");
            String creator = req.queryParams("creator");
            String blogPost = req.queryParams("blogPost");
            BlogEntry blogEntry = new BlogEntry(title, creator, blogPost);
            dao.addEntry(blogEntry);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        /*get("/new", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            model.put("username", req.cookie("username"));
            return new ModelAndView(model, "new.hbs");
        }, new HandlebarsTemplateEngine());

        get("/edit", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            model.put("username", req.cookie("username"));
            return new ModelAndView(model, "edit.hbs");
        }, new HandlebarsTemplateEngine());

        post("/password", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            String username = req.queryParams("username");
            res.cookie("username", username);
            model.put("username", username);
            return new ModelAndView(model, "password.hbs");
        }, new HandlebarsTemplateEngine());*/

    }
}
