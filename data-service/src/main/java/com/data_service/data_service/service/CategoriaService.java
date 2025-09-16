package com.data_service.data_service.service;

import com.data_service.data_service.entity.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    Categoria save(Categoria categoria);
    Categoria update(Long id, Categoria categoria);
    void delete(Long id);
    Categoria findById(Long id);
    List<Categoria> findAll();
}
