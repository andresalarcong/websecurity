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

    @PostMapping
    public Mono<ResponseEntity<Producto>> agregarProducto(@Valid @RequestBody Producto producto) {
        return productoService.guardarProducto(producto)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Flux<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Producto>> obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Producto>> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        return productoService.actualizarProducto(id, producto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminarProducto(@PathVariable Long id) {
        return productoService.eliminarProducto(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}