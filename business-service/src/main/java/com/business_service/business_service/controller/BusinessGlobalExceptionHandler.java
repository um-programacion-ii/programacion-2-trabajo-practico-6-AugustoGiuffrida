package com.business_service.business_service.controller;

import com.business_service.business_service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@RestControllerAdvice
public class BusinessGlobalExceptionHandler {

    @ExceptionHandler(ProductoNoEncontrado.class)
    public ResponseEntity<Map<String, Object>> handleProductoNoEncontrado(ProductoNoEncontrado ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "Producto no encontrado",
                        "detalle", ex.getMessage()
                ));
    }

    @ExceptionHandler(CategoriaNoEncontrada.class)
    public ResponseEntity<Map<String, Object>> handleCategoriaNoEncontrada(CategoriaNoEncontrada ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "Categoría no encontrada",
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

    @ExceptionHandler(MicroserviceCommunicationException.class)
    public ResponseEntity<Map<String, Object>> handleMicroserviceCommunication(MicroserviceCommunicationException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "error", "Error de comunicación con el servicio de datos",
                        "detalle", ex.getMessage()
                ));
    }

    @ExceptionHandler(ValidacionNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleValidacionNegocio(ValidacionNegocioException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Error de validación de negocio",
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
