package com.data_service.data_service.repository;

import com.data_service.data_service.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByPrecioBetween(BigDecimal min, BigDecimal max);

    @Query("SELECT p FROM Producto p JOIN p.inventario i WHERE i.cantidad < i.stockMinimo")
    List<Producto> findProductosWithLowStock();
    @Query("SELECT p FROM Producto p WHERE p.categoria.nombre = :categoriaNombre")
    List<Producto> findByCategoryName(@Param("categoriaNombre") String categoriaNombre);

}
