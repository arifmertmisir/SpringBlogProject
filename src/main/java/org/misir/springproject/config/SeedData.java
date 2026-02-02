package org.misir.springproject.config;

import org.misir.springproject.models.Account;
import org.misir.springproject.models.Authority;
import org.misir.springproject.models.Post;
import org.misir.springproject.service.AccountService;
import org.misir.springproject.service.AuthorityService;
import org.misir.springproject.service.PostService;
import org.misir.springproject.util.constants.Privillages;
import org.misir.springproject.util.constants.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SeedData implements CommandLineRunner {
    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public void run(String... args) {
        for(Privillages auth : Privillages.values()) {
            Authority authority = new Authority();
            authority.setId(auth.getId());
            authority.setName(auth.getprivillage());
            authorityService.save(authority);
        }

        Account account = new Account();
        account.setEmail("amisir@testmail.com");
        account.setPassword("test123");
        account.setFirstName("Mert");
        account.setLastName("Misir");
        account.setRole(Roles.ADMIN.getRole());

        Account account2 = new Account();
        account2.setEmail("max.mustermann@testmail.com");
        account2.setPassword("test321");
        account2.setFirstName("Max");
        account2.setLastName("Mustermann");
        account2.setRole(Roles.USER.getRole());

        Account account3 = new Account();
        account3.setEmail("editor@editor.com");
        account3.setPassword("pass123");
        account3.setFirstName("Editor");
        account3.setLastName("Editormann");
        account3.setRole(Roles.EDITOR.getRole());

        Account account4 = new Account();
        account4.setEmail("super_editor@editor.com");
        account4.setPassword("pass123");
        account4.setFirstName("Super Editor");
        account4.setLastName("Super Editormann");
        account4.setRole(Roles.EDITOR.getRole());
        Set<Authority> authorities = new HashSet<>();
        authorityService.findById(Privillages.ACCESS_ADMIN_PANEL.getId()).ifPresent(authorities::add);
        authorityService.findById(Privillages.RESET_ANY_USER_PASSWORD.getId()).ifPresent(authorities::add);
        account4.setAuthorities(authorities);
        System.out.println(account4.getAuthorities());

        accountService.save(account);
        accountService.save(account2);
        accountService.save(account3);
        accountService.save(account4);

        List<Post> posts = postService.findAll();
        if (posts.isEmpty()) {
            Post post = new Post();
            post.setTitle("First Post");
            post.setBody("This is the first Post body.....");
            post.setAccount(account);
            postService.save(post);

            Post post2 = new Post();
            post2.setTitle("Second Post");
            post2.setBody("This is the second Post body.....");
            post2.setAccount(account2);
            postService.save(post2);
        }
    }
}
