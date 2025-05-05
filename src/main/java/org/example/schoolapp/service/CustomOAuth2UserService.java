package org.example.schoolapp.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());

        if ("github".equals(registrationId)) {
            String email = (String) attributes.get("email");

            if (email == null) {
                String login = (String) attributes.get("login");
                attributes.put("email", login + "@github.local");
                attributes.put("fallback_username", login);
            }
        }

        if ("facebook".equals(registrationId)) {
            String email = (String) attributes.get("email");
            if (email == null) {
                String id = (String) attributes.get("id");
                attributes.put("email", id + "@facebook.local");
                attributes.put("fallback_name", attributes.get("name"));
            }
        }

        return new DefaultOAuth2User(
                oauth2User.getAuthorities(),
                attributes,
                "email"
        );
    }
}
