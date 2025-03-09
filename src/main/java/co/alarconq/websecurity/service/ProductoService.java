package co.alarconq.websecurity.service;

import co.alarconq.websecurity.domain.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {
    /**
     * Guarda un nuevo producto.
     *
     * @param producto entidad a guardar
     * @return producto guardado con ID asignado
     */
    Mono<Producto> guardarProducto(Producto producto);

    /**
     * Lista todos los productos.
     *
     * @return flujo de productos
     */
    Flux<Producto> listarProductos();

    /**
     * Obtiene un producto por su ID.
     *
     * @param id identificador del producto
     * @return producto encontrado o empty si no existe
     */
    Mono<Producto> obtenerProductoPorId(Long id);

    /**
     * Actualiza un producto existente.
     *
     * @param id identificador del producto
     * @param producto datos actualizados
     * @return producto actualizado o empty si no existe
     */
    Mono<Producto> actualizarProducto(Long id, Producto producto);

    /**
     * Elimina un producto por su ID.
     *
     * @param id identificador del producto a eliminar
     * @return completable que indica finalización
     */
    Mono<Void> eliminarProducto(Long id);
}