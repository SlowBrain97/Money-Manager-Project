package org.example.backend.features.oauth2;

public interface iOauth2UserInfo {
  String getName();
  String getEmail();
  String getProvider();
  String getProviderId();
  String getImageUrl();
}
