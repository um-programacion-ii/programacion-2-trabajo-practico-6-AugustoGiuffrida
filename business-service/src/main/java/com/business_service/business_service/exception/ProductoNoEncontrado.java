package com.business_service.business_service.exception;

public class ProductoNoEncontrado extends RuntimeException{
    public ProductoNoEncontrado(Long id){
        super("No se encontro producto con el id: "+id);
    }
}
