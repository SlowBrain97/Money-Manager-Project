package org.example.backend.Service;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
  private String secretKey = "eXRyeXRqdGs2NWk5NG90ajUzaW90a3JlanRuZ2Vqa2xtcmplbndyaGpld2dpbzUzaDZ0NTR0eWhydGdzZTMyNDNlcjM0cmV0";

  public static long expirationAccessToken = 1000*60*15;

  public static long expirationRefreshToken = 1000*60*60*24*15;
  public String generateJwt (String username, Map<String,?> customClaims){
    Map<String,Object> claims = new HashMap<>(customClaims);
    return Jwts.builder()
            .claims(claims)
            .subject(username)
            .id(UUID.randomUUID().toString())
            .expiration(new Date(System.currentTimeMillis() +expirationAccessToken))
            .issuedAt(new Date(System.currentTimeMillis()))
            .signWith(getSigningKey())
            .compact();
  }

  public String generateRefreshToken (String username,String jti ,Map<String,?> customClaims){
    Map<String,Object> claims = new HashMap<>(customClaims);
    return Jwts.builder().claims(claims)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis()+expirationRefreshToken))
            .subject(username)
            .id(jti)
            .signWith(getSigningKey())
            .compact();
  }

  public Claims extractAllToken(String token){

    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }
  public boolean validateToken (String token , UserDetails userDetails){
    String username = this.extractUsername(token);
    return username.equals(userDetails.getUsername()) && this.isTokenValid(token);
  }
  boolean isTokenValid(String token){
    try{
      Jwts.parser().verifyWith(this.getSigningKey()).build().parseSignedClaims(token);
      return true;
    }
    catch (JwtException ex){
      return false;
    }
  }
  public <T> T extractToken(String token, Function<Claims,T> func){
    Claims claims = this.extractAllToken(token);
    return func.apply(claims);
  }
  public String extractUsername (String token){
    return this.extractToken(token,Claims::getSubject);
  }
  public Date extractExpiration(String token){
    return this.extractToken(token,Claims::getExpiration);
  }
  public String extractJti(String token){
    return this.extractToken(token, Claims::getId);
  }
  private SecretKey getSigningKey (){
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    return Keys.hmacShaKeyFor(bytes);
  }


  public Cookie createRefreshTokenCookie (String token){
    Cookie cookie = new Cookie("refreshToken", token);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setPath("/api/v1/jwt/refresh");
    cookie.setMaxAge((int)expirationRefreshToken/1000);
    return cookie;
  }

  public Cookie createAccessTokenCookie (String token){
    Cookie cookie = new Cookie("accessToken", token);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge((int)expirationAccessToken/1000);
    return cookie;
  }
}
