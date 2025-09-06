package com.data_service.data_service.service;

import com.data_service.data_service.entity.Inventario;
import com.data_service.data_service.exception.InventarioNoEncontrado;
import com.data_service.data_service.repository.InventarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService{
    private final InventarioRepository inventarioRepository;

    public InventarioServiceImpl(InventarioRepository inventarioRepository){
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public BigDecimal calculateTotalValue(){
        return inventarioRepository.calculateTotalValue();
    }

    @Override
    public Inventario save(Inventario inventario){
        return inventarioRepository.save(inventario);
    }

    @Override
    public Inventario update(Long id, Inventario inventario){
        if(!inventarioRepository.existsById(id)){
            throw new InventarioNoEncontrado(id);
        }
        inventario.setId(id);
        return inventarioRepository.save(inventario);
    }

    @Override
    public void delete(Long id){
        if(!inventarioRepository.existsById(id)){
            throw new InventarioNoEncontrado(id);
        }
        inventarioRepository.deleteById(id);
    }

    @Override
    public Inventario findById(Long id){
        return inventarioRepository.findById(id).orElseThrow(()-> new InventarioNoEncontrado(id));
    }

    @Override
    public List<Inventario> findAll(){
        return inventarioRepository.findAll();
    }

}
