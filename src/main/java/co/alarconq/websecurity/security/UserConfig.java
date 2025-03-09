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

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioRepository repositorio de usuarios
     * @param passwordEncoder codificador de contraseñas
     */
    public UserConfig(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Configura el servicio de detalles de usuario reactivo.
     *
     * @param databaseUserDetailsService servicio personalizado de detalles de usuario
     * @return servicio de detalles de usuario reactivo
     */
    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService(DatabaseUserDetailsService databaseUserDetailsService) {
        return databaseUserDetailsService;
    }

    /**
     * Inicializa un usuario por defecto cuando arranca la aplicación.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void inicializarUsuarios() {
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