package co.alarconq.websecurity.service;

import co.alarconq.websecurity.domain.Producto;
import co.alarconq.websecurity.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Producto> guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Producto> actualizarProducto(Long id, Producto producto) {
        return productoRepository.findById(id)
                .flatMap(p -> {
                    p.setNombre(producto.getNombre());
                    p.setPrecio(producto.getPrecio());
                    return productoRepository.save(p);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> eliminarProducto(Long id) {
        return productoRepository.deleteById(id);
    }
}