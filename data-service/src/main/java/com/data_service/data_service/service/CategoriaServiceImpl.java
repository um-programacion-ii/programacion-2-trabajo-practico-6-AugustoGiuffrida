package com.data_service.data_service.service;

import com.data_service.data_service.entity.Categoria;
import com.data_service.data_service.exception.CategoriaNoEncontrada;
import com.data_service.data_service.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService{
    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public Categoria save(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria update(Long id, Categoria categoria){
        if(!categoriaRepository.existsById(id)){
            throw new CategoriaNoEncontrada(id);
        }
        categoria.setId(id);
        return categoriaRepository.save(categoria);
    }

    @Override
    public void delete(Long id){
        if (!categoriaRepository.existsById(id)){
            throw new CategoriaNoEncontrada(id);
        }
        categoriaRepository.deleteById(id);
    }

    @Override
    public Categoria findById(Long id){
        return categoriaRepository.findById(id).orElseThrow(()->new CategoriaNoEncontrada(id));
    }

    @Override
    public List<Categoria> findAll(){
        return categoriaRepository.findAll();
    }
}
