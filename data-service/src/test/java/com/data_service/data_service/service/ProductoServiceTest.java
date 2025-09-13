package com.data_service.data_service.service;

import com.data_service.data_service.entity.Producto;
import com.data_service.data_service.exception.ProductoNoEncontrado;
import com.data_service.data_service.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Test
    void saveProducto_success() {
        Producto producto = new Producto();
        producto.setNombre("Producto Test");

        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.save(producto);

        assertNotNull(resultado);
        assertEquals("Producto Test", resultado.getNombre());
        verify(productoRepository).save(producto);
    }

    @Test
    void updateProducto_success() {
        Producto producto = new Producto();
        producto.setNombre("Producto Actualizado");

        when(productoRepository.existsById(1L)).thenReturn(true);
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.update(1L, producto);

        assertNotNull(resultado);
        assertEquals("Producto Actualizado", resultado.getNombre());
        assertEquals(1L, resultado.getId());
        verify(productoRepository).save(producto);
    }

    @Test
    void updateProducto_notFound() {
        Producto producto = new Producto();

        when(productoRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductoNoEncontrado.class,
                () -> productoService.update(1L, producto));

        verify(productoRepository, never()).save(any());
    }

    @Test
    void deleteProducto_success() {
        when(productoRepository.existsById(1L)).thenReturn(true);

        productoService.delete(1L);

        verify(productoRepository).deleteById(1L);
    }

    @Test
    void deleteProducto_notFound() {
        when(productoRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductoNoEncontrado.class,
                () -> productoService.delete(1L));

        verify(productoRepository, never()).deleteById(any());
    }

    @Test
    void findProductoById_success() {
        Producto producto = new Producto();
        producto.setNombre("Producto Test");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.findById(1L);

        assertEquals(producto, resultado);
        verify(productoRepository).findById(1L);
    }

    @Test
    void findProductoById_notFound() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductoNoEncontrado.class,
                () -> productoService.findById(1L));

        verify(productoRepository).findById(1L);
    }

    @Test
    void findAllProductos_withResults() {
        Producto producto = new Producto();

        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.findAll();

        assertEquals(1, resultado.size());
        verify(productoRepository).findAll();
    }

    @Test
    void findAllProductos_emptyList() {
        when(productoRepository.findAll()).thenReturn(List.of());

        List<Producto> resultado = productoService.findAll();

        assertTrue(resultado.isEmpty());
        verify(productoRepository).findAll();
    }

    @Test
    void findByPrecioBetween_success() {
        Producto producto = new Producto();
        producto.setNombre("Producto Rango");

        when(productoRepository.findByPrecioBetween(BigDecimal.TEN, BigDecimal.valueOf(100)))
                .thenReturn(List.of(producto));

        List<Producto> resultado = productoService.findByPrecioBetween(BigDecimal.TEN, BigDecimal.valueOf(100));

        assertEquals(1, resultado.size());
        verify(productoRepository).findByPrecioBetween(BigDecimal.TEN, BigDecimal.valueOf(100));
    }

    @Test
    void findProductosWithLowStock_success() {
        Producto producto = new Producto();
        producto.setNombre("Producto Bajo Stock");

        when(productoRepository.findProductosWithLowStock()).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.findProductosWithLowStock();

        assertEquals(1, resultado.size());
        verify(productoRepository).findProductosWithLowStock();
    }

    @Test
    void findByCategoryName_success() {
        Producto producto = new Producto();
        producto.setNombre("Producto Categoria");

        when(productoRepository.findByCategoryName("Electrónica")).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.findByCategoryName("Electrónica");

        assertEquals(1, resultado.size());
        verify(productoRepository).findByCategoryName("Electrónica");
    }
}
