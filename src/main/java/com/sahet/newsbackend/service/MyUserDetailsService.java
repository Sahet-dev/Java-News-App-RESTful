package com.sahet.newsbackend.service;

import com.sahet.newsbackend.model.UserPrincipal;
import com.sahet.newsbackend.model.Users;
import com.sahet.newsbackend.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {


    private final UserRepo repo;
    public MyUserDetailsService(UserRepo repo) {
        this.repo = repo;
    }


    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {


        Users user = input.contains("@")
                ? repo.findByEmail(input)
                : repo.findByUsername(input);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + input);
        }

        return new UserPrincipal(user);
    }
}
