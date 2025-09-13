package com.data_service.data_service.mapper;

import com.data_service.data_service.dto.ProductoDTO;
import com.data_service.data_service.dto.ProductoRequest;
import com.data_service.data_service.entity.Categoria;
import com.data_service.data_service.entity.Inventario;
import com.data_service.data_service.entity.Producto;

public class ProductoMapper {

    public static ProductoDTO toDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());

        if (producto.getCategoria() != null) {
            dto.setCategoriaNombre(producto.getCategoria().getNombre());
        }

        if (producto.getInventario() != null) {
            dto.setStockBajo(producto.getInventario().getCantidad() < 5);
        }
        return dto;
    }

    public static Producto toEntity(ProductoRequest request, Categoria categoria) {
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setCategoria(categoria);

        Inventario inventario = new Inventario();
        inventario.setCantidad(request.getStock());
        inventario.setProducto(producto);
        producto.setInventario(inventario);

        return producto;
    }
}

