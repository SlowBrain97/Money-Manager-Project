package org.example.backend.Service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.Dto.Auth.Request.LoginRequest;
import org.example.backend.Dto.Auth.Response.LoginResponse;
import org.example.backend.Dto.User.UserDTO;
import org.example.backend.Entity.CustomUserDetails;
import org.example.backend.Exception.CommonException;
import org.example.backend.Exception.CustomJwtException;
import org.example.backend.Mapper.UserMapper;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final UserDetailServiceImpl userDetailService;
  private final JwtService jwtService;
  private final UserMapper userMapper;
  private final RedisTemplate<String, String> redisTemplate;
  private final CookieService cookieService;



  public LoginResponse loginResolve(LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {

    UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

    authenticationManager.authenticate(authenticationToken);
    CustomUserDetails user = (CustomUserDetails) userDetailService.loadUserByUsername(loginRequest.getEmail());
    UserDTO userDTO = userMapper.mapToDto(user.getUser());


    String accessToken = jwtService.generateJwt(loginRequest.getEmail(), new HashMap<>());
    String refreshJti = UUID.randomUUID().toString();
    String refreshToken = jwtService.generateRefreshToken(loginRequest.getEmail(), refreshJti, new HashMap<>());

    cookieService.setRefreshTokenCookie(request,response,refreshToken);
    try {
      redisTemplate.opsForValue().set("refresh" + refreshJti, user.getUsername(), 30, TimeUnit.DAYS);
    } catch (RedisConnectionFailureException ex) {
      log.warn("Redis unvailable" + ex.getMessage());
    }
    return LoginResponse.builder().accessToken(accessToken).user(userDTO).build();
  }

  public String refreshToken(HttpServletResponse response, HttpServletRequest request) {

    Cookie[] cookies = request.getCookies();
    String accessToken = request.getHeader("Authorization").substring(7);
    String refreshToken = Arrays.stream(cookies).
            filter(cookie -> cookie.getName().equals("refreshToken")).findFirst()
            .map(Cookie::getValue).orElse(null);

    if (refreshToken == null || !jwtService.isTokenValid(refreshToken)) {
      throw new CustomJwtException("Invalid Refresh Token or Cookie has no Token");
    }
    Claims claimsRefreshToken = jwtService.extractAllToken(refreshToken);
    Claims claimsAccessToken = jwtService.extractAllToken(accessToken);

    long ttl = claimsAccessToken.getExpiration().getTime() - System.currentTimeMillis();

    String accessTokenJti = claimsAccessToken.getId();
    String refreshTokenJti = claimsRefreshToken.getId();
    String username = claimsRefreshToken.getSubject();

    String newAccessToken = jwtService.generateJwt(username, new HashMap<>());
    String newJti = UUID.randomUUID().toString();
    String newRefreshToken = jwtService.generateRefreshToken(username, newJti, new HashMap<>());

    try {
      redisTemplate.delete("refresh" + refreshTokenJti);
      redisTemplate.opsForValue().set("blackList" + accessTokenJti, "1", ttl);
      redisTemplate.opsForValue()
              .set("refresh" + newJti, username, JwtService.expirationRefreshToken, TimeUnit.MILLISECONDS);
    } catch (RedisConnectionFailureException ex) {
      log.warn("Redis unvailable" + ex.getMessage());
    }


    cookieService.setRefreshTokenCookie(request,response,newRefreshToken);

    return newAccessToken;
  }


  public void logout(HttpServletRequest request, HttpServletResponse response, String accessToken) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) throw new CustomJwtException("Request have no cookies");
    Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals("refreshToken")).findFirst()
            .map(Cookie::getValue).ifPresent(jwt -> {
              if (jwtService.isTokenValid(jwt)) {
                String jti = jwtService.extractJti(jwt);
                redisTemplate.delete("refresh" + jti);
              }
            });

    cookieService.deleteRefreshTokenCookie(request,response);

    Claims claims = jwtService.extractAllToken(accessToken);
    String jti = claims.getId();
    long ttl = claims.getExpiration().getTime() - System.currentTimeMillis();
    try {
      redisTemplate.opsForValue().set("blackList" + jti, "1", ttl, TimeUnit.MILLISECONDS);
    } catch (RedisConnectionFailureException ex) {
      log.warn("Redis unvailable" + ex.getMessage());
    }

    try {
      response.getWriter().write(HttpStatus.OK.value());
    } catch (IOException exception) {
      throw new CommonException("Occur error while writing on response", "/api/v1/auth/logout");
    }
  }



}