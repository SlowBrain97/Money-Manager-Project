package org.example.backend.features.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;


public class GoogleOauth2User implements iOauth2UserInfo{
  private final Map<String,Object> userAttributes;

  public GoogleOauth2User(Map<String, Object> userAttributes) {
    this.userAttributes = userAttributes;
  }

  @Override
  public String getName() {
    return (String)this.userAttributes.get("name");
  }

  @Override
  public String getEmail() {
    return (String)this.userAttributes.get("email");
  }

  @Override
  public String getProvider() {
    return (String)this.userAttributes.get("provider");
  }

  @Override
  public String getProviderId() {
    return (String)this.userAttributes.get("providerId");
  }

  @Override
  public String getImageUrl() {
    return (String)this.userAttributes.get("imageUrl");
  }
}
