package com.data_service.data_service.service;

import com.data_service.data_service.entity.Producto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {
    List<Producto> findByPrecioBetween(BigDecimal min, BigDecimal max);
    List<Producto> findProductosWithLowStock();
    List<Producto> findByCategoryName(String categoriaNombre);
    Producto save(Producto producto);
    Producto update(Producto producto, Long id);
    void delete(Long id);
    Producto findById(Long id);
    List<Producto>findAll();
}
