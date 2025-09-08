package com.data_service.data_service.mapper;

import com.data_service.data_service.dto.InventarioDTO;
import com.data_service.data_service.entity.Inventario;

public class InventarioMapper {

    public static InventarioDTO toDTO(Inventario inventario) {
        if (inventario == null) {
            return null;
        }

        InventarioDTO dto = new InventarioDTO();
        dto.setId(inventario.getId());
        dto.setCantidad(inventario.getCantidad());
        dto.setStockMinimo(inventario.getStockMinimo());
        dto.setFechaActualizacion(inventario.getFechaActualizacion());

        return dto;
    }

    // Convierte DTO a entidad
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
