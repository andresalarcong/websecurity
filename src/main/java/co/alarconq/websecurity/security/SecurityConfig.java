package co.alarconq.websecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(1) // Mayor prioridad para las APIs
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // Solo aplica a rutas /api/**
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/productos/**").permitAll() // Permite acceso a la API de productos
                        .anyRequest().authenticated()
                )
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/api/productos/**")); // Desactiva CSRF para la API de productos

        return http.build();
    }

    @Bean
    @Order(2) // Menor prioridad para el resto de la aplicación web
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**") // Aplica al resto de rutas
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/publico", "/login", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/privado", true)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/?lang=en")
                        .permitAll());

        return http.build();
    }
}