package com.app.zhardem.services.impl;

import com.app.zhardem.enums.Role;
import com.app.zhardem.models.User;
import com.app.zhardem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createUser(email, attributes));

        log.info("Loaded OAuth2 user: {}", email);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email"
        );
    }

    public User createUser(String email, Map<String, Object> attributes) {
        String fullName = (String) attributes.getOrDefault("name", "Unknown");

        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        return userRepository.save(user);
    }
}