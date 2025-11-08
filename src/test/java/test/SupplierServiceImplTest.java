package test;

import com.scb.supplychainbrief.module.supply.dto.SupplierDto;
import com.scb.supplychainbrief.module.supply.mapper.SupplierMapper;
import com.scb.supplychainbrief.module.supply.model.Supplier;
import com.scb.supplychainbrief.module.supply.repository.SupplierRepository;
import com.scb.supplychainbrief.module.supply.repository.SupplyOrderRepository;
import com.scb.supplychainbrief.module.supply.service.SupplierServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {


    @Mock
    private SupplierRepository supplierRepository;


    @Mock
    private SupplierMapper supplierMapper;


    @Mock
    private SupplyOrderRepository supplyOrderRepository;


    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Test
    void testGetSupplierById_WhenSupplierExists() {

        long supplierId = 1L;


        Supplier fakeSupplier = new Supplier();
        fakeSupplier.setIdSupplier(supplierId);
        fakeSupplier.setName("Test Supplier");


        SupplierDto.SupplierResponse fakeResponseDto = new SupplierDto.SupplierResponse();
        fakeResponseDto.setIdSupplier(supplierId);
        fakeResponseDto.setName("Test Supplier");


        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(fakeSupplier));


        when(supplierMapper.toSupplierResponse(fakeSupplier)).thenReturn(fakeResponseDto);


        SupplierDto.SupplierResponse result = supplierService.getSupplierById(supplierId);


        assertNotNull(result);
        assertEquals(supplierId, result.getIdSupplier());
        assertEquals("Test Supplier", result.getName());
    }

    @Test
    void testGetSupplierById_WhenSupplierDoesNotExist() {

        long supplierId = 99L;

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class, () -> {
            supplierService.getSupplierById(supplierId);
        });


        verify(supplierMapper, never()).toSupplierResponse(any());
    }
}
