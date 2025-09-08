package com.data_service.data_service.controller;

import com.data_service.data_service.dto.InventarioDTO;
import com.data_service.data_service.dto.ProductoDTO;
import com.data_service.data_service.dto.ProductoRequest;
import com.data_service.data_service.entity.Categoria;
import com.data_service.data_service.entity.Inventario;
import com.data_service.data_service.entity.Producto;
import com.data_service.data_service.mapper.InventarioMapper;
import com.data_service.data_service.mapper.ProductoMapper;
import com.data_service.data_service.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<ProductoDTO>> findAllProducts() {
        List<ProductoDTO> productos = productoService.findAll()
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoDTO> findProductById(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        return ResponseEntity.ok(ProductoMapper.toDTO(producto));
    }

    @GetMapping("/productos/rango-precio")
    public ResponseEntity<List<ProductoDTO>> findByPrecioBetween(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        List<ProductoDTO> productos = productoService.findByPrecioBetween(min, max)
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/productos/stock-bajo")
    public ResponseEntity<List<ProductoDTO>> findProductosWithLowStock() {
        List<ProductoDTO> productos = productoService.findProductosWithLowStock()
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/productos/categoria/{nombre}")
    public ResponseEntity<List<ProductoDTO>> findByCategoryName(@PathVariable String nombre) {
        List<ProductoDTO> productos = productoService.findByCategoryName(nombre)
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    @PostMapping("/productos")
    public ResponseEntity<ProductoDTO> saveProduct(@RequestBody ProductoRequest request) {
        Categoria categoria = categoriaService.findById(request.getCategoriaId());
        Producto producto = ProductoMapper.toEntity(request, categoria);
        Producto saved = productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoMapper.toDTO(saved));
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<ProductoDTO> updateProduct(@PathVariable Long id, @RequestBody ProductoRequest request) {
        Categoria categoria = categoriaService.findById(request.getCategoriaId());
        Producto producto = ProductoMapper.toEntity(request, categoria);
        Producto updated = productoService.update(id, producto);
        return ResponseEntity.ok(ProductoMapper.toDTO(updated));
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //---Inventario---//
    @GetMapping("/inventario")
    public ResponseEntity<List<InventarioDTO>> findAllInventories(){
        List<InventarioDTO> inventories= inventarioService.findAll()
                .stream()
                .map(InventarioMapper::toDTO)
                .toList();
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/inventario/{id}")
    public ResponseEntity<InventarioDTO> findInventoryById(@PathVariable Long id){
        Inventario inventory = inventarioService.findById(id);
        return ResponseEntity.ok(InventarioMapper.toDTO(inventory));
    }

    @GetMapping("/inventario/valor")
    public ResponseEntity<BigDecimal> calculateTotalValue(){
        return ResponseEntity.ok(inventarioService.calculateTotalValue());
    }

    @PostMapping("/inventario")
    public ResponseEntity<InventarioDTO> saveInventory(@RequestBody InventarioDTO dto){
        Inventario saved = inventarioService.save(InventarioMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(InventarioMapper.toDTO(saved));
    }

    @PutMapping("/inventario/{id}")
    public ResponseEntity<InventarioDTO> updateInventory(@PathVariable Long id, @RequestBody InventarioDTO dto){
        Inventario updated = inventarioService.update(id, InventarioMapper.toEntity(dto));
        return ResponseEntity.ok(InventarioMapper.toDTO(updated));
    }

    @DeleteMapping("/inventario/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id){
        inventarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //---Categoria---//
}
