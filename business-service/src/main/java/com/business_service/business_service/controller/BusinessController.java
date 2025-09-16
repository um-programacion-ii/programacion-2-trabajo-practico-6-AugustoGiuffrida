package com.business_service.business_service.controller;

import com.business_service.business_service.dto.CategoriaDTO;
import com.business_service.business_service.dto.InventarioDTO;
import com.business_service.business_service.dto.ProductoDTO;
import com.business_service.business_service.dto.ProductoRequest;
import com.business_service.business_service.service.CategoriaBusinessService;
import com.business_service.business_service.service.InventarioBusinessService;
import com.business_service.business_service.service.ProductoBusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
public class BusinessController {

    private final ProductoBusinessService productoBusinessService;
    private final CategoriaBusinessService categoriaBusinessService;
    private final InventarioBusinessService inventarioBusinessService;

    public BusinessController(ProductoBusinessService productoBusinessService,
                              CategoriaBusinessService categoriaBusinessService,
                              InventarioBusinessService inventarioBusinessService) {
        this.productoBusinessService = productoBusinessService;
        this.categoriaBusinessService = categoriaBusinessService;
        this.inventarioBusinessService = inventarioBusinessService;
    }


    @GetMapping("/productos")
    public ResponseEntity<List<ProductoDTO>> findAllProducts() {
        return ResponseEntity.ok(productoBusinessService.findAllProducts());
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoDTO> findProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productoBusinessService.findProductById(id));
    }

    @GetMapping("/productos/categoria/{nombre}")
    public ResponseEntity<List<ProductoDTO>> findProductByCategory(@PathVariable String name) {
        return ResponseEntity.ok(productoBusinessService.findProductByCategory(name));
    }

    @PostMapping("/productos")
    public ResponseEntity<ProductoDTO> saveProduct(@RequestBody ProductoRequest request) {
        ProductoDTO newProduct = productoBusinessService.saveProduct(request);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<ProductoDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoBusinessService.updateProduct(id, request));
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productoBusinessService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaDTO>> findAllCategory() {
        return ResponseEntity.ok(categoriaBusinessService.findAllCategory());
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<CategoriaDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoriaDTO categoriaDTO) {
        return ResponseEntity.ok(categoriaBusinessService.updateCategory(id, categoriaDTO));
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoriaBusinessService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/inventario/low-stock")
    public ResponseEntity<List<InventarioDTO>> findInventoriesWithLowStock() {
        return ResponseEntity.ok(inventarioBusinessService.findInventoriesWithLowStock());
    }

    @GetMapping("/inventario/{productoId}")
    public ResponseEntity<InventarioDTO> findInventoriesByProductId(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioBusinessService.findInventoriesByProductId(productoId));
    }

    @GetMapping("/inventario/{productoId}/disponibilidad")
    public ResponseEntity<Map<String, Boolean>> checkAvailability(
            @PathVariable Long productId,
            @RequestParam Integer cantidad) {
        boolean available = inventarioBusinessService.findInventoriesByProductId(productId).getCantidad() >= cantidad;
        return ResponseEntity.ok(Collections.singletonMap("available", available));
    }

    @GetMapping("/inventario/valor")
    public ResponseEntity<Map<String, BigDecimal>> getTotalInventoryValue() {
        BigDecimal totalValue = productoBusinessService.findAllProducts().stream()
                .map(p -> p.getPrecio().multiply(BigDecimal.valueOf(p.getInventario().getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ResponseEntity.ok(Collections.singletonMap("totalValue", totalValue));
    }
}
