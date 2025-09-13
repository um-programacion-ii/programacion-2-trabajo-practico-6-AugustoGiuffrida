package com.business_service.business_service.service;

import com.business_service.business_service.client.DataServiceClient;
import com.business_service.business_service.dto.ProductoDTO;
import com.business_service.business_service.dto.ProductoRequest;
import com.business_service.business_service.exception.MicroserviceCommunicationException;
import com.business_service.business_service.exception.ProductoNoEncontrado;
import com.business_service.business_service.exception.ValidacionNegocioException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class ProductoBusinessService {
    private final DataServiceClient dataServiceClient;

    public ProductoBusinessService(DataServiceClient dataServiceClient){
        this.dataServiceClient = dataServiceClient;
    }

    public List<ProductoDTO> findAllProducts(){
        try {
            return dataServiceClient.findAllProducts();
        }catch (FeignException e){
            log.error("Error al obtener producto del microservicio de datos", e);
            throw new MicroserviceCommunicationException();
        }
    }

    public List<ProductoDTO> findProductByCategory(String categoriaNombre){
        try {
            return dataServiceClient.findProductByCategory(categoriaNombre);
        }catch (FeignException e){
            log.error("Error al obtener el producto del microservicio de datos", e);
            throw new MicroserviceCommunicationException();
        }
    }

    public ProductoDTO findProductById(Long id){
        try {
            return dataServiceClient.findProductById(id);
        } catch (FeignException.NotFound e) {
            throw  new ProductoNoEncontrado(id);
        } catch (FeignException e){
            log.error("Error al obtener el producto del microservicio de datos", e);
            throw new MicroserviceCommunicationException();
        }
    }

    public ProductoDTO saveProduct(ProductoRequest productoRequest){
        validateProduct(productoRequest);
        try {
            return dataServiceClient.saveProduct(productoRequest);
        } catch (FeignException e) {
            log.error("Error al guardar el producto del microservicio de datos", e);
            throw new MicroserviceCommunicationException();        }
    }

    public ProductoDTO updateProduct(Long id, ProductoRequest productoRequest){
        validateProduct(productoRequest);
        try {
            return dataServiceClient.updateProduct(id, productoRequest);
        } catch (FeignException e) {
            log.error("Error al actualizar el producto del microservicio de datos", e);
            throw new MicroserviceCommunicationException();        }
    }

    public void deleteProduct(Long id){
        try {
            dataServiceClient.deleteProduct(id);
        }catch (FeignException.NotFound e) {
            throw new ProductoNoEncontrado(id);
        }catch (FeignException e) {
            log.error("Error al eliminar el producto del microservicio de datos", e);
            throw new MicroserviceCommunicationException();        }
    }

    private void validateProduct(ProductoRequest productoRequest){
        if(productoRequest.getPrecio().compareTo(BigDecimal.ZERO) <=0){
            throw new ValidacionNegocioException("El precio debe ser mayor a cero");
        }
        if(productoRequest.getStock()<0){
            throw new ValidacionNegocioException("El stock no puede ser negativo");
        }
    }
}
