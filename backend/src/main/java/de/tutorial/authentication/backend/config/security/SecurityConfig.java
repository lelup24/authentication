package de.tutorial.authentication.backend.config.security;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtUtil jwtUtil;
  private final MyUserDetailsService myUserDetailsService;
  private final JwtTokenFilter jwtTokenFilter;

  public SecurityConfig(
      final JwtUtil jwtUtil,
      final MyUserDetailsService myUserDetailsService,
      final JwtTokenFilter jwtTokenFilter) {
    this.jwtUtil = jwtUtil;
    this.myUserDetailsService = myUserDetailsService;
    this.jwtTokenFilter = jwtTokenFilter;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    final UserAuthenticationFilter userAuthenticationFilter =
        new UserAuthenticationFilter(jwtUtil, daoAuthenticationProvider());
    userAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");

    http.cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .addFilter(userAuthenticationFilter)
        .authorizeHttpRequests(
            a ->
                a.requestMatchers("/api/v1/login", "/api/v1/register", "/api/v1/unsecured")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
    return daoAuthenticationProvider;
  }
}
