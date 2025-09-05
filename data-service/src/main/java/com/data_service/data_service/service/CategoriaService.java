package com.data_service.data_service.service;

import com.data_service.data_service.entity.Categoria;

import java.util.List;

public interface CategoriaService {
    Categoria save(Categoria categoria);
    Categoria update(Categoria categoria, Long id);
    void delete(Long id);
    Categoria findById(Long id);
    List<Categoria> findAll();
}
