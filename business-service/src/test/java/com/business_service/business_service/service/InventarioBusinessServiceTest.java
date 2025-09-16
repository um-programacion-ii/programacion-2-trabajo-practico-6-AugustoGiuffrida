package com.business_service.business_service.service;

import com.business_service.business_service.client.DataServiceClient;
import com.business_service.business_service.dto.InventarioDTO;
import com.business_service.business_service.exception.InventarioNoEncontrado;
import com.business_service.business_service.exception.MicroserviceCommunicationException;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioBusinessServiceTest {

    @Mock
    private DataServiceClient dataServiceClient;

    @InjectMocks
    private InventarioBusinessService inventarioBusinessService;

    @Test
    void findInventoriesWithLowStock_success() {
        InventarioDTO dto = new InventarioDTO();
        when(dataServiceClient.findInventoriesWithLowStock()).thenReturn(List.of(dto));

        List<InventarioDTO> result = inventarioBusinessService.findInventoriesWithLowStock();

        assertEquals(1, result.size());
        verify(dataServiceClient).findInventoriesWithLowStock();
    }

    @Test
    void findInventoriesWithLowStock_communicationError() {
        when(dataServiceClient.findInventoriesWithLowStock()).thenThrow(FeignException.FeignServerException.class);

        assertThrows(MicroserviceCommunicationException.class,
                () -> inventarioBusinessService.findInventoriesWithLowStock());
    }

    @Test
    void findInventoriesByProductId_success() {
        InventarioDTO dto = new InventarioDTO();
        when(dataServiceClient.findInventoriesByProductId(1L)).thenReturn(dto);

        InventarioDTO result = inventarioBusinessService.findInventoriesByProductId(1L);

        assertEquals(dto, result);
        verify(dataServiceClient).findInventoriesByProductId(1L);
    }

    @Test
    void findInventoriesByProductId_notFound() {
        when(dataServiceClient.findInventoriesByProductId(1L))
                .thenThrow(FeignException.NotFound.class);

        assertThrows(InventarioNoEncontrado.class,
                () -> inventarioBusinessService.findInventoriesByProductId(1L));
    }

    @Test
    void updateInventory_success() {
        InventarioDTO dto = new InventarioDTO();
        when(dataServiceClient.updateInventory(1L, 5)).thenReturn(dto);

        InventarioDTO result = inventarioBusinessService.updateInventory(1L, 5);

        assertEquals(dto, result);
        verify(dataServiceClient).updateInventory(1L, 5);
    }

    @Test
    void updateInventory_communicationError() {
        when(dataServiceClient.updateInventory(1L, 5)).thenThrow(FeignException.FeignServerException.class);

        assertThrows(MicroserviceCommunicationException.class,
                () -> inventarioBusinessService.updateInventory(1L, 5));
    }
}
