package com.grandvista.payment;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.*;
@Configuration
public class SecurityConfig {
 @Bean SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwt)throws Exception {
  http.csrf(c->c.disable()).addFilterBefore(jwt,org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
   .authorizeHttpRequests(a->a.anyRequest().permitAll());
  return http.build();
 }
}
