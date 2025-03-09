package co.alarconq.websecurity;

import co.alarconq.websecurity.domain.Producto;
import co.alarconq.websecurity.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductoControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductoRepository productoRepository;

    @BeforeEach
    void setUp() {
        // Clean the repository before each test
        StepVerifier.create(productoRepository.deleteAll()).verifyComplete();
    }

    @Test
    void cuandoAgregarProducto_entoncesDebeRetornarProductoCreado() {
        Producto producto = new Producto();
        producto.setNombre("Producto 1");
        producto.setPrecio(100.0);

        webTestClient.post().uri("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(producto), Producto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Producto 1")
                .jsonPath("$.precio").isEqualTo(100.0);
    }

    @Test
    void cuandoListarProductos_entoncesDebeRetornarListaDeProductos() {
        // Add test data
        Producto producto = new Producto();
        producto.setNombre("Producto Test");
        producto.setPrecio(150.0);

        StepVerifier.create(productoRepository.save(producto))
                .expectNextCount(1)
                .verifyComplete();

        webTestClient.get().uri("/api/productos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Producto.class);
    }

    @Test
    void cuandoObtenerProductoPorId_entoncesDebeRetornarProducto() {
        Producto producto = new Producto();
        producto.setNombre("Producto 2");
        producto.setPrecio(200.0);

        // Save product and get its ID
        Long savedProductId = productoRepository.save(producto)
                .map(Producto::getId)
                .block();

        webTestClient.get().uri("/api/productos/{id}", savedProductId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Producto 2")
                .jsonPath("$.precio").isEqualTo(200.0);
    }

    @Test
    void cuandoActualizarProducto_entoncesDebeRetornarProductoActualizado() {
        Producto producto = new Producto();
        producto.setNombre("Producto 3");
        producto.setPrecio(150.0);

        // Save product and get saved product
        Producto savedProducto = productoRepository.save(producto).block();

        Producto updatedProducto = new Producto();
        updatedProducto.setId(savedProducto.getId());
        updatedProducto.setNombre("Producto 3 actualizado");
        updatedProducto.setPrecio(180.0);

        webTestClient.put().uri("/api/productos/{id}", savedProducto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedProducto), Producto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Producto 3 actualizado")
                .jsonPath("$.precio").isEqualTo(180.0);
    }

    @Test
    void cuandoEliminarProducto_entoncesDebeRetornarNoContent() {
        Producto producto = new Producto();
        producto.setNombre("Producto 4");
        producto.setPrecio(250.0);

        // Save product and get its ID
        Long savedProductId = productoRepository.save(producto)
                .map(Producto::getId)
                .block();

        webTestClient.delete().uri("/api/productos/{id}", savedProductId)
                .exchange()
                .expectStatus().isNoContent();

        // Verify product no longer exists
        webTestClient.get().uri("/api/productos/{id}", savedProductId)
                .exchange()
                .expectStatus().isNotFound();
    }
}