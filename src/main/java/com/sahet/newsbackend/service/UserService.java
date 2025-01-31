package com.sahet.newsbackend.service;

import com.sahet.newsbackend.model.Users;
import com.sahet.newsbackend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    // Constructor injection
    @Autowired
    public UserService(UserRepo userRepo, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<?> register(Users user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (user.getRole() != null && !user.getRole().equals("USER")) {
                throw new IllegalArgumentException("Cannot assign role: " + user.getRole());
            }
            user.setRole("USER");

            userRepo.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
        } catch (DataIntegrityViolationException e) {
            // Handle duplicate username error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username already exists.");
        }
    }



//    public String verify(Users user) {
//        Authentication auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
//        );
//
//        if (auth.isAuthenticated()) {
//            return String.valueOf(jwtService.generateToken(user.getUsername()));
//        }
//        return "Auth Check Failed";
//    }

    public String verify(Users user) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        if (auth.isAuthenticated()) {
            Users dbUser = userRepo.findByUsername(user.getUsername());
            String token = jwtService.generateToken(user.getUsername());
            if ("ADMIN".equalsIgnoreCase(dbUser.getRole())) {
                return "{\"token\":\"" + token + "\", \"role\":\"ADMIN\"}";
            }
            return "{\"token\":\"" + token + "\", \"role\":\"USER\"}";
        }
        return "Auth Check Failed";
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .build();
    }


}
