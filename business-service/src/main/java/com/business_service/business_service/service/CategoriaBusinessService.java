package com.business_service.business_service.service;

import com.business_service.business_service.client.DataServiceClient;
import com.business_service.business_service.dto.CategoriaDTO;
import com.business_service.business_service.dto.ProductoDTO;
import com.business_service.business_service.dto.ProductoRequest;
import com.business_service.business_service.exception.MicroserviceCommunicationException;
import com.business_service.business_service.exception.ProductoNoEncontrado;
import com.business_service.business_service.exception.ValidacionNegocioException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class CategoriaBusinessService {
    private final DataServiceClient dataServiceClient;

    public CategoriaBusinessService(DataServiceClient dataServiceClient){
        this.dataServiceClient = dataServiceClient;
    }

    public List<CategoriaDTO> findAllCategory(){
        try {
            return dataServiceClient.findAllCategory();
        } catch (FeignException e){
            log.error("Error al obtener categoría del microservicio de datos", e);
            throw new MicroserviceCommunicationException();
        }
    }

    public CategoriaDTO updateCategory(Long id, CategoriaDTO categoriaDTO){
        try {
            return dataServiceClient.updateCategory(id, categoriaDTO);
        } catch (FeignException e){
            log.error("Error al actualizar categoría del microservicio de datos", e);
            throw new MicroserviceCommunicationException();
        }
    }

    public void deleteCategory(Long id){
        try {
            dataServiceClient.deleteCategory(id);
        } catch (FeignException e){
            log.error("Error al eliminar categoría del microservicio de datos", e);
            throw new MicroserviceCommunicationException();
        }
    }

}
