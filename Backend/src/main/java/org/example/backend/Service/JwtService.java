package org.example.backend.Service;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
  private String secretKey = "eXRyeXRqdGs2NWk5NG90ajUzaW90a3JlanRuZ2Vqa2xtcmplbndyaGpld2dpbzUzaDZ0NTR0eWhydGdzZTMyNDNlcjM0cmV0";

  private long expirationTime;

  private long expirationRefreshToken;
  public String generateJwt (String username, Map<String,?> customClaims){
    Map<String,Object> claims = new HashMap<>(customClaims);
    return Jwts.builder()
            .claims(claims)
            .subject(username)
            .expiration(new Date(System.currentTimeMillis() +expirationTime))
            .issuedAt(new Date(System.currentTimeMillis()))
            .signWith(getSigningKey())
            .compact();
  }

  public String generateRefreshToken (String username, Map<String,?> customClaims){
    Map<String,Object> claims = new HashMap<>(customClaims);
    return Jwts.builder().claims(claims)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis()+expirationRefreshToken))
            .subject(username)
            .signWith(getSigningKey())
            .compact();
  }

  public Claims extractAllToken(String token){

    return Jwts.parser().build().parseSignedClaims(token).getPayload();
  }
  public boolean validateToken (String token , UserDetails userDetails){
    String username = this.extractUsername(token);
    return username.equals(userDetails.getUsername()) && this.isTokenExpirated(token);
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
  private Key getSigningKey (){
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    return Keys.hmacShaKeyFor(bytes);
  }
  private boolean isTokenExpirated(String token){
    return this.extractExpiration(token).before(new Date());
  }

}
