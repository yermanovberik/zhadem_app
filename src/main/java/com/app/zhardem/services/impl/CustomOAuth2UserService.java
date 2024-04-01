package com.app.zhardem.services.impl;

import com.app.zhardem.enums.Role;
import com.app.zhardem.models.User;
import com.app.zhardem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createUser(email, oAuth2User.getAttributes()));

        // Свои действия после успешной аутентификации/регистрации пользователя
        log.info("Authenticated user: {}", email);

        // Возвращаем пользовательские данные с ролями в виде authorities
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                oAuth2User.getAttributes(),
                "email");
    }

    private User createUser(String email, Map<String, Object> attributes) {
        User user = new User();
        user.setEmail(email);
        user.setFullName((String) attributes.getOrDefault("name", "Unknown"));
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // Случайный пароль, поскольку аутентификация через OAuth2
        return userRepository.save(user);
    }



}