package org.example.backend.webConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.Exception.CustomJwtException;
import org.example.backend.Service.JwtService;
import org.example.backend.Service.UserDetailServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final UserDetailServiceImpl userDetailService;
  private final AntPathMatcher pathMatcher = new AntPathMatcher();
  private final RedisTemplate<String,String> redisTemplate;
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String path = request.getRequestURI();
    if (pathMatcher.match("/api/v1/user/create", path) || pathMatcher.match("/api/v1/public/**", path)  || pathMatcher.match("/api/v1/auth/login",path)) {
      filterChain.doFilter(request, response);
      return;
    }

      String username = null;
      String token = null;

      String jwt = request.getHeader("Authorization");
      log.info(jwt);
      if (jwt != null && jwt.startsWith("Bearer ")){
        token = jwt.substring(7);
        try{
          String jti = jwtService.extractJti(token);
          if (Boolean.TRUE.equals(redisTemplate.hasKey("blackList"+jti))) {
            throw new CustomJwtException("Your token is in black list!");
          }
          username = jwtService.extractUsername(token);
          log.info("username getting");
        }
        catch (Exception e){
          System.out.println("Token is not valid");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
          UserDetails userDetails = userDetailService.loadUserByUsername(username);
          if (jwtService.validateToken(token,userDetails)){

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("end securityCOntext");
            log.info(authenticationToken.getAuthorities().toString());
          }
        }
      }

      filterChain.doFilter(request,response);
  }
}
