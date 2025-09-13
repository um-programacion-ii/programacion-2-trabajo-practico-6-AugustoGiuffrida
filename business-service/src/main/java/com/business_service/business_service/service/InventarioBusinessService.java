package com.business_service.business_service.service;

import com.business_service.business_service.client.DataServiceClient;
import com.business_service.business_service.dto.CategoriaDTO;
import com.business_service.business_service.dto.InventarioDTO;
import com.business_service.business_service.dto.ProductoDTO;
import com.business_service.business_service.exception.InventarioNoEncontrado;
import com.business_service.business_service.exception.MicroserviceCommunicationException;
import com.business_service.business_service.exception.ProductoNoEncontrado;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InventarioBusinessService {
    private final DataServiceClient dataServiceClient;

    public InventarioBusinessService(DataServiceClient dataServiceClient){
        this.dataServiceClient = dataServiceClient;
    }

    public List<InventarioDTO> findInventoriesWithLowStock(){
        try {
            return dataServiceClient.findInventoriesWithLowStock();
        }catch (FeignException e){
            log.error("Error al obtener inventario del microservicio de datos", e);
            throw new MicroserviceCommunicationException();
        }
    }

    public InventarioDTO findInventoriesByProductId(Long productoId){
        try {
            return dataServiceClient.findInventoriesByProductId(productoId);
        }catch (FeignException.NotFound e) {
            throw  new InventarioNoEncontrado(productoId);
        }
        catch (FeignException e){
            log.error("Error al actualizar el inventario del microservicio de datos", e);
            throw new MicroserviceCommunicationException();
        }
    }

    public InventarioDTO updateInventory(Long id, Integer cantidad){
        try {
            return dataServiceClient.updateInventory(id, cantidad);
        } catch (FeignException e){
            log.error("Error al actualizar el inventario del microservicio de datos", e);
            throw new MicroserviceCommunicationException();
        }
    }
}
