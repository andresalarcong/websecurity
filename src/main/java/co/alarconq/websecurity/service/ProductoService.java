package co.alarconq.websecurity.service;

import co.alarconq.websecurity.domain.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {
    Mono<Producto> guardarProducto(Producto producto);
    Flux<Producto> listarProductos();
    Mono<Producto> obtenerProductoPorId(Long id);
    Mono<Producto> actualizarProducto(Long id, Producto producto);
    Mono<Void> eliminarProducto(Long id);
}