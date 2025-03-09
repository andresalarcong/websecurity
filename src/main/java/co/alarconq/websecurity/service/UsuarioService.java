package co.alarconq.websecurity.service;

import co.alarconq.websecurity.domain.Usuario;
import co.alarconq.websecurity.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<Usuario> guardarUsuario(Usuario usuario) {
        // Encriptar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public Flux<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Mono<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Mono<Usuario> obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Mono<Usuario> actualizarUsuario(Long id, Usuario usuario) {
        return usuarioRepository.findById(id)
                .flatMap(u -> {
                    u.setUsername(usuario.getUsername());
                    // Solo actualizamos la contraseña si no está vacía
                    if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                        u.setPassword(passwordEncoder.encode(usuario.getPassword()));
                    }
                    u.setRoles(usuario.getRoles());
                    u.setEnabled(usuario.isEnabled());
                    u.setAccountNonExpired(usuario.isAccountNonExpired());
                    u.setAccountNonLocked(usuario.isAccountNonLocked());
                    u.setCredentialsNonExpired(usuario.isCredentialsNonExpired());
                    return usuarioRepository.save(u);
                });
    }

    public Mono<Void> eliminarUsuario(Long id) {
        return usuarioRepository.deleteById(id);
    }
}