package com.bloodbank.bms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin@bloodbank.com")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();
                
        UserDetails staff = User.builder()
                .username("staff@bloodbank.com")
                .password(passwordEncoder().encode("staff123"))
                .roles("STAFF")
                .build();
                
        UserDetails doctor = User.builder()
                .username("doctor@bloodbank.com")
                .password(passwordEncoder().encode("doctor123"))
                .roles("STAFF")
                .build();
                
        return new InMemoryUserDetailsManager(admin, staff, doctor);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public URLs - No login required
                .requestMatchers("/", "/css/**", "/js/**", "/images/**", 
                               "/donor/**", "/blood/**", "/about", "/contact", 
                               "/login", "/error", "/recipient/**", "/staff/register").permitAll()
                // Staff URLs - Require STAFF role
                .requestMatchers("/staff/**").hasRole("STAFF")
                // Admin URLs - Require ADMIN role
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Any other request - Require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/admin/dashboard")
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
}