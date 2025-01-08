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



//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Users user = repo.findByUsername(username);
//
//        if (user == null) {
//            throw new UsernameNotFoundException(username);
//        }
//        return new UserPrincipal(user);
//    }

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        System.out.println("Attempting to load user by: " + input);

        Users user = input.contains("@")
                ? repo.findByEmail(input)
                : repo.findByUsername(input);

        if (user == null) {
            System.out.println("User not found for: " + input);
            throw new UsernameNotFoundException("User not found: " + input);
        }

        System.out.println("User found: " + user);
        return new UserPrincipal(user);
    }
}
