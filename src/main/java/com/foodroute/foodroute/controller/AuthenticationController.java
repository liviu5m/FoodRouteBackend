package com.foodroute.foodroute.controller;

import com.foodroute.foodroute.dto.LoginUserDto;
import com.foodroute.foodroute.dto.RegisterUserDto;
import com.foodroute.foodroute.dto.VerifyUserDto;
import com.foodroute.foodroute.model.User;
import com.foodroute.foodroute.repository.CartRepository;
import com.foodroute.foodroute.repository.UserRepository;
import com.foodroute.foodroute.responses.LoginResponse;
import com.foodroute.foodroute.service.AuthenticationService;
import com.foodroute.foodroute.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDto registerUserDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errors = new ArrayList<String>();
                result.getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(errors);
            }
            if(!registerUserDto.getPasswordConfirmation().equals(registerUserDto.getPassword())) return ResponseEntity.badRequest().body("Passwords do not match");

            User registerdUser = authenticationService.signup(registerUserDto);
            return ResponseEntity.ok(registerdUser);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(loginUserDto.getEmail());
            System.out.println(optionalUser.get());
            if(optionalUser.isPresent() && optionalUser.get().getProvider() != null) throw new RuntimeException("You can only authenticate using google provider on this account.");
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);
            LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
            System.out.println(authenticatedUser);
            return ResponseEntity.ok(loginResponse);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        }catch(RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        }catch(RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
