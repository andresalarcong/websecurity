package co.alarconq.websecurity;

import co.alarconq.websecurity.domain.Producto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void cuandoAgregarProducto_entoncesDebeRetornarProductoCreado() throws Exception {
        Producto producto = new Producto();
        producto.setNombre("Producto 1");
        producto.setPrecio(100.0);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk()) // Esperamos un estado 200 OK
                .andExpect(jsonPath("$.nombre").value("Producto 1"))
                .andExpect(jsonPath("$.precio").value(100.0));
    }

    @Test
    void cuandoListarProductos_entoncesDebeRetornarListaDeProductos() throws Exception {
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").exists())
                .andExpect(jsonPath("$[0].precio").exists());
    }

    @Test
    void cuandoObtenerProductoPorId_entoncesDebeRetornarProducto() throws Exception {
        // Primero, agregamos un producto para que exista en la base de datos.
        Producto producto = new Producto();
        producto.setNombre("Producto 2");
        producto.setPrecio(200.0);

        String json = objectMapper.writeValueAsString(producto);
        String response = mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long productoId = Long.parseLong(JsonPath.read(response, "$.id").toString());

        mockMvc.perform(get("/api/productos/{id}", productoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Producto 2"))
                .andExpect(jsonPath("$.precio").value(200.0));
    }

    @Test
    void cuandoActualizarProducto_entoncesDebeRetornarProductoActualizado() throws Exception {
        // Primero, agregamos un producto
        Producto producto = new Producto();
        producto.setNombre("Producto 3");
        producto.setPrecio(150.0);

        String json = objectMapper.writeValueAsString(producto);
        String response = mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long productoId = Long.parseLong(JsonPath.read(response, "$.id").toString());
        // Ahora actualizamos el producto
        producto.setNombre("Producto 3 actualizado");
        producto.setPrecio(180.0);

        mockMvc.perform(put("/api/productos/{id}", productoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Producto 3 actualizado"))
                .andExpect(jsonPath("$.precio").value(180.0));
    }

    @Test
    void cuandoEliminarProducto_entoncesDebeRetornarNoContent() throws Exception {
        // Primero, agregamos un producto
        Producto producto = new Producto();
        producto.setNombre("Producto 4");
        producto.setPrecio(250.0);

        String json = objectMapper.writeValueAsString(producto);
        String response = mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long productoId = Long.parseLong(JsonPath.read(response, "$.id").toString());
        // Ahora eliminamos el producto
        mockMvc.perform(delete("/api/productos/{id}", productoId))
                .andExpect(status().isNoContent());

        // Verificamos que el producto ha sido eliminado
        mockMvc.perform(get("/api/productos/{id}", productoId))
                .andExpect(status().isNotFound());
    }
}
