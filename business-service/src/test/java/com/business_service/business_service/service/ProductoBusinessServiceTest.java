package com.business_service.business_service.service;

import com.business_service.business_service.client.DataServiceClient;
import com.business_service.business_service.dto.ProductoDTO;
import com.business_service.business_service.dto.ProductoRequest;
import com.business_service.business_service.exception.MicroserviceCommunicationException;
import com.business_service.business_service.exception.ProductoNoEncontrado;
import com.business_service.business_service.exception.ValidacionNegocioException;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoBusinessServiceTest {

    @Mock
    private DataServiceClient dataServiceClient;

    @InjectMocks
    private ProductoBusinessService productoBusinessService;

    @Test
    void findAllProducts_success() {
        ProductoDTO dto = new ProductoDTO();
        when(dataServiceClient.findAllProducts()).thenReturn(List.of(dto));

        List<ProductoDTO> result = productoBusinessService.findAllProducts();

        assertEquals(1, result.size());
        verify(dataServiceClient).findAllProducts();
    }

    @Test
    void findAllProducts_communicationError() {
        when(dataServiceClient.findAllProducts()).thenThrow(FeignException.FeignServerException.class);

        assertThrows(MicroserviceCommunicationException.class,
                () -> productoBusinessService.findAllProducts());
    }

    @Test
    void findProductById_success() {
        ProductoDTO dto = new ProductoDTO();
        when(dataServiceClient.findProductById(1L)).thenReturn(dto);

        ProductoDTO result = productoBusinessService.findProductById(1L);

        assertEquals(dto, result);
        verify(dataServiceClient).findProductById(1L);
    }

    @Test
    void findProductById_notFound() {
        when(dataServiceClient.findProductById(1L)).thenThrow(FeignException.NotFound.class);

        assertThrows(ProductoNoEncontrado.class,
                () -> productoBusinessService.findProductById(1L));
    }

    @Test
    void saveProduct_success() {
        ProductoRequest request = new ProductoRequest();
        request.setPrecio(BigDecimal.TEN);
        request.setStock(10);

        ProductoDTO dto = new ProductoDTO();
        when(dataServiceClient.saveProduct(request)).thenReturn(dto);

        ProductoDTO result = productoBusinessService.saveProduct(request);

        assertEquals(dto, result);
        verify(dataServiceClient).saveProduct(request);
    }

    @Test
    void saveProduct_invalidPrice() {
        ProductoRequest request = new ProductoRequest();
        request.setPrecio(BigDecimal.ZERO);
        request.setStock(10);

        assertThrows(ValidacionNegocioException.class,
                () -> productoBusinessService.saveProduct(request));
    }

    @Test
    void saveProduct_invalidStock() {
        ProductoRequest request = new ProductoRequest();
        request.setPrecio(BigDecimal.TEN);
        request.setStock(-5);

        assertThrows(ValidacionNegocioException.class,
                () -> productoBusinessService.saveProduct(request));
    }

    @Test
    void saveProduct_communicationError() {
        ProductoRequest request = new ProductoRequest();
        request.setPrecio(BigDecimal.TEN);
        request.setStock(5);

        when(dataServiceClient.saveProduct(request)).thenThrow(FeignException.FeignServerException.class);

        assertThrows(MicroserviceCommunicationException.class,
                () -> productoBusinessService.saveProduct(request));
    }

    @Test
    void updateProduct_success() {
        ProductoRequest request = new ProductoRequest();
        request.setPrecio(BigDecimal.TEN);
        request.setStock(5);

        ProductoDTO dto = new ProductoDTO();
        when(dataServiceClient.updateProduct(1L, request)).thenReturn(dto);

        ProductoDTO result = productoBusinessService.updateProduct(1L, request);

        assertEquals(dto, result);
        verify(dataServiceClient).updateProduct(1L, request);
    }

    @Test
    void updateProduct_invalidPrice() {
        ProductoRequest request = new ProductoRequest();
        request.setPrecio(BigDecimal.ZERO);
        request.setStock(5);

        assertThrows(ValidacionNegocioException.class,
                () -> productoBusinessService.updateProduct(1L, request));
    }

    @Test
    void updateProduct_invalidStock() {
        ProductoRequest request = new ProductoRequest();
        request.setPrecio(BigDecimal.TEN);
        request.setStock(-1);

        assertThrows(ValidacionNegocioException.class,
                () -> productoBusinessService.updateProduct(1L, request));
    }

    @Test
    void updateProduct_communicationError() {
        ProductoRequest request = new ProductoRequest();
        request.setPrecio(BigDecimal.TEN);
        request.setStock(5);

        when(dataServiceClient.updateProduct(1L, request)).thenThrow(FeignException.FeignServerException.class);

        assertThrows(MicroserviceCommunicationException.class,
                () -> productoBusinessService.updateProduct(1L, request));
    }

    @Test
    void deleteProduct_success() {
        doNothing().when(dataServiceClient).deleteProduct(1L);

        assertDoesNotThrow(() -> productoBusinessService.deleteProduct(1L));
        verify(dataServiceClient).deleteProduct(1L);
    }

    @Test
    void deleteProduct_notFound() {
        doThrow(FeignException.NotFound.class).when(dataServiceClient).deleteProduct(1L);

        assertThrows(ProductoNoEncontrado.class,
                () -> productoBusinessService.deleteProduct(1L));
    }

    @Test
    void deleteProduct_communicationError() {
        doThrow(FeignException.FeignServerException.class).when(dataServiceClient).deleteProduct(1L);

        assertThrows(MicroserviceCommunicationException.class,
                () -> productoBusinessService.deleteProduct(1L));
    }
}