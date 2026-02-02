package org.misir.springproject.controller;
import org.misir.springproject.models.Account;
import org.misir.springproject.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
public String submitRegister(@ModelAttribute Account account){
     accountService.save(account);
     return "redirect:/";
}

@GetMapping("/login")
    public String showLoginForm(Model model) {
    return "account_views/login";
}

@GetMapping("/profile")
    public String profile(Model model) {
    return "account_views/profile";
}
}
