package com.business_service.business_service.service;

import com.business_service.business_service.client.DataServiceClient;
import com.business_service.business_service.dto.CategoriaDTO;
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
class CategoriaBusinessServiceTest {

    @Mock
    private DataServiceClient dataServiceClient;

    @InjectMocks
    private CategoriaBusinessService categoriaBusinessService;

    @Test
    void findAllCategory_success() {
        CategoriaDTO dto = new CategoriaDTO();
        when(dataServiceClient.findAllCategory()).thenReturn(List.of(dto));

        List<CategoriaDTO> result = categoriaBusinessService.findAllCategory();

        assertEquals(1, result.size());
        verify(dataServiceClient).findAllCategory();
    }

    @Test
    void findAllCategory_communicationError() {
        when(dataServiceClient.findAllCategory()).thenThrow(FeignException.FeignServerException.class);

        assertThrows(MicroserviceCommunicationException.class, () -> categoriaBusinessService.findAllCategory());
    }

    @Test
    void updateCategory_success() {
        CategoriaDTO dto = new CategoriaDTO();
        when(dataServiceClient.updateCategory(1L, dto)).thenReturn(dto);

        CategoriaDTO result = categoriaBusinessService.updateCategory(1L, dto);

        assertEquals(dto, result);
        verify(dataServiceClient).updateCategory(1L, dto);
    }

    @Test
    void updateCategory_communicationError() {
        CategoriaDTO dto = new CategoriaDTO();
        when(dataServiceClient.updateCategory(1L, dto)).thenThrow(FeignException.FeignServerException.class);

        assertThrows(MicroserviceCommunicationException.class, () -> categoriaBusinessService.updateCategory(1L, dto));
    }

    @Test
    void deleteCategory_success() {
        doNothing().when(dataServiceClient).deleteCategory(1L);

        assertDoesNotThrow(() -> categoriaBusinessService.deleteCategory(1L));
        verify(dataServiceClient).deleteCategory(1L);
    }

    @Test
    void deleteCategory_communicationError() {
        doThrow(FeignException.FeignServerException.class).when(dataServiceClient).deleteCategory(1L);

        assertThrows(MicroserviceCommunicationException.class, () -> categoriaBusinessService.deleteCategory(1L));
    }
}
