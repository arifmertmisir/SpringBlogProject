package org.misir.springproject.controller;
import org.misir.springproject.models.Post;
import org.misir.springproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "home_views/home";
    }
    @GetMapping("/editor")
    public String editor(Model model) {
        return "account_views/editor";
    }
}
