package org.misir.springproject.controller;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.misir.springproject.models.Account;
import org.misir.springproject.service.AccountService;
import org.misir.springproject.util.constants.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

@Controller
public class AccountController {

    @Value("${my.app.photo-prefix}")
    private String photoPrefix;

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

    @PostMapping("/update_photo")
    @PreAuthorize("isAuthenticated()")
    public String updatePhoto(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes, Principal principal){
        if(file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No file uploaded!");
            return "redirect:/profile";
        }

        String originalFilename = file.getOriginalFilename();
        String fileName = "default_name.jpg";
        if (StringUtils.hasText(originalFilename)) {
             fileName = StringUtils.cleanPath(originalFilename);
        }

        try {
            String generatedString = RandomStringUtils.insecure().nextAlphanumeric(10);
            String finalFileName = generatedString + '_' + fileName;
            String absoluteFileLocation = AppUtil.getUploadPath(finalFileName);
            Path path = Paths.get(absoluteFileLocation);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            redirectAttributes.addFlashAttribute("message", "Successfully uploaded " + fileName);

            if(principal == null) {
                return "redirect:/login";
            }
            String authUsername = principal.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(authUsername);
            if(optionalAccount.isEmpty()) {
                return "redirect:/?error";
            }

            Account existingAccount = accountService.findById(optionalAccount.get().getId())
                    .orElseThrow(() -> new RuntimeException("Account with id " + optionalAccount.get().getId() + " does not exist"));
            String relativeFileLocation = photoPrefix.replace("images/", "uploads/" + finalFileName);
            existingAccount.setPhoto(relativeFileLocation);
            accountService.save(existingAccount);
            return "redirect:/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
            return "redirect:/profile?error";
        }
    }
}
