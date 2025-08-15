package com.foodroute.foodroute.security;

import com.foodroute.foodroute.dto.LoginUserDto;
import com.foodroute.foodroute.model.Cart;
import com.foodroute.foodroute.model.User;
import com.foodroute.foodroute.repository.CartRepository;
import com.foodroute.foodroute.repository.UserRepository;
import com.foodroute.foodroute.responses.LoginResponse;
import com.foodroute.foodroute.service.AuthenticationService;
import com.foodroute.foodroute.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final ObjectProvider<AuthenticationManager> authenticationManagerProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CartRepository cartRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attributes = token.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        Optional<User> optionalUser = userRepository.findByEmail(email);
        System.out.println(email + attributes);
        User user;
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
        }else {
            user = new User();
            user.setEmail(email);
            user.setUsername(email);
            user.setPassword(passwordEncoder.encode("google"));
            user.setFullName((String) attributes.get("name"));
            user.setProvider(token.getAuthorizedClientRegistrationId());
            user.setEnabled(true);
            User savedUser = userRepository.save(user);
            Cart cart = new Cart(savedUser);
            cartRepository.save(cart);
        }
        try {
            authenticationManagerProvider.getObject()
                    .authenticate(new UsernamePasswordAuthenticationToken(email, user.getPassword()));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        String jwtToken = jwtService.generateToken(user);
        Cookie jwtCookie = new Cookie("jwt", jwtToken);
        System.out.println();
        jwtCookie.setHttpOnly(false);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(86400);
        response.addCookie(jwtCookie);
        response.sendRedirect("http://localhost:5173/");

    }
}
