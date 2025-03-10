package com.swd392.preOrderBlindBox.security;

import com.swd392.preOrderBlindBox.service.JwtTokenService;
import com.swd392.preOrderBlindBox.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final UserService userService;
  private final JwtTokenService jwtTokenService;

  public SecurityConfig(UserService userService, JwtTokenService jwtTokenService) {
    this.userService = userService;
    this.jwtTokenService = jwtTokenService;
  }

  private final String[] WHITE_LIST = {
          "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html"
  };

  private final String[] PUBLIC_LIST = {"/api/v1/users/login", "/api/v1/blindbox-series", "/api/v1/blindbox-series/*"};

  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.addAllowedOrigin("http://localhost:5173");
    corsConfig.addAllowedOrigin("https://preorder-blindbox.vercel.app");
    corsConfig.addAllowedMethod("*");
    corsConfig.addAllowedHeader("*");
    corsConfig.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig);

    return new CorsFilter(source);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
          AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public JwtAuthenticationFilter authTokenFilter() {
    return new JwtAuthenticationFilter(jwtTokenService, userService);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(
                    session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(
                    request ->
                            request
                                    .requestMatchers(WHITE_LIST)
                                    .permitAll()
                                    .requestMatchers(PUBLIC_LIST)
                                    .permitAll()
                                    .requestMatchers("/api/v1/users/create-test-user").permitAll()
                                    .anyRequest()
                                    .authenticated());
    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}