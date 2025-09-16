package com.business_service.business_service.exception;

public class CategoriaNoEncontrada extends RuntimeException{
    public CategoriaNoEncontrada(Long id){
        super("No se encontre categoria con el id: "+id);
    }
}
