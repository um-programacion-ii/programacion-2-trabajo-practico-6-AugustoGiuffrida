package com.data_service.data_service.controller;

import com.data_service.data_service.dto.CategoriaDTO;
import com.data_service.data_service.dto.InventarioDTO;
import com.data_service.data_service.dto.ProductoDTO;
import com.data_service.data_service.dto.ProductoRequest;
import com.data_service.data_service.entity.Categoria;
import com.data_service.data_service.entity.Inventario;
import com.data_service.data_service.entity.Producto;
import com.data_service.data_service.mapper.CategoriaMapper;
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

    /**
     * Obtiene la lista de todos los productos.
     * @return Lista de ProductoDTO
     */
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> findAllProducts() {
        List<ProductoDTO> productos = productoService.findAll()
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtiene un producto por su ID.
     * @param id ID del producto
     * @return ProductoDTO correspondiente o NOT_FOUND si no existe
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoDTO> findProductById(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        return ResponseEntity.ok(ProductoMapper.toDTO(producto));
    }

    /**
     * Obtiene productos cuyo precio se encuentra entre min y max.
     * @param min Precio mínimo
     * @param max Precio máximo
     * @return Lista de ProductoDTO
     */
    @GetMapping("/productos/rango-precio")
    public ResponseEntity<List<ProductoDTO>> findByPrecioBetween(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        List<ProductoDTO> productos = productoService.findByPrecioBetween(min, max)
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtiene productos con stock bajo.
     * @return Lista de ProductoDTO con stock bajo
     */
    @GetMapping("/productos/stock-bajo")
    public ResponseEntity<List<ProductoDTO>> findProductosWithLowStock() {
        List<ProductoDTO> productos = productoService.findProductosWithLowStock()
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtiene productos filtrados por nombre de categoría.
     * @param nombre Nombre de la categoría
     * @return Lista de ProductoDTO
     */
    @GetMapping("/productos/categoria/{nombre}")
    public ResponseEntity<List<ProductoDTO>> findByCategoryName(@PathVariable String nombre) {
        List<ProductoDTO> productos = productoService.findByCategoryName(nombre)
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    /**
     * Crea un nuevo producto.
     * @param request Datos del producto a crear
     * @return ProductoDTO creado
     */
    @PostMapping("/productos")
    public ResponseEntity<ProductoDTO> saveProduct(@RequestBody ProductoRequest request) {
        Categoria categoria = categoriaService.findById(request.getCategoriaId());
        Producto producto = ProductoMapper.toEntity(request, categoria);
        Producto saved = productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoMapper.toDTO(saved));
    }

    /**
     * Actualiza un producto existente.
     * @param id ID del producto a actualizar
     * @param request Datos actualizados del producto
     * @return ProductoDTO actualizado o NOT_FOUND si no existe
     */
    @PutMapping("/productos/{id}")
    public ResponseEntity<ProductoDTO> updateProduct(@PathVariable Long id, @RequestBody ProductoRequest request) {
        Categoria categoria = categoriaService.findById(request.getCategoriaId());
        Producto producto = ProductoMapper.toEntity(request, categoria);
        Producto updated = productoService.update(id, producto);
        return ResponseEntity.ok(ProductoMapper.toDTO(updated));
    }


    /**
     * Elimina un producto por su ID.
     * @param id ID del producto a eliminar
     * @return NO_CONTENT si se eliminó correctamente
     */
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //---Inventario---//

    /**
     * Obtiene todos los inventarios.
     * @return Lista de InventarioDTO
     */
    @GetMapping("/inventario")
    public ResponseEntity<List<InventarioDTO>> findAllInventories(){
        List<InventarioDTO> inventories= inventarioService.findAll()
                .stream()
                .map(InventarioMapper::toDTO)
                .toList();
        return ResponseEntity.ok(inventories);
    }

    /**
     * Obtiene un inventario por su ID.
     * @param id ID del inventario
     * @return InventarioDTO o NOT_FOUND si no existe
     */
    @GetMapping("/inventario/{id}")
    public ResponseEntity<InventarioDTO> findInventoryById(@PathVariable Long id){
        Inventario inventory = inventarioService.findById(id);
        return ResponseEntity.ok(InventarioMapper.toDTO(inventory));
    }

    /**
     * Calcula el valor total de todos los inventarios.
     * @return BigDecimal con valor total
     */
    @GetMapping("/inventario/valor")
    public ResponseEntity<BigDecimal> calculateTotalValue(){
        return ResponseEntity.ok(inventarioService.calculateTotalValue());
    }

    /**
     * Crea un nuevo inventario.
     * @param dto Datos del inventario
     * @return InventarioDTO creado
     */
    @PostMapping("/inventario")
    public ResponseEntity<InventarioDTO> saveInventory(@RequestBody InventarioDTO dto){
        Inventario saved = inventarioService.save(InventarioMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(InventarioMapper.toDTO(saved));
    }

    /**
     * Actualiza un inventario existente.
     * @param id ID del inventario
     * @param dto Datos actualizados
     * @return InventarioDTO actualizado
     */
    @PutMapping("/inventario/{id}")
    public ResponseEntity<InventarioDTO> updateInventory(@PathVariable Long id, @RequestBody InventarioDTO dto){
        Inventario updated = inventarioService.update(id, InventarioMapper.toEntity(dto));
        return ResponseEntity.ok(InventarioMapper.toDTO(updated));
    }

    /**
     * Elimina un inventario por su ID.
     * @param id ID del inventario a eliminar
     * @return NO_CONTENT si se eliminó correctamente
     */
    @DeleteMapping("/inventario/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id){
        inventarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //---Categoria---//

    /**
     * Obtiene todas las categorías.
     * @return Lista de CategoriaDTO
     */
    @GetMapping("/categoria")
    public ResponseEntity<List<CategoriaDTO>> findAllCategories() {
        List<CategoriaDTO> categorias = categoriaService.findAll()
                .stream()
                .map(CategoriaMapper::toDTO)
                .toList();
        return ResponseEntity.ok(categorias);
    }

    /**
     * Obtiene una categoría por su ID.
     * @param id ID de la categoría
     * @return CategoriaDTO o NOT_FOUND si no existe
     */
    @GetMapping("/categoria/{id}")
    public ResponseEntity<CategoriaDTO> findCategoryById(@PathVariable Long id) {
        Categoria category = categoriaService.findById(id);
        return ResponseEntity.ok(CategoriaMapper.toDTO(category));
    }

    /**
     * Crea una nueva categoría.
     * @param dto Datos de la categoría
     * @return CategoriaDTO creada
     */
    @PostMapping("/categoria")
    public ResponseEntity<CategoriaDTO> saveCategory(@RequestBody CategoriaDTO dto) {
        Categoria saved = categoriaService.save(CategoriaMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaMapper.toDTO(saved));
    }


    /**
     * Actualiza una categoría existente.
     * @param id ID de la categoría
     * @param dto Datos actualizados de la categoría
     * @return CategoriaDTO actualizada
     */
    @PutMapping("/categoria/{id}")
    public ResponseEntity<CategoriaDTO> updateCategory(@PathVariable Long id, @RequestBody CategoriaDTO dto) {
        Categoria updated = categoriaService.update(id, CategoriaMapper.toEntity(dto));
        return ResponseEntity.ok(CategoriaMapper.toDTO(updated));
    }

    /**
     * Elimina una categoría por su ID.
     * @param id ID de la categoría a eliminar
     * @return NO_CONTENT si se eliminó correctamente
     */
    @DeleteMapping("/categoria/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
