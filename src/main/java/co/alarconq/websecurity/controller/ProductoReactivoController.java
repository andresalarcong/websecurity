package co.alarconq.websecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.alarconq.websecurity.domain.Producto;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/reactivo/productos")
public class ProductoReactivoController {

    @GetMapping
    public Flux<Producto> listarProductos() {

        return Flux.just(
                new Producto(1L, "Laptop", 1200.0),
                new Producto(2L, "Mouse", 25.0),
                new Producto(3L, "Teclado", 45.0)
        );
    }
}
