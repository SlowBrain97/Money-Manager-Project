package org.example.backend.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.Entity.User;
import org.example.backend.Service.CookieService;
import org.example.backend.Service.JwtService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {
  private final JwtService jwtService;
  private final RedisTemplate<String,String> redisTemplate;
  private final CookieService cookieService;
  private final ObjectMapper objectMapper;
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    String email = oAuth2User.getAttribute("email");


    String jti = UUID.randomUUID().toString();
    String refreshToken = jwtService.generateRefreshToken(email,jti,new HashMap<>());
    String accessToken = jwtService.generateJwt(email,new HashMap<>());

    try{
      redisTemplate.opsForValue().set("refresh"+jti,"1",JwtService.expirationRefreshToken, TimeUnit.MILLISECONDS);

    }catch (RedisException ex){
      log.warn("Redis unavailable," + ex.getMessage());
    }
    cookieService.setRefreshTokenCookie(request,response,refreshToken);


    String userInfo = "{\"email\": \"" + email + "\"}";

    // Gửi accessToken và userInfo qua postMessage
    response.setContentType("text/html");
    response.getWriter().write(
            "<script>" +
                    "if (window.opener) {" +
                    "  window.opener.postMessage({" +
                    "    accessToken: '" + accessToken + "'," +
                    "    userInfo: " + userInfo +
                    "  }, 'https://localhost:5173/');" +
                    "  window.close();" +
                    "} else {" +
                    "  console.error('No opener window');" +
                    "}" +
                    "</script>");
  }
}
