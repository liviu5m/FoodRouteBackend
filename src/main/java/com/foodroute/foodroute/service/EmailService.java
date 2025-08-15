package com.foodroute.foodroute.service;

import com.foodroute.foodroute.model.User;
import com.foodroute.foodroute.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;
    private UserRepository userRepository;

    @Value("${email.admin}")
    private String adminEmail;

    public EmailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void sendVerificationEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        emailSender.send(message);
    }

    public void sendEmail(User user, String subject, String text) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(new InternetAddress(user.getEmail(), user.getFullName()));

        helper.setTo(adminEmail);
        helper.setSubject(subject);
        helper.setText(text, true);

        emailSender.send(message);
    }


    public void sendFeedbackEmail(Long userId, String subject, String comment) {
        User user = userRepository.findById(userId).get();
        String htmlMessage = "<html>"
                + "<head>"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"
                + "<title>New User Feedback</title>"
                + "</head>"
                + "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f5f5f5;\">"
                + "<div style=\"max-width: 600px; margin: 20px auto; padding: 20px;\">"
                + "<div style=\"background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); overflow: hidden;\">"
                + "<div style=\"background-color: #4a6baf; padding: 20px; color: white;\">"
                + "<h1 style=\"margin: 0; font-size: 22px;\">New User Feedback Received</h1>"
                + "</div>"
                + "<div style=\"padding: 25px;\">"
                + "<h2 style=\"color: #333; margin-top: 0; font-size: 18px;\">Subject: " + subject + "</h2>"
                + "<div style=\"margin: 15px 0; padding: 15px; background-color: #f9f9f9; border-left: 4px solid #4a6baf;\">"
                + "<p style=\"margin: 0; color: #555; line-height: 1.6;\">" + comment + "</p>"
                + "</div>"
                + "<p style=\"font-size: 14px; color: #777; margin-top: 20px;\">Sent by: " + user.getFullName() + " (" + user.getEmail() + ")</p>"
                + "</div>"
                + "<div style=\"background-color: #f5f5f5; padding: 15px; text-align: center; font-size: 12px; color: #999;\">"
                + "<p style=\"margin: 0;\">Please respond to this feedback within 48 hours</p>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            System.out.println(user.getEmail());
            sendEmail(user, subject, htmlMessage);
        }catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
