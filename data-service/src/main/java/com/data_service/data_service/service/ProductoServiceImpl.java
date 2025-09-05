package com.data_service.data_service.service;

import com.data_service.data_service.entity.Producto;
import com.data_service.data_service.exception.ProductoNoEncontrado;
import com.data_service.data_service.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService{
    private ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository){
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> findByPrecioBetween(BigDecimal min, BigDecimal max){
        return productoRepository.findByPrecioBetween(min, max);
    }

    @Override
    public List<Producto> findProductosWithLowStock(){
        return productoRepository.findProductosWithLowStock();
    }

    @Override
    public List<Producto> findByCategoryName(String categoriaNombre){
        return productoRepository.findByCategoryName(categoriaNombre);
    }

    @Override
    public Producto save(Producto producto){
        return productoRepository.save(producto);
    }

    @Override
    public Producto update(Long id, Producto producto){
        if(!productoRepository.existsById(id)){
            throw new ProductoNoEncontrado(id);
        }
        producto.setId(id);
        return productoRepository.save(producto);
    }

    @Override
    public void delete(Long id){
        if(!productoRepository.existsById(id)){
            throw new ProductoNoEncontrado(id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    public  Producto findById(Long id){
        return productoRepository.findById(id).orElseThrow(()-> new ProductoNoEncontrado(id));
    }

    @Override
    public List<Producto> findAll(){
        return productoRepository.findAll();
    }
}
