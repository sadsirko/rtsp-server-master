package com.rtsp.rtspserver.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Ensure CSRF is disabled if not handling CSRF tokens in client
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/cameras").authenticated()  // Ensures all access to /api/cameras requires authentication
                        .anyRequest().permitAll()
                )
                .httpBasic();  // Uses HTTP Basic Authentication
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("1")
                .password("1")
                .roles("USER")  // Make sure the role "USER" is enough to access your protected resources
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
