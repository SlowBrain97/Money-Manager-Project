package org.example.backend.Service;

public interface Oauth2UserInfo {

  String getName();
  String getEmail();
  String getProvider();
  String getProviderId();
  String getImageUrl();
}
