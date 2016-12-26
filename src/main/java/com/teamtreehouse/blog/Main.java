package com.teamtreehouse.blog;

import com.teamtreehouse.blog.dao.BlogDao;
import com.teamtreehouse.blog.dao.BlogDaoImpl;
import com.teamtreehouse.blog.model.BlogEntry;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    private static final String FLASH_MESSAGE_KEY = "flash_message";

    public static void main(String[] args) {
        staticFileLocation("/public");
        BlogDao dao = new BlogDaoImpl();

        before((req, res) -> {
            if (req.cookie("username") != null) {
                req.attribute("username", req.cookie("username"));
            }
        });

        before("/edit", (req, res) -> {
            if (req.attribute("username") == null) {
                setFlashMessage(req, "Please enter a user name first!");
                res.redirect("/password");
                halt();
            }
            if (req.attribute("password") != "admin") {
                setFlashMessage(req, "Incorrect password, please try again.");
                res.redirect("/password");
                halt();
            } else {
                Map<String, Object> model = new HashMap<>();
                String username = req.queryParams("username");
                res.cookie("username", username);
                model.put("username", username);
            }
        });

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("entries", dao.findAllEntries());
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/", (req, res) -> {
            String username = req.queryParams("username");
            res.cookie("username", username);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        get("/edit", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            return new ModelAndView(model, "edit.hbs");
        }, new HandlebarsTemplateEngine());

        post("/edit", (req, res) -> {
            String title = req.queryParams("title");
            String creator = req.queryParams("creator");
            String blogPost = req.queryParams("blogPost");
            BlogEntry blogEntry = new BlogEntry(title, creator, blogPost);
            dao.addEntry(blogEntry);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        get("/password", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            return new ModelAndView(model, "password.hbs");
        }, new HandlebarsTemplateEngine());

        post("/password", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String username = req.queryParams("username");
            model.put("username", req.attribute("username"));
            model.put("flashMessage", captureFlashMessage(req));
            res.redirect("/password");
            String password = req.queryParams("password");
            model.put("password", req.attribute("password"));
            model.put("flashMessage", captureFlashMessage(req));
            res.redirect("/edit");
            return null;
        }, new HandlebarsTemplateEngine());

    }

    private static void setFlashMessage(Request req, String message) {
        req.session().attribute(FLASH_MESSAGE_KEY, message);
    }

    private static String getFlashMessage(Request req) {
        if (req.session(false) == null) {
            return null;
        }
        if (!req.session().attributes().contains(FLASH_MESSAGE_KEY)) {
            return null;
        }
        return (String) req.session().attribute(FLASH_MESSAGE_KEY);
    }

    private static String captureFlashMessage(Request req) {
        String message = getFlashMessage(req);
        if (message != null) {
            req.session().removeAttribute(FLASH_MESSAGE_KEY);
        }
        return message;
    }
}
