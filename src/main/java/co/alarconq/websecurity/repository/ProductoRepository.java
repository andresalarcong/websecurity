package co.alarconq.websecurity.repository;

import co.alarconq.websecurity.domain.Producto;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends ReactiveCrudRepository<Producto, Long> {
}