package org.misir.springproject.controller;
import jakarta.validation.Valid;
import org.misir.springproject.models.Account;
import org.misir.springproject.models.Post;
import org.misir.springproject.service.AccountService;
import org.misir.springproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model, Principal principal) {
        Optional<Post> post = postService.findById(id);
        String authUsername = "email";
        if (post.isEmpty()) {
            return "404";
        }

        model.addAttribute("post" , post.get());
        if (principal != null) {
            authUsername = principal.getName();
        }
        if (authUsername.equals(post.get().getAccount().getEmail())) {
            model.addAttribute("isOwner", true);
        } else {
            model.addAttribute("isOwner", false);
        }
        return "post_views/post";
    }

    @GetMapping("post/add")
    @PreAuthorize("isAuthenticated()")
    public String addPost(Model model, Principal principal) {
        String authUsername = "email";
        if (principal != null) {
            authUsername = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findByEmail(authUsername);
        if (optionalAccount.isPresent()) {
            Post post = new Post();
            post.setAccount(optionalAccount.get());
            model.addAttribute("post" , post);
            return "post_views/post_add";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("post/add")
    @PreAuthorize("isAuthenticated()")
    public String addPost(@Valid @ModelAttribute Post post, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "post_views/post_add";
        }

        String authUsername = "email";
        if (principal != null) {
            authUsername = principal.getName();
        }
        if (!post.getAccount().getEmail().equals(authUsername)) {
            return "redirect:/?error";
        }

        postService.save(post);
        return "redirect:/post/" + post.getId();
    }

    @GetMapping("post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getPostForEdit(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.findById(id);
        if(optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post" , post);
            return "post_views/post_edit";
        } else  {
            return "404";
        }
    }

    @PostMapping("post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable Long id, @Valid @ModelAttribute Post post, BindingResult bindingResult ,Principal principal) {
        if (bindingResult.hasErrors()) {
            return "post_views/post_edit";
        }

        Optional<Post> optionalPost = postService.findById(id);

        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            if (!existingPost.getAccount().getEmail().equals(principal.getName())) {
                return "redirect:/?error";
            }
            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());
            postService.save(existingPost);
            return "redirect:/post/" + existingPost.getId();
        }
        return "404";
    }

    @GetMapping("post/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String getPostForDelete(@PathVariable Long id, Model model, Principal principal) {
        Optional<Post> optionalPost = postService.findById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            if (!existingPost.getAccount().getEmail().equals(principal.getName())) {
                return "redirect:/?error";
            }
            model.addAttribute("post" , existingPost);
            return "post_views/post_delete";
        }
        return "404";
    }

    @PostMapping("post/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable Long id, Principal principal) {
        Optional<Post> optionalPost = postService.findById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            if (!existingPost.getAccount().getEmail().equals(principal.getName())) {
                return "redirect:/?error";
            }
            postService.delete(existingPost);
            return "redirect:/";
        }
        return "404";
    }
}
