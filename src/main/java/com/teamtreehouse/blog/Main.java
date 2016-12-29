package com.teamtreehouse.blog;

import com.teamtreehouse.blog.dao.BlogDao;
import com.teamtreehouse.blog.dao.BlogDaoImpl;
import com.teamtreehouse.blog.model.BlogEntry;
import com.teamtreehouse.blog.model.Comment;
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

        dao.addEntry(new BlogEntry("The best day I’ve ever had", "Clark Kent", "example text 1"));
        dao.addEntry(new BlogEntry("The absolute worst day I’ve ever had", "Wally West", "example text 2"));
        dao.addEntry(new BlogEntry("That time at the mall", "Diana Prince", "example text 3"));

        for (int i = 1; i <= 3; i++) {
            BlogEntry blogEntryToAdd = new BlogEntry(
                    "title " + i,
                    "creator " + i,
                    "blogPost " + i
            );
            blogEntryToAdd.addComment(
                    new Comment(
                    "commenter " + i,
                     "blogComment " + i
                    )
            );
            dao.addEntry(blogEntryToAdd);
        }

        before((req, res) -> {
            // Check to see if a cookie is present and assigns the value to req.attribute for re-use.
            if (req.cookie("username") != null) {
                req.attribute("username", req.cookie("username"));
            }
        });

        before("/edit", (req, res) -> {
            // Prompts for a username if one isn't present.
            if (req.attribute("username") == null) {
                setFlashMessage(req, "Please sign in first");
                res.redirect("/password");
                halt();
            }
            // Restricts ability to edit posts until a username of "admin" is entered.
            if (!req.attribute("username").equals("admin")) {
                setFlashMessage(req, "Incorrect user name. Please try again.");
                res.redirect("/password");
                halt();
            }
        });

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            // Finds all exiting entries and adds them to the model, then displays them on the home page.
            model.put("entries", dao.findAllEntries());
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        get("/new", (req, res) -> {
            Map<String, String> model = new HashMap<>();

            // Displays page with form for adding a new blog entry.
            return new ModelAndView(model, "new.hbs");
        }, new HandlebarsTemplateEngine());

        post("/new", (req, res) -> {
            String title = req.queryParams("title");
            String creator = req.queryParams("creator");
            String blogPost = req.queryParams("blogPost");

            // Creates a new blog entry with a title, creator and body text via the addEntry method and redirects to home page.
            BlogEntry blogEntry = new BlogEntry(title, creator, blogPost);
            dao.addEntry(blogEntry);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        get("/edit/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            // Finds a specific blog entry and displays it on the edit page.
            BlogEntry blogEntry = dao.findEntryBySlug(req.params("slug"));
            model.put("blogEntry", blogEntry);
            return new ModelAndView(model, "edit.hbs");
        }, new HandlebarsTemplateEngine());

        post("/detail/:slug/edit", (req, res) -> {

            // Finds a specific blog entry, then replaces each parameter with the new values below.
            BlogEntry blogEntry = dao.findEntryBySlug(req.params("slug"));
            String title = req.queryParams("title");
            String creator = req.queryParams("creator");
            String blogPost = req.queryParams("blogPost");
            blogEntry.editEntry(title, creator, blogPost);
            res.redirect("/");
            return null;
        });

        get("/password", (req, res) -> {
            Map<String, String> model = new HashMap<>();

            // Displays the password page.
            return new ModelAndView(model, "password.hbs");
        }, new HandlebarsTemplateEngine());

        post("/password", (req, res) -> {
            Map<String, String> model = new HashMap<>();

            // Stores the response cookie into username, then redirects to home.
            String username = req.queryParams("username");
            res.cookie("username", username);
            model.put("username", username);
            res.redirect("/edit");
            return null;
        }, new HandlebarsTemplateEngine());

        get("/detail/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            // Finds a specific blog entry, then displays it on the detail page.
            model.put("blogEntry", dao.findEntryBySlug(req.params("slug")));
            return new ModelAndView(model, "detail.hbs");
        }, new HandlebarsTemplateEngine());

        post("/detail/:slug/comment", (req, res) -> {

            // Finds a specific blog entry, passes in a new comment, then redirects to home.
            BlogEntry blogEntry = dao.findEntryBySlug(req.params("slug"));
            String author = req.queryParams("author");
            String body = req.queryParams("body");
            blogEntry.addComment(new Comment(author, body));
            res.redirect("/");
            return null;
        });

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
