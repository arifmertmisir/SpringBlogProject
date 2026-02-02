package org.misir.springproject.service;

import org.misir.springproject.models.Authority;
import org.misir.springproject.repositories.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;

    public void save(Authority authority){
        authorityRepository.save(authority);
    }

    public Optional<Authority> findById(Long id){
        return authorityRepository.findById(id);
    }

}