package org.example.backend.webConfig;


import lombok.RequiredArgsConstructor;
import org.example.backend.Service.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
  private final JwtRequestFilter jwtRequestFilter;



  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/v1/**").allowedOrigins("http://localhost:5317/").allowCredentials(true).allowedMethods("GET","POST","PUT","PATCH","DELETE");
    WebMvcConfigurer.super.addCorsMappings(registry);
  }

  @Bean
  public SecurityFilterChain filterChain (HttpSecurity security) throws Exception{
    return security.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth ->
            auth.requestMatchers("/api/v1/public/**").permitAll()
                    .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/v1/user/**").permitAll()
                    .requestMatchers("/api/v1/bill/**").permitAll()
                    .requestMatchers("/api/v1/auth/login").permitAll()
                    .anyRequest().authenticated())
            .addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    PageableHandlerMethodArgumentResolver resolve = new PageableHandlerMethodArgumentResolver();
    resolve.setMaxPageSize(30);
    resolvers.add(resolve);
    WebMvcConfigurer.super.addArgumentResolvers(resolvers);
  }

  @Bean
  public AuthenticationManager authenticationManager (AuthenticationConfiguration configuration) throws Exception{
    return configuration.getAuthenticationManager();
  }
  @Bean
  public PasswordEncoder passwordEncoder (){
    return new BCryptPasswordEncoder();
  }

}
