package co.alarconq.websecurity.repository;

import co.alarconq.websecurity.domain.Usuario;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UsuarioRepository extends ReactiveCrudRepository<Usuario, Long> {
    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario a buscar
     * @return usuario encontrado o empty si no existe
     */
    Mono<Usuario> findByUsername(String username);
}