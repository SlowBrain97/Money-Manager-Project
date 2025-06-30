package org.example.backend.webConfig;


import lombok.RequiredArgsConstructor;
import org.example.backend.Service.CustomOauth2UserService;
import org.example.backend.Service.JwtService;
import org.example.backend.Service.UserDetailServiceImpl;
import org.example.backend.utils.Oauth2SuccessHandler;
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
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
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
  private final UserDetailServiceImpl userDetailService;
  private final Oauth2SuccessHandler oauth2SuccessHandler;
  private final CustomOauth2UserService customOauth2UserService;

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
                    .requestMatchers("/api/v1/user/**").hasAnyRole("USER","ADMIN")
                    .requestMatchers("/api/v1/user/create").permitAll()
                    .requestMatchers("/api/v1/bill/**").hasRole("USER")
                    .requestMatchers("/api/v1/auth/login").permitAll()
                    .requestMatchers("/", "/login/oauth2", "/oauth2/**").permitAll()
                    .requestMatchers("/api/v1/auth/**").hasRole("USER")
                    .anyRequest().authenticated())
            .authenticationProvider(daoAuthenticationProvider())
            .addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2Login(oauth2 -> {
              oauth2.authorizationEndpoint(authorize -> authorize.baseUri("/oauth2/authorize"))
                      .userInfoEndpoint(userInfo -> userInfo.userService(customOauth2UserService))
                      .successHandler(oauth2SuccessHandler);
            })

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
  public DaoAuthenticationProvider daoAuthenticationProvider(){
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    daoAuthenticationProvider.setUserDetailsService(userDetailService);
    return daoAuthenticationProvider;
  }
  @Bean
  public PasswordEncoder passwordEncoder (){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CookieSerializer refreshTokenCookie (){
    DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
    defaultCookieSerializer.setCookieName("refreshToken");
    defaultCookieSerializer.setCookiePath("/api/v1/auth/refresh");
    defaultCookieSerializer.setCookieMaxAge((int)(JwtService.expirationRefreshToken/1000));
    defaultCookieSerializer.setSameSite("None");
    defaultCookieSerializer.setUseHttpOnlyCookie(true);
    return defaultCookieSerializer;
  }

}
