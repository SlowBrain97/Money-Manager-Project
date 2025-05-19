package org.example.backend.webConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.Entity.CustomUserDetails;
import org.example.backend.Service.UserDetailServiceImpl;
import org.example.backend.Service.UserServiceImpl;
import org.example.backend.utils.JwtUtils;
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
public class JwtRequestFilter extends OncePerRequestFilter {
  private final JwtUtils jwtUtils;
  private final UserDetailServiceImpl userDetailService;
  private final AntPathMatcher pathMatcher = new AntPathMatcher();
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String path = request.getRequestURI();
    if (pathMatcher.match("/api/v1/user/**", path) || pathMatcher.match("/api/v1/public/**", path) || pathMatcher.match("/api/v1/bill/**", path)) {
      filterChain.doFilter(request, response);
      return;
    }

      String username = null;
      String token = null;

      String jwt = request.getHeader("Authorization");

      if (jwt != null && jwt.startsWith("Bearer ")){
        token = jwt.substring(7);
        try{
          username = jwtUtils.extractUsername(token);
        }
        catch (Exception e){
          System.out.println("Token is not valid");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
          UserDetails userDetails = userDetailService.loadUserByUsername(username);
          if (jwtUtils.validateToken(token,userDetails)){
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          }
        }
      }

      filterChain.doFilter(request,response);
  }
}
