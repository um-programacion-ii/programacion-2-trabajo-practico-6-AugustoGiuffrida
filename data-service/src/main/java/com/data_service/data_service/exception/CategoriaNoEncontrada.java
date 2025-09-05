package com.data_service.data_service.exception;

public class CategoriaNoEncontrada extends RuntimeException{
    public CategoriaNoEncontrada(Long id){
        super("No se encontre categoria con el id: "+id);
    }
}
