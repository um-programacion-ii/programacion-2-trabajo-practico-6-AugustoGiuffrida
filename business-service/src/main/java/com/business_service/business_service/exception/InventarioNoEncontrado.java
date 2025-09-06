package com.business_service.business_service.exception;

public class InventarioNoEncontrado extends RuntimeException{
    public InventarioNoEncontrado(Long id){
        super("No se encontro el inventario con el id: "+id);
    }
}
