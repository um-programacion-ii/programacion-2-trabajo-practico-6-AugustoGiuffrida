package com.business_service.business_service.communication;

import com.business_service.business_service.dto.CategoriaDTO;
import com.business_service.business_service.dto.InventarioDTO;
import com.business_service.business_service.dto.ProductoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BusinessServiceCommunicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private WireMockServer wireMockServer;

    @BeforeAll
    void setup() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        System.setProperty("data.service.url", "http://localhost:8080");
    }

    @AfterAll
    void teardown() {
        wireMockServer.stop();
    }

    //================ PRODUCTOS =================//

    @Test
    void GETProductById_success() throws Exception {
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(
                        urlEqualTo("/data/productos/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"nombre\":\"Producto A\",\"precio\":10}")));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Producto A"));

        wireMockServer.verify(com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor(
                urlEqualTo("/data/productos/1")));
    }

    @Test
    void POSTProduct_success() throws Exception {
        ProductoRequest request = new ProductoRequest("Producto B","Desc B", BigDecimal.valueOf(15),5,1L);

        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(
                        urlEqualTo("/data/productos"))
                .withRequestBody(com.github.tomakehurst.wiremock.client.WireMock.equalToJson(
                        objectMapper.writeValueAsString(request)))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":2,\"nombre\":\"Producto B\"}")));

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Producto B"));
    }

    @Test
    void PUTProduct_success() throws Exception {
        ProductoRequest request = new ProductoRequest("Producto D","Desc D", BigDecimal.valueOf(20),10,1L);

        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.put(
                        urlEqualTo("/data/productos/1"))
                .withRequestBody(com.github.tomakehurst.wiremock.client.WireMock.equalToJson(
                        objectMapper.writeValueAsString(request)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"nombre\":\"Producto D\"}")));

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Producto D"));
    }

    @Test
    void DELETEProduct_success() throws Exception {
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.delete(
                        urlEqualTo("/data/productos/1"))
                .willReturn(aResponse().withStatus(204)));

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void GETProductById_notFound() throws Exception {
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(
                        com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo("/data/productos/999"))
                .willReturn(com.github.tomakehurst.wiremock.client.WireMock.aResponse()
                        .withStatus(404)));

        mockMvc.perform(get("/api/productos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Producto no encontrado"));
    }

    @Test
    void GETProductById_serviceUnavailable() throws Exception {
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(
                        com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo("/data/productos/1"))
                .willReturn(com.github.tomakehurst.wiremock.client.WireMock.aResponse()
                        .withStatus(500)));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("Error de comunicación con el servicio de datos"));
    }

    @Test
    void POSTProduct_validationError() throws Exception {
        // Precio negativo para disparar ValidacionNegocioException
        ProductoRequest request = new ProductoRequest("Producto X","Desc", BigDecimal.valueOf(-10),5,1L);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Error de validación de negocio"));
    }


    //================ CATEGORIAS =================//

    @Test
    void GETAllCategories_success() throws Exception {
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(
                        urlEqualTo("/data/categorias"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\":1,\"nombre\":\"Cat A\"}]")));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Cat A"));
    }


    @Test
    void PUTCategory_success() throws Exception {
        CategoriaDTO categoria = new CategoriaDTO(null,"Cat C","Desc C");

        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.put(
                        urlEqualTo("/data/categorias/1"))
                .withRequestBody(com.github.tomakehurst.wiremock.client.WireMock.equalToJson(
                        objectMapper.writeValueAsString(categoria)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"nombre\":\"Cat C\"}")));

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Cat C"));
    }

    @Test
    void DELETECategory_success() throws Exception {
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.delete(
                        urlEqualTo("/data/categorias/1"))
                .willReturn(aResponse().withStatus(204)));

        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }

    //================ INVENTARIO =================//

    @Test
    void GETInventoryByProductId_success() throws Exception {
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(
                        urlEqualTo("/data/inventario/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"cantidad\":5}")));

        mockMvc.perform(get("/api/inventario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(5));
    }
    @Test
    void GETInventoryByProductId_notFound() throws Exception {
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(
                        com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo("/data/inventario/999"))
                .willReturn(com.github.tomakehurst.wiremock.client.WireMock.aResponse()
                        .withStatus(404)));

        mockMvc.perform(get("/api/inventario/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Inventario no encontrado"));
    }


}

