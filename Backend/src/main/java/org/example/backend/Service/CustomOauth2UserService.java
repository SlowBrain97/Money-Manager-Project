package org.example.backend.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.Entity.Oauth2UserEntity;
import org.example.backend.Entity.User;
import org.example.backend.Repository.Oauth2UserRepositoty;
import org.example.backend.Repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {
  private final UserRepository userRepository;
  private final Oauth2UserRepositoty oauth2UserRepositoty;
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oauth2User = super.loadUser(userRequest);
    String email = oauth2User.getAttribute("email");
    log.info("*User attribute : " + oauth2User.getAttributes());
    userRepository.findByEmail(email).orElseGet(()->{
      User newUser = new User(
              oauth2User.getAttribute("email"),
              randomPassword(),
              oauth2User.getAttribute("family_name"),
              oauth2User.getAttribute("given_name"),
              oauth2User.getAttribute("picture")
      );
      var savedUser = userRepository.save(newUser);
      Oauth2UserEntity oauth2UserEntity = new Oauth2UserEntity("google", oauth2User.getAttribute("sub"), savedUser);
      oauth2UserRepositoty.save(oauth2UserEntity);
      return newUser;
    });
    return oauth2User;
  }

  private String randomPassword(){
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < 12; i++) {
      int randomIndex = (int)(Math.random() * chars.length());
      builder.append(chars.charAt(randomIndex));
    }

    return builder.toString();
  }
}
