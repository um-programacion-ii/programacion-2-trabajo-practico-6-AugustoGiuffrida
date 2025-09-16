package com.data_service.data_service.service;

import com.data_service.data_service.entity.Categoria;
import com.data_service.data_service.exception.CategoriaNoEncontrada;
import com.data_service.data_service.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @Test
    void saveCategoria_success() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Categoria Test");

        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria resultado = categoriaService.save(categoria);

        assertNotNull(resultado);
        assertEquals("Categoria Test", resultado.getNombre());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void updateCategoria_success() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Categoria Actualizada");

        when(categoriaRepository.existsById(1L)).thenReturn(true);
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria resultado = categoriaService.update(1L, categoria);

        assertNotNull(resultado);
        assertEquals("Categoria Actualizada", resultado.getNombre());
        assertEquals(1L, resultado.getId());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void updateCategoria_notFound() {
        Categoria categoria = new Categoria();

        when(categoriaRepository.existsById(1L)).thenReturn(false);

        assertThrows(CategoriaNoEncontrada.class,
                () -> categoriaService.update(1L, categoria));

        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void deleteCategoria_success() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);

        categoriaService.delete(1L);

        verify(categoriaRepository).deleteById(1L);
    }

    @Test
    void deleteCategoria_notFound() {
        when(categoriaRepository.existsById(1L)).thenReturn(false);

        assertThrows(CategoriaNoEncontrada.class,
                () -> categoriaService.delete(1L));

        verify(categoriaRepository, never()).deleteById(any());
    }

    @Test
    void findCategoriaById_success() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Categoria Test");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.findById(1L);

        assertEquals(categoria, resultado);
        verify(categoriaRepository).findById(1L);
    }

    @Test
    void findCategoriaById_notFound() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoriaNoEncontrada.class,
                () -> categoriaService.findById(1L));

        verify(categoriaRepository).findById(1L);
    }

    @Test
    void findAllCategorias_withResults() {
        Categoria categoria = new Categoria();

        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<Categoria> resultado = categoriaService.findAll();

        assertEquals(1, resultado.size());
        verify(categoriaRepository).findAll();
    }

    @Test
    void findAllCategorias_emptyList() {
        when(categoriaRepository.findAll()).thenReturn(List.of());

        List<Categoria> resultado = categoriaService.findAll();

        assertTrue(resultado.isEmpty());
        verify(categoriaRepository).findAll();
    }
}
