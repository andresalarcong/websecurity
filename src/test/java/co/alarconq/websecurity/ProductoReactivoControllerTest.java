package co.alarconq.websecurity;

import co.alarconq.websecurity.controller.ProductoReactivoController;
import co.alarconq.websecurity.domain.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductoReactivoControllerTest {
    @Autowired
    private ProductoReactivoController productoReactivoController;

    @Test
    public void testListaProductos() {
        Flux<Producto> productos = productoReactivoController.listarProductos();
        StepVerifier.create(productos)
                .expectNextMatches(p -> p.getNombre().equals("Laptop"))
                .expectNextMatches(p -> p.getNombre().equals("Mouse"))
                .expectNextMatches(p -> p.getNombre().equals("Teclado"))
                .verifyComplete();
    }
}