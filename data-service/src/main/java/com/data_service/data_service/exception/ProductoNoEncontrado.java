package com.data_service.data_service.exception;

public class ProductoNoEncontrado extends RuntimeException{
    public ProductoNoEncontrado(Long id){
        super("No se encontro producto con el id: "+id);
    }
}
