package com.example.fdtracker;

import com.example.fdtracker.model.FixedDeposit;
import com.example.fdtracker.repository.FixedDepositRepository;
import com.example.fdtracker.service.FixedDepositService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FixedDepositServiceTest {

    @Mock
    private FixedDepositRepository repository;

    @InjectMocks
    private FixedDepositService service;

    @Test
    void testSave() {
        FixedDeposit fd = new FixedDeposit(1L, "John Doe", "SBI", "123456", 
            LocalDate.now(), LocalDate.now().plusYears(1), 
            BigDecimal.valueOf(10000), BigDecimal.valueOf(11000), "Test FD");
        
        when(repository.save(fd)).thenReturn(fd);
        
        FixedDeposit result = service.save(fd);
        
        assertNotNull(result);
        assertEquals("John Doe", result.getPersonName());
        assertEquals("SBI", result.getBankName());
        verify(repository).save(fd);
    }

    @Test
    void testFindAll() {
        List<FixedDeposit> deposits = Arrays.asList(
            new FixedDeposit(1L, "John Doe", "SBI", "123456", LocalDate.now(), 
                LocalDate.now().plusYears(1), BigDecimal.valueOf(10000), BigDecimal.valueOf(11000), "Test FD")
        );
        
        when(repository.findAll()).thenReturn(deposits);
        
        List<FixedDeposit> result = service.findAll();
        
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getPersonName());
        verify(repository).findAll();
    }

    @Test
    void testDeleteById() {
        service.deleteById(1L);
        verify(repository).deleteById(1L);
    }
}