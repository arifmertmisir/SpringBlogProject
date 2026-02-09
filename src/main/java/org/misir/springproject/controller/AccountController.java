package org.misir.springproject.controller;
import jakarta.validation.Valid;
import org.misir.springproject.models.Account;
import org.misir.springproject.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class AccountController {

@Autowired
private AccountService accountService;

@GetMapping("/register")
public String showRegisterForm(Model model) {
    model.addAttribute("account", new Account());
    return "account_views/register";
}

@PostMapping("/register")
public String submitRegister(@Valid  @ModelAttribute Account account, BindingResult result){
    if(result.hasErrors()){
        return "account_views/register";
    }
     accountService.save(account);
     return "redirect:/";
}

@GetMapping("/login")
    public String showLoginForm(Model model) {
    return "account_views/login";
}

@GetMapping("/profile")
@PreAuthorize("isAuthenticated()")
public String profile(Model model, Principal principal) {
    if(principal == null) {
        return "redirect:/login";
    }

    String authUsername = principal.getName();
    Optional<Account> optionalAccount = accountService.findByEmail(authUsername);

    if(optionalAccount.isEmpty()) {
        return "redirect:/?error";
    }

    Account account = optionalAccount.get();
    model.addAttribute("account", account);
    model.addAttribute("photo", account.getPhoto());
    return "account_views/profile";
}

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(@Valid @ModelAttribute Account account, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "account_views/profile";
        }
        if(principal == null) {
            return "redirect:/login";
        }
        String authUsername = principal.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(authUsername);
        if(optionalAccount.isEmpty()) {
            return "redirect:/?error";
        }

        Account existingAccount = accountService.findById(account.getId())
                        .orElseThrow(() -> new RuntimeException("Account with id " + account.getId() + " does not exist"));
        existingAccount.setEmail(account.getEmail());
        existingAccount.setPassword(account.getPassword());
        existingAccount.setFirstName(account.getFirstName());
        existingAccount.setLastName(account.getLastName());
        existingAccount.setAge(account.getAge());
        existingAccount.setBirthDate(account.getBirthDate());
        existingAccount.setGender(account.getGender());
        accountService.save(optionalAccount.get());
        return "redirect:/";
    }
}
