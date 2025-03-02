package co.alarconq.websecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/productos").permitAll() // Permite el acceso sin autenticación a /api/productos
                                .anyRequest().authenticated() // Requiere autenticación para otras rutas
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/productos")); // Ignora la protección CSRF para /api/productos

        return http.build();
    }
}