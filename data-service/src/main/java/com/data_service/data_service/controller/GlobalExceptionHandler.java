package com.data_service.data_service.controller;

import com.data_service.data_service.exception.CategoriaNoEncontrada;
import com.data_service.data_service.exception.InventarioNoEncontrado;
import com.data_service.data_service.exception.ProductoNoEncontrado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductoNoEncontrado.class)
    public ResponseEntity<Map<String, Object>> handleProductoNoEncontrado(ProductoNoEncontrado ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "Producto no encontrado",
                        "detalle", ex.getMessage()
                ));
    }

    @ExceptionHandler(CategoriaNoEncontrada.class)
    public ResponseEntity<Map<String, Object>> handleCategoriaNoEncontrado(CategoriaNoEncontrada ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "Categoria no encontrada",
                        "detalle", ex.getMessage()
                ));
    }

    @ExceptionHandler(InventarioNoEncontrado.class)
    public ResponseEntity<Map<String, Object>> handleInventarioNoEncontrado(InventarioNoEncontrado ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "Inventario no encontrado",
                        "detalle", ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "Error interno del servidor",
                        "detalle", ex.getMessage()
                ));
    }

}
