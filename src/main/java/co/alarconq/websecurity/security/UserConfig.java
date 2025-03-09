package co.alarconq.websecurity.security;

import co.alarconq.websecurity.domain.Usuario;
import co.alarconq.websecurity.repository.UsuarioRepository;
import co.alarconq.websecurity.service.DatabaseUserDetailsService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

@Configuration
public class UserConfig {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UserConfig(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService(DatabaseUserDetailsService databaseUserDetailsService) {
        return databaseUserDetailsService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void inicializarUsuarios() {
        // Inicializar usuario por defecto si no existe ninguno
        usuarioRepository.count()
                .flatMap(count -> {
                    if (count == 0) {
                        Usuario usuarioDefault = Usuario.builder()
                                .username("user")
                                .password(passwordEncoder.encode("password"))
                                .roles("ROLE_USER")
                                .enabled(true)
                                .accountNonExpired(true)
                                .accountNonLocked(true)
                                .credentialsNonExpired(true)
                                .build();
                        return usuarioRepository.save(usuarioDefault);
                    }
                    return Mono.empty();
                })
                .subscribe();
    }
}