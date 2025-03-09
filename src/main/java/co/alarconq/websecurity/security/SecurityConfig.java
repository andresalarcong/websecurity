package co.alarconq.websecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityWebFilterChain apiSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/productos/**").permitAll()
                        .anyExchange().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    public SecurityWebFilterChain webSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/**"))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/", "/publico", "/login", "/css/**", "/js/**").permitAll()
                        .anyExchange().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                )
                .logout(logout -> logout
                        .logoutSuccessHandler((exchange, authentication) -> {
                            exchange.getExchange().getResponse().setStatusCode(org.springframework.http.HttpStatus.FOUND);
                            exchange.getExchange().getResponse().getHeaders().setLocation(URI.create("/?lang=en"));
                            return Mono.empty();
                        })
                )
                .build();
    }
}