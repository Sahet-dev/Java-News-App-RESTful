package com.sahet.newsbackend.controller;

import com.sahet.newsbackend.model.Users;
import com.sahet.newsbackend.service.JWTService;
import com.sahet.newsbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final JWTService jwtService;
    private final UserService userService;
    public UserController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;}

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String  login(@RequestBody Users user) {
        return userService.verify(user);
    }

    @GetMapping("/protected")
    public String protectedEndpoint(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract the token from the Authorization header
            String token = authorizationHeader.substring(7); // Remove the "Bearer " prefix

            // Extract username from token
            String username = jwtService.extractUsername(token);

            // Load user details from database
            UserDetails userDetails = userService.loadUserByUsername(username);

            // Validate the token
            if (jwtService.validateToken(token, userDetails)) {
                return "Access granted to protected endpoint!";
            }
        }
        return "Unauthorized access!";
    }

}
