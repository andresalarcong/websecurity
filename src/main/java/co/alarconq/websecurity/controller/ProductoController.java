package co.alarconq.websecurity.controller;

import co.alarconq.websecurity.domain.Producto;
import co.alarconq.websecurity.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    /**
     * Crea un nuevo producto.
     *
     * @param producto datos del producto a crear
     * @return respuesta con el producto creado
     */
    @PostMapping
    public Mono<ResponseEntity<Producto>> agregarProducto(@Valid @RequestBody Producto producto) {
        return productoService.guardarProducto(producto)
                .map(ResponseEntity::ok);
    }

    /**
     * Obtiene todos los productos.
     *
     * @return flujo de productos
     */
    @GetMapping
    public Flux<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    /**
     * Obtiene un producto por su ID.
     *
     * @param id identificador del producto
     * @return respuesta con el producto encontrado o notFound si no existe
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Producto>> obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza un producto existente.
     *
     * @param id identificador del producto a actualizar
     * @param producto datos actualizados del producto
     * @return respuesta con el producto actualizado o notFound si no existe
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Producto>> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        return productoService.actualizarProducto(id, producto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id identificador del producto a eliminar
     * @return respuesta sin contenido (204)
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminarProducto(@PathVariable Long id) {
        return productoService.eliminarProducto(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}