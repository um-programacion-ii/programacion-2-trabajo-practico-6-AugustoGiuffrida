package com.data_service.data_service.controller;

import com.data_service.data_service.dto.ProductoDTO;
import com.data_service.data_service.dto.ProductoRequest;
import com.data_service.data_service.entity.Categoria;
import com.data_service.data_service.entity.Producto;
import com.data_service.data_service.mapper.ProductoMapper;
import com.data_service.data_service.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {
    private final ProductoService productoService;
    private final InventarioService inventarioService;
    private final CategoriaService categoriaService;

    public DataController(ProductoService productoService, InventarioService inventarioService, CategoriaService categoriaService){
        this.productoService = productoService;
        this.inventarioService = inventarioService;
        this.categoriaService = categoriaService;
    }

    //---Producto---//
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> findAll() {
        List<ProductoDTO> productos = productoService.findAll()
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> findById(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        return ResponseEntity.ok(ProductoMapper.toDTO(producto));
    }

    @GetMapping("/rango-precio")
    public ResponseEntity<List<ProductoDTO>> findByPrecioBetween(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        List<ProductoDTO> productos = productoService.findByPrecioBetween(min, max)
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<ProductoDTO>> findProductosWithLowStock() {
        List<ProductoDTO> productos = productoService.findProductosWithLowStock()
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/categoria/{nombre}")
    public ResponseEntity<List<ProductoDTO>> findByCategoryName(@PathVariable String nombre) {
        List<ProductoDTO> productos = productoService.findByCategoryName(nombre)
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> save(@RequestBody ProductoRequest request) {
        Categoria categoria = categoriaService.findById(request.getCategoriaId());
        Producto producto = ProductoMapper.toEntity(request, categoria);
        Producto saved = productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> update(@PathVariable Long id, @RequestBody ProductoRequest request) {
        Categoria categoria = categoriaService.findById(request.getCategoriaId());
        Producto producto = ProductoMapper.toEntity(request, categoria);
        Producto updated = productoService.update(id, producto);
        return ResponseEntity.ok(ProductoMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
