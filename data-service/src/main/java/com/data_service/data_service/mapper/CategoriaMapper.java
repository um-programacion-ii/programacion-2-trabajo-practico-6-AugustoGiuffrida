package com.data_service.data_service.mapper;

import com.data_service.data_service.dto.CategoriaDTO;
import com.data_service.data_service.entity.Categoria;

public class CategoriaMapper {

    public static CategoriaDTO toDTO(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }

    public static Categoria toEntity(CategoriaDTO dto) {
        if (dto == null) {
            return null;
        }
        Categoria categoria = new Categoria();
        categoria.setId(dto.getId());
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return categoria;
    }
}