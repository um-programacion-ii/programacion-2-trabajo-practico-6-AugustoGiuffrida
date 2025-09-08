package com.data_service.data_service.mapper;

import com.data_service.data_service.dto.InventarioDTO;
import com.data_service.data_service.entity.Inventario;

public class InventarioMapper {

    public static InventarioDTO toDTO(Inventario inventario) {
        if (inventario == null) {
            return null;
        }
        return new InventarioDTO(
                inventario.getId(),
                inventario.getCantidad(),
                inventario.getStockMinimo(),
                inventario.getFechaActualizacion()
        );
    }

    public static Inventario toEntity(InventarioDTO dto) {
        if (dto == null) {
            return null;
        }
        Inventario inventario = new Inventario();
        inventario.setId(dto.getId());
        inventario.setCantidad(dto.getCantidad());
        inventario.setStockMinimo(dto.getStockMinimo());
        inventario.setFechaActualizacion(dto.getFechaActualizacion());
        return inventario;
    }
}
