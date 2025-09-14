package com.business_service.business_service.controller;


import com.business_service.business_service.dto.CategoriaDTO;
import com.business_service.business_service.dto.ProductoDTO;
import com.business_service.business_service.dto.InventarioDTO;
import com.business_service.business_service.dto.ProductoRequest;
import com.business_service.business_service.exception.ProductoNoEncontrado;
import com.business_service.business_service.exception.ValidacionNegocioException;
import com.business_service.business_service.service.CategoriaBusinessService;
import com.business_service.business_service.service.InventarioBusinessService;
import com.business_service.business_service.service.ProductoBusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusinessController.class)
public class BusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoBusinessService productoBusinessService;

    @MockitoBean
    private CategoriaBusinessService categoriaBusinessService;

    @MockitoBean
    private InventarioBusinessService inventarioBusinessService;

    //================ PRODUCTOS =================//

    @Test
    void GETAllProducts_returnsListAndStatus200() throws Exception {
        ProductoDTO producto = new ProductoDTO();
        producto.setId(1L);
        producto.setNombre("Producto A");

        when(productoBusinessService.findAllProducts()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Producto A"));
    }

    @Test
    void GETProductById_returnsProductoAndStatus200() throws Exception {
        ProductoDTO producto = new ProductoDTO();
        producto.setId(1L);
        producto.setNombre("Producto A");

        when(productoBusinessService.findProductById(1L)).thenReturn(producto);

        mockMvc.perform(get("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Producto A"));
    }

    @Test
    void GETNonExistentProductById_returnsStatus404() throws Exception {
        when(productoBusinessService.findProductById(1L)).thenThrow(new ProductoNoEncontrado(1L));

        mockMvc.perform(get("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void POSTProduct_createsProductAndReturns201() throws Exception {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto A");
        request.setPrecio(BigDecimal.TEN);
        request.setStock(5);
        request.setCategoriaId(1L);

        ProductoDTO response = new ProductoDTO();
        response.setId(1L);
        response.setNombre("Producto A");

        when(productoBusinessService.saveProduct(any(ProductoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Producto A"));
    }

    @Test
    void POSTProduct_invalidPrice_returnsBadRequest() throws Exception {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto A");
        request.setPrecio(BigDecimal.ZERO); // precio inválido
        request.setStock(5);
        request.setCategoriaId(1L);

        when(productoBusinessService.saveProduct(any()))
                .thenThrow(new ValidacionNegocioException("El precio debe ser mayor a cero"));

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Error de validación de negocio"))
                .andExpect(jsonPath("$.detalle").value("El precio debe ser mayor a cero"));
    }


    @Test
    void PUTProduct_updatesProductAndReturns200() throws Exception {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto B");
        request.setPrecio(BigDecimal.TEN);
        request.setStock(10);
        request.setCategoriaId(1L);

        ProductoDTO response = new ProductoDTO();
        response.setId(1L);
        response.setNombre("Producto B");

        when(productoBusinessService.updateProduct(eq(1L), any(ProductoRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Producto B"));
    }

    @Test
    void DELETEProduct_returnsStatus204() throws Exception {
        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void DELETENonExistentProduct_returnsStatus404() throws Exception {
        doThrow(new ProductoNoEncontrado(1L)).when(productoBusinessService).deleteProduct(1L);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNotFound());
    }

    //================ CATEGORIAS =================//

    @Test
    void GETAllCategories_returnsListAndStatus200() throws Exception {
        CategoriaDTO categoria = new CategoriaDTO();
        categoria.setId(1L);
        categoria.setNombre("Categoria A");

        when(categoriaBusinessService.findAllCategory()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Categoria A"));
    }

    @Test
    void PUTCategory_updatesCategoryAndReturns200() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Categoria B");

        CategoriaDTO response = new CategoriaDTO();
        response.setId(1L);
        response.setNombre("Categoria B");

        when(categoriaBusinessService.updateCategory(eq(1L), any(CategoriaDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Categoria B"));
    }

    @Test
    void DELETECategory_returnsStatus204() throws Exception {
        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }

    //================ INVENTARIO =================//

    @Test
    void GETInventoriesLowStock_returnsListAndStatus200() throws Exception {
        InventarioDTO inv = new InventarioDTO();
        inv.setId(1L);
        inv.setCantidad(2);

        when(inventarioBusinessService.findInventoriesWithLowStock()).thenReturn(List.of(inv));

        mockMvc.perform(get("/api/inventario/low-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cantidad").value(2));
    }

    @Test
    void GETInventoryByProductId_returnsInventoryAndStatus200() throws Exception {
        InventarioDTO inv = new InventarioDTO();
        inv.setId(1L);
        inv.setCantidad(5);

        when(inventarioBusinessService.findInventoriesByProductId(1L)).thenReturn(inv);

        mockMvc.perform(get("/api/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(5));
    }

}
