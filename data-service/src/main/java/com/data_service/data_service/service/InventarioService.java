package com.data_service.data_service.service;

import com.data_service.data_service.entity.Inventario;

import java.math.BigDecimal;
import java.util.List;

public interface InventarioService {
    BigDecimal calculateTotalValue();
    Inventario save(Inventario inventario);
    Inventario update(Long id, Inventario inventario);
    void delete(Long id);
    Inventario findById(Long id);
    List<Inventario> findAll();
}
