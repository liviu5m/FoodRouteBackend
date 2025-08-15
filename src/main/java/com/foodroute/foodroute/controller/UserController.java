package com.foodroute.foodroute.controller;

import com.foodroute.foodroute.dto.UpdateUserDto;
import com.foodroute.foodroute.model.User;
import com.foodroute.foodroute.repository.UserRepository;
import com.foodroute.foodroute.service.AuthenticationService;
import com.foodroute.foodroute.service.UserService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService, UserRepository userRepository, AuthenticationService authenticationService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.getPrincipal() instanceof DefaultOidcUser) {
                // Handle OIDC/OAuth2 user
                DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
                String email = oidcUser.getEmail(); // Standard claim
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                return ResponseEntity.ok(user);

            } else if (authentication.getPrincipal() instanceof User) {
                // Handle regular form login user
                User currentUser = (User) authentication.getPrincipal();
                User user = userRepository.findByUsername(currentUser.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                return ResponseEntity.ok(user);

            } else {
                return ResponseEntity.badRequest().body("Unsupported authentication type");
            }
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();
        System.out.println(users);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDto updateUserDto, BindingResult result) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if(optionalUser.isPresent()) {
                User user = optionalUser.get();
                if(updateUserDto.getType().equals("data")) {
                    Optional<User> usernameUser = userRepository.findByUsername(updateUserDto.getUsername());
                    if(usernameUser.isPresent() && !updateUserDto.getCurrentUsername().equals(user.getUsername())) throw new RuntimeException("Username already exists");
                    System.out.println(user);
                    user.setFullName(updateUserDto.getFullName());
                    user.setUsername(updateUserDto.getUsername());
                    user.setAddress(updateUserDto.getAddress());
                    user.setPhoneNumber(updateUserDto.getPhoneNumber());
                    userRepository.save(user);
                    return ResponseEntity.ok(user);
                }else if(updateUserDto.getType().equals("password")) {
                    if (result.hasErrors()) throw new RuntimeException("Password length must be at least 8 characters");
                    if(!passwordEncoder.matches(updateUserDto.getCurrentPassword(), user.getPassword())) throw new RuntimeException("Invalid current password");
                    if(!updateUserDto.getNewPassword().equals(updateUserDto.getPasswordConfirmation())) throw new RuntimeException("New password and confirmation do not match.");

                    user.setPassword(passwordEncoder.encode(updateUserDto.getNewPassword()));
                    userRepository.save(user);
                    return ResponseEntity.ok(user);
                }
            }else throw new RuntimeException("User not found");
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("");
    }
}
