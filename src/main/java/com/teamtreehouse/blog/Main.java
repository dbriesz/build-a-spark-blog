package com.teamtreehouse.blog;

import com.teamtreehouse.blog.dao.BlogDao;
import com.teamtreehouse.blog.dao.BlogDaoImpl;
import com.teamtreehouse.blog.dao.NotFoundException;
import com.teamtreehouse.blog.model.BlogEntry;
import com.teamtreehouse.blog.model.Comment;
import com.teamtreehouse.blog.model.Tag;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.*;

import static spark.Spark.*;

public class Main {
    private static final String FLASH_MESSAGE_KEY = "flash_message";

    public static void main(String[] args) {
        staticFileLocation("/public");
        BlogDao dao = new BlogDaoImpl();

        // Adds 3 example blog entries with comments.
        BlogEntry entry1 = new BlogEntry("The best day I’ve ever had", "Clark Kent",
                "Lois, Jon and I went to the country fair today. Jon rode a roller coaster for the first time... ");
        dao.addEntry(entry1);
        entry1.addComment(new Comment("Lois Lane", "Best. Day. Ever."));
        entry1.addTag(new Tag("county fair"));

        BlogEntry entry2 = new BlogEntry("The absolute worst day I’ve ever had", "Wally West",
                "I'm back in Keystone City through some miracle, yet Linda doesn't remember me at all...");
        dao.addEntry(entry2);
        entry2.addComment(new Comment("Abra Kadabra", "I have finally defeated you!"));
        entry2.addTag(new Tag("bad day"));

        BlogEntry entry3 = new BlogEntry("That time at the mall", "Diana Prince",
                "Went to the mall today for the first time and felt very overwhelmed...");
        dao.addEntry(entry3);
        entry3.addComment(new Comment("Steve Trevor", "<3"));
        entry3.addTag(new Tag("shopping"));

        before((req, res) -> {
            // Check to see if a cookie is present and assigns the value to req.attribute for re-use.
            if (req.cookie("username") != null) {
                req.attribute("username", req.cookie("username"));
            }
        });

        before("/admin/*", (req, res) -> {
            res.cookie("/","redirectTo", req.uri(), -1, false, false);

            // Prompts for a username if one isn't present, sets flash message and redirects to password page.
            if (req.attribute("username") == null) {
                setFlashMessage(req, "Please sign in first.");
                res.redirect("/password");
                halt();
            }

            // Restricts ability to add a new entry until a username of "admin" is entered, sets flash message and redirects to password page.
            if (!req.attribute("username").equals("admin")) {
                setFlashMessage(req, "Incorrect user name.  Please try again.");
                res.redirect("/password");
                halt();
            }
        });

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            // Finds all exiting entries and adds them to the model, then displays them on the home page.
            // Captures flash message and displays model on index page.
            model.put("entries", dao.findAllEntries());
            model.put("flashMessage", captureFlashMessage(req));
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        get("/admin/new", (req, res) -> {
            Map<String, String> model = new HashMap<>();

            // Displays page with form for adding a new blog entry.
            return new ModelAndView(model, "admin/new.hbs");
        }, new HandlebarsTemplateEngine());

        post("/admin/new", (req, res) -> {
            String title = req.queryParams("title");
            String creator = req.queryParams("creator");
            String blogPost = req.queryParams("blogPost");

            // Creates a new blog entry with a title, creator and body text via the addEntry method and redirects to home page.
            BlogEntry blogEntry = new BlogEntry(title, creator, blogPost);
            dao.addEntry(blogEntry);
            setFlashMessage(req, "New entry published!");
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        get("/admin/edit/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            // Finds a specific blog entry and displays it on the edit page.
            BlogEntry blogEntry = dao.findEntryBySlug(req.params("slug"));
            model.put("blogEntry", blogEntry);
            return new ModelAndView(model, "admin/edit.hbs");
        }, new HandlebarsTemplateEngine());

        post("/detail/:slug/admin/edit", (req, res) -> {

            // Finds a specific blog entry, then replaces each parameter with the new values below.
            BlogEntry blogEntry = dao.findEntryBySlug(req.params("slug"));
            String title = req.queryParams("title");
            String creator = req.queryParams("creator");
            String blogPost = req.queryParams("blogPost");
            blogEntry.editEntry(title, creator, blogPost);
            setFlashMessage(req, "Entry updated!");
            res.redirect("/");
            return null;
        });

        get("/password", (req, res) -> {
            Map<String, String> model = new HashMap<>();

            // Displays password prompt page.
            model.put("flashMessage", captureFlashMessage(req));
            return new ModelAndView(model, "password.hbs");
        }, new HandlebarsTemplateEngine());

        post("/password", (req, res) -> {
            Map<String, String> model = new HashMap<>();

            // Stores the response cookie into username, then redirects to home.
            String username = req.queryParams("username");
            res.cookie("/", "username", username,-1, false, false );
            model.put("username", username);
            String redirectTo = req.cookie("redirectTo");

            if (redirectTo == null) {
                res.redirect("/");
            }

            res.redirect(redirectTo);

            return null;
        }, new HandlebarsTemplateEngine());

        get("/detail/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            // Finds a specific blog entry, then displays it on the detail page.
            model.put("blogEntry", dao.findEntryBySlug(req.params("slug")));
            model.put("flashMessage", captureFlashMessage(req));
            return new ModelAndView(model, "detail.hbs");
        }, new HandlebarsTemplateEngine());

        post("/detail/:slug/comment", (req, res) -> {

            // Finds a specific blog entry, passes in a new comment, then redirects to the detail page.
            BlogEntry blogEntry = dao.findEntryBySlug(req.params("slug"));
            String author = req.queryParams("author");
            if (author.equals("")) {
                author = "Anonymous";
            }

            String body = req.queryParams("body");
            boolean commentAdded = blogEntry.addComment(new Comment(author, body));
            if (commentAdded) {
                setFlashMessage(req, "Thanks for your comment!");
            }
            res.redirect("/detail/" + blogEntry.getSlug());
            return null;
        });

        post("/admin/edit/:slug/tag", (req, res) -> {

            // Finds a specific blog entry, adds tags via a comma separated List.
            // Flash message set after a tag is added.  Redirects to home page.
            BlogEntry blogEntry = dao.findEntryBySlug(req.params("slug"));
            String tags = req.queryParams("tag");
            List<String> tagList = Arrays.asList(tags.split(","));
            boolean tagAdded = false;
            for (String tag : tagList) {
                tagAdded = blogEntry.addTag(new Tag(tag));
            }

            if (tagAdded) {
                setFlashMessage(req, "Tag added!");
            }
            res.redirect("/");
            return null;
        });

        post("/admin/edit/:slug/delete", (req, res) -> {

            // Finds a specific blog entry and deletes that entry.  Flash message set after entry deleted.
            BlogEntry blogEntry = dao.findEntryBySlug(req.params("slug"));
            dao.deleteEntry((blogEntry));
            setFlashMessage(req, "Entry deleted!");
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
