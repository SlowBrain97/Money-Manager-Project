package org.example.backend.webConfig;

import org.example.backend.Service.JwtService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class CookieConfig {

  @Bean
  @Qualifier("jwtRefreshTokenCookie")
  public CookieSerializer jwtRefreshTokenCookie(){
    DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
    defaultCookieSerializer.setCookieName("refreshToken");
    defaultCookieSerializer.setCookiePath("/api/v1/auth/refresh");
    defaultCookieSerializer.setUseHttpOnlyCookie(true);
    defaultCookieSerializer.setSameSite("None");
    defaultCookieSerializer.setCookieMaxAge((int)(JwtService.expirationRefreshToken/1000));
    defaultCookieSerializer.setUseSecureCookie(true);

    return defaultCookieSerializer;
  }
}
