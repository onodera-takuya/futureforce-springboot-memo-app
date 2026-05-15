package com.lesson.memo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ComponentScan("com.lesson.memo")
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers( "/admin/signup","/admin/signin","/h2-console","/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
        		.ignoringRequestMatchers("/h2-console/**")
            )
            .headers(headers -> headers
            	.frameOptions(frame -> frame.sameOrigin())
            )
            .formLogin((form) -> form
                .loginPage("/admin/signin")
                .loginProcessingUrl("/admin/signin")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/memo", true)
                .failureUrl("/admin/signin?error")
                .permitAll()
            );
        return http.build();        
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http, 
    		PasswordEncoder passwordEncoder, 
    		UserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }
}