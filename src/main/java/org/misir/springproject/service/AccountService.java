package org.misir.springproject.service;

import org.misir.springproject.models.Account;
import org.misir.springproject.repositories.AccountRepository;
import org.misir.springproject.util.constants.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {
    @Value("${my.app.photo-prefix}")
    private String photoPrefix;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if(account.getRole() == null) account.setRole(Roles.USER.getRole());
        if(account.getPhoto() == null) {
            account.setPhoto(photoPrefix + "icons8-user-default-64.png");
        }
        accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByEmailIgnoreCase(email);
        if (optionalAccount.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        Account account = optionalAccount.get();
        List<GrantedAuthority> grantedAuthorities = account.getAuthorities().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getName()))
                .collect(Collectors.toCollection(ArrayList::new));
        grantedAuthorities.add(new SimpleGrantedAuthority(account.getRole()));

        return new User(account.getEmail(), account.getPassword(), grantedAuthorities);
    }

    public Optional<Account> findByEmail(String email){
        return accountRepository.findByEmailIgnoreCase(email);
    }
}