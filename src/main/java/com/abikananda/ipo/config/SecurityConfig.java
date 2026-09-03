package com.abikananda.ipo.config;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
@Configuration public class SecurityConfig {
 @Bean SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
  return http.authorizeHttpRequests(a->a
    .requestMatchers("/actuator/health","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll()
    .requestMatchers(HttpMethod.GET,"/api/v1/**").permitAll()
    .requestMatchers("/api/v1/**").authenticated().anyRequest().denyAll())
    .httpBasic(Customizer.withDefaults()).build();
 }
}
