package org.example.backend.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.stereotype.Component;
import org.springframework.session.web.http.CookieSerializer.CookieValue;
@Component
public class CookieService {

  private final CookieSerializer refreshTokenCookieSerializer;

  public CookieService(@Qualifier("jwtRefreshTokenCookie")CookieSerializer refreshTokenCookieSerializer) {
    this.refreshTokenCookieSerializer = refreshTokenCookieSerializer;
  }

  public void setRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken){
    refreshTokenCookieSerializer.writeCookieValue(new CookieValue(request,response,refreshToken));
  }

  public void deleteRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response){
    if (refreshTokenCookieSerializer instanceof DefaultCookieSerializer ){
      ((DefaultCookieSerializer) refreshTokenCookieSerializer).setCookieMaxAge(0);
      refreshTokenCookieSerializer.writeCookieValue(new CookieValue(request,response,""));
      ((DefaultCookieSerializer) refreshTokenCookieSerializer).setCookieMaxAge((int)(JwtService.expirationRefreshToken/1000));
    }
  }
}
