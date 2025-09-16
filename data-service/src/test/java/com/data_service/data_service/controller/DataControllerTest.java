package com.data_service.data_service.controller;


import com.data_service.data_service.controller.DataController;
import com.data_service.data_service.dto.ProductoRequest;
import com.data_service.data_service.dto.CategoriaDTO;
import com.data_service.data_service.dto.InventarioDTO;
import com.data_service.data_service.entity.Categoria;
import com.data_service.data_service.entity.Inventario;
import com.data_service.data_service.entity.Producto;
import com.data_service.data_service.exception.ProductoNoEncontrado;
import com.data_service.data_service.exception.InventarioNoEncontrado;
import com.data_service.data_service.exception.CategoriaNoEncontrada;
import com.data_service.data_service.service.CategoriaServiceImpl;
import com.data_service.data_service.service.InventarioServiceImpl;
import com.data_service.data_service.service.ProductoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(DataController.class)
public class DataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoServiceImpl productoService;

    @MockitoBean
    private InventarioServiceImpl inventarioService;

    @MockitoBean
    private CategoriaServiceImpl categoriaService;

    //================ PRODUCTOS =================//

    @Test
    void GETAllProducts_returnsListAndStatus200() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto A");

        when(productoService.findAll()).thenReturn(List.of(producto));

        mockMvc.perform(get("/data")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Producto A"));
    }

    @Test
    void GETProductById_returnsProductoAndStatus200() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto A");

        when(productoService.findById(1L)).thenReturn(producto);

        mockMvc.perform(get("/data/productos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Producto A"));
    }

    @Test
    void GETNonExistentProductById_returnsStatus404() throws Exception {
        when(productoService.findById(1L)).thenThrow(new ProductoNoEncontrado(1L));

        mockMvc.perform(get("/data/productos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void POSTProduct_createsProductAndReturns201() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto A");
        Categoria categoria = new Categoria();
        categoria.setId(1L);

        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto A");
        request.setCategoriaId(1L);
        request.setPrecio(BigDecimal.TEN);
        request.setStock(10);

        when(categoriaService.findById(1L)).thenReturn(categoria);
        when(productoService.save(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/data/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Producto A"));
    }

    @Test
    void PUTProduct_updatesProductAndReturns200() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Actualizado");
        Categoria categoria = new Categoria();
        categoria.setId(1L);

        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto Actualizado");
        request.setCategoriaId(1L);
        request.setPrecio(BigDecimal.TEN);
        request.setStock(10);

        when(categoriaService.findById(1L)).thenReturn(categoria);
        when(productoService.update(eq(1L), any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/data/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Producto Actualizado"));
    }

    @Test
    void DELETEProduct_returnsStatus204() throws Exception {
        mockMvc.perform(delete("/data/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void DELETENonExistentProduct_returnsStatus404() throws Exception {
        doThrow(new ProductoNoEncontrado(1L)).when(productoService).delete(1L);

        mockMvc.perform(delete("/data/productos/1"))
                .andExpect(status().isNotFound());
    }

    //================ INVENTARIO =================//

    @Test
    void GETAllInventories_returnsListAndStatus200() throws Exception {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setCantidad(5);

        when(inventarioService.findAll()).thenReturn(List.of(inventario));

        mockMvc.perform(get("/data/inventario")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cantidad").value(5));
    }

    @Test
    void GETInventoryById_returnsInventoryAndStatus200() throws Exception {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setCantidad(5);

        when(inventarioService.findById(1L)).thenReturn(inventario);

        mockMvc.perform(get("/data/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cantidad").value(5));
    }

    @Test
    void GETNonExistentInventoryById_returnsStatus404() throws Exception {
        when(inventarioService.findById(1L)).thenThrow(new InventarioNoEncontrado(1L));

        mockMvc.perform(get("/data/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void POSTInventory_createsInventoryAndReturns201() throws Exception {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setCantidad(5);

        InventarioDTO dto = new InventarioDTO();
        dto.setCantidad(5);

        when(inventarioService.save(any(Inventario.class))).thenReturn(inventario);

        mockMvc.perform(post("/data/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cantidad").value(5));
    }

    @Test
    void PUTInventory_updatesInventoryAndReturns200() throws Exception {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setCantidad(10);

        InventarioDTO dto = new InventarioDTO();
        dto.setCantidad(10);

        when(inventarioService.update(eq(1L), any(Inventario.class))).thenReturn(inventario);

        mockMvc.perform(put("/data/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cantidad").value(10));
    }

    @Test
    void DELETEInventory_returnsStatus204() throws Exception {
        mockMvc.perform(delete("/data/inventario/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void DELETENonExistentInventory_returnsStatus404() throws Exception {
        doThrow(new InventarioNoEncontrado(1L)).when(inventarioService).delete(1L);

        mockMvc.perform(delete("/data/inventario/1"))
                .andExpect(status().isNotFound());
    }

    //================ CATEGORIAS =================//

    @Test
    void GETAllCategories_returnsListAndStatus200() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Categoria A");

        when(categoriaService.findAll()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/data/categoria")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Categoria A"));
    }

    @Test
    void GETCategoryById_returnsCategoryAndStatus200() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Categoria A");

        when(categoriaService.findById(1L)).thenReturn(categoria);

        mockMvc.perform(get("/data/categoria/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Categoria A"));
    }

    @Test
    void GETNonExistentCategoryById_returnsStatus404() throws Exception {
        when(categoriaService.findById(1L)).thenThrow(new CategoriaNoEncontrada(1L));

        mockMvc.perform(get("/data/categoria/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void POSTCategory_createsCategoryAndReturns201() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Categoria A");

        when(categoriaService.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Categoria A");

        mockMvc.perform(post("/data/categoria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Categoria A"));
    }

    @Test
    void PUTCategory_updatesCategoryAndReturns200() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Categoria B");

        when(categoriaService.update(eq(1L), any(Categoria.class))).thenReturn(categoria);

        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Categoria B");

        mockMvc.perform(put("/data/categoria/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Categoria B"));
    }

    @Test
    void DELETECategory_returnsStatus204() throws Exception {
        mockMvc.perform(delete("/data/categoria/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void DELETENonExistentCategory_returnsStatus404() throws Exception {
        doThrow(new CategoriaNoEncontrada(1L)).when(categoriaService).delete(1L);

        mockMvc.perform(delete("/data/categoria/1"))
                .andExpect(status().isNotFound());
    }
}