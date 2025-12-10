package doan.bai_2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] PUBLIC_GET_ENDPOINTS = {
        "/register",
        "/login"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(request -> request
            // public endpoint
            .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
            .requestMatchers(HttpMethod.POST, PUBLIC_GET_ENDPOINTS).permitAll()
            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

            // admin
            .requestMatchers("/admin/**").hasRole("ADMIN")

            // user and admin
            .requestMatchers("/home/**").authenticated()
            .anyRequest().authenticated()
        ).formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/home", true)
            .failureUrl("/login?error=true")
            .permitAll()
        ).logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .permitAll()
        );  
            
        return http.build();
    }
}
