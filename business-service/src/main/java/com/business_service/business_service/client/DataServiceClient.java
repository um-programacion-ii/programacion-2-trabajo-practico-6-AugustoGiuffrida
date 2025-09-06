package com.business_service.business_service.client;

import com.business_service.business_service.dto.CategoriaDTO;
import com.business_service.business_service.dto.InventarioDTO;
import com.business_service.business_service.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "data-service", url = "${data.service.url}")
public interface DataServiceClient {

     @GetMapping("/data/productos")
     List<ProductoDTO> findAllProducts();

     @GetMapping("/data/productos/{id}")
     ProductoDTO findProductById(@PathVariable Long id);

     @PostMapping("/data/productos")
     ProductoDTO saveProduct(@RequestBody ProductoDTO productoDTO);

     @PutMapping("/data/productos/{id}")
     ProductoDTO updateProduct(@PathVariable Long id, @RequestBody ProductoDTO productoDTO);

     @DeleteMapping("/data/productos/{id}")
     void deleteProduct(@PathVariable Long id);

     @GetMapping("/data/productos/categoria/{nombre}")
     List<ProductoDTO> findProductByCategory(@PathVariable String categoriaNombre);

     @GetMapping("/data/categorias")
     List<CategoriaDTO> findAllCategory();

     @GetMapping("/data/inventario/stock-bajo")
     List<InventarioDTO> findInventoriesWithLowStock()  ;
}
