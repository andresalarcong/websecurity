package co.alarconq.websecurity.service;



import co.alarconq.websecurity.domain.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    Producto guardarProducto(Producto producto);
    List<Producto> listarProductos();
    Optional<Producto> obtenerProductoPorId(Long id);
    Producto actualizarProducto(Long id, Producto producto);
    void eliminarProducto(Long id);
}