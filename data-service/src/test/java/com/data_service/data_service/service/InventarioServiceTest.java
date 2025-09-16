package com.data_service.data_service.service;

import com.data_service.data_service.entity.Inventario;
import com.data_service.data_service.exception.InventarioNoEncontrado;
import com.data_service.data_service.repository.InventarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    @Test
    void saveInventario_success() {
        Inventario inventario = new Inventario();
        inventario.setCantidad(10);

        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        Inventario resultado = inventarioService.save(inventario);

        assertNotNull(resultado);
        assertEquals(10, resultado.getCantidad());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void updateInventario_success() {
        Inventario inventario = new Inventario();
        inventario.setCantidad(20);

        when(inventarioRepository.existsById(1L)).thenReturn(true);
        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        Inventario resultado = inventarioService.update(1L, inventario);

        assertNotNull(resultado);
        assertEquals(20, resultado.getCantidad());
        assertEquals(1L, resultado.getId());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void updateInventario_notFound() {
        Inventario inventario = new Inventario();

        when(inventarioRepository.existsById(1L)).thenReturn(false);

        assertThrows(InventarioNoEncontrado.class,
                () -> inventarioService.update(1L, inventario));

        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void deleteInventario_success() {
        when(inventarioRepository.existsById(1L)).thenReturn(true);

        inventarioService.delete(1L);

        verify(inventarioRepository).deleteById(1L);
    }

    @Test
    void deleteInventario_notFound() {
        when(inventarioRepository.existsById(1L)).thenReturn(false);

        assertThrows(InventarioNoEncontrado.class,
                () -> inventarioService.delete(1L));

        verify(inventarioRepository, never()).deleteById(any());
    }

    @Test
    void findInventarioById_success() {
        Inventario inventario = new Inventario();
        inventario.setCantidad(50);

        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        Inventario resultado = inventarioService.findById(1L);

        assertEquals(inventario, resultado);
        verify(inventarioRepository).findById(1L);
    }

    @Test
    void findInventarioById_notFound() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InventarioNoEncontrado.class,
                () -> inventarioService.findById(1L));

        verify(inventarioRepository).findById(1L);
    }

    @Test
    void findAllInventarios_withResults() {
        Inventario inventario = new Inventario();

        when(inventarioRepository.findAll()).thenReturn(List.of(inventario));

        List<Inventario> resultado = inventarioService.findAll();

        assertEquals(1, resultado.size());
        verify(inventarioRepository).findAll();
    }

    @Test
    void findAllInventarios_emptyList() {
        when(inventarioRepository.findAll()).thenReturn(List.of());

        List<Inventario> resultado = inventarioService.findAll();

        assertTrue(resultado.isEmpty());
        verify(inventarioRepository).findAll();
    }

    @Test
    void calculateTotalValue_success() {
        BigDecimal total = new BigDecimal("1500.00");

        when(inventarioRepository.calculateTotalValue()).thenReturn(total);

        BigDecimal resultado = inventarioService.calculateTotalValue();

        assertNotNull(resultado);
        assertEquals(total, resultado);
        verify(inventarioRepository).calculateTotalValue();
    }

    @Test
    void calculateTotalValue_nullResult() {
        when(inventarioRepository.calculateTotalValue()).thenReturn(null);

        BigDecimal resultado = inventarioService.calculateTotalValue();

        assertNull(resultado);
        verify(inventarioRepository).calculateTotalValue();
    }
}
