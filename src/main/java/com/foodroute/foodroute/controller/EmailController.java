package com.foodroute.foodroute.controller;

import com.foodroute.foodroute.dto.EmailDto;
import com.foodroute.foodroute.model.User;
import com.foodroute.foodroute.repository.UserRepository;
import com.foodroute.foodroute.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<?> sendEmail(@RequestBody EmailDto emailDto) {
        try {
            emailService.sendFeedbackEmail(emailDto.getUserId(), emailDto.getSubject(), emailDto.getComment());
            return ResponseEntity.ok("Email sent");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
