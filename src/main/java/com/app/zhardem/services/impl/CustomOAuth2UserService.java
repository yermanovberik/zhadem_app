package com.app.zhardem.services.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Получить атрибуты пользователя из OAuth2User
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Теперь вы можете извлечь необходимую информацию о пользователе
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");
        // ... другие данные

        // Сохранение или обновление данных пользователя в вашей системе
        // ...

        return oAuth2User; // или возвращать кастомного OAuth2User, если нужно
    }
}