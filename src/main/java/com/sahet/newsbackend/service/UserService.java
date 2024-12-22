package com.sahet.newsbackend.service;

import com.sahet.newsbackend.model.Users;
import com.sahet.newsbackend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public Users register(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return  userRepo.save(user);
    }

    public String verify(Users user) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        if (auth.isAuthenticated()) {
            return String.valueOf(jwtService.generateToken(user.getUsername()));
        }
        return "Auth Check Failed";
    }
}
